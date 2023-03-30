import java.sql.*;
import java.util.Scanner;

public class TransactionDao {
    private Connection connection;

    public void showExpense() {
        connect();
        String sql = "SELECT * FROM home_budget.transaction WHERE type = 'wydatki'";
        printTransaction(sql);
        closeConnection();
    }

    public void showIncome() {
        connect();
        String sql = "SELECT * FROM home_budget.transaction WHERE type = 'przychody'";
        printTransaction(sql);
        closeConnection();
    }

    public void printTransaction(String sql) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String type = resultSet.getString("type");
                String description = resultSet.getString("description");
                int amount = resultSet.getInt("amount");
                Date date = resultSet.getDate("date");
                System.out.println(id + " - " + type + ", " + description + ", kwota transakcji: " + amount + ", data: " + date);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        connect();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj id transakcji, którą chcesz usunąć");
        long id = scanner.nextLong();
        scanner.nextLine();

        try {
            String sql = "DELETE FROM home_budget.transaction WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public void modify() {
        connect();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj id transakcji, którą chcesz zaktualizować");
        long id = scanner.nextLong();
        scanner.nextLine();

        System.out.println("Podaj typ (wydatki lub przychody)");
        String type = scanner.nextLine();

        System.out.println("Podaj opis");
        String description = scanner.nextLine();

        System.out.println("Podaj kwotę");
        int amount = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Podaj datę");
        Date date = Date.valueOf(scanner.nextLine());

        Transaction transaction = new Transaction(id, type, description, amount, date);

        try {
            String sql = "UPDATE home_budget.transaction SET type = ?, description = ?, amount = ?, date = ?";
            executeStatement(transaction, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public void add() {
        connect();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj typ transakcji (wydatki lub przychody)");
        String type = scanner.nextLine();

        System.out.println("Podaj opis transakcji");
        String description = scanner.nextLine();

        System.out.println("Podaj kwotę transakcji");
        int amount = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Podaj datę transakcji");
        Date date = Date.valueOf(scanner.nextLine());

        Transaction transaction = new Transaction(type, description, amount, date);
        try {
            String sql = "INSERT INTO home_budget.transaction(type, description, amount, date) VALUES (?, ?, ?, ?)";
            executeStatement(transaction, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    private void executeStatement(Transaction transaction, String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, transaction.getType());
        preparedStatement.setString(2, transaction.getDescription());
        preparedStatement.setInt(3, transaction.getAmount());
        preparedStatement.setDate(4, (Date) transaction.getDate());
        preparedStatement.executeUpdate();
    }

    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Sterownik do bazy nie zostal odnaleziony.");
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/home_budget?serverTimezone=UTC", "root", "asd123");
        } catch (SQLException e) {
            System.err.println("Błąd podczas nawiązywania połączenia");
        }
    }
}
