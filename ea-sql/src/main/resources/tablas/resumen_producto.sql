CREATE TABLE TBL_PRODUCT_SUMMARY (
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,nquantity NUMBER            NOT NULL
	,nprofitpercentsale NUMBER   NULL
	,sivapercentsaleapply VARCHAR2(30) NULL
	,sproduct   VARCHAR2(30)     NOT NULL           
	,npurcharsevalue NUMBER      NOT NULL 
	,nsalevalue NUMBER           NULL
);
ALTER TABLE TBL_PRODUCT_SUMMARY ADD PRIMARY KEY(scode);
ALTER TABLE TBL_PRODUCT_SUMMARY ADD FOREIGN KEY (sivapercentsaleapply) REFERENCES TBL_PARAMETER(scode);
ALTER TABLE TBL_PRODUCT_SUMMARY ADD FOREIGN KEY (sproduct) REFERENCES TBL_PRODUCT(scode);