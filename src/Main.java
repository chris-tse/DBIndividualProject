import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String dbURL = "jdbc:oracle:thin:@//oracle.cs.ou.edu:1521/pdborcl.cs.ou.edu",
               login = "tse1666",
               pw = "QYbr6Sm";

        // Create JDBC Connection
        try {
            System.out.println("Connecting to database...");
            Connection dbConnection = DriverManager.getConnection(dbURL, login, pw);
            //run(dbConnection);
        } catch (SQLException x) {
            System.err.println("Error: Couldn't connect to the database.");
        }
    }
}
