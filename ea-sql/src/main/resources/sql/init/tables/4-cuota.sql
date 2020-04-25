CREATE TABLE TBL_QUOTE(
 	 scode    VARCHAR2(30)        NOT NULL
	,screater VARCHAR2(100)       NULL
	,sdeleter VARCHAR2(100)       NULL
	,supdater VARCHAR2(100)       NULL
	,dcreate  DATETIME            NOT NULL DEFAULT NOW()
	,ddelete  DATETIME            NULL
	,dupdate  DATETIME            NULL
	,dpay     DATETIME            NOT NULL
	,nvalue   NUMBER              NOT NULL
	,nquote   NUMBER              NOT NULL
	,nneto    NUMBER              NOT NULL
	,ntax     NUMBER              NOT NULL
	,ntaxpayed NUMBER             NULL
	,npayed   NUMBER              NULL
	,sstate   VARCHAR2(30)         NOT NULL
	,speriod  VARCHAR2(30)        NOT NULL
	,sdocument VARCHAR2(30)       NOT NULL
);
ALTER TABLE TBL_QUOTE ADD CONSTRAINT KY_Q_PRIMARY PRIMARY KEY (scode);
ALTER TABLE TBL_QUOTE ADD CONSTRAINT FK_Q_SP_FOREING FOREIGN KEY (sstate) REFERENCES TBL_PARAMETER(scode);
ALTER TABLE TBL_QUOTE ADD CONSTRAINT FK_Q_PP_FOREING FOREIGN KEY (speriod) REFERENCES TBL_PARAMETER(scode);
ALTER TABLE TBL_QUOTE ADD CONSTRAINT FK_Q_D_FOREING FOREIGN KEY (sdocument) REFERENCES TBL_DOCUMENT(scode);