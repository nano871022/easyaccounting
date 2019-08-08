CREATE TABLE TBL_MENU_PERM_USER(
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,smenu    VARCHAR2(30)       NOT NULL
	,sperm    VARCHAR2(30)       NOT NULL
	,sgroupuser VARCHAR2(30)     NULL
	,suser    VARCHAR2(30)       NULL
	,nstate   NUMBER             NOT NULL DEFAULT 0
);


ALTER TABLE TBL_MENU_PERM_USER ADD CONSTRAINT K_MENU_PERM_PRIMARY PRIMARY KEY (scode);
ALTER TABLE TBL_MENU_PERM_USER ADD CONSTRAINT U_MENU_PERM_ UNIQUE (smenu,sperm);