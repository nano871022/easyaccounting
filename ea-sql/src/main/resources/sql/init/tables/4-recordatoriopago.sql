CREATE TABLE TBL_PAYMENT_REMINDER(
 	 scode    VARCHAR2(30)        NOT NULL
	,screater VARCHAR2(100)       NULL
	,sdeleter VARCHAR2(100)       NULL
	,supdater VARCHAR2(100)       NULL
	,dcreate  DATETIME            NOT NULL DEFAULT NOW()
	,ddelete  DATETIME            NULL
	,dupdate  DATETIME            NULL
	,sdocument VARCHAR2(30)       NOT NULL
	,dpay DATETIME                NOT NULL
);
ALTER TABLE TBL_PAYMENT_REMINDER ADD CONSTRAINT KY_PR_PRIMARY PRIMARY KEY (scode);
ALTER TABLE TBL_PAYMENT_REMINDER ADD CONSTRAINT FK_PR_D_FOREING FOREIGN KEY (sdocument) REFERENCES TBL_DOCUMENT(scode);