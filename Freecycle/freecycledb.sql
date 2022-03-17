CREATE DATABASE IF NOT EXISTS ha07;

USE ha07;

DROP TABLE IF EXISTS IMAGE;
DROP TABLE IF EXISTS POST;
DROP TABLE IF EXISTS MEMBER;

CREATE TABLE IF NOT EXISTS MEMBER
(
	MEMBER_ID INT NOT NULL AUTO_INCREMENT,
    USERNAME VARCHAR(20) UNIQUE NOT NULL,
    PASSWORD CHAR(128) NOT NULL,
    EMAIL VARCHAR(50) UNIQUE NOT NULL,
    BIOGRAPHY VARCHAR(2500) NOT NULL,
    DATE_REGISTERED DATETIME NOT NULL,
    ACCOUNT_STATUS VARCHAR(5) NOT NULL,
    
    PRIMARY KEY (MEMBER_ID)
);

INSERT INTO MEMBER VALUES(NULL, 'stephanbissoon', '7791c7e8017632f3fa891a1594ca2b057a35e25bd00fac40806afa955fea0c3553d0d4ad02189e30be9bb122259537968b0b066ba2fcf1a7722511b57f576ba1', 'rajivstephanbissoon@gmail.com', 'Stephan from Glastonbury', NOW(), 'A');
INSERT INTO MEMBER VALUES(NULL, 'steve123', 'a2128510c8fb3c84422e7501ab15cdb3a4e3d6b76d768d7fad523938d70c8dd55da7119f0a99b5f91d03e1b30d978cef14271c8ddb1622c74ef431b1e0e53d4c', 'rajiv@pisl.co.tt', 'Steve from Bristol', NOW(), 'A');
INSERT INTO MEMBER VALUES(NULL, 'stephan123', '18e429affc853882f55acefc04a2d5525ba3c6b6733104ad8c17ef4ce85319efca7bc320d9ff75a472195fc6cf20e97b0c5cf15809ed998a275a3ce8724accfc', 'rb3896g@greenwich.ac.uk', 'Steph from Liverpool', NOW(), 'A');

CREATE TABLE IF NOT EXISTS POST
(
	POST_ID INT NOT NULL AUTO_INCREMENT,
    TITLE VARCHAR(50) NOT NULL,
	DESCRIPTION VARCHAR(2500) NOT NULL,
    DATE_TIME DATETIME NOT NULL,
    MEMBER_ID INT NOT NULL,
    
    PRIMARY KEY (POST_ID),
    FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER (MEMBER_ID)
);

INSERT INTO POST VALUES(NULL, 'Antique Grandfathers Clock', 'A clock from the olden days. 6ft. tall and still chimes.', NOW(), 1);
INSERT INTO POST VALUES(NULL, 'Roman Vase', 'A vase from Rome.', NOW(), 1);
INSERT INTO POST VALUES(NULL, 'Renaissance Painting', 'A copy of the infamous Mona Lisa painting. Envisioned during the renaissance era but printed during the early 2000s as it is a copy', NOW(), 1);
INSERT INTO POST VALUES(NULL, 'Porcelain Egyptian Bowl', 'Porcelain bowl from egypt which resembles the ones produced during ancient egyptian times.', NOW(), 1);
INSERT INTO POST VALUES(NULL, '70s Style Clothing', 'Clothing from the disco 70s era.', NOW(), 1);
INSERT INTO POST VALUES(NULL, '80s Style Clothing', 'Clothing from the hip hop 80s era.', NOW(), 2);
INSERT INTO POST VALUES(NULL, '90s Style Clothing', 'Clothing from the 90s era.', NOW(), 2);
INSERT INTO POST VALUES(NULL, 'Indian Tapestry', 'Tapestry from India', NOW(), 2);
INSERT INTO POST VALUES(NULL, 'Vintage Quilt', 'Cozy quilt.', NOW(), 2);
INSERT INTO POST VALUES(NULL, 'Gothic Headdress', 'Gothic Headdress.', NOW(), 2);
INSERT INTO POST VALUES(NULL, 'Playstation 2', 'Old Playstation 2 which doesnt work anymore. Most likely can only be used as a keepsake.', NOW(), 3);
INSERT INTO POST VALUES(NULL, 'Drafting Table', 'Drafting table for drawings and sketches.', NOW(), 3);
INSERT INTO POST VALUES(NULL, 'Kerosene Powered Lamp', 'Kerosene lamp which burns keroses to produce light.', NOW(), 3);
INSERT INTO POST VALUES(NULL, 'Copper Genie Lamp', 'Rusted genie lamp. Serves no purpose really. Best of luck if you try to get the genie out!', NOW(), 3);
INSERT INTO POST VALUES(NULL, 'Typewriter', 'Mechanical typewriter. Requires a ribbon change.', NOW(), 3);

CREATE TABLE IF NOT EXISTS IMAGE
(
	IMG_ID INT NOT NULL AUTO_INCREMENT,
	NAME VARCHAR(50) NOT NULL,
	ALT_TEXT VARCHAR(50) NOT NULL,
	POST_ID INT NOT NULL,
    
    PRIMARY KEY (IMG_ID),
	FOREIGN KEY (POST_ID) REFERENCES POST (POST_ID)
);

INSERT INTO IMAGE VALUES(NULL, 'mona_lisa.jpg', 'Mona Lisa Painting', 3);
INSERT INTO IMAGE VALUES(NULL, 'egyptian_bowl.jpg', 'Porcelain Egpytian Bowl', 4);
INSERT INTO IMAGE VALUES(NULL, 'gothic_headdress.jpg', 'Gothic Headdress', 10);
INSERT INTO IMAGE VALUES(NULL, 'ps2.jpg', 'Playstation 2', 11);

SELECT * FROM MEMBER;
SELECT * FROM POST;
SELECT * FROM IMAGE;