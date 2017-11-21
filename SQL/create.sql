DROP INDEX NeedNameIndex;
DROP INDEX PolicySSNIndex;
DROP INDEX CaresForSSNIndex;
DROP INDEX ServesOnNameIndex;
DROP INDEX ReportSSNIndex;
DROP INDEX ONameIndex;
DROP INDEX CNameIndex;
DROP INDEX ExpenseDateIndex;
DROP TABLE TeamLeader;
DROP TABLE TeamReport;
DROP TABLE VolunteerHours;
DROP TABLE Affiliation;
DROP TABLE EmergencyContact;
DROP TABLE ContactInfo;
DROP TABLE ClientNeed;
DROP TABLE ClientPolicy;
DROP TABLE CaresFor;
DROP TABLE ServesOn;
DROP TABLE ReportsTo;
DROP TABLE Sponsors;
DROP TABLE EmployeeExpense;
DROP TABLE DonorCardDonation;
DROP TABLE DonorCheckDonation;
DROP TABLE OrgCardDonation;
DROP TABLE OrgCheckDonation;
DROP TABLE DonorDonation;
DROP TABLE OrgDonation;
DROP TABLE Client;
DROP TABLE Volunteer;
DROP TABLE Employee;
DROP TABLE Donor;
DROP TABLE InsurancePolicy;
DROP TABLE Team;
DROP TABLE Church;
DROP TABLE Business;
DROP TABLE Org;
DROP TABLE Person;

CREATE TABLE Person (
  SSN        VARCHAR(9),
  name       VARCHAR(30),
  birth_date DATE,
  race       VARCHAR(20),
  profession VARCHAR(20),
  gender     CHAR(1),
  PRIMARY KEY (SSN)
);

CREATE TABLE Client (
  SSN            VARCHAR(9),
  doctor_name    VARCHAR(30),
  doctor_phone   VARCHAR(15),
  attorney_phone VARCHAR(15),
  first_assigned DATE,
  PRIMARY KEY (SSN),
  FOREIGN KEY (SSN) REFERENCES Person
)
ORGANIZATION INDEX;

CREATE TABLE Volunteer (
  SSN                  VARCHAR(9),
  date_joined          DATE,
  most_recent_training VARCHAR(20),
  PRIMARY KEY (SSN),
  FOREIGN KEY (SSN) REFERENCES Person
)
ORGANIZATION INDEX;

CREATE TABLE Employee (
  SSN            VARCHAR(9),
  salary         NUMBER(*, 2),
  marital_status VARCHAR(20),
  hire_date      DATE,
  PRIMARY KEY (SSN),
  FOREIGN KEY (SSN) REFERENCES Person
)
ORGANIZATION INDEX;

CREATE TABLE Donor (
  SSN        VARCHAR(9),
  anonymous  CHAR(1),
  PRIMARY KEY (SSN),
  FOREIGN KEY (SSN) REFERENCES Person
)
ORGANIZATION INDEX;

CREATE TABLE InsurancePolicy (
  policy_id     VARCHAR(8),
  provider_id   VARCHAR(8),
  provider_addr VARCHAR(50),
  type          VARCHAR(20),
  PRIMARY KEY (policy_id)
)
ORGANIZATION HEAP;

CREATE TABLE Team (
  name        VARCHAR(30),
  type        VARCHAR(20),
  date_formed DATE,
  PRIMARY KEY (name)
)
ORGANIZATION HEAP;

CREATE TABLE TeamLeader (
  SSN  VARCHAR(9),
  name VARCHAR(30),
  PRIMARY KEY (name),
  FOREIGN KEY (SSN) REFERENCES Volunteer,
  FOREIGN KEY (name) REFERENCES Team
);

CREATE TABLE VolunteerHours (
  SSN         VARCHAR(9),
  name        VARCHAR(30),
  hours       NUMBER(*, 1),
  report_date DATE,
  PRIMARY KEY (SSN, name, report_date),
  FOREIGN KEY (SSN) REFERENCES Volunteer,
  FOREIGN KEY (name) REFERENCES Team
);

CREATE TABLE Org (
  name           VARCHAR(30),
  mailing_addr   VARCHAR(50),
  phone          VARCHAR(15),
  contact_person VARCHAR(30),
  anonymous      CHAR(1),
  PRIMARY KEY (name)
)
ORGANIZATION INDEX;

CREATE TABLE Church (
  name        VARCHAR(30),
  affiliation VARCHAR(30),
  PRIMARY KEY (name),
  FOREIGN KEY (name) REFERENCES Org
)
ORGANIZATION HEAP;

CREATE TABLE Business (
  name    VARCHAR(30),
  type    VARCHAR(30),
  bsize   NUMBER,
  website VARCHAR(30),
  PRIMARY KEY (name),
  FOREIGN KEY (name) REFERENCES Org
)
ORGANIZATION HEAP;

CREATE TABLE EmergencyContact (
  SSN          VARCHAR(9),
  name         VARCHAR(30),
  phone        VARCHAR(15),
  relationship VARCHAR(20),
  PRIMARY KEY (SSN, name, phone, relationship),
  FOREIGN KEY (SSN) REFERENCES Person
)
ORGANIZATION HEAP;

CREATE TABLE Affiliation (
  SSN  VARCHAR(9),
  name VARCHAR(30),
  PRIMARY KEY (SSN, name),
  FOREIGN KEY (SSN) REFERENCES Person,
  FOREIGN KEY (name) REFERENCES Org
)
ORGANIZATION HEAP;

