CREATE TABLE TBL_PAY(
 	 scode    VARCHAR2(30)        NOT NULL
	,screater VARCHAR2(100)       NULL
	,sdeleter VARCHAR2(100)       NULL
	,supdater VARCHAR2(100)       NULL
	,dcreate  DATETIME            NOT NULL DEFAULT NOW()
	,ddelete  DATETIME            NULL
	,dupdate  DATETIME            NULL
	,dpay     DATETIME            NOT NULL
	,dpayed   DATETIME            NOT NULL
	,squote   VARCHAR2(30)        NOT NULL
	,sdocument VARCHAR2(30)       NOT NULL
	,sbank    VARCHAR2(30)        NULL
	,nvaluepay NUMBER             NOT NULL
	,nvaluepayed NUMBER           NOT NULL
	,ntaxpayed NUMBER             NOT NULL
	,saccount  VARCHAR2(30)       NULL
	,bquote    BIT                NULL
	,bpartial  BIT                NULL
	,bpayall   BIT                NULL
	,svouncherNum VARCHAR2(100)   NULL
);
ALTER TABLE TBL_PAY ADD CONSTRAINT KY_P_PRIMARY PRIMARY KEY (scode);
ALTER TABLE TBL_PAY ADD CONSTRAINT FK_P_B_FOREING FOREIGN KEY (sbank) REFERENCES TBL_BANK(scode);
ALTER TABLE TBL_PAY ADD CONSTRAINT FK_Q_D_FOREING FOREIGN KEY (sdocument) REFERENCES TBL_DOCUMENT(scode);