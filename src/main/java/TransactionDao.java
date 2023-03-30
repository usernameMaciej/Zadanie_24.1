import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    private Connection connection;

    public List<Transaction> showExpense() {
        connect();

        String sql = "SELECT * FROM home_budget.transaction WHERE type = 'wydatki'";

        return executeTransactions(sql);
    }

    public List<Transaction> showIncome() {
        connect();

        String sql = "SELECT * FROM home_budget.transaction WHERE type = 'przychody'";

        return executeTransactions(sql);
    }

    private List<Transaction> executeTransactions(String sql) {
        List<Transaction> transactions = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String type = resultSet.getString("type");
                String description = resultSet.getString("description");
                int amount = resultSet.getInt("amount");
                Date date = resultSet.getDate("date");
                transactions.add(new Transaction(id, type, description, amount, date));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeConnection();
        return transactions;
    }

    public void delete(Long id) {
        connect();

        try {
            String sql = "DELETE FROM home_budget.transaction WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        closeConnection();
    }

    public void modify(Transaction transaction) {
        connect();

        try {
            String sql = "UPDATE home_budget.transaction SET type = ?, description = ?, amount = ?, date = ?";
            executeStatement(transaction, sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeConnection();
    }

    public void add(Transaction transaction) {
        connect();
        try {
            String sql = "INSERT INTO home_budget.transaction(type, description, amount, date) VALUES (?, ?, ?, ?)";
            executeStatement(transaction, sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/home_budget?serverTimezone=UTC", "root", "asd123");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
