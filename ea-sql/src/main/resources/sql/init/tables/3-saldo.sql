CREATE TABLE TBL_BALANCE (
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,nquantity NUMBER            NOT NULL
	,ntotal    NUMBER            NOT NULL
	,smovement VARCHAR2(30)      NOT NULL
);
ALTER TABLE TBL_BALANCE ADD PRIMARY KEY (scode);
ALTER TABLE TBL_BALANCE ADD FOREIGN KEY (smovement) REFERENCES TBL_MOVEMENT (scode);