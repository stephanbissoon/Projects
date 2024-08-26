import mysql.connector;
import pandas as pd;
import matplotlib.pyplot as plt;
import redis;
import json;
from datetime import datetime;

db_url = "localhost";
db_name = "money_motions";
db_user = "root";
db_password = "password";

def insert():
	dataframe = pd.read_excel("trans.xls", skiprows=[0, 1, 2])

	dataframe = dataframe.drop(["Cheque", "Ending Balance", "Teller Comment"], axis=1);

	data = [];

	for (_, row) in dataframe.iterrows():
		date = str(row["Date"])[0:10];
		amount = row["Amount"];
		description = str(row["Description"]).strip();
		currency = None;
		forex_amount = None;

		split_desc = description.split();

		if split_desc[-1] == "CAD":
			currency = "CAD";
			forex_amount = split_desc[-2];
		
		if split_desc[-1] == "USD":
			currency = "USD";
			forex_amount = split_desc[-2];
		
		if split_desc[-1] == "TTO":
			currency = "TTD";
			forex_amount = amount;

		data.append((date, amount, description, currency, forex_amount));

	[print(x) for x in data]

	if(input("\nProceed to save to database? ").lower() == "yes"):
		db = mysql.connector.connect(host=db_url, database=db_name, user=db_user, password=db_password);
	
		sql = "INSERT INTO transaction VALUES(NULL, %s, %s, %s, %s, %s)";

		db.cursor().executemany(sql, data);
		db.commit();
		db.close();

def graph():
	rediz = redis.Redis(host = 'localhost', port = 6379, decode_responses = True);

	info = {};
	original_dates = [];

	# Check if SQL results is in cache
	if(rediz.exists("graph") == False):
		print("\nRedis miss. Querying db...");

		db = mysql.connector.connect(host=db_url, database=db_name, user=db_user, password=db_password);
		cursor = db.cursor();
		cursor.execute("SELECT date, ABS(amount)/forex_amount AS exchange_rate FROM transaction WHERE currency = 'CAD' ORDER BY date");
		result = cursor.fetchall();
		db.close();

		info = {
			"dates" : [],
			"exchangeRates" : []
		}

		for res in result:
			original_dates.append(res[0]);
			info["dates"].append(str(res[0]));
			info["exchangeRates"].append(float(res[1]));

		# Save to Redis as a JSON string
		rediz.set("graph", json.dumps(info), ex = 30);

	else:
		print("\nRedis hit");
	
		info = json.loads(rediz.get("graph"));
		original_dates = [datetime.strptime(x, "%Y-%m-%d") for x in info["dates"]] # Using the dates object produces a cleaner graph

	dataframe = pd.DataFrame({
		'Date': original_dates,
		'ExchangeRate': info["exchangeRates"]
	});

	plt.fill_between(dataframe['Date'], dataframe['ExchangeRate'], color="skyblue", alpha=0.4);
	plt.plot(dataframe['Date'], dataframe['ExchangeRate']);
	plt.xlabel('Date')
	plt.ylabel('Exchange Rate')
	plt.title('CAD to TTD Exchange Rates Over Time')
	plt.show()

def metrics():
	rediz = redis.Redis(host = 'localhost', port = 6379, decode_responses = True);

	info = {};

	# Check if SQL results is in cache
	if(rediz.exists("metrics") == False):
		print("\nRedis miss. Querying db...");

		db = mysql.connector.connect(host=db_url, database=db_name, user=db_user, password=db_password);
		cursor = db.cursor();

		cursor.execute("WITH " +
		"AVERAGE AS (SELECT AVG(amount/forex_amount) AS avg_forex_rate FROM transaction WHERE currency = 'CAD' AND date >= '2019-08-21' ORDER BY date), " +
		"TOTAL AS (SELECT SUM(amount) AS total FROM transaction WHERE date >= '2019-08-21' AND amount > 0) " +
		"SELECT total AS TTD, ROUND((total / avg_forex_rate), 2) AS CAD, avg_forex_rate FROM AVERAGE, TOTAL");
		
		result = cursor.fetchone();
		db.close();

		info = {
			"ttd" : float(result[0]),
			"cad" :  float(result[1]),
			"forex_rate" : float(result[2]),
		};
	
		# Save to Redis
		rediz.hset("metrics", mapping = info);
		rediz.expire("metrics", 10);

	else:
		print("\nRedis hit");
		info = rediz.hgetall("metrics");

	print("TTD: %s\nCAD: %s\nForex Rate: %s" % (info["ttd"], info["cad"], info["forex_rate"]));

def main():
	while((user_input := input("\n1. Insert data\n2. Graph\n3. Metrics\n")) != "0"):
		if(user_input == "1"):
			insert();
		elif(user_input == "2"):
			graph();
		elif(user_input == "3"):
			metrics();
main();