CREATE TABLE TBL_USERS(
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(100)      NOT NULL
	,spassword VARCHAR2(200)     NOT NULL
	,dstart    DATETIME          NOT NULL
	,dend      DATETIME          NULL
	,sperson   VARCHAR2(30)      NOT NULL
	,sgroupuser VARCHAR2(30)     NOT NULL
	,nstate    NUMBER            NOT NULL    
);
ALTER TABLE TBL_USERS ADD CONSTRAINT k_users_primary PRIMARY KEY (scode);
ALTER TABLE TBL_USERS ADD CONSTRAINT u_users_name    UNIQUE (sname);
ALTER TABLE TBL_USERS ADD CONSTRAINT u_users_name    UNIQUE (sperson);