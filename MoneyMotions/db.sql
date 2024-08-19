/*CREATE DATABASE IF NOT EXISTS money_motions;

DROP TABLE IF EXISTS transaction;

CREATE TABLE transaction (
  transaction_id INT NOT NULL AUTO_INCREMENT,
  date DATE NOT NULL,
  amount DECIMAL(8, 2) NOT NULL,
  description VARCHAR(60) NOT NULL,
  currency CHAR(3) NULL,
  forex_amount DECIMAL(8, 2) NULL,
  PRIMARY KEY (transaction_id)
);*/

USE money_motions;

SELECT *, ABS(amount)/forex_amount AS xr
FROM transaction
WHERE description LIKE "%CAD%" AND currency IS NULL;