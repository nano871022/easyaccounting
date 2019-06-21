CREATE TABLE TBL_ENTRY (
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sdriverin VARCHAR2(30)      NULL
	,sdriverout VARCHAR2(30)      NULL
	,sdescription VARCHAR2(200)      NULL
	,senterprise VARCHAR2(32)      NOT NULL
	,din      DATETIME             NOT NULL
	,dout     DATETIME             NULL
	,splate   VARCHAR2(8)          NULL
	,sowner   VARCHAR2(30)         NULL
	,sphonecontact VARCHAR2(15)    NULL
	,nstimatedtime NUMBER          NULL
	,nworktime     NUMBER          NULL
	,sworker   VARCHAR2(30)        NULL
);
ALTER TABLE TBL_ENTRY ADD PRIMARY KEY (scode);
ALTER TABLE TBL_ENTRY ADD FOREIGN KEY (sworker) REFERENCES TBL_EMPLOYEE(scode);
ALTER TABLE TBL_ENTRY ADD FOREIGN KEY (senterprise) REFERENCES TBL_ENTERPRISE(scode);
ALTER TABLE TBL_ENTRY ADD FOREIGN KEY (sdriverin) REFERENCES TBL_PERSON(scode);
ALTER TABLE TBL_ENTRY ADD FOREIGN KEY (sdriverout) REFERENCES TBL_PERSON(scode);
-- tabla intermedia entrada repuesto
CREATE TABLE TBL_ENTRY_REPLACEMENT (
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sentry   VARCHAR2(30)       NOT NULL
	,sreplacement VARCHAR2(30)   NOT NULL
);
ALTER TABLE TBL_ENTRY_REPLACEMENT ADD PRIMARY KEY (scode);
ALTER TABLE TBL_ENTRY_REPLACEMENT ADD FOREIGN KEY (sentry) REFERENCES TBL_ENTRY(scode);
ALTER TABLE TBL_ENTRY_REPLACEMENT ADD FOREIGN KEY (sreplacement) REFERENCES TBL_REPLACEMENT(scode);
ALTER TABLE TBL_ENTRY_REPLACEMENT ADD UNIQUE (sentry,sreplacement);
-- tabla intermedia entrada service
CREATE TABLE TBL_ENTRY_SERVICE(
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sentry   VARCHAR2(30)       NOT NULL
	,sservice VARCHAR2(30)       NOT NULL
);
ALTER TABLE TBL_ENTRY_SERVICE ADD PRIMARY KEY (scode);
ALTER TABLE TBL_ENTRY_SERVICE ADD FOREIGN KEY (sentry) REFERENCES TBL_ENTRY(scode);
ALTER TABLE TBL_ENTRY_SERVICE ADD FOREIGN KEY (sreplacement) REFERENCES TBL_SERVICE(scode);
ALTER TABLE TBL_ENTRY_SERVICE ADD UNIQUE (sentry,sservice);
