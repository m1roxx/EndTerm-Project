import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


class RegMethods {
    public static void logIn(Connection connection, Scanner scanner, Statement statement) throws SQLException {
        System.out.print("Enter login: ");
        String login = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        ResultSet resultSet = statement.executeQuery("SELECT role FROM users WHERE login = '" + login + "' AND password = '" + password + "'");
        if (resultSet.next()) {
            String role = resultSet.getString("role");
            System.out.println("Login successful!");

            if ("admin".equals(role)) {
                MenuList.adminMenu(connection, statement);
            } else if ("customer".equals(role)) {
                MenuList.customerMenu(connection, statement, login);
            } else {
                System.err.println("Unknown role: " + role);
            }
        } else {
            System.err.println("Invalid login or password");
        }
    }

    public static void signUp(Connection connection, Scanner scanner, Statement statement) {
        System.out.print("Create login: ");
        String newLogin = scanner.nextLine();
        System.out.print("Create password: ");
        String newPassword = scanner.nextLine();


        String insertQuery = "INSERT INTO users (login, password) VALUES (?, ?)";

         try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, newLogin);
            preparedStatement.setString(2, newPassword);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Registration completed successfully.");
            } else {
                System.out.println("Failed to register user.");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while executing the request: " + e.getMessage());
        }
    }

}

class customerMenuMethods{
    public static void viewAllFlights(Connection connection, Scanner scanner) {
        String sqlQuery = "SELECT * FROM flights ORDER BY id ASC";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            if (!resultSet.next()) {
                System.out.println("No flights found.");
                return;
            }

            System.out.println("+-------+-----------------+------------+----------+------------+-------+-----------+");
            System.out.println("|  ID   |      Route      |    Date    |   Time   |   Price    | Seats |   Class   ");
            System.out.println("+-------+-----------------+------------+----------+------------+-------+-----------+");

            do {
                int id = resultSet.getInt("id");
                String route = resultSet.getString("route");
                Date date = resultSet.getDate("date");
                Time time = resultSet.getTime("time");
                int price = resultSet.getInt("price");
                String flightclass = resultSet.getString("flightclass");
                int seats = resultSet.getInt("seat");
                System.out.printf("| %5d | %-15s | %tF | %tT | ₸%-9d | %5d | %-9s | \n", id, route, date, time, price, seats, flightclass);
            } while (resultSet.next());

            System.out.println("+-------+-----------------+------------+----------+------------+-------+-----------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void bookTicket(Connection connection, Scanner scanner, Statement statement,String login) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE login = '" + login+"'");
        int customerId = 0;
        if (resultSet.next()) {
            customerId = resultSet.getInt("id");
        }
        System.out.print("Enter flight id you want to book: ");
        int bookId = scanner.nextInt();
        statement.executeUpdate("INSERT INTO booked(id, route, date, time, price, seat, flightclass, customerid) SELECT id, route, date, time, price, seat, flightclass," + customerId + "  FROM flights WHERE id =" + bookId);
        System.out.println("Ticket was booked");
    }
    public static void viewBookedTickets(Connection connection, Scanner scanner, Statement statement, String login) throws SQLException{
        ResultSet resSet = statement.executeQuery("SELECT id FROM users WHERE login = '" + login+ "'");
        int customerId = 0;
        if(resSet.next()) {
            customerId = resSet.getInt("id");
        }
        ResultSet resultSet = statement.executeQuery("SELECT * FROM booked WHERE customerid = " +customerId+ " ORDER BY id ASC");
            if (!resultSet.next()) {
                System.out.println("No flights found.");
                return;
            }

            System.out.println("+-------+-----------------+------------+----------+------------+-------+-----------+");
            System.out.println("|  ID   |      Route      |    Date    |   Time   |   Price    | Seats |   Class   ");
            System.out.println("+-------+-----------------+------------+----------+------------+-------+-----------+");

            do {
                int id = resultSet.getInt("id");
                String route = resultSet.getString("route");
                Date date = resultSet.getDate("date");
                Time time = resultSet.getTime("time");
                int price = resultSet.getInt("price");
                String flightclass = resultSet.getString("flightclass");
                int seats = resultSet.getInt("seat");
                System.out.printf("| %5d | %-15s | %10tF | %8tT | ₸%9d | %5d | %-9s | \n", id, route, date, time, price, seats, flightclass);
            } while (resultSet.next());

            System.out.println("+-------+-----------------+------------+----------+------------+-------+-----------+");


    }

    public static void cancelTicket(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter the ID of the ticket you want to cancel: ");
        int ticketId = scanner.nextInt();
        scanner.nextLine();

        String deleteQuery = "DELETE FROM booked WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, ticketId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Ticket with ID " + ticketId + " canceled successfully.");
            } else {
                System.out.println("No ticket found with ID " + ticketId + ".");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while canceling the ticket: " + e.getMessage());
        }
    }
}

