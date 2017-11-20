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
DROP TABLE DonorDonationCK;
DROP TABLE OrganizationDonationCK;
DROP TABLE DonorDonationCC;
DROP TABLE OrganizationDonationCC;
DROP TABLE Client;
DROP TABLE Volunteer;
DROP TABLE Employee;
DROP TABLE Donor;
DROP TABLE InsurancePolicy;
DROP TABLE Team;
DROP TABLE Church;
DROP TABLE Business;
DROP TABLE Org;


CREATE TABLE Client (
    SSN            VARCHAR(9),
    name           VARCHAR(30),
    birth_date     DATE,
    race           VARCHAR(20),
    profession     VARCHAR(20),
    gender         CHAR(1),
    doctor_name    VARCHAR(30),
    doctor_phone   VARCHAR(15),
    attorney_phone VARCHAR(15),
    first_assigned DATE,
    PRIMARY KEY (SSN)
)
ORGANIZATION INDEX;

CREATE INDEX CNameIndex ON Client(name);

CREATE TABLE Volunteer (
    SSN                  VARCHAR(9),
    name                 VARCHAR(30),
    birth_date           DATE,
    race                 VARCHAR(20),
    profession           VARCHAR(20),
    gender               CHAR(1),
    date_joined          DATE,
    most_recent_training VARCHAR(20),
    PRIMARY KEY (SSN)
)
ORGANIZATION INDEX;

CREATE TABLE Employee (
    SSN            VARCHAR(9),
    name           VARCHAR(30),
    birth_date     DATE,
    race           VARCHAR(20),
    profession     VARCHAR(20),
    gender         CHAR(1),
    salary         NUMBER(*, 2),
    marital_status VARCHAR(20),
    hire_date      DATE,
    PRIMARY KEY (SSN)
)
ORGANIZATION INDEX;

CREATE TABLE Donor (
    SSN        VARCHAR(9),
    name       VARCHAR(30),
    birth_date DATE,
    race       VARCHAR(20),
    profession VARCHAR(20),
    gender     CHAR(1),
    PRIMARY KEY (SSN)
)
ORGANIZATION INDEX;

CREATE TABLE InsurancePolicy (
    policy_id     VARCHAR(8),
    provider_id   VARCHAR(8),
    provider_addr VARCHAR(128),
    type          VARCHAR(20),
    PRIMARY KEY (policy_id)
)
ORGANIZATION HEAP;

CREATE TABLE Team (
    name        VARCHAR(30),
    type        VARCHAR(20),
    date_formed DATE,
    leader_SSN  VARCHAR(9),
    PRIMARY KEY (name)
)
ORGANIZATION HEAP;

CREATE TABLE Org (
    name           VARCHAR(30),
    mailing_addr   VARCHAR(128),
    phone          VARCHAR(15),
    contact_person VARCHAR(30),
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
    FOREIGN KEY (SSN) REFERENCES Client,
    FOREIGN KEY (SSN) REFERENCES Volunteer,
    FOREIGN KEY (SSN) REFERENCES Employee,
    FOREIGN KEY (SSN) REFERENCES Donor
)
ORGANIZATION HEAP;

CREATE TABLE Affiliation (
    SSN  VARCHAR(9),
    name VARCHAR(30),
    PRIMARY KEY (SSN, name),
    FOREIGN KEY (SSN) REFERENCES Client,
    FOREIGN KEY (SSN) REFERENCES Volunteer,
    FOREIGN KEY (SSN) REFERENCES Employee,
    FOREIGN KEY (SSN) REFERENCES Donor,
    FOREIGN KEY (name) REFERENCES Org
)
ORGANIZATION HEAP;

CREATE TABLE ContactInfo (
    SSN             VARCHAR(9),
    mailing_addr    VARCHAR(128),
    email_addr      VARCHAR(50),
    home_num        VARCHAR(15),
    cell_num        VARCHAR(15),
    on_mailing_list CHAR(1),
    PRIMARY KEY (SSN),
    FOREIGN KEY (SSN) REFERENCES Client,
    FOREIGN KEY (SSN) REFERENCES Volunteer,
    FOREIGN KEY (SSN) REFERENCES Employee,
    FOREIGN KEY (SSN) REFERENCES Donor
)
ORGANIZATION INDEX;

CREATE TABLE ClientNeed (
    SSN        VARCHAR(9),
    name       VARCHAR(20),
    importance NUMBER(*),
    PRIMARY KEY (SSN, name),
    FOREIGN KEY (SSN) REFERENCES Client
)
ORGANIZATION INDEX;

CREATE INDEX NeedNameIndex ON ClientNeed(name);

