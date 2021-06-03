insert into TBL_GROUP_USER(scode,dcreate,sname,nstate,sdescription) 
VALUES('tblgroupuseradministrator001','2019-12-10','Administrador','Grupo para administradores.');
insert into TBL_PERSON (scode,dcreate,sname,sdocument,semail,dbirthday,sdocumenttype) 
VALUES ('tblpersonafirstuser000001','2019-12-10','admin','1','admin@ea.com.co','2019-12-10','parametro2019072400012');
insert into TBL_USERS(scode,dcreate,sname,spassword,dstart,sperson,sgroupuser,nstate) 
VALUES('tblusersfirstadmin0000001','2019-12-10','admin','Admin2019.','2019-10-12','tblpersonafirstuser000001','tblgroupuseradministrator001',1);