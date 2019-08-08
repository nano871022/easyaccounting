CREATE TABLE TBL_LANGUAGES (
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,scodename VARCHAR2(100)     NOT NULL
	,stext     VARCAHR2(500)     NOT NULL
	,sidiom    VARCHAR2(10)      NOT NULL
	,siconpath VARCHAR2(300)     NULL
	,siconcss  VARCHAR2(100)     NULL
	,nstate    NUMBER            NOT NULL DEFAULT 0
);
ALTER TABLE TBL_LANGUAGES ADD CONSTRAINT k_languages_primary PRIMARY KEY (scode);
ALTER TABLE TBL_LANGUAGES ADD CONSTRAINT u_languages_unique UNIQUE (scodename);