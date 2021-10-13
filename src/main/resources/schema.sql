-- A sample table that holds sample home appliances that you can probably see at home.

CREATE TABLE HOME_APPLIANCE (
  ID        INTEGER PRIMARY KEY,
  NAME 		VARCHAR(64) NOT NULL,
  CATEGORY  VARCHAR(64) NOT NULL,
  PRICE 	VARCHAR(64) NOT NULL);
  
-- A sample sequence that will be used in assigning aggregate key to HOME_APPLIANCE table. 

CREATE SEQUENCE HOME_APPLIANCE_SEQ START WITH 1 INCREMENT BY 1;