class AdminMenuMethods{
    public static void viewAllFlights(Connection connection, Scanner scanner) {
        String sqlQuery = "SELECT * FROM flights ORDER BY id ASC";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            if (!resultSet.next()) {
                System.out.println("No flights found.");
                return;
            }

            System.out.println("+-------+-----------------+------------+----------+------------+-------+-----------+");
            System.out.println("|  ID   |      Route      |    Date    |   Time   |   Price    | Seats |   Class   ");
            System.out.println("+-------+-----------------+------------+----------+------------+-------+-----------+");

            do {
                int id = resultSet.getInt("id");
                String route = resultSet.getString("route");
                Date date = resultSet.getDate("date");
                Time time = resultSet.getTime("time");
                int price = resultSet.getInt("price");
                String flightclass = resultSet.getString("flightclass");
                int seats = resultSet.getInt("seat");
                System.out.printf("| %5d | %-15s | %tF | %tT | ₸%-9d | %5d | %-9s | \n", id, route, date, time, price, seats, flightclass);
            } while (resultSet.next());

            System.out.println("+-------+-----------------+------------+----------+------------+-------+-----------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewAllBookedTickets(Connection connection, Statement statement) {
        String sqlQuery = "SELECT * FROM booked";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            if (!resultSet.next()) {
                System.out.println("No booked tickets found.");
                return;
            }

            System.out.println("+-------+---------------------+------------+----------+------------+-------+-----------+-----------+");
            System.out.println("|  ID   |        Route        |    Date    |   Time   |   Price    | Seats |   Class   | CustomerID|");
            System.out.println("+-------+---------------------+------------+----------+------------+-------+-----------+-----------+");

            do {
                int id = resultSet.getInt("id");
                String route = resultSet.getString("route");
                Date date = resultSet.getDate("date");
                Time time = resultSet.getTime("time");
                int price = resultSet.getInt("price");
                int seats = resultSet.getInt("seat");
                String flightClass = resultSet.getString("flightclass");
                int customerId = resultSet.getInt("customerid");

                System.out.printf("| %5d | %-19s | %tF | %tT | ₸%-9d | %5d | %-9s | %9d |\n", id, route, date, time, price, seats, flightClass, customerId);
            } while (resultSet.next());

            System.out.println("+-------+---------------------+------------+----------+------------+-------+-----------+-----------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addNewTicket(Connection connection, Scanner scanner) {
        try {
            System.out.println("Adding a new ticket...");

            System.out.print("Enter route: ");
            scanner.nextLine();
            String routeString = scanner.nextLine();
            String route = String.valueOf(routeString);

            System.out.print("Enter date (YYYY-MM-DD): ");
            String dateString = scanner.nextLine();
            Date date = Date.valueOf(dateString);

            System.out.print("Enter time (HH:MM:SS): ");
            String timeString = scanner.nextLine();
            Time time = Time.valueOf(timeString);

            System.out.print("Enter price: ");
            int price = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter class: ");
            String flightClass = scanner.nextLine();

            System.out.print("Enter seat number: ");
            int seat = scanner.nextInt();
            scanner.nextLine();

            String insertQuery = "INSERT INTO flights (route, date, time, price, seat, flightClass) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, route);
            preparedStatement.setDate(2, date);
            preparedStatement.setTime(3, time);
            preparedStatement.setInt(4, price);
            preparedStatement.setInt(5, seat);
            preparedStatement.setString(6, flightClass);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("New ticket added successfully.");
            } else {
                System.out.println("Failed to add new ticket.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateRoute(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter the ID of the route you want to update: ");
            int routeId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter new route: ");
            String newRoute = scanner.nextLine();
            System.out.print("Enter new date (YYYY-MM-DD): ");
            String newDateStr = scanner.nextLine();
            Date newDate = Date.valueOf(newDateStr);
            System.out.print("Enter new time (HH:MM:SS): ");
            String newTimeStr = scanner.nextLine();
            Time newTime = Time.valueOf(newTimeStr);
            System.out.print("Enter new price: ");
            int newPrice = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter new class: ");
            String newClass = scanner.nextLine();
            System.out.print("Enter new seat number: ");
            int newSeat = scanner.nextInt();
            scanner.nextLine();

            String updateQuery = "UPDATE flights SET route = ?, date = ?, time = ?, price = ?, flightClass = ?, seat = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newRoute);
            preparedStatement.setDate(2, newDate);
            preparedStatement.setTime(3, newTime);
            preparedStatement.setInt(4, newPrice);
            preparedStatement.setString(5, newClass);
            preparedStatement.setInt(6, newSeat);
            preparedStatement.setInt(7, routeId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Route updated successfully.");
            } else {
                System.out.println("Failed to update route.");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while updating the route: " + e.getMessage());
        }
    }

    public static void deleteRoute(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter the ID of the route you want to delete: ");
            int routeId = scanner.nextInt();
            scanner.nextLine();

            String deleteQuery = "DELETE FROM flights WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, routeId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Route deleted successfully.");
            } else {
                System.out.println("Failed to delete route.");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while deleting the route: " + e.getMessage());
        }
    }

}