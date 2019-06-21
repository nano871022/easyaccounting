CREATE TABLE TBL_BANK(
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
    ,sdescription VARCHAR2(200)  NULL
    ,sstate   VARCHAR2(30)       NOT NULL
    ,sname    VARCHAR2(50)       NULL
    ,snumberaccount NUMBER       NULL
    ,stypebank VARCHAR2(30)      NOT NULL
    ,stypeaccount VARCHAR2(30)   NOT NULL
    ,dopening DATETIME           NOT NULL
    ,dclosing DATETIME           NULL
);
ALTER TABLE TBL_BANK ADD PRIMARY KEY (scode);
ALTER TABLE TBL_BANK ADD FOREIGN KEY (sstate) REFERENCES TBL_PARAMETER(scode);
ALTER TABLE TBL_BANK ADD FOREIGN KEY (stypebank) REFERENCES TBL_PARAMETER(scode);
ALTER TABLE TBL_BANK ADD FOREIGN KEY (stypeaccount) REFERENCES TBL_PARAMETER(scode);