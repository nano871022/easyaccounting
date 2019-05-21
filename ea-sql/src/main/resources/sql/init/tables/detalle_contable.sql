CREATE TABLE TBL_ACCOUNTING_DETAIL(
 	 scode    VARCHAR2(30)        NOT NULL
	,screater VARCHAR2(100)       NULL
	,sdeleter VARCHAR2(100)       NULL
	,supdater VARCHAR2(100)       NULL
	,dcreate  DATETIME            NOT NULL DEFAULT NOW()
	,ddelete  DATETIME            NULL
	,dupdate  DATETIME            NULL
	,scodedocument VARCHAR2(30)   NOT NULL                
	,sconcept VARCHAR2(30)        NOT NULL
	,saccountingaccount VARCHAR2(30)  NULL
	,sobservation VARCHAR2(200)       NULL
	,nrow NUMBER                  NULL
	,nvalue NUMBER                NULL
);
ALTER TABLE TBL_ACCOUNTING_DETAIL ADD PRIMARY KEY (scode);
ALTER TABLE TBL_ACCOUNTING_DETAIL ADD FOREIGN KEY (sconcept) REFERENCES TBL_CONCEPT (scode);
ALTER TABLE TBL_ACCOUNTING_DETAIL ADD FOREIGN KEY (saccountingaccount) REFERENCES TBL_ACCOUNTING_ACCOUNT (scode);