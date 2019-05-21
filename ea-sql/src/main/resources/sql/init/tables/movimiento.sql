CREATE TABLE TBL_MOVEMENT(
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,nquantity NUMBER            NOT NULL
	,npurcharseprice NUMBER      NOT NULL   
	,sproduct VARCHAR2(30)       NOT NULL
	,stype    VARCHAR2(30)       NOT NULL
	,nvalue   NUMBER             NOT NULL
);
ALTER TABLE TBL_MOVEMENT ADD PRIMARY KEY (scode);
ALTER TABLE TBL_MOVEMENT ADD FOREIGN KEY (sproduct) REFERENCES TBL_PRODUCT(scode);
ALTER TABLE TBL_MOVEMENT ADD FOREIGN KEY (stype) REFERENCES TBL_PARAMETER_INVENTORY (scode);	