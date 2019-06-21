CREATE TABLE TBL_PRODUCT (
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(50)       NOT NULL
	,sdescription VARCHAR2(150)  NULL 
	,sreferences VARCHAR2(40)    NULL
);
ALTER TABLE TBL_PRODUCT ADD PRIMARY KEY (scode);	