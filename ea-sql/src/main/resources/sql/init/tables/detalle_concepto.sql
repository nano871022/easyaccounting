CREATE TABLE TBL_CONCEPT_DETAIL (
 	 scode    VARCHAR2(30)        NOT NULL
	,screater VARCHAR2(100)       NULL
	,sdeleter VARCHAR2(100)       NULL
	,supdater VARCHAR2(100)       NULL
	,dcreate  DATETIME            NOT NULL DEFAULT NOW()
	,ddelete  DATETIME            NULL
	,dupdate  DATETIME            NULL
	,sicaactivity    VARCHAR2(30) NOT NULL
	,nbaseconsumptiontax NUMBER NOT NULL
	,scostcenter     VARCHAR2(30) NOT NULL
	,scodedocument   VARCHAR2(30) NOT NULL
	,sconcept        VARCHAR2(30) NOT NULL 
	,sdescription    VARCHAR2(100) NOT NULL
	,sobservation    VARCHAR2(300) NOT NULL
	,nivapercent      NUMBER NOT NULL
	,nrow             NUMBER NULL
	,nrateicaactivity NUMBER NULL
	,sthird          VARCHAR2(30) NOT NULL
	,sconcepttype    VARCHAR2(30) NOT NULL
	,ngrossvalue     NUMBER NULL
	,ndiscountvalue  NUMBER NULL
	,nconsumptiontaxvalue NUMBER NULL
	,nivavalue       NUMBER NULL
	,nnetworth       NUMBER NULL
);
ALTER TABLE TBL_CONCEPT_DETAIL ADD PRIMARY KEY (scode);
ALTER TABLE TBL_CONCEPT_DETAIL ADD FOREIGN KEY (sicaactivity) TBL_ICA_ACTIVITY REFERENCES (scode);
ALTER TABLE TBL_CONCEPT_DETAIL ADD FOREIGN KEY (scostcenter) REFERENCES TBL_COST_CENTER (scode);
ALTER TABLE TBL_CONCEPT_DETAIL ADD FOREIGN KEY (sconcept) REFERENCES TBL_CONCEPT (scode);
ALTER TABLE TBL_CONCEPT_DETAIL ADD FOREIGN KEY (sconcepttype) REFERENCES TBL_PARAMETER (scode);
ALTER TABLE TBL_CONCEPT_DETAIL ADD FOREIGN KEY (sthird) REFERENCES TBL_THIRD (scode);