CREATE TABLE ContactInfo (
  SSN             VARCHAR(9),
  mailing_addr    VARCHAR(50),
  email_addr      VARCHAR(50),
  home_num        VARCHAR(15),
  work_num        VARCHAR(15),
  cell_num        VARCHAR(15),
  on_mailing_list CHAR(1),
  PRIMARY KEY (SSN),
  FOREIGN KEY (SSN) REFERENCES Person
)
ORGANIZATION INDEX;

CREATE TABLE ClientNeed (
  SSN        VARCHAR(9),
  name       VARCHAR(20),
  importance NUMBER(*),
  PRIMARY KEY (SSN, name),
  FOREIGN KEY (SSN) REFERENCES Client ON DELETE CASCADE
)
ORGANIZATION INDEX;


CREATE INDEX NeedNameIndex
  ON ClientNeed (name);

CREATE TABLE ClientPolicy (
  SSN       VARCHAR(9),
  policy_id VARCHAR(8),
  PRIMARY KEY (SSN, policy_id),
  FOREIGN KEY (SSN) REFERENCES Client ON DELETE CASCADE,
  FOREIGN KEY (policy_id) REFERENCES InsurancePolicy
)
ORGANIZATION INDEX;

CREATE INDEX PolicySSNIndex
  ON ClientPolicy (SSN);

CREATE TABLE CaresFor (
  SSN    VARCHAR(9),
  name   VARCHAR(30),
  active CHAR(1),
  PRIMARY KEY (SSN, name),
  FOREIGN KEY (SSN) REFERENCES Client ON DELETE CASCADE,
  FOREIGN KEY (name) REFERENCES Team
)
ORGANIZATION INDEX;

CREATE INDEX CaresForSSNIndex
  ON CaresFor (SSN);

CREATE TABLE ServesOn (
  SSN    VARCHAR(9),
  name   VARCHAR(30),
  active CHAR(1),
  PRIMARY KEY (SSN, name),
  FOREIGN KEY (SSN) REFERENCES Volunteer,
  FOREIGN KEY (name) REFERENCES Team
)
ORGANIZATION INDEX;

CREATE INDEX ServesOnNameIndex
  ON ServesOn (name);

CREATE TABLE ReportsTo (
  name VARCHAR(30),
  SSN  VARCHAR(9),
  PRIMARY KEY (name),
  FOREIGN KEY (name) REFERENCES Team,
  FOREIGN KEY (SSN) REFERENCES Employee
)
ORGANIZATION INDEX;

CREATE INDEX ReportSSNIndex
  ON ReportsTo (SSN);

CREATE TABLE TeamReport (
  name        VARCHAR(30),
  report_date DATE,
  content     VARCHAR(300),
  PRIMARY KEY (name, report_date),
  FOREIGN KEY (name) REFERENCES Team
);

CREATE TABLE Sponsors (
  oname VARCHAR(30),
  tname VARCHAR(30),
  PRIMARY KEY (oname, tname),
  FOREIGN KEY (oname) REFERENCES Org,
  FOREIGN KEY (tname) REFERENCES Team
)
ORGANIZATION INDEX;

CREATE INDEX ONameIndex
  ON Sponsors (oname);

CREATE TABLE EmployeeExpense (
  SSN         VARCHAR(9),
  amount      NUMBER(*, 2),
  description VARCHAR(100),
  edate       DATE,
  PRIMARY KEY (SSN, amount, description, edate),
  FOREIGN KEY (SSN) REFERENCES Employee
)
ORGANIZATION INDEX;

CREATE INDEX ExpenseDateIndex
  ON EmployeeExpense (edate);

CREATE TABLE DonorDonation (
  SSN      VARCHAR(9),
  ddate    DATE,
  amount   NUMBER(*, 2),
  campaign VARCHAR(30),
  PRIMARY KEY (SSN, ddate),
  FOREIGN KEY (SSN) REFERENCES Person
);

CREATE TABLE OrgDonation (
  name     VARCHAR(30),
  ddate    DATE,
  amount   NUMBER(*, 2),
  campaign VARCHAR(30),
  PRIMARY KEY (name, ddate),
  FOREIGN KEY (name) REFERENCES Org
);

CREATE TABLE DonorCheckDonation (
  SSN       VARCHAR(9),
  ddate     DATE,
  check_num VARCHAR(5),
  PRIMARY KEY (SSN, ddate, check_num),
  FOREIGN KEY (SSN, ddate) REFERENCES DonorDonation
);

CREATE TABLE DonorCardDonation (
  SSN       VARCHAR(9),
  ddate     DATE,
  card_num  VARCHAR(19),
  card_type VARCHAR(16),
  exp_date  DATE,
  PRIMARY KEY (SSN, ddate, card_num),
  FOREIGN KEY (SSN, ddate) REFERENCES DonorDonation
);

CREATE TABLE OrgCheckDonation (
  name      VARCHAR(30),
  ddate     DATE,
  check_num VARCHAR(5),
  PRIMARY KEY (name, ddate, check_num),
  FOREIGN KEY (name, ddate) REFERENCES OrgDonation
);

CREATE TABLE OrgCardDonation (
  name      VARCHAR(30),
  ddate     DATE,
  card_num  VARCHAR(19),
  card_type VARCHAR(16),
  exp_date  DATE,
  PRIMARY KEY (name, ddate, card_num),
  FOREIGN KEY (name, ddate) REFERENCES DonorDonation
);