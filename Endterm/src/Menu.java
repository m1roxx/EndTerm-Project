import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
class MenuList {

    public static void customerMenu(Connection connection, Statement statement, String login) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("1. View all flights \n" +
                    "2. Book a ticket \n" +
                    "3. Show my booked tickets \n" +
                    "4. Cancel ticket\n" +
                    "5. Exit from account\n" +
                    "Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    customerMenuMethods.viewAllFlights(connection, scanner);
                    break;
                case 2:
                    try {
                        customerMenuMethods.bookTicket(connection, scanner, statement, login);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:
                    try {
                        customerMenuMethods.viewBookedTickets(connection, scanner, statement, login);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 4:
                    customerMenuMethods.cancelTicket(connection, scanner);
                case 5:
                    System.out.println("Exiting account...");
                    return;
                default:
                    System.err.println("Error: Invalid choice.");
            }
        }
    }


    public static void adminMenu(Connection connection, Statement statement) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Menu:\n" +
                    "1. View all flights\n" +
                    "2. View all booked tickets\n" +
                    "3. Add new route\n" +
                    "4. Update route\n" +
                    "5. Delete route\n" +
                    "6. Exit from account\n" +
                    "Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    AdminMenuMethods.viewAllFlights(connection, scanner);
                    break;
                case 2:
                    AdminMenuMethods.viewAllBookedTickets(connection, statement);
                    break;
                case 3:
                    AdminMenuMethods.addNewTicket(connection, scanner);
                    break;
                case 4:
                    AdminMenuMethods.updateRoute(connection, scanner);
                case 5:
                    AdminMenuMethods.deleteRoute(connection, scanner);
                case 6:
                    System.out.println("Exiting account...");
                    return;
                default:
                    System.err.println("Error: Invalid choice.");
            }
        }
    }
}