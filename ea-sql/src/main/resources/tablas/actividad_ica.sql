CREATE TABLE TBL_ICA_ACTIVITY(
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(30)       NOT NULL
	,sdescription VARCHAR2(200)  NULL
	,sbase    VARCHAR2(10)       NULL
	,srate    VARCHAR2(10)       NULL
	,sicacode VARCHAR2(30)       NULL
);
ALTER TABLE TBL_ICA_ACTIVITY ADD PRIMARY KEY (scode);