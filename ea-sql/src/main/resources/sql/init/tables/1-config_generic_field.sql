CREATE TABLE TBL_CONFIG_GENERIC_FIELD (
 	 scode    VARCHAR2(30)        NOT NULL
	,screater VARCHAR2(100)       NULL
	,sdeleter VARCHAR2(100)       NULL
	,supdater VARCHAR2(100)       NULL
	,dcreate  DATETIME            NOT NULL DEFAULT NOW()
	,ddelete  DATETIME            NULL
	,dupdate  DATETIME            NULL
	,sname    VARCHAR2(100)       NOT NULL
	,salias    VARCHAR2(100)      NULL
	,sclasspath VARCHAR2(300)     NOT NULL
	,sclasspathbean VARCHAR2(300) NOT NULL
	,sdescription VARCHAR2(300)   NULL
	,sfieldshow VARCHAR2(300)     NULL
	,sdefault VARCHAR2(100)       NULL
	,sgroup     VARCHAR2(300)     NULL
	,nwidth    NUMBER             NULL
	,norder    NUMBER             NULL
	,bcolumn   NUMBER             NULL DEFAULT 0
	,bvisible   NUMBER            NULL DEFAULT 1
	,bfield    NUMBER             NULL DEFAULT 0
	,brequired NUMBER	       NULL DEFAULT 0
	,nstate    NUMBER             NULL DEFAULT 0
	,sformat   VARCHAR2(300)      NULL
);
ALTER TABLE TBL_CONFIG_GENERIC_FIELD ADD CONSTRAINT K_confgenfield_PRIMARY PRIMARY KEY (scode);
ALTER TABLE TBL_CONFIG_GENERIC_FIELD ADD CONSTRAINT u_confgenfield_unique UNIQUE (sname,sclasspath,sclasspathbean,bfield,bcolumn);
