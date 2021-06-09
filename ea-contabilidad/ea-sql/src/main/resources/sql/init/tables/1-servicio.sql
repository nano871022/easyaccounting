CREATE TABLE TBL_SERVICE (
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname  VARCHAR2(50)       NOT NULL
	,sdescription VARCHAR2(200)  NULL
	,nvaluehandwork NUMBER       NOT NULL
	
);
ALTER TABLE TBL_SERVICE ADD CONSTRAINT KY_SERVICE_PK PRIMARY KEY (scode);