CREATE TABLE ClientPolicy (
    SSN       VARCHAR(9),
    policy_id VARCHAR(8),
    PRIMARY KEY (SSN, policy_id),
    FOREIGN KEY (SSN) REFERENCES Client,
    FOREIGN KEY (policy_id) REFERENCES InsurancePolicy
)
ORGANIZATION INDEX;

CREATE INDEX PolicySSNIndex ON ClientPolicy(SSN);

CREATE TABLE CaresFor (
    SSN    VARCHAR(9),
    name   VARCHAR(30),
    active CHAR(1),
    PRIMARY KEY (SSN, name),
    FOREIGN KEY (SSN) REFERENCES Client,
    FOREIGN KEY (name) REFERENCES Team
)
ORGANIZATION INDEX;

CREATE INDEX CaresForSSNIndex ON CaresFor(SSN);

CREATE TABLE ServesOn (
    SSN             VARCHAR(9),
    name            VARCHAR(30),
    sdate           DATE,
    active          CHAR(1),
    hours_per_month NUMBER(*),
    PRIMARY KEY (SSN, name, sdate),
    FOREIGN KEY (SSN) REFERENCES Volunteer,
    FOREIGN KEY (name) REFERENCES Team
)
ORGANIZATION INDEX;

CREATE INDEX ServesOnNameIndex ON ServesOn(name);

CREATE INDEX ServesOnDateIndex ON ServesOn(sdate);

CREATE TABLE ReportsTo (
    name        VARCHAR(30),
    report_date DATE,
    SSN         VARCHAR(9),
    PRIMARY KEY (name, report_date),
    FOREIGN KEY (name) REFERENCES Team,
    FOREIGN KEY (SSN) REFERENCES Employee
)
ORGANIZATION INDEX;

CREATE INDEX ReportSSNIndex ON ReportsTo(SSN);

CREATE TABLE Sponsors (
    oname VARCHAR(30),
    tname VARCHAR(30),
    PRIMARY KEY (oname, tname),
    FOREIGN KEY (oname) REFERENCES Org,
    FOREIGN KEY (tname) REFERENCES Team
)
ORGANIZATION INDEX;

CREATE INDEX ONameIndex ON Sponsors(oname);

CREATE TABLE EmployeeExpense (
    SSN    VARCHAR(9),
    amount NUMBER,
    description VARCHAR(100),
    edate DATE,
    PRIMARY KEY (SSN, amount, description, edate),
    FOREIGN KEY (SSN) REFERENCES Employee
)
ORGANIZATION INDEX;

CREATE INDEX ExpenseDateIndex ON EmployeeExpense(edate);

CREATE TABLE DonorDonationCK (
    SSN VARCHAR(9),
    ddate DATE,
    amount NUMBER(*, 2),
    campaign VARCHAR(30),
    check_num VARCHAR(5),
    anonymous CHAR(1),
    PRIMARY KEY (SSN, ddate, amount, campaign, check_num),
    FOREIGN KEY (SSN) REFERENCES Donor
)
ORGANIZATION INDEX;

CREATE INDEX DonorCKSSNIndex ON DonorDonationCK(SSN);

CREATE TABLE OrganizationDonationCK (
    name VARCHAR(30),
    ddate DATE,
    amount NUMBER(*, 2),
    campaign VARCHAR(30),
    check_num VARCHAR(5),
    anonymous CHAR(1),
    PRIMARY KEY (name, ddate, amount, campaign, check_num),
    FOREIGN KEY (name) REFERENCES Org
)
ORGANIZATION HEAP;

CREATE TABLE DonorDonationCC (
    SSN VARCHAR(9),
    ddate DATE,
    amount NUMBER(*, 2),
    campaign VARCHAR(30),
    card_num VARCHAR(19),
    card_type VARCHAR(16),
    exp_date DATE,
    anonymous CHAR(1),
    PRIMARY KEY (SSN, ddate, amount, campaign, card_num),
    FOREIGN KEY (SSN) REFERENCES Donor
)
ORGANIZATION INDEX;

CREATE INDEX DonorCCSSNIndex ON DonorDonationCC(SSN);

CREATE TABLE OrganizationDonationCC (
    name VARCHAR(30),
    ddate DATE,
    amount NUMBER(*, 2),
    campaign VARCHAR(30),
    card_num VARCHAR(19),
    card_type VARCHAR(16),
    exp_date DATE,
    anonymous CHAR(1),
    PRIMARY KEY (name, ddate, amount, campaign, card_num),
    FOREIGN KEY (name) REFERENCES Org
)
ORGANIZATION HEAP;