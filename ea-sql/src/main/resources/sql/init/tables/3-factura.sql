CREATE TABLE TBL_BILL(
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,dbill    DATETIME           NOT NULL   
	,dregistre DATETIME          NOT NULL
	,dexpiration DATETIME        NULL
	,sobservation VARCHAR2(200)  NULL
	,senterprise VARCHAR2(30)    NOT NULL
);
ALTER TABLE TBL_BILL ADD PRIMARY KEY (scode);
ALTER TABLE TBL_BILL ADD FOREIGN KEY (senterprise) REFERENCES TBL_ENTERPRISE(scode);