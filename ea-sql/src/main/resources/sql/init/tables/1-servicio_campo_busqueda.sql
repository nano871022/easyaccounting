CREATE TABLE TBL_FIELD_SERVICE_SEARCH (
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sfield   VARCHAR2(100)      NULL
	,ncolumn  NUMBER             NULL
	,sconfiguration VARCHAR2(100) NULL
	,smarker  VARCHAR2(100)      NULL
	,sservice VARCHAR2(100)      NULL
);
ALTER TABLE TBL_ ADD PRIMARY KEY (scode);