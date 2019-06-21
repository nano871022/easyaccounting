CREATE TABLE TBL_OFFICE (
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
    ,sname    VARCHAR2(50)       NOT NULL
    ,sdetail  VARCHAR2(100)      NULL
    ,saddress VARCHAR2(70)       NULL
    ,scity    VARCHAR2(30)       NOT NULL
);
ALTER TABLE TBL_OFFICE ADD PRIMARY KEY (scode);
ALTER TABLE TBL_OFFICE ADD FOREIGN KEY (scity) REFERENCES TBL_CITY (scode); 