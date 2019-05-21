CREATE TABLE TBL_CONFIGURATION(
 	 scode    VARCHAR2(30)        NOT NULL
	,screater VARCHAR2(100)       NULL
	,sdeleter VARCHAR2(100)       NULL
	,supdater VARCHAR2(100)       NULL
	,dcreate  DATETIME            NOT NULL DEFAULT NOW()
	,ddelete  DATETIME            NULL
	,dupdate  DATETIME            NULL
	,sfile    VARCHAR2(100)       NULL
	,soutputfile VARCHAR2(100)    NULL
	,sdescription VARCHAR2(200)   NULL
	,nreport   NUMBER             NULL
	,sconfiguration VARCHAR2(100) NULL
);
ALTER TABLE TBL_CONFIGURATION ADD PRIMARY KEY (scode);