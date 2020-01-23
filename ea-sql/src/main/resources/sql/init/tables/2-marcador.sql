CREATE TABLE TBL_MARKER(
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sconfiguration VARCHAR2(30) NULL
	,smarker  VARCHAR2(50)       NOT NULL
	,norder   NUMBER             NOT NULL
	,siotype  VARCHAR2(10)       NOT NULL
);
ALTER TABLE TBL_MARKER ADD CONSTRAINT KY_M_PRIMARY PRIMARY KEY (scode);
ALTER TABLE TBL_MARKER ADD CONSTRAINT KY_M_FOREIGN FOREIGN KEY (sconfiguration) REFERENCES TBL_CONFIGURATION(scode)