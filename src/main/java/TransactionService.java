import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class TransactionService {
    private final Scanner scanner;
    private final TransactionDao transactionDao = new TransactionDao();

    public TransactionService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void add() {
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

        transactionDao.add(transaction);
    }


    public void modify() {
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

        transactionDao.modify(transaction);
    }

    public void delete() {
        System.out.println("Podaj id transakcji, którą chcesz usunąć");
        long id = scanner.nextLong();
        scanner.nextLine();

        transactionDao.delete(id);
    }

    public void showIncome() {
        List<Transaction> transactions = transactionDao.showIncome();
        System.out.println(transactions);
    }

    public void showExpense() {
        List<Transaction> transactions = transactionDao.showExpense();
        System.out.println(transactions);
    }
}
