-- 1. Enter a new team into the database
INSERT INTO TEAM VALUES ('TeamName', 'TeamType', '20-NOV-17');
INSERT INTO TEAM VALUES ('Team2', 'TeamType', '20-NOV-17');

-- 2. Enter a new client into the database and associate him or her with one or more teams
INSERT INTO PERSON VALUES ('123456789');
INSERT INTO CLIENT VALUES
  ('123456789', 'John', '13-JUL-94', 'Caucasian', 'Engineer', 'M', 'Doktor', '1234567890', '2345678901', '11-NOV-16');
INSERT INTO CARESFOR VALUES ('123456789', 'TeamName', 'T');
INSERT INTO CONTACTINFO VALUES ('123456789', 'john addr', 'john@addr.com', '1357', '2468', 'F');

-- 3. Enter a new volunteer into the database and associate him or her with one or more teams
INSERT INTO PERSON VALUES ('567890123');
INSERT INTO Volunteer VALUES ('567890123', 'Bob', '23-FEB-99', 'Caucasian', 'Student', 'M', '20-NOV-17', 'Oklahoma');
INSERT INTO SERVESON VALUES ('567890123', 'TeamName', 'T');
--if no team leader for team, insert into TEAMLEADER
SELECT *
FROM TEAMLEADER
WHERE NAME = 'TeamName';
INSERT INTO TEAMLEADER VALUES ('567890123', 'TeamName');

-- 4. Enter the number of hours a volunteer worked this month for a particular team
INSERT INTO VOLUNTEERHOURS VALUES ('567890123', 'TeamName', 10.5, sysdate);

-- 5. Enter a new employee into the database and associate him or her with one or more teams
INSERT INTO PERSON VALUES ('901234567');
INSERT INTO Employee
VALUES ('901234567', 'Jacob', '12-OCT-94', 'Caucasian', 'Artist', 'M', 80000.00, 'Married', '10-NOV-17');
-- Given list Team1,Team2,,...
INSERT INTO REPORTSTO VALUES ('TeamName', '901234567');
INSERT INTO REPORTSTO VALUES ('Team2', '901234567');
-- ...

-- 6. Enter an expense charged by an employee
INSERT INTO EMPLOYEEEXPENSE VALUES ('901234567', 150.00, 'Ink cartridges for printer', '05-OCT-15');
INSERT INTO EMPLOYEEEXPENSE VALUES ('901234567', 20.00, 'New ID Badge', '05-OCT-16');
INSERT INTO EMPLOYEEEXPENSE VALUES ('901234567', 2.50, 'Forgot lunch money', '05-OCT-17');

-- 7. Enter a new organization and associate it to one or more PAN teams
INSERT INTO ORG VALUES ('OrgName', '11 Mail St', '1234567890', 'Contak', 'F');
-- Given list Team1,Team2,,...
INSERT INTO SPONSORS VALUES ('OrgName', 'TeamName');
--INSERT INTO SPONSORS VALUES ('OrgName', 'Team2');
-- ...

-- 8. Enter a new donor and associate him or her with several donations
INSERT INTO PERSON VALUES ('975310862');
INSERT INTO Donor VALUES ('975310862', 'Yuri', to_date('1992/05/22', 'YYYY/MM/DD'), 'Asian', 'Engineer', 'F', 'F');

INSERT INTO DONORDONATION
VALUES ('975310862', to_date('2017/11/17 08:30:00', 'YYYY/MM/DD HH24:MI:SS'), 1500.00, 'CampName');
-- if card
INSERT INTO DONORCARDDONATION VALUES
  ('975310862', to_date('2017/11/17 08:30:00', 'YYYY/MM/DD HH24:MI:SS'), '403748372932', 'Visa',
   to_date('2022/06/01', 'YYYY/MM/DD'));

-- 9. Enter a new organization and associate it with several donations
INSERT INTO ORG VALUES ('OrgName2', '11 Mail St', '1234567890', 'Contak', 'F');
INSERT INTO ORGDONATION
VALUES ('OrgName2', to_date('2017/11/17 11:45:00', 'YYYY/MM/DD HH24:MI:SS'), 1500.00, 'CampName');
-- if check
INSERT INTO ORGCHECKDONATION VALUES
  ('OrgName2', to_date('2017/11/17 11:45:00', 'YYYY/MM/DD HH24:MI:SS'), '12345');

-- 10. Retrieve the name and phone number of the doctor of a particular client
SELECT
  DOCTOR_NAME,
  DOCTOR_PHONE
FROM CLIENT
WHERE SSN = '123456789';

-- 11. Retrieve the total amount of expenses charged by each employee for a particular period of time.
-- The list should be sorted by the total amount of expenses
SELECT
  SSN,
  SUM(amount) AS A
