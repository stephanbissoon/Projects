import mysql.connector;
import pandas as pd;
import matplotlib.pyplot as plt;

db_url = "ls-2da50b196d26766c15d8f3f0704738df3e7bae9f.cdqeoq0qq4tv.ca-central-1.rds.amazonaws.com";
db_name = "money_motions";
db_user = "money_motions";
db_password = "m0n3h_m0$hunz*";

def main():
	dataframe = pd.read_excel("trans.xls", skiprows=[0, 1, 2])

	dataframe = dataframe.drop(["Cheque", "Ending Balance", "Teller Comment"], axis=1);

	val = [];

	for (_, row) in dataframe.iterrows():
		date = str(row["Date"])[0:10];
		amount = row["Amount"];
		description = str(row["Description"]).strip();
		currency = None;
		forex_amount = None;

		split_desc = description.split();

		if "CAD" in split_desc:
			currency = "CAD";
			forex_amount = split_desc[-2];
		
		if "USD" in split_desc:
			currency = "USD";
			forex_amount = split_desc[-2];
		
		if split_desc[-1] == "TT":
			currency = "TTD";
			forex_amount = amount;

		val.append((date, amount, description, currency, forex_amount));

	[print(x) for x in val]

	if(input("\nProceed to save to database? ").lower() == "yes"):
		db = mysql.connector.connect(host=db_url, database=db_name, user=db_user, password=db_password);
	
		sql = "INSERT INTO transaction VALUES(NULL, %s, %s, %s, %s, %s)";

		db.cursor().executemany(sql, val);
		db.commit();
		db.close();

def graph():
    db = mysql.connector.connect(host=db_url, database=db_name, user=db_user, password=db_password);
    cursor = db.cursor();
    cursor.execute("SELECT date, ABS(amount)/forex_amount AS exchange_rate FROM transaction WHERE currency = 'CAD' ORDER BY date");
    result = cursor.fetchall();
    db.close();

    dates = [];
    exchange_rates = [];

    for res in result:
        dates.append(res[0]);
        exchange_rates.append(res[1]);

    dataframe = pd.DataFrame({
        'Date': dates,
        'ExchangeRate': exchange_rates
    })

    plt.fill_between(dataframe['Date'], dataframe['ExchangeRate'], color="skyblue", alpha=0.4);
    plt.plot(dataframe['Date'], dataframe['ExchangeRate']);
    plt.xlabel('Date')
    plt.ylabel('Exchange Rate')
    plt.title('CAD to TTD Exchange Rates Over Time')
    plt.show()

def metrics():
	db = mysql.connector.connect(host=db_url, database=db_name, user=db_user, password=db_password);
	cursor = db.cursor();

	cursor.execute("WITH " +
    "AVERAGE AS (SELECT AVG(amount/forex_amount) AS avg_forex_rate FROM transaction WHERE currency = 'CAD' AND date >= '2019-08-21' ORDER BY date), " +
    "TOTAL AS (SELECT SUM(amount) AS total FROM transaction WHERE date >= '2019-08-21' AND amount > 0) " +
	"SELECT total AS TTD, ROUND((total / avg_forex_rate), 2) AS CAD, avg_forex_rate FROM AVERAGE, TOTAL");
	
	result = cursor.fetchone();
	db.close();

	print("TTD: %s\nCAD: %s\nForex Rate: %s" % (result[0], result[1], result[2]))

user_input = input("1. Insert data\n2. Graph\n3. Metrics\n")

if(user_input == "1"):
	main();
elif(user_input == "2"):
	graph();
elif(user_input == "3"):
	metrics();
