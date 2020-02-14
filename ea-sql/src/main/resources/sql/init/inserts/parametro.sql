INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription) VALUES ('parametro2019060700001','2019-06-07','Tipo de monedas','Monedas mmundiales');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription,sgroup) VALUES ('parametro2019060700002','2019-06-07','COP','Moneda Colombiana','parametro2019060700001');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription,sgroup) VALUES ('parametro2019060700003','2019-06-07','DOLAR','Moneda Estado Unidense','parametro2019060700001');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription) VALUES ('parametro2019060700004','2019-06-07','Estado Pais','Estado Activacion Pais');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription,sgroup) VALUES ('parametro2019060700005','2019-06-07','Activo','Estado Activado','parametro2019060700004');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription,sgroup) VALUES ('parametro2019060700006','2019-06-07','Inactivo','Estado Inactivo','parametro2019060700004');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription,sgroup) VALUES ('parametro2019072400007','2019-07-24','Tipo de Documento','tipos de documento dinamicos.','*');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription,sgroup) VALUES ('parametro2019072400008','2019-07-24','Documento X Pagar','Documento dinámico por pagar.','parametro2019072400007');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription,sgroup) VALUES ('parametro2019072400009','2019-07-24','Documento X Cobrar','Documento dinámico por cobrar.','parametro2019072400007');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription,sgroup) VALUES ('parametro2019072400010','2019-07-24','Factura','Documento dinámico tipo factura','parametro2019072400007');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription,sgroup) VALUES ('parametro2019072400011','2019-07-24','Tipo Documento Personas','Tipos de documentos personas y empresas.','*');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription,sgroup) VALUES ('parametro2019072400012','2019-07-24','CC','Cedula de ciudadania','parametro2019072400011');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription,sgroup) VALUES ('parametro2019072400013','2019-07-24','NIT','Tipo de identificación para empresas}','parametro2019072400011');
INSERT INTO TBL_PARAMETER (scode,dcreate,sname,sdescription,sgroup) VALUES ('parametro2019072400014','2019-07-24','Pasaporte','Tipo de identificación para extrangeros','parametro2019072400011');
INSERT INTO PUBLIC.TBL_PARAMETER (SCODE,SCREATER,SDELETER,SUPDATER,DCREATE,DDELETE,DUPDATE,SNAME,SDESCRIPTION,SVALUE,SVALUE2,NORDER,NSTATE,SGROUP) VALUES (
'Parametro202021111112314','admin',NULL,NULL,'2020-02-11 00:00:00.000',NULL,NULL,'Estados Generales','Estados generales por numero',NULL,NULL,NULL,1,'*');
INSERT INTO PUBLIC.TBL_PARAMETER (SCODE,SCREATER,SDELETER,SUPDATER,DCREATE,DDELETE,DUPDATE,SNAME,SDESCRIPTION,SVALUE,SVALUE2,NORDER,NSTATE,SGROUP) VALUES (
'Parametro202021111114915','admin',NULL,NULL,'2020-02-11 00:00:00.000',NULL,NULL,'Activo','Estado Activado','1',NULL,1,1,'Parametro202021111112314');
INSERT INTO PUBLIC.TBL_PARAMETER (SCODE,SCREATER,SDELETER,SUPDATER,DCREATE,DDELETE,DUPDATE,SNAME,SDESCRIPTION,SVALUE,SVALUE2,NORDER,NSTATE,SGROUP) VALUES (
'Parametro20202111112716','admin',NULL,NULL,'2020-02-11 00:00:00.000',NULL,NULL,'Inactivo','Estado Inactivo','0',NULL,2,1,'Parametro202021111112314');
INSERT INTO PUBLIC.TBL_PARAMETER (SCODE,SCREATER,SDELETER,SUPDATER,DCREATE,DDELETE,DUPDATE,SNAME,SDESCRIPTION,SVALUE,SVALUE2,NORDER,NSTATE,SGROUP) VALUES (
'parametro2019060700001',NULL,NULL,NULL,'2019-06-07 00:00:00.000',NULL,NULL,'Tipo de monedas','Monedas Mundiales',NULL,NULL,NULL,1,'*');
INSERT INTO PUBLIC.TBL_PARAMETER (SCODE,SCREATER,SDELETER,SUPDATER,DCREATE,DDELETE,DUPDATE,SNAME,SDESCRIPTION,SVALUE,SVALUE2,NORDER,NSTATE,SGROUP) VALUES (
'parametro2019060700003',NULL,NULL,NULL,'2019-06-07 00:00:00.000',NULL,NULL,'DOLAR','Moneda EEUU',NULL,NULL,NULL,1,'parametro2019060700001');
