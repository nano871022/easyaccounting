CREATE TABLE TBL_CASH(
 	 scode    VARCHAR2(30)        NOT NULL
	,screater VARCHAR2(100)       NULL
	,sdeleter VARCHAR2(100)       NULL
	,supdater VARCHAR2(100)       NULL
	,dcreate  DATETIME            NOT NULL DEFAULT NOW()
	,ddelete  DATETIME            NULL
	,dupdate  DATETIME            NULL
	,doutput  DATETIME            NULL
	,dinput   DATETIME            NULL
	,spay  VARCHAR2(30)           NULL
	,snumaccount VARCHAR2(30)     NOT NULL
	,sbank    VARCHAR2(30)        NULL
	,nvalue   NUMBER              NULL
	,stypecash VARCHAR2(100)      NOT NULL
);
ALTER TABLE TBL_CASH ADD CONSTRAINT KY_C_PRIMARY PRIMARY KEY (scode);
ALTER TABLE TBL_CASH ADD CONSTRAINT FK_C_B_FOREIGN FOREIGN KEY (sbank) REFERENCES TBL_BANK(scode);
ALTER TABLE TBL_CASH add CONSTRAINT fk_C_P_FOREIGN FOREIGN KEY (spay) REFERENCES TBL_PAY(scode);