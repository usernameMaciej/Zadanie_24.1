import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class TransactionDao {
    private Connection connection;
    private final Scanner scanner = new Scanner(System.in);
    private Transaction transaction;

    public void run() {
        int userOption;
        do {
            System.out.println("Co chcesz zrobić?");
            System.out.println(Arrays.toString(Option.values()));
            userOption = scanner.nextInt();
            scanner.nextLine();
            chooseOption(userOption);
        } while (userOption != Option.EXIT.id);
    }

    private void chooseOption(int userOption) {
        Option option = Option.userOption(userOption);

        switch (option) {
            case EXIT -> System.out.println("Koniec programu");
            case ADD -> add(transaction);
            case MODIFY -> modify(transaction);
            case DELETE -> delete(transaction.getId());
            case SHOW_INCOME -> showIncome();
            case SHOW_EXPENSE -> showExpense();
            default -> System.out.println("Zły wybór opcji");
        }
    }

    private void showExpense() {
        connect();
        String sql = "SELECT * FROM home_budget.transaction WHERE type = 'wydatki'";
        printTransaction(sql);
        closeConnection();
    }

    private void showIncome() {
        connect();
        String sql = "SELECT * FROM home_budget.transaction WHERE type = 'przychody'";
        printTransaction(sql);
        closeConnection();
    }

    private void printTransaction(String sql) {
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

    private void delete(Long id) {
        connect();
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

    private void modify(Transaction transaction) {
        connect();
        try {
            String sql = "UPDATE home_budget.transaction SET type = ?, description = ?, amount = ?, date = ?";
            prepareStatements(transaction, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    private void add(Transaction transaction) {
        connect();
        try {
            String sql = "INSERT INTO home_budget.transaction(type, description, amount, date) VALUES (?, ?, ?, ?)";
            prepareStatements(transaction, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    private void prepareStatements(Transaction transaction, String sql) throws SQLException {
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

    private enum Option {
        EXIT(0, "Wyjście"), ADD(1, "Dodawanie transakcji"), MODIFY(2, "Modyfikacja transakcji"), DELETE(3, "Usuwanie transakcji"), SHOW_INCOME(4, "Wyświetlanie wszystkich przychodów"), SHOW_EXPENSE(5, "Wyświetlanie wszystkich wydatków");

        private int id;
        private String description;

        Option(int id, String description) {
            this.id = id;
            this.description = description;
        }

        public static Option userOption(int userOption) {
            return Option.values()[userOption];
        }

        @Override
        public String toString() {
            return id + " - " + description;
        }
    }
}
