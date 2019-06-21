CREATE TABLE TBL_CENTER_COST(
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(50)       NOT NULL
	,sdescription VARCHAR2(200)  NULL
	,senterprise VARCHAR2(30)    NOT NULL
	,sstate   VARCHAR2(30)       NOT NULL
	,norden   NUMBER             NULL
);
ALTER TABLE TBL_CENTER_COST ADD PRIMARY KEY (scode);
ALTER TABLE TBL_CENTER_COST ADD FOREIGN KEY (senterprise) REFERENCES TBL_ENTERPRISE (scode);
ALTER TABLE TBL_CENTER_COST ADD FOREIGN KEY (sstate) REFERENCES TBL_PARAMETER (scode);