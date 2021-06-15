CREATE TABLE COMMUNICATION_SCHEDULING (
	ID INT NOT NULL AUTO_INCREMENT,
	CREATION_DATE TIMESTAMP NOT NULL,
	SCHEDULE_DATE TIMESTAMP NOT NULL,
	PHONE_NUMBER varchar2(20),
	EMAIL varchar2(100),
	SEND_TYPE INT NOT NULL,
	MESSAGE varchar2(100) NOT NULL,
	STATUS INT,
	STATUS_DESCRIPTION varchar2(100),
	PRIMARY KEY (ID)
 );
