CREATE TABLE TBL_LOCATION (
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(50)       NOT NULL
	,sdescription VARCHAR2(200)  NULL
	,sstore   VARCHAR2(30)       NOT NULL
);
ALTER TABLE TBL_LOCATION ADD PRIMARY KEY (scode);
ALTER TABLE TBL_LOCATION ADD FOREIGN KEY (sstore) REFERENCES TBL_STORE (scode);