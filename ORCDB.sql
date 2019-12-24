create table EMPLOYEE (
   id INT NOT NULL,
   first_name VARCHAR(20) default NULL,
   last_name  VARCHAR(20) default NULL,
   salary     INT  default NULL,
   PRIMARY KEY (id)
);
grant ALL PRIVILEGES on employee to system;

CREATE SEQUENCE HIBERNATE_SEQUENCE
START WITH 1
MAXVALUE 999999999999999999999999999
MINVALUE 1
NOCYCLE
CACHE 20
NOORDER;

grant ALL PRIVILEGES on HIBERNATE_SEQUENCE to system;

create table CERTIFICATE (
   id INT NOT NULL,
   certificate_name VARCHAR(30) default NULL,
   employee_id INT default NULL,
   PRIMARY KEY (id)
);