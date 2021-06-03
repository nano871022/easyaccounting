CREATE TABLE TBL_STORE(
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(50)       NOT NULL
	,sdescription VARCHAR2(150)  NULL
	,saddress  VARCHAR2(100)     NULL
);
ALTER TABLE TBL_STORE ADD CONSTRAINT KY_S_PRIMARY PRIMARY KEY (scode);	