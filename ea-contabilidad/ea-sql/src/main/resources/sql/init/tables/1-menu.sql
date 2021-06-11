CREATE TABLE TBL_MENU(
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,surl     VARCHAR2(300)      NOT NULL
	,sclasspath VARCHAR2(300)    NOT NULL
	,siconapth VARCHAR2(300)     NULL
	,siconcss  VARCHAR2(200)     NULL
	,sshortcut VARCHAR2(30)      NULL
	,nstate    NUMBER            NOT NULL default 0
);
ALTER TABLE TBL_MENU ADD CONSTRAINT k_menu_primary PRIMARY KEY (scode);
ALTER TABLE TBL_MENU ADD CONSTRAINT u_menu_unique UNIQUE (surl);
ALTER TABLE TBL_MENU ADD CONSTRAINT u_menu_unique_key UNIQUE (sshortcut);