FROM EMPLOYEEEXPENSE
WHERE EDATE >= to_date('2015/09/01', 'YYYY/MM/DD') AND EDATE <= to_date('2016/01/01', 'YYYY/MM/DD')
GROUP BY SSN
ORDER BY A DESC;

-- 12. Retrieve the list of volunteers that are members of teams that support a particular client
SELECT SSN
FROM SERVESON
WHERE name IN (
  SELECT name
  FROM CARESFOR
  WHERE SSN = '123');

-- 13. Retrieve the names and contact information of the clients that are supported by teams
-- sponsored by an organization whose name starts with a letter between B and K.
-- The client list should be sorted by name
INSERT INTO ORG VALUES ('ChingChong', '11 Mail St', '1234567890', 'Contak', 'T');
INSERT INTO SPONSORS VALUES ('ChingChong', 'TeamName');

SELECT
  q1.NAME,
  q2.MAILING_ADDR,
  q2.EMAIL_ADDR,
  q2.HOME_NUM,
  q2.WORK_NUM,
  q2.CELL_NUM
FROM (
       SELECT
         SSN,
         NAME
       FROM PERSON
       WHERE SSN IN (
         SELECT SSN
         FROM CARESFOR
         WHERE NAME IN (
           SELECT TNAME
           FROM SPONSORS
           WHERE ONAME BETWEEN 'B' AND 'L'))) q1
  INNER JOIN
  (SELECT
     SSN,
     MAILING_ADDR,
     EMAIL_ADDR,
     HOME_NUM,
     WORK_NUM,
     CELL_NUM
   FROM CONTACTINFO
   WHERE SSN IN (
     SELECT SSN
     FROM CARESFOR
     WHERE NAME IN (
       SELECT TNAME
       FROM SPONSORS
       WHERE ONAME BETWEEN 'B' AND 'L'))) q2
    ON q1.SSN = q2.SSN
ORDER BY q1.NAME ASC;

-- 14. Retrieve the name and total amount donated by donors that are also employees.
-- The list should be sorted by the total amount of the donations,
-- and indicate if each donor wishes to remain anonymous
INSERT INTO PERSON VALUES ('901234534');
INSERT INTO Employee
VALUES ('901234534', 'Bob', '13-OCT-94', 'Caucasian', 'Artist', 'M', 70000.00, 'Single', '09-NOV-17');
INSERT INTO Donor
VALUES ('901234534', 'Bob', '13-OCT-94', 'Caucasian', 'Artist', 'M', 'T');
INSERT INTO DONORDONATION
VALUES ('901234534', sysdate, 1000.00, 'NewCamp');

SELECT
  q2.NAME,
  q1.A,
  q1.ANONYMOUS
FROM
  (SELECT
     DONOR.SSN,
     SUM(AMOUNT) AS A,
     DONOR.ANONYMOUS
   FROM DONOR, DONORDONATION
   WHERE DONORDONATION.SSN = DONOR.SSN AND DONOR.SSN IN
                                           ((SELECT SSN
                                             FROM DONOR)
                                            INTERSECT
                                            (SELECT SSN
                                             FROM EMPLOYEE))
   GROUP BY DONOR.SSN, DONOR.ANONYMOUS) q1
  INNER JOIN
  (SELECT
     SSN,
     NAME
   FROM PERSON
   WHERE SSN IN (
     (SELECT SSN
      FROM DONOR)
     INTERSECT
     (SELECT SSN
      FROM EMPLOYEE))) q2
    ON q1.SSN = q1.SSN

ORDER BY A DESC;

-- 15. For each team, retrieve the name and associated contact information of the
-- volunteer that has worked the most total hours between March and June
INSERT INTO PERSON VALUES ('567890101');
INSERT INTO Volunteer VALUES ('567890101', 'Sasuke', '23-FEB-98', 'Asian', 'Shinobi', 'M', '12-SEP-15', 'Japan');
INSERT INTO SERVESON VALUES ('567890101', 'TeamName', 'T');
INSERT INTO CONTACTINFO VALUES ('567890101', 'sasuke addr', 'sasuke@addr.com', '2345', '3456', 'T');
INSERT INTO CONTACTINFO VALUES ('567890123', 'bob addr', 'bob@addr.com', '2345', '3456', 'T');
INSERT INTO VOLUNTEERHOURS VALUES ('567890101', 'TeamName', 10.5, to_date('2017/04/26', 'YYYY/MM/DD'));
INSERT INTO VOLUNTEERHOURS VALUES ('567890101', 'TeamName', 8.5, to_date('2017/05/24', 'YYYY/MM/DD'));
INSERT INTO VOLUNTEERHOURS VALUES ('567890101', 'TeamName', 11.0, to_date('2017/06/26', 'YYYY/MM/DD'));
INSERT INTO VOLUNTEERHOURS VALUES ('567890123', 'TeamName', 3.5, to_date('2017/03/26', 'YYYY/MM/DD'));
INSERT INTO VOLUNTEERHOURS VALUES ('567890123', 'TeamName', 5.5, to_date('2017/04/29', 'YYYY/MM/DD'));
INSERT INTO VOLUNTEERHOURS VALUES ('567890123', 'TeamName', 9.0, to_date('2017/05/21', 'YYYY/MM/DD'));


