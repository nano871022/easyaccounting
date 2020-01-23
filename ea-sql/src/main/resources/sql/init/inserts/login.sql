INSERT INTO TBL_GROUP_USER(scode,sname,nstate,sdescription) 
VALUES('tblgroupuseradministrator001','Administrador',1,'Grupo para administradores.');
INSERT INTO TBL_PERSON (scode,dcreate,sname,sdocument,semail,dbirthday,sdocumenttype) 
VALUES ('tblpersonafirstuser000001','2019-12-10','admin','0000000001','admin@ea.com.co','2019-12-10','parametro2019072400012');
INSERT INTO TBL_USERS(scode,dcreate,sname,spassword,dstart,sperson,sgroupuser,nstate) 
VALUES('tblusersfirstadmin0000001','2019-12-10','admin','Admin2019.','2019-10-12','tblpersonafirstuser000001','tblgroupuseradministrator001',1);