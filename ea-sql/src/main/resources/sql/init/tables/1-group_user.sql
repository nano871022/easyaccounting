CREATE TABLE TBL_GROUP_USER(
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(100)      NOT NULL
	,sdescription VARCHAR2(200)  NULL
	,nstate   NUMBER             NOT NULL DEFAULT 0
);
ALTER TABLE TBL_GROUP_USER ADD CONSTRAINT k_groupuser_primary PRIMARY KEY (scode);
ALTER TABLE TBL_GROUP_USER ADD CONSTRAINT u_groupuser_unique UNIQUE (sname);