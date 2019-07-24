CREATE TABLE TBL_ENTERPRISE(
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,semail   VARCHAR2(100)      NULL
	,ndigitvalid NUMBER          NULL
	,saddress VARCHAR2(100)      NULL
	,smoneydefect VARCHAR2(30)   NOT NULL
	,snit     VARCHAR2(12)       NULL
	,sname    VARCHAR2(50)       NOT NULL
	,scounter VARCHAR2(30)       NULL
	,srepresentative VARCHAR2(30) NULL
	,scountry  VARCHAR2(30)      NOT NULL
	,sphone    VARCHAR2(12)      NULL
);
ALTER TABLE TBL_ENTERPRISE ADD PRIMARY KEY (scode);
ALTER TABLE TBL_ENTERPRISE ADD FOREIGN KEY (smoneydefect) REFERENCES TBL_PARAMETER (scode);
ALTER TABLE TBL_ENTERPRISE ADD FOREIGN KEY (scountry) REFERENCES TBL_COUNTRY (scode);
ALTER TABLE TBL_ENTERPRISE ADD FOREIGN KEY (scounter) REFERENCES TBL_PERSON (scode);
ALTER TABLE TBL_ENTERPRISE ADD FOREIGN KEY (srepresentative) REFERENCES TBL_PERSON (scode);