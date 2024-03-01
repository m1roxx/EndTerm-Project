import java.sql.*;
import java.sql.Connection;
import java.util.Scanner;
import java.util.Properties;



public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        try {
            System.out.println("Welcome to flight booking system!");
            while (true) {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://localhost:5432/Endterm";

                Properties authorization = new Properties();
                authorization.put("user", "postgres");
                authorization.put("password", "1234");

                Connection connection = DriverManager.getConnection(url, authorization);

                java.sql.Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);

                System.out.print("Menu:\n" +
                        "1. Log in\n" +
                        "2. Sign up\n" +
                        "3. Exit\n" +
                        "Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        RegMethods.logIn(connection, scanner, statement);
                        break;
                    case 2:
                        RegMethods.signUp(connection, scanner, statement);
                        break;
                    case 3:
                        System.out.println("Exiting program...");
                        return;
                    default:
                        System.err.println("Error");
                        break;
                }


                ResultSet table = statement.executeQuery("SELECT * FROM flights");

                if (table != null) {
                    table.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }

        } catch (Exception e) {
            System.err.println("Error accessing database!");
            e.printStackTrace();
        }
    }
}

