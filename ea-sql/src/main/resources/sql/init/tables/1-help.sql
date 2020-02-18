CREATE TABLE TBL_HELP(
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,stitle   VARCHAR2(100)      NOT NULL
	,sbody    VARCHAR2(2000)     NOT NULL
	,smsgright VARCHAR2(500)     NULL     
	,smsgbottom VARCHAR2(500)    NULL
	,sclasspathbean VARCHAR2(300) NOT NULL
	,scss     VARCHAR2(200)      NULL
	,siconcss VARCHAR2(100)      NULL
	,siconpath VARCHAR2(200)     NULL
	,simagepath VARCHAR2(200)    NULL
	,nstate   NUMBER             NOT NULL DEFAULT 0
);
ALTER TABLE TBL_HELP ADD CONSTRAINT k_help_primary PRIMARY KEY (scode);