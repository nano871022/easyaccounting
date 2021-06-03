CREATE TABLE TBL_PERSON(
     scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(50)       NOT NULL
	,slastname VARCHAR2(50)      NULL
	,saddress  VARCHAR2(100)     NULL
	,sdocument VARCHAR2(15)      NOT NULL
	,semail    VARCHAR2(100)     NOT NULL
	,dbirthday DATETIME          NOT NULL
	,sphone    VARCHAR2(12)      NULL
	,sdocumenttype VARCHAR2(30)  NOT NULL
	,sprofession   VARCHAR2(30)  NULL
	,sprofessionalcard VARCHAR2(20) NULL
);
ALTER TABLE TBL_PERSON ADD CONSTRAINT KY_PS_PRIMARY PRIMARY KEY (scode);
ALTER TABLE TBL_PERSON ADD CONSTRAINT KY_PS_DT_FOREING FOREIGN KEY (sdocumenttype) REFERENCES TBL_PARAMETER (scode);
ALTER TABLE TBL_PERSON ADD CONSTRAINT KY_PS_PF_FOREING FOREIGN KEY (sprofession) REFERENCES TBL_PARAMETER (scode);
