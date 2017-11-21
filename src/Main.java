import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String dbURL = "jdbc:oracle:thin:@//oracle.cs.ou.edu:1521/pdborcl.cs.ou.edu",
               login = "tse1666",
               pw = "QYbr6Sm6";

        // Create JDBC Connection
        try {
            System.out.println("Connecting to database...");
            Connection dbConnection = DriverManager.getConnection(dbURL, login, pw);
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
                    //insertExpense(dbc);
                    break;
                case 7:
//                    insertSponsor(dbc);
                    break;
                case 8:
//                    insertDonor(dbc);
                    break;
                case 9:
//                    insertDonorOrg(dbc);
                    break;
                case 10:
//                    getClientNamePhone(dbc);
                    break;
                case 11:
//                    getExpenses(dbc);
                    break;
                case 12:
//                    getSupportVolunteers(dbc);
                    break;
                case 13:
//                    getBtoK(dbc);
                    break;
                case 14:
//                    getDonorEmployees(dbc);
                    break;
                case 15:
//                    getMostHourVolunteer(dbc);
                    break;
                case 16:
//                    increaseSalary(dbc);
                    break;
                case 17:
//                    deleteSomeClients(dbc);
                    break;
                case 18:
//                    importTeams(dbc);
                    break;
                case 19:
//                    exportMailingList(dbc);
                    break;
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
        if(checkSSN(dbc, SSN)) {
            System.out.println("Skipping insertion into Person");
            return true;
        }
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
            return true;
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return false;
        }
    }

    public static boolean checkSSN(Connection dbc, String SSN) {
        PreparedStatement stmt;
        String checkSSN = "SELECT COUNT(*) AS C FROM PERSON WHERE SSN = ?";
        ResultSet res;
        try {
            stmt = dbc.prepareStatement(checkSSN);
            stmt.setString(1, SSN);
            res = stmt.executeQuery();
            res.next();
            if (res.getInt("C") > 0) {
                System.out.println("SSN exists in Person.");
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
        if(!insertPerson(dbc, scanner, SSN)) return;

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
            System.out.println("Client insertion successful. Press enter to continue...");
            scanner.nextLine();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
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

        if(!insertPerson(dbc, scanner, SSN))  return;

        String insertVolunteer = "INSERT INTO Volunteer VALUES ('?', '?', to_date('?', 'YYYY/MM/DD'), '?', '?', '?', sysdate, '?')";
        System.out.print("Please enter the name of the volunteer: ");
        String name = scanner.nextLine();
        System.out.print("Please enter the birth date of the volunteer (YYYY/MM/DD): ");
        String bdate = scanner.nextLine();
        System.out.print("Please enter the race of the volunteer: ");
        String race = scanner.nextLine();
        System.out.print("Please enter the profession of the volunteer: ");
        String profession = scanner.nextLine();
        System.out.print("Please enter the gender of the volunteer (M/F): ");
        String gender = scanner.nextLine();
        System.out.print("Please enter the location of the volunteer's most recent training: ");
        String trainloc = scanner.nextLine();
        try {
            stmt = dbc.prepareStatement(insertVolunteer);
            stmt.setString(1, SSN);
            stmt.setString(2, name);
            stmt.setString(3, bdate);
            stmt.setString(4, race);
            stmt.setString(5, profession);
            stmt.setString(6, gender);
            stmt.setString(7, trainloc);
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }

        String insertVolunteerContact = "INSERT INTO CONTACTINFO VALUES " +
                "(?, ?, ?, ?, ?, ?, ?)";
        System.out.print("Please enter the volunteer's mailing address: ");
        String mail = scanner.nextLine();
        System.out.print("Please enter the volunteer's email address: ");
        String email = scanner.nextLine();
        System.out.print("Please enter the volunteer's home number: ");
        String homephone = scanner.nextLine();
        System.out.print("Please enter the volunteer's work number: ");
        String workphone = scanner.nextLine();
        System.out.print("Please enter the volunteer's cell number: ");
        String cellphone = scanner.nextLine();
        System.out.print("Add volunteer to mailing list? (Y/N) ");
        String maillist = scanner.nextLine();
        try {
            stmt = dbc.prepareStatement(insertVolunteerContact);
            stmt.setString(1, SSN);
            stmt.setString(2, mail);
            stmt.setString(3, email);
            stmt.setString(4, homephone);
            stmt.setString(5, workphone);
            stmt.setString(6, cellphone);
            stmt.setString(7, maillist);
            stmt.executeUpdate();
        } catch (SQLException e) {
            fail(scanner, e.getMessage());
            return;
        }

        String insertServesOn = "INSERT INTO SERVESON VALUES ('?', '?', 'T')";
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
        try {
            stmt = dbc.prepareStatement(insertHours);
            stmt.setString(1, SSN);
            stmt.setString(2, team);
            stmt.setFloat(3, hours);
            stmt.executeQuery();
            System.out.println("Record successful");
        } catch (SQLException e){
            System.err.println("Could not insert into VolunteerHours");
            e.printStackTrace();
            return;
        }
    }

    public static void insertEmployee(Connection dbc) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the SSN of the employee: ");
        String SSN = scanner.nextLine();
        System.out.print("Enter the name of the employee: ");
        String name = scanner.nextLine();
        System.out.print("Enter the birth date of the employee (YYYY/MM/DD): ");
        String bdate = scanner.nextLine();
        System.out.print("Enter the race of the employee: ");
        String race = scanner.nextLine();
        System.out.print("Enter the profession of the employee: ");
        String profession = scanner.nextLine();
        System.out.print("Enter the gender of the employee: ");
        String gender = scanner.nextLine();
        System.out.print("Enter the sylart of the employee: ");
        float salary = scanner.nextFloat();
        System.out.print("Enter the marital status of the employee: ");
        String marital = scanner.nextLine();
        System.out.println("Enter the list of teams that will be reporting to this employee (Team1, Team2): ");
        String teams = scanner.nextLine();

        String[] teamArr = teams.split(",");
        PreparedStatement stmt;
        try {
            stmt = dbc.prepareStatement("INSERT INTO Employee VALUES ('?', '?', to_date('?', 'YYYY/MM/DD'), '?', '?', 'M', 80000.00, 'Married', )");
            stmt.setString(1, SSN);
            stmt.setString(2, name);
            stmt.setString(3, bdate);
            stmt.setString(4, race);
            stmt.setString(5, profession);
            stmt.setString(6, gender);
            stmt.setFloat(7, salary);
            stmt.setString(8, marital);
            stmt.executeQuery();
            System.out.println("Insertion into Employee successful");
        } catch (SQLException e) {
            System.err.println("Could not insert into Employee");
            return;
        }

        try {
            for (String team : teamArr) {
                stmt = dbc.prepareStatement("INSERT INTO ReportsTo VALUES ('?', '?');");
                stmt.setString(1, team);
                stmt.setString(2, SSN);
                stmt.executeQuery();
                System.out.println("Insertion into ReportsTo successful");
            }
        } catch (SQLException e) {
            System.err.println("Could not insert into ReportsTo");
            return;
        }
    }

    public static void printInstructions() {
        System.out.println("WELCOME TO THE PAN CLIENT AND DONOR DATABASE SYSTEM");
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
    }
}
