CREATE TABLE TBL_PERMISSION(
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(100)      NOT NULL
	,sdescription VARCHAR2(200)  NULL
	,saction  VARCAHR2(100)      NULL
	,nstate   NUMBER             NOT NULL DEFAULT 0
);
ALTER TABLE TBL_PERMISSION ADD CONSTRAINT k_perm_primary PRIMARY KEY (scode);
ALTER TABLE TBL_PERMISSION ADD CONSTRAINT k_perm_unique UNIQUE (sname);