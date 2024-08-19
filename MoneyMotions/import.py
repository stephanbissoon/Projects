import mysql.connector;
import pandas as pd;

def main():
	dataframe = pd.read_csv("8278.csv");

	data = [];

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

		data.append((date, amount, description, currency, forex_amount));

	db_url = "localhost";
	db_name = "money_motions";
	db_user = "root";
	db_password = "password";

	db = mysql.connector.connect(host=db_url, database=db_name, user=db_user, password=db_password);

	sql = "INSERT INTO transaction VALUES(NULL, %s, %s, %s, %s, %s)";

	db.cursor().executemany(sql, data);
	db.commit();
	db.close();
	
main();