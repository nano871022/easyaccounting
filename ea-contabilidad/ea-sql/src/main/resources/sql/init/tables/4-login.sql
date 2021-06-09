create table TBL_LOGIN(
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,ddateinit DATETIME          NOT NULL DEFAULT NOW()
	,ddateend DATETIME           NOT NULL
	,sipmachine VARCHAR2(30)     NOT NULL
	,sippublic VARCHAR2(30)      NOT NULL
	,nremember NUMBER            NOT NULL DEFAULT 1
	,susercode VARCHAR2(30)      NOT NULL
);
ALTER TABLE TBL_LOGIN ADD CONSTRAINT login_key PRIMARY KEY(scode);
ALTER TABLE TBL_LOGIN ADD CONSTRAINT login_user FOREIGN KEY susercode REFERENCES TBL_USERS(scode);
