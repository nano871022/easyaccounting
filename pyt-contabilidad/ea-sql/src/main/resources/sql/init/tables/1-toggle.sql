CREATE TABLE TBL_TOGGLE (
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(50)       NOT NULL
	,nactived NUMBER  NULL
);
ALTER TABLE TBL_TOGGLE ADD CONSTRAINT KY_SV_PRIMARY PRIMARY KEY (scode);