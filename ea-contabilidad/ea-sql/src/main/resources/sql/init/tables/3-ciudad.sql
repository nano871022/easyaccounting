CREATE TABLE TBL_CITY(
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(30)       NULL
	,sdepartament VARCHAR2(30)   NOT NULL	
);
ALTER TABLE TBL_CITY ADD CONSTRAINT KY_CY_PRIMARY PRIMARY KEY (scode);
ALTER TABLE TBL_CITY ADD CONSTRAINT KY_CY_FOREING FOREIGN KEY (sdepartament) REFERENCES TBL_DEPARTAMENT(scode);