CREATE DATABASE money_motions;

USE money_motions;

CREATE TABLE transaction (
  transaction_id INT NOT NULL AUTO_INCREMENT,
  date DATE NOT NULL,
  amount DECIMAL(8, 2) NOT NULL,
  description VARCHAR(60) NOT NULL,
  currency CHAR(3) NULL,
  forex_amount DECIMAL(8, 2) NULL,
  PRIMARY KEY (transaction_id)
);

/*CREATE USER "money_motions"@"%" IDENTIFIED BY "m0n3h_m0$hunz*";
GRANT SELECT, INSERT, UPDATE, DELETE ON money_motions.* TO "money_motions"@"%";*/