INSERT INTO PERSON VALUES ('567890222');
INSERT INTO Volunteer VALUES ('567890222', 'Joe', '23-FEB-99', 'Caucasian', 'Student', 'M', '20-NOV-17', 'Oklahoma');
INSERT INTO SERVESON VALUES ('567890222', 'Team2', 'T');
INSERT INTO CONTACTINFO VALUES ('567890222', 'joe addr', 'joe@addr.com', '2345', '3456', 'T');
INSERT INTO VOLUNTEERHOURS VALUES ('567890222', 'Team2', 9.0, to_date('2017/05/21', 'YYYY/MM/DD'));
INSERT INTO VOLUNTEERHOURS VALUES ('567890222', 'Team2', 19.0, to_date('2017/04/26', 'YYYY/MM/DD'));

SELECT
  v.NAME,
  c.MAILING_ADDR,
  c.EMAIL_ADDR,
  c.HOME_NUM,
  c.CELL_NUM
FROM VOLUNTEER v, CONTACTINFO c
WHERE v.SSN = c.SSN AND v.SSN IN (
  SELECT q1.SSN
  FROM
    (SELECT
       SSN,
       SUM(HOURS) AS G
     FROM VOLUNTEERHOURS
     GROUP BY SSN) q1
    INNER JOIN
    (SELECT
       NAME,
       MAX(H) AS F
     FROM
       (
         SELECT
           NAME,
           SSN,
           SUM(HOURS) AS H
         FROM VOLUNTEERHOURS
         GROUP BY NAME, SSN
       )
     GROUP BY NAME) q2
      ON q1.G = q2.F);



WITH CTE1 AS (
    SELECT q1.SSN,
      q2.NAME
    FROM
      (SELECT
         SSN,
         SUM(HOURS) AS G
       FROM VOLUNTEERHOURS
       GROUP BY SSN) q1
      INNER JOIN
      (SELECT
         NAME,
         MAX(H) AS F
       FROM
         (SELECT
            NAME,
            SSN,
            SUM(HOURS) AS H
          FROM VOLUNTEERHOURS
          GROUP BY NAME, SSN
         )
       GROUP BY NAME) q2
        ON q1.G = q2.F)
SELECT
  q3.TNAME,
  q3.NAME,
  q4.MAILING_ADDR,
  q4.EMAIL_ADDR,
  q4.HOME_NUM,
  q4.WORK_NUM,
  q4.CELL_NUM
FROM
  (SELECT
     PERSON.SSN,
     PERSON.NAME,
     CTE1.NAME AS TNAME
   FROM PERSON
     INNER JOIN CTE1
       ON PERSON.SSN = CTE1.SSN) q3
  INNER JOIN
  (SELECT
     CONTACTINFO.SSN,
     MAILING_ADDR,
     EMAIL_ADDR,
     HOME_NUM,
     WORK_NUM,
     CELL_NUM
   FROM CONTACTINFO
     INNER JOIN CTE1
       ON CONTACTINFO.SSN = CTE1.SSN) q4
    ON q3.SSN = q4.SSN;

SELECT PERSON.SSN,
FROM PERSON
  INNER JOIN CONTACTINFO
-- 16. Increase the salary by 10% of all employees to whom more than one team must report
INSERT INTO TEAM VALUES ('Team3', 'TeamType', '20-NOV-17');
INSERT INTO REPORTSTO VALUES ('Team3', '901234534');


UPDATE EMPLOYEE
SET SALARY = SALARY * 1.1
WHERE SSN IN (
  SELECT SSN
  FROM (
    SELECT
      SSN,
      COUNT(SSN) AS C
    FROM REPORTSTO
    GROUP BY SSN)
  WHERE C > 1);

-- 17. Delete all clients who do not have health insurance and
-- whose value of importance for transportation is less than 5
INSERT INTO CLIENTNEED VALUES ('123456789', 'transportation', 3);

DELETE FROM CLIENT
WHERE SSN IN (
  SELECT SSN
  FROM CLIENTNEED
  WHERE name = 'transportation' AND importance < 5);

-- 19 Retrieve names and mailing addresses of people on mailing list
SELECT
  PERSON.NAME,
  CONTACTINFO.MAILING_ADDR
FROM PERSON
INNER JOIN CONTACTINFO
ON PERSON.SSN = CONTACTINFO.SSN
WHERE ON_MAILING_LIST = 'Y';
