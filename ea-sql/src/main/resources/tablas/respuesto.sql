CREATE TABLE TBL_REPLACEMENT(
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(50)       NOT NULL
	,nivapercent NUMBER          NOT NULL
	,sreference  VARCHAR2(100)   NOT NULL
	,nvaluemarket NUMBER         NOT NULL
	,nvaluesale   NUMBER         NOT NULL
);
ALTER TABLE TBL_REPLACEMENT ADD PRIMARY KEY (scode);