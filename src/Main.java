import oracle.jdbc.proxy.annotation.Pre;

import javax.xml.transform.Result;
import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.SortedMap;

public class Main {
    public static void main(String[] args) {
        String dbURL = "jdbc:oracle:thin:@//oracle.cs.ou.edu:1521/pdborcl.cs.ou.edu",
               login = "tse1666",
               pw = "QYbr6Sm6";

        // Create JDBC Connection
        try {
            System.out.println("Connecting to database...");
            Connection dbConnection = DriverManager.getConnection(dbURL, login, pw);
            System.out.println("WELCOME TO THE PAN CLIENT AND DONOR DATABASE SYSTEM");
            run(dbConnection);
        } catch (SQLException x) {
            x.printStackTrace();
            System.err.println("Error: Couldn't connect to the database.");
        }
    }

    public static void run(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        int input;
        while(true) {
            printInstructions();
            input = scanner.nextInt();
            switch (input) {
                case 1:
                    insertTeam(dbc);
                    break;
                case 2:
                    insertClient(dbc);
                    break;
                case 3:
                    insertVolunteer(dbc);
                    break;
                case 4:
                    recordVolunteerHours(dbc);
                    break;
                case 5:
                    insertEmployee(dbc);
                    break;
                case 6:
                    recordExpense(dbc);
                    break;
                case 7:
                    insertSponsor(dbc);
                    break;
                case 8:
                    insertDonor(dbc);
                    break;
                case 9:
                    insertDonorOrg(dbc);
                    break;
                case 10:
                    getClientDoctor(dbc);
                    break;
                case 11:
                    getExpenses(dbc);
                    break;
                case 12:
                    getSupportVolunteers(dbc);
                    break;
                case 13:
                    getBtoK(dbc);
                    break;
                case 14:
                    getDonorEmployees(dbc);
                    break;
                case 15:
                    getMostHourVolunteer(dbc);
                    break;
                case 16:
                    increaseSalary(dbc);
                    break;
                case 17:
                    deleteSomeClients(dbc);
                    break;
                case 18:
                    importTeams(dbc);
                    break;
                case 19:
                    exportMailingList(dbc);
                    break;
                case 20:
                    System.out.println();
                    return;
                default:
                    System.out.println("Unknown command; please try again.");
            }
        }
    }

    public static void fail(Scanner scanner, String msg) {
        System.out.println(msg);
        System.out.println("Returning to menu. Press enter to continue...");
        scanner.nextLine();
    }

    public static boolean insertPerson(Connection dbc, Scanner scanner, String SSN) {
        String insertPerson = "INSERT INTO PERSON VALUES (?, ?, to_date(?, 'YYYY/MM/DD'), ?, ?, ?)";

        System.out.print("Please enter the name: ");
        String name = scanner.nextLine();
        System.out.print("Please enter the birth date (YYYY/MM/DD): ");
        String bdate = scanner.nextLine();
        System.out.print("Please enter the race: ");
        String race = scanner.nextLine();
        System.out.print("Please enter the profession: ");
        String profession = scanner.nextLine();
        System.out.print("Please enter the gender (M/F): ");
        String gender = scanner.nextLine();

        PreparedStatement stmt;
        try {
            stmt = dbc.prepareStatement(insertPerson);
            stmt.setString(1, SSN);
            stmt.setString(2, name);
            stmt.setString(3, bdate);
            stmt.setString(4, race);
            stmt.setString(5, profession);
            stmt.setString(6, gender);
            stmt.executeUpdate();
            System.out.println("Inserted into Person");
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return false;
        }

        String insertClientContact = "INSERT INTO CONTACTINFO VALUES " +
                "(?, ?, ?, ?, ?, ?, ?)";
        System.out.print("Please enter the client's mailing address: ");
        String mail = scanner.nextLine();
        System.out.print("Please enter the client's email address: ");
        String email = scanner.nextLine();
        System.out.print("Please enter the client's home number: ");
        String homephone = scanner.nextLine();
        System.out.print("Please enter the client's work number: ");
        String workphone = scanner.nextLine();
        System.out.print("Please enter the client's cell number: ");
        String cellphone = scanner.nextLine();
        System.out.print("Add client to mailing list? (Y/N) ");
        String maillist = scanner.nextLine();
        try {
            stmt = dbc.prepareStatement(insertClientContact);
            stmt.setString(1, SSN);
            stmt.setString(2, mail);
            stmt.setString(3, email);
            stmt.setString(4, homephone);
            stmt.setString(5, workphone);
            stmt.setString(6, cellphone);
            stmt.setString(7, maillist);
            stmt.executeUpdate();
            System.out.println("Inserted to ContactInfo");
            return true;
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return false;
        }
    }

