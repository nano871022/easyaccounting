CREATE TABLE TBL_MARKER_SERVICE(
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,snamefield VARCHAR2(100)    NOT NULL
	,smarker  VARCHAR2(100)      NOT NULL
	,sconfiguration VARCAHR2(100) NULL
	,sserice  VARCHAR2(100)      NOT NULL
);
ALTER TABLE TBL_MARKER_SERVICE ADD PRIMARY KEY (scode);