-- A sample table that holds sample home appliances that you can probably see at home.

CREATE TABLE HOME_APPLIANCE (
  ID        INTEGER PRIMARY KEY,
  NAME 		VARCHAR(64) NOT NULL,
  CATEGORY  VARCHAR(64) NOT NULL,
  PRICE 	NUMERIC (7, 2) NOT NULL DEFAULT 0);
  
CREATE TABLE HOME_APPLIANCE_GROUP (
  ID           INTEGER PRIMARY KEY,  
  CATEGORY     VARCHAR(64) NOT NULL,
  COUNT        VARCHAR(64) NOT NULL DEFAULT '0',
  TOTAL_PRICE  NUMERIC (7, 2) NOT NULL DEFAULT 0,
  PERCENTAGE   VARCHAR(64) NOT NULL DEFAULT '0');
  
-- Sample sequences that will be used in assigning aggregate key to HOME_APPLIANCE and HOME_APPLIANCE_DESTINATION tables. 

CREATE SEQUENCE HOME_APPLIANCE_SEQ START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE HOME_APPLIANCE_GROUP_SEQ START WITH 1 INCREMENT BY 1;