    public static boolean checkSSN(Connection dbc, String SSN, String table) {
        PreparedStatement stmt;
        String checkSSN = "SELECT COUNT(*) AS C FROM " + table + " WHERE SSN = ?";
        ResultSet res;
        try {
            stmt = dbc.prepareStatement(checkSSN);
            stmt.setString(1, SSN);
            res = stmt.executeQuery();
            res.next();
            if (res.getInt("C") > 0) {
                System.out.println("SSN exists in " + table +  ".");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return true;
        }
    }

    public static void insertTeam(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter team name: ");
        String name = scanner.nextLine();
        System.out.print("Please enter the team type: ");
        String type = scanner.nextLine();
        String insertTeam = "INSERT INTO TEAM VALUES (?, ?, sysdate)";
        PreparedStatement stmt;
        try {
            stmt = dbc.prepareStatement(insertTeam);
            stmt.setString(1, name);
            stmt.setString(2, type);
            stmt.executeUpdate();
            System.out.println("Team insertion successful");
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
        }
    }

    public static void insertClient(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;
        System.out.print("Please enter the SSN: ");
        String SSN = scanner.nextLine();
        if(checkSSN(dbc, SSN, "PERSON")) {
            System.out.println("Skipping insertion into Person");
        } else {
            insertPerson(dbc, scanner, SSN);
        }

        String insertClient = "INSERT INTO CLIENT VALUES " +
                "(?, ?, ?, ?, sysdate)";
        System.out.print("Please enter the name of the client's doctor: ");
        String drname = scanner.nextLine();
        System.out.print("Please enter the phone number of the client's doctor: ");
        String drphone = scanner.nextLine();
        System.out.print("Please enter the phone number of the client's attorney: ");
        String attphone = scanner.nextLine();
        try {
            stmt = dbc.prepareStatement(insertClient);
            stmt.setString(1, SSN);
            stmt.setString(2, drname);
            stmt.setString(3, drphone);
            stmt.setString(4, attphone);
            stmt.executeUpdate();
            System.out.println("Inserted into Client");
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }

        String insertCaresFor = "INSERT INTO CARESFOR VALUES (?, ?, 'T')";
        System.out.print("Please enter the list of the teams that will take care of the client (Team1,Team2,...): ");
        String teams = scanner.nextLine();
        String[] teamArr = teams.split(",");
        try {
            // insert CaresFor for all teams specified
            for (String team : teamArr) {
                stmt = dbc.prepareStatement(insertCaresFor);
                stmt.setString(1, SSN);
                stmt.setString(2, team);
                stmt.executeUpdate();
            }
            System.out.println("Inserted to CaresFor");
            System.out.println("Client insertion complete. Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void insertVolunteer(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;
        System.out.print("Please enter the SSN of the volunteer: ");
        String SSN = scanner.nextLine();

        if(checkSSN(dbc, SSN, "PERSON")) {
            System.out.println("Skipping insertion into Person");
        } else {
            insertPerson(dbc, scanner, SSN);
        }

        String insertVolunteer = "INSERT INTO Volunteer VALUES (?, sysdate, ?)";
        System.out.print("Please enter the location of the volunteer's most recent training: ");
        String trainloc = scanner.nextLine();
        try {
            stmt = dbc.prepareStatement(insertVolunteer);
            stmt.setString(1, SSN);
            stmt.setString(2, trainloc);
            stmt.executeUpdate();
            System.out.println("Inserted into Volunteer");
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }

        String insertServesOn = "INSERT INTO SERVESON VALUES (?, ?, 'T')";
        System.out.print("Please enter the list of teams this volunteer will serve (Team1,Team2,...): ");
        String teams = scanner.nextLine();
        String[] teamArr = teams.split(",");
        try {
            for (String team : teamArr) {
                stmt = dbc.prepareStatement(insertServesOn);
                stmt.setString(1, SSN);
                stmt.setString(2, team);
                stmt.executeUpdate();
            }
            System.out.println("Inserted into ServesOn");
            System.out.println("Volunteer insertion complete. Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void recordVolunteerHours(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;

        String insertHours = "INSERT INTO VOLUNTEERHOURS VALUES (?, ?, ?, sysdate)";
        System.out.print("Enter the SSN of the volunteer whose hours you'd like to record: ");
        String SSN = scanner.nextLine();
        System.out.print("Enter the team for which you'd like to record: ");
        String team = scanner.nextLine();
        System.out.print("Enter the number of hours worked: ");
        float hours = scanner.nextFloat();
        scanner.nextLine();
        try {
            stmt = dbc.prepareStatement(insertHours);
            stmt.setString(1, SSN);
            stmt.setString(2, team);
            stmt.setFloat(3, hours);
            stmt.executeUpdate();
            System.out.println("Record successful");
            System.out.println("Returning to menu. Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e){
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void insertEmployee(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;
        System.out.print("Please enter the SSN of the employee: ");
        String SSN = scanner.nextLine();

        if(checkSSN(dbc, SSN, "PERSON")) {
            System.out.println("Skipping insertion into Person");
        } else {
            insertPerson(dbc, scanner, SSN);
        }

        String insertEmployee = "INSERT INTO Employee VALUES (?, ?, ?, sysdate)";
        System.out.print("Please enter the employee's salary: ");
        float salary = scanner.nextFloat();
        scanner.nextLine();
        System.out.print("Please enter the employee's marital status: ");
        String mstatus = scanner.nextLine();
        try {
            stmt = dbc.prepareStatement(insertEmployee);
            stmt.setString(1, SSN);
            stmt.setFloat(2, salary);
            stmt.setString(3, mstatus);
            stmt.executeUpdate();
            System.out.println("Inserted into Employee");
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }

        String insertReportsTo = "INSERT INTO REPORTSTO VALUES (?, ?)";
        System.out.print("Please enter the list of teams this volunteer will serve (Team1,Team2,...): ");
        String teams = scanner.nextLine();
        String[] teamArr = teams.split(",");
        try {
            for (String team : teamArr) {
                stmt = dbc.prepareStatement(insertReportsTo);
                stmt.setString(1, team);
                stmt.setString(2, SSN);
                stmt.executeUpdate();
            }
            System.out.println("Inserted into ReportsTo");
            System.out.println("Employee insertion complete. Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void recordExpense(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;

        String insertExpense = "INSERT INTO EMPLOYEEEXPENSE VALUES (?, ?, ?, to_date(?, 'YYYY/MM/DD'))";
        System.out.print("Enter the SSN of the employee recording the expense: ");
        String SSN = scanner.nextLine();
        System.out.print("Enter the expense amount: ");
        float amount = scanner.nextFloat();
        scanner.nextLine();
        System.out.print("Enter the expense description: ");
        String desc = scanner.nextLine();
        System.out.print("Enter the date of the expense: ");
        String date = scanner.nextLine();

        try {
            stmt = dbc.prepareStatement(insertExpense);
            stmt.setString(1, SSN);
            stmt.setFloat(2, amount);
            stmt.setString(3, desc);
            stmt.setString(4, date);
            stmt.executeUpdate();
            System.out.println("Record successful");
            System.out.println("Returning to menu. Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void insertSponsor(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;

        String insertOrg = "INSERT INTO ORG VALUES (?, ?, ?, ?, ?)";
        String insertChurch = "INSERT INTO CHURCH VALUES (?, ?)";
        String insertBusiness = "INSERT INTO BUSINESS VALUES (?, ?, ?, ?)";
        System.out.print("Enter the name of the organization: ");
        String name = scanner.nextLine();
        System.out.print("Enter the address of the organization: ");
        String addr = scanner.nextLine();
        System.out.print("Enter the phone of the organization: ");
        String phone = scanner.nextLine();
        System.out.print("Enter the name of the contact person of the organization: ");
        String contact = scanner.nextLine();
        System.out.print("Does this organization wish to be anonymous? (T/F): ");
        String anon = scanner.nextLine();
        System.out.print("Is this organization a [C]hurch, [B]usiness, or [N]either? (C/B/N): ");
        String type = scanner.nextLine();
        try {
            stmt = dbc.prepareStatement(insertOrg);
            stmt.setString(1, name);
            stmt.setString(2, addr);
            stmt.setString(3, phone);
            stmt.setString(4, contact);
            stmt.setString(5, anon);
            stmt.executeUpdate();
            System.out.println("Inserted into Org");
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }

        if (type.equals("C")) {
            System.out.print("Enter the affiliation of the church: ");
            String affil = scanner.nextLine();
            try {
                stmt = dbc.prepareStatement(insertChurch);
                stmt.setString(1, name);
                stmt.setString(2, affil);
                stmt.executeUpdate();
                System.out.println("Inserted into Church");
            } catch (SQLException e) {
                fail(scanner, e.getMessage());
                return;
            }
        }

        if (type.equals("B")) {
            System.out.print("Enter the type of business: ");
            String btype = scanner.nextLine();
            System.out.print("Enter the number of employees at this company: ");
            int bsize = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter the website of the business: ");
            String site = scanner.nextLine();
            try {
                stmt = dbc.prepareStatement(insertBusiness);
                stmt.setString(1, name);
                stmt.setString(2, btype);
                stmt.setInt(3, bsize);
                stmt.setString(4, site);
                stmt.executeUpdate();
                System.out.println("Inserted into Business");
            } catch (SQLException e) {
                fail(scanner, e.getMessage());
                return;
            }
        }

        String orgTeam = "INSERT INTO SPONSORS VALUES (?, ?)";
        System.out.print("Please enter the list of teams this organization sponsors (Team1,Team2,...): ");
        String teams = scanner.nextLine();
        String[] teamArr = teams.split(",");
        try {
            for (String team : teamArr) {
                stmt = dbc.prepareStatement(orgTeam);
                stmt.setString(1, name);
                stmt.setString(2, team);
                stmt.executeUpdate();
            }
            System.out.println("Inserted into Sponsors");
            System.out.println("Org insertion complete. Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void insertDonor(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;
        System.out.print("Please enter the SSN of the donor: ");
        String SSN = scanner.nextLine();

        if(checkSSN(dbc, SSN, "PERSON")) {
            System.out.println("Skipping insertion into Person");
        } else {
            insertPerson(dbc, scanner, SSN);
        }

        String insertDonor = "INSERT INTO DONOR VALUES (?, ?)";
        System.out.print("Enter whether the donor would like to be anonymous (T/F): ");
        String anon = scanner.nextLine();
        try {
            stmt = dbc.prepareStatement(insertDonor);
            stmt.setString(1, SSN);
            stmt.setString(2, anon);
            stmt.executeUpdate();
            System.out.println("Inserted into Donor");
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
        System.out.print("How many donations to insert for " + SSN + "?");
        int n = scanner.nextInt();
        scanner.nextLine();

        String insertDonation = "INSERT INTO DONORDONATION VALUES (?, to_date(?, 'YYYY/MM/DD'), ?, ?)";
        String insertDonorCheck = "INSERT INTO DONORCHECKDONATION VALUES (?, to_date(?, 'YYYY/MM/DD'), ?)";
        String insertDonorCard = "INSERT INTO DONORCARDDONATION VALUES (?, to_date(?, 'YYYY/MM/DD'), ?, ?, to_date(?, 'YYYY/MM/DD'))";

        for (int i = 0; i < n; i++) {
            System.out.print("Enter the date of the donation (YYYY/MM/DD): ");
            String ddate = scanner.nextLine();
            System.out.print("Enter the amount of the donation: ");
            float amount = scanner.nextFloat();
            scanner.nextLine();
            System.out.print("Enter the campaign of the donation: ");
            String camp = scanner.nextLine();
            try {
                stmt = dbc.prepareStatement(insertDonation);
                stmt.setString(1, SSN);
                stmt.setString(2, ddate);
                stmt.setFloat(3, amount);
                stmt.setString(4, camp);
                stmt.executeUpdate();
                System.out.println("Inserted to DonorDonation");
            } catch (SQLException e) {
                fail(scanner, e.getMessage());
                return;
            }
            System.out.print("Enter whether it was by check or card (check/card): ");
            String dtype = scanner.nextLine();

            if (dtype.equals("check")) {
                System.out.print("Enter the check number: ");
                String check_num = scanner.nextLine();
                try {
                    stmt = dbc.prepareStatement(insertDonorCheck);
                    stmt.setString(1, SSN);
                    stmt.setString(2, ddate);
                    stmt.setString(3, check_num);
                    stmt.executeUpdate();
                    System.out.println("Inserted to DonorCheckDonation");
                } catch (SQLException e) {
                    fail(scanner, e.getMessage());
                    return;
                }
            }
            if (dtype.equals("card")) {
                System.out.print("Enter the card number: ");
                String card_num = scanner.nextLine();
                System.out.print("Enter the card type: ");
                String card_type = scanner.nextLine();
                System.out.print("Enter the card expiry date: ");
                String card_exp = scanner.nextLine();
                try {
                    stmt = dbc.prepareStatement(insertDonorCard);
                    stmt.setString(1, SSN);
                    stmt.setString(2, ddate);
                    stmt.setString(3, card_num);
                    stmt.setString(4, card_type);
                    stmt.setString(5, card_exp);
                    stmt.executeUpdate();
                    System.out.println("Inserted to DonorCardDonation");
                } catch (SQLException e) {
                    fail(scanner, e.getMessage());
                    return;
                }
            }

        }
        System.out.println("Donor and donation insertions complete. Press enter to continue...");
        scanner.nextLine();
    }

    public static void insertDonorOrg(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;

        String insertOrg = "INSERT INTO ORG VALUES (?, ?, ?, ?, ?)";
        String insertChurch = "INSERT INTO CHURCH VALUES (?, ?)";
        String insertBusiness = "INSERT INTO BUSINESS VALUES (?, ?, ?, ?)";
        System.out.print("Enter the name of the donor organization: ");
        String name = scanner.nextLine();
        System.out.print("Enter the address of the donor organization: ");
        String addr = scanner.nextLine();
        System.out.print("Enter the phone of the donor organization: ");
        String phone = scanner.nextLine();
        System.out.print("Enter the name of the contact person of the donor organization: ");
        String contact = scanner.nextLine();
        System.out.print("Does this donor organization wish to be anonymous? (T/F): ");
        String anon = scanner.nextLine();
        System.out.print("Is this organization a [C]hurch, [B]usiness, or [N]either? (C/B/N): ");
        String type = scanner.nextLine();
        try {
            stmt = dbc.prepareStatement(insertOrg);
            stmt.setString(1, name);
            stmt.setString(2, addr);
            stmt.setString(3, phone);
            stmt.setString(4, contact);
            stmt.setString(5, anon);
            stmt.executeUpdate();
            System.out.println("Inserted into Org");
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
        if (type.equals("C")) {
            System.out.print("Enter the affiliation of the church: ");
            String affil = scanner.nextLine();
            try {
                stmt = dbc.prepareStatement(insertChurch);
                stmt.setString(1, name);
                stmt.setString(2, affil);
                stmt.executeUpdate();
                System.out.println("Inserted into Church");
            } catch (SQLException e) {
                fail(scanner, e.getMessage());
                return;
            }
        }

        if (type.equals("B")) {
            System.out.print("Enter the type of business: ");
            String btype = scanner.nextLine();
            System.out.print("Enter the number of employees at this company: ");
            int bsize = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter the website of the business: ");
            String site = scanner.nextLine();
            try {
                stmt = dbc.prepareStatement(insertBusiness);
                stmt.setString(1, name);
                stmt.setString(2, btype);
                stmt.setInt(3, bsize);
                stmt.setString(4, site);
                stmt.executeUpdate();
                System.out.println("Inserted into Business");
            } catch (SQLException e) {
                fail(scanner, e.getMessage());
                return;
            }
        }

        System.out.print("How many donations to insert for " + name + "?");
        int n = scanner.nextInt();
        scanner.nextLine();

        String insertOrgDonation = "INSERT INTO ORGDONATION VALUES (?, to_date(?, 'YYYY/MM/DD'), ?, ?)";
        String insertOrgCheck = "INSERT INTO DONORCHECKDONATION VALUES (?, to_date(?, 'YYYY/MM/DD'), ?)";
        String insertOrgCard = "INSERT INTO DONORCARDDONATION VALUES (?, to_date(?, 'YYYY/MM/DD'), ?, ?, to_date(?, 'YYYY/MM/DD'))";

        for (int i = 0; i < n; i++) {
            System.out.print("Enter the date of the donation (YYYY/MM/DD): ");
            String ddate = scanner.nextLine();
            System.out.print("Enter the amount of the donation: ");
            float amount = scanner.nextFloat();
            scanner.nextLine();
            System.out.print("Enter the campaign of the donation: ");
            String camp = scanner.nextLine();
            try {
                stmt = dbc.prepareStatement(insertOrgDonation);
                stmt.setString(1, name);
                stmt.setString(2, ddate);
                stmt.setFloat(3, amount);
                stmt.setString(4, camp);
                stmt.executeUpdate();
                System.out.println("Inserted to OrgDonation");
            } catch (SQLException e) {
                fail(scanner, e.getMessage());
                return;
            }

            System.out.print("Enter whether it was by check or card (check/card): ");
            String dtype = scanner.nextLine();
            if (dtype.equals("check")) {
                System.out.print("Enter the check number: ");
                String check_num = scanner.nextLine();
                try {
                    stmt = dbc.prepareStatement(insertOrgCheck);
                    stmt.setString(1, name);
                    stmt.setString(2, ddate);
                    stmt.setString(3, check_num);
                    stmt.executeUpdate();
                    System.out.println("Inserted to OrgCheckDonation");
                } catch (SQLException e) {
                    fail(scanner, e.getMessage());
                    return;
                }
            }
            if (dtype.equals("card")) {
                System.out.print("Enter the card number: ");
                String card_num = scanner.nextLine();
                System.out.print("Enter the card type: ");
                String card_type = scanner.nextLine();
                System.out.print("Enter the card expiry date: ");
                String card_exp = scanner.nextLine();
                try {
                    stmt = dbc.prepareStatement(insertOrgCard);
                    stmt.setString(1, name);
                    stmt.setString(2, ddate);
                    stmt.setString(3, card_num);
                    stmt.setString(4, card_type);
                    stmt.setString(5, card_exp);
                    stmt.executeUpdate();
                    System.out.println("Inserted to OrgCardDonation");
                } catch (SQLException e) {
                    fail(scanner, e.getMessage());
                    return;
                }
            }
        }

        System.out.println("Organization and donation insertions complete. Press enter to continue...");
        scanner.nextLine();
    }

    public static void getClientDoctor(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;
        System.out.print("Enter the SSN of the client whose doctor to be retrieved: ");
        String SSN = scanner.nextLine();
        String query = "SELECT DOCTOR_NAME, DOCTOR_PHONE FROM CLIENT WHERE SSN = ?";
        try {
            stmt = dbc.prepareStatement(query);
            stmt.setString(1, SSN);
            ResultSet res = stmt.executeQuery();
            res.next();
            System.out.println("-------------------------------------------");
            System.out.format("|%-20s|%-20s| %n", "Doctor Name", "Doctor Phone");
            System.out.println("-------------------------------------------");
            System.out.format("|%-20s|%-20s| %n", res.getString("DOCTOR_NAME"), res.getString("DOCTOR_PHONE"));
            System.out.println("-------------------------------------------");
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void getExpenses(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;

        String query =
                "SELECT " +
                "   SSN, " +
                "   SUM(amount) as EXP " +
                "FROM EMPLOYEEEXPENSE " +
                "WHERE EDATE >= to_date(?, 'YYYY/MM/DD') AND EDATE <= to_date(?, 'YYYY/MM/DD') " +
                "GROUP BY SSN " +
                "ORDER BY EXP DESC";

        System.out.print("Enter the beginning of the date range (YYYY/MM/DD): ");
        String start = scanner.nextLine();
        System.out.print("Enter the end of the date range (YYYY/MM/DD): ");
        String end = scanner.nextLine();
        try {
            stmt = dbc.prepareStatement(query);
            stmt.setString(1, start);
            stmt.setString(2, end);
            ResultSet res = stmt.executeQuery();
            System.out.println("-----------------------------------------");
            System.out.format("| %-20s| %-16s| %n", "Employee SSN", "Total Expense");
            System.out.println("-----------------------------------------");
            while(res.next()) {
                String SSN = res.getString("SSN");
                float exp = res.getFloat("EXP");
                System.out.format("| %-20s| %-16.2f| %n", SSN, exp);
            }
            System.out.println("-----------------------------------------");
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void getSupportVolunteers(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;
        System.out.print("Enter the SSN of the client for which to retrieve supporting volunteers: ");
        String SSN = scanner.nextLine();
        String query =
                "SELECT SSN " +
                "FROM SERVESON " +
                "WHERE NAME IN ( " +
                "   SELECT NAME " +
                "   FROM CARESFOR " +
                "   WHERE SSN = ?)";
        try {
            stmt = dbc.prepareStatement(query);
            stmt.setString(1, SSN);
            ResultSet res = stmt.executeQuery();
            System.out.println("------------------");
            System.out.format("| %-15s| %n", "Volunteer SSN");
            System.out.println("------------------");
            while(res.next()) {
                System.out.format("| %-15s| %n", res.getString("SSN"));
            }
            System.out.println("------------------");
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void getBtoK(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;
        String query =
                "SELECT " +
                "  q1.NAME, " +
                "  q2.MAILING_ADDR, " +
                "  q2.EMAIL_ADDR, " +
                "  q2.HOME_NUM, " +
                "  q2.WORK_NUM, " +
                "  q2.CELL_NUM " +
                "FROM ( " +
                "       SELECT " +
                "         SSN, " +
                "         NAME " +
                "       FROM PERSON " +
                "       WHERE SSN IN (" +
                "         SELECT SSN" +
                "         FROM CARESFOR" +
                "         WHERE NAME IN (" +
                "           SELECT TNAME" +
                "           FROM SPONSORS" +
                "           WHERE ONAME BETWEEN 'B' AND 'L'))) q1 " +
                "  INNER JOIN " +
                "  (SELECT " +
                "     SSN, " +
                "     MAILING_ADDR, " +
                "     EMAIL_ADDR, " +
                "     HOME_NUM, " +
                "     WORK_NUM, " +
                "     CELL_NUM"  +
                "   FROM CONTACTINFO" +
                "   WHERE SSN IN (" +
                "     SELECT SSN" +
                "     FROM CARESFOR" +
                "     WHERE NAME IN (" +
                "       SELECT TNAME" +
                "       FROM SPONSORS" +
                "       WHERE ONAME BETWEEN 'B' AND 'L'))) q2 " +
                "    ON q1.SSN = q2.SSN " +
                "ORDER BY q1.NAME ASC";
        try {
            stmt = dbc.prepareStatement(query);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                String name = res.getString("NAME");
                String mail = res.getString("MAILING_ADDR");
                String email = res.getString("EMAIL_ADDR");
                String home = res.getString("HOME_NUM");
                String work = res.getString("WORK_NUM");
                String cell = res.getString("CELL_NUM");
                System.out.println("Name: " + name);
                System.out.println("Mailing address: " + mail);
                System.out.println("Email address: " + email);
                System.out.println("Home: " + home);
                System.out.println("Work: " + work);
                System.out.println("Cell: " + cell);
                System.out.println();
            }
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void getDonorEmployees(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;
        String query =
                "SELECT " +
                "  q2.NAME," +
                "  q1.A," +
                "  q1.ANONYMOUS " +
                "FROM " +
                "  (SELECT " +
                "     DONOR.SSN," +
                "     SUM(AMOUNT) AS A, " +
                "     DONOR.ANONYMOUS " +
                "   FROM DONOR, DONORDONATION" +
                "   WHERE DONORDONATION.SSN = DONOR.SSN AND DONOR.SSN IN" +
                "                                           ((SELECT SSN" +
                "                                             FROM DONOR)" +
                "                                            INTERSECT" +
                "                                            (SELECT SSN" +
                "                                             FROM EMPLOYEE))" +
                "   GROUP BY DONOR.SSN, DONOR.ANONYMOUS) q1" +
                "  INNER JOIN" +
                "  (SELECT" +
                "     SSN," +
                "     NAME" +
                "   FROM PERSON" +
                "   WHERE SSN IN (" +
                "     (SELECT SSN" +
                "      FROM DONOR)" +
                "     INTERSECT" +
                "     (SELECT SSN" +
                "      FROM EMPLOYEE))) q2 " +
                "    ON q1.SSN = q1.SSN " +
                "ORDER BY A DESC";
        try {
            stmt = dbc.prepareStatement(query);
            ResultSet res = stmt.executeQuery();
            System.out.println("--------------------------------------------------------");
            System.out.format("| %-20s | %-16s | %-10s | %n", "Donor Name", "Total Donations", "Anonymous");
            System.out.println("--------------------------------------------------------");
            while(res.next()) {
                String name = res.getString("NAME");
                float amount = res.getFloat("A");
                String anon = res.getString("ANONYMOUS");
                System.out.format("| %-20s | %-16.2f | %-10s | %n", name, amount, anon);
            }
            System.out.println("--------------------------------------------------------");
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void getMostHourVolunteer(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;
        String query =
                "WITH CTE1 AS ( " +
                        "    SELECT q1.SSN, " +
                        "      q2.NAME " +
                        "    FROM " +
                        "      (SELECT" +
                        "         SSN," +
                        "         SUM(HOURS) AS G" +
                        "       FROM VOLUNTEERHOURS" +
                        "       GROUP BY SSN) q1" +
                        "      INNER JOIN" +
                        "      (SELECT" +
                        "         NAME," +
                        "         MAX(H) AS F" +
                        "       FROM" +
                        "         (SELECT" +
                        "            NAME," +
                        "            SSN," +
                        "            SUM(HOURS) AS H" +
                        "          FROM VOLUNTEERHOURS" +
                        "          GROUP BY NAME, SSN" +
                        "         )" +
                        "       GROUP BY NAME) q2" +
                        "        ON q1.G = q2.F) " +
                        "SELECT" +
                        "  q3.TNAME," +
                        "  q3.NAME," +
                        "  q4.MAILING_ADDR," +
                        "  q4.EMAIL_ADDR," +
                        "  q4.HOME_NUM," +
                        "  q4.WORK_NUM," +
                        "  q4.CELL_NUM " +
                        "FROM" +
                        "  (SELECT" +
                        "     PERSON.SSN," +
                        "     PERSON.NAME," +
                        "     CTE1.NAME AS TNAME" +
                        "   FROM PERSON" +
                        "     INNER JOIN CTE1" +
                        "       ON PERSON.SSN = CTE1.SSN) q3" +
                        "  INNER JOIN" +
                        "  (SELECT" +
                        "     CONTACTINFO.SSN," +
                        "     MAILING_ADDR," +
                        "     EMAIL_ADDR," +
                        "     HOME_NUM," +
                        "     WORK_NUM," +
                        "     CELL_NUM" +
                        "   FROM CONTACTINFO" +
                        "     INNER JOIN CTE1" +
                        "       ON CONTACTINFO.SSN = CTE1.SSN) q4" +
                        "    ON q3.SSN = q4.SSN";
        try {
            stmt = dbc.prepareStatement(query);
            ResultSet res = stmt.executeQuery();
            while(res.next()) {
                String tname = res.getString("TNAME");
                String name = res.getString("NAME");
                String mail = res.getString("MAILING_ADDR");
                String email = res.getString("EMAIL_ADDR");
                String home = res.getString("HOME_NUM");
                String work = res.getString("WORK_NUM");
                String cell = res.getString("CELL_NUM");
                System.out.println("Team: " + tname);
                System.out.println("Name: " + name);
                System.out.println("Mailing Address: " + mail);
                System.out.println("Email Address: " + email);
                System.out.println("Home: " + home);
                System.out.println("Work: " + work);
                System.out.println("Cell: " + cell);
                System.out.println();
            }
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void increaseSalary(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;
        String query =
                "UPDATE EMPLOYEE " +
                "SET SALARY = SALARY * 1.1 " +
                "WHERE SSN IN ( " +
                "  SELECT SSN " +
                "  FROM ( " +
                "    SELECT " +
                "      SSN, " +
                "      COUNT(SSN) AS C " +
                "    FROM REPORTSTO " +
                "    GROUP BY SSN) " +
                "  WHERE C > 1)";
        try {
            stmt = dbc.prepareStatement(query);
            stmt.executeUpdate();
            System.out.println("Salaries updated.");
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void deleteSomeClients(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt;
        String query =
                "DELETE FROM CLIENT\n" +
                "WHERE SSN IN (\n" +
                "  SELECT SSN\n" +
                "  FROM CLIENTNEED\n" +
                "  WHERE name = 'transportation' AND importance < 5)";
        try {
            stmt = dbc.prepareStatement(query);
            stmt.executeUpdate();
            System.out.println("Specified clients deleted.");
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void importTeams(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the file to import from (file.csv): ");
        String filename = scanner.nextLine();
        String insertTeam = "INSERT INTO TEAM VALUES (?, ?, to_date(?, 'YYYY/MM/DD'))";
        String line;
        PreparedStatement stmt;
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            int num = 0;
            while((line = br.readLine()) != null) {
                String[] values = line.split(",");
                stmt = dbc.prepareStatement(insertTeam);
                stmt.setString(1, values[0]);
                stmt.setString(2, values[1]);
                stmt.setString(3, values[2]);
                int res = stmt.executeUpdate();
                if (res > 0) num++;
            }
            System.out.println("Inserting from file complete. " + num + " teams inserted.");
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        } catch (FileNotFoundException e) {
            fail(scanner, "Unable to open file " + filename);
            return;
        } catch (IOException e) {
            fail(scanner, e.getMessage());
            return;
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void exportMailingList(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the output file (output.csv): ");
        String filename = scanner.nextLine();
        String query =
                "SELECT " +
                "  PERSON.NAME, " +
                "  CONTACTINFO.MAILING_ADDR " +
                "FROM PERSON " +
                "INNER JOIN CONTACTINFO " +
                "ON PERSON.SSN = CONTACTINFO.SSN " +
                "WHERE ON_MAILING_LIST = 'Y' ";
        PreparedStatement stmt;
        try {
            FileWriter fw = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(fw);
            stmt = dbc.prepareStatement(query);
            ResultSet res = stmt.executeQuery();
            while(res.next()) {
                bw.write(res.getString("NAME") + "," + res.getString("MAILING_ADDR"));
                bw.newLine();
            }
            bw.close();
            System.out.println("Finished writing to file " + filename + " .");
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        } catch (IOException e) {
            fail(scanner, e.getMessage());
            return;
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }
    }

    public static void printInstructions() {
        System.out.println("Choose a command by entering the number of the command:");
        System.out.println();
        System.out.println("(1) Enter a new team");
        System.out.println("(2) Enter a new client and assign team(s)");
        System.out.println("(3) Enter a new volunteer and assign team(s)");
        System.out.println("(4) Record this month's volunteer hours");
        System.out.println("(5) Enter a new employee and assign team(s)");
        System.out.println("(6) Record an employee expense");
        System.out.println("(7) Enter a new organization and assign team(s)");
        System.out.println("(8) Enter a new donor and assign donation(s)");
        System.out.println("(9) Enter a new organization and assign donation(s)");
        System.out.println("(10) Get name and phone number of a client's doctor");
        System.out.println("(11) Get the total amount of expenses charged by each employee for a particular period of time");
        System.out.println("(12) Get volunteers that are members of teams that support a particular client");
        System.out.println("(13) Get names and contact information of the clients that are supported by teams sponsored by an organization whose name starts with a letter between B and K");
        System.out.println("(14) Get names and total amounts donated by donors that are also employees and whether they are anonymous");
        System.out.println("(15) Get the name and associated contact information of the volunteer that has worked the most total hours between March and June for each team");
        System.out.println("(16) Increase the salary by 10% of all employees to whom more than one team must report");
        System.out.println("(17) Delete all clients who do not have health insurance and whose value of importance for transportation is less than 5");
        System.out.println("(18) Import teams from external csv");
        System.out.println("(19) Export mailing list to csv");
        System.out.println("(20) Quit");
    }

}
