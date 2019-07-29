CREATE TABLE TBL_PARAMETER_GROUP_INVENTORY (
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sgroup   VARCHAR2(30)        NOT NULL
	,sparameter VARCHAR2(30)     NOT NULL
);
ALTER TABLE TBL_PARAMETER_GROUP_INVENTORY ADD CONSTRAINT KY_PGI_PRIMARY PRIMARY KEY (scode);
ALTER TABLE TBL_PARAMETER_GROUP_INVENTORY ADD CONSTRAINT KY_PGI_GP_UQ UNIQUE (sgroup);
ALTER TABLE TBL_PARAMETER_GROUP_INVENTORY ADD CONSTRAINT KY_PGI_P_UQ UNIQUE (sparameter);
ALTER TABLE TBL_PARAMETER_GROUP_INVENTORY ADD CONSTRAINT KY_PGI_P_FOREING FOREIGN KEY (sparameter) REFERENCES TBL_INVENTORY_PARAMETER(scode);