CREATE TABLE TBL_PARAMETER(
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sname    VARCHAR2(30)       NOT NULL
	,sdescription VARCHAR2(200)  NULL
	,svalue   VARCHAR2(100)      NULL
	,svalue2  VARCHAR2(100)      NULL
	,norder   NUMBER             NULL
	,bstate   NUMBER             NOT NULL DEFAULT 1
	,sgroup   VARCHAR2(30)       NOT NULL DEFAULT '*'
);
ALTER TABLE TBL_PARAMETER ADD PRIMARY KEY (SCODE);
-- TABLA PARA ALMACENAR LOS DATOS QUE FUERON MODIFICADOS
CREATE TABLE TBL_PARAMETER_UPDATE(
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,supdated VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,dupdated  DATETIME           NULL
	,sname    VARCHAR2(30)       NOT NULL
	,sdescription VARCHAR2(200)  NULL
	,svalue   VARCHAR2(100)      NULL
	,svalue2  VARCHAR2(100)      NULL
	,norder   NUMBER             NULL
	,bstate   NUMBER             NOT NULL DEFAULT 1
	,sgroup   VARCHAR2(30)       NOT NULL DEFAULT '*'
);
-- TABLA PARA ALMACENAR LOS DATOS QUE FUERON ELIMINADOS
CREATE TABLE TBL_PARAMETER_DELETE(
	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,sdeleted VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,ddeleted DATETIME           NULL
	,sname    VARCHAR2(30)       NOT NULL
	,sdescription VARCHAR2(200)  NULL
	,svalue   VARCHAR2(100)      NULL
	,svalue2  VARCHAR2(100)      NULL
	,norder   NUMBER             NULL
	,bstate   NUMBER             NOT NULL DEFAULT 1
	,sgroup   VARCHAR2(30)       NOT NULL DEFAULT '*'
);
