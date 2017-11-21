INSERT INTO Client VALUES ('123456789', 'John', '13-JUL-94', 'Caucasian', 'Engineer', 'M', 'Doktor', '1234567890', '2345678901', '11-NOV-16');
INSERT INTO Client VALUES ('234567890', 'Jane', '12-MAR-84', 'Caucasian', 'Chef', 'F', 'Doktor', '1234567890', '2345678901', '09-JAN-12');
INSERT INTO Client VALUES ('345678901', 'Hiroki', '04-JUN-95', 'Asian', 'Carpenter', 'M', 'Iisha', '1231112222', '2342221111', '19-MAR-14');
INSERT INTO Client VALUES ('456789012', 'Hitomi', '05-FEB-91', 'Asian', 'Teacher', 'F', 'Iisha', '1231112222', '2342221111', '23-DEC-99');
INSERT INTO Volunteer VALUES ('567890123', 'Bob', '23-FEB-99', 'Caucasian', 'Student', 'M', '20-NOV-17', 'Oklahoma');
INSERT INTO Volunteer VALUES ('678901234', 'Jill', '13-JUL-99', 'Caucasian', 'Student', 'F', '31-OCT-16', 'Florida');
INSERT INTO Volunteer VALUES ('789012345', 'Asad', '12-FEB-84', 'African-American', 'Engineer', 'M', '10-JUN-14', 'Texas');
INSERT INTO Volunteer VALUES ('890123456', 'Ming', '20-AUG-91', 'Asian', 'Musician', 'F', '04-APR-12', 'California');
INSERT INTO Employee VALUES ('901234567', 'Jacob', '12-OCT-94', 'Caucasian', 'Artist', 'M', 80000.00, 'Married', '10-NOV-17');
INSERT INTO Employee VALUES ('135790246', 'Alex', '17-DEC-93', 'Caucasian', 'Artist', 'F', 82500.00, 'Married', '16-OCT-16');
INSERT INTO Donor VALUES ('135790864', 'Bryson', '20-SEP-96', 'Caucasian', 'Engineer', 'M');
INSERT INTO Donor VALUES ('975310862', 'Yuri', '22-MAY-92', 'Asian', 'Engineer', 'F');
INSERT INTO InsurancePolicy VALUES ('12341102', '12341001', '123 W Main St', 'Health');
INSERT INTO InsurancePolicy VALUES ('99011001', '99011234', '200 Broadway Dr', 'Life');
INSERT INTO InsurancePolicy VALUES ('12341103', '12341001', '123 W Main St', 'Health');
INSERT INTO InsurancePolicy VALUES ('12341198', '12341001', '123 W Main St', 'Health');


insert into MYDATES values(to_date('1922/06/01', 'YYYY/MM/DD'));
select * from MYDATES;