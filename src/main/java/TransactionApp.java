import java.util.Arrays;
import java.util.Scanner;

public class TransactionApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TransactionDao transactionDao = new TransactionDao();
        int userOption;

        do {
            System.out.println("Co chcesz zrobić?");
            System.out.println(Arrays.toString(Option.values()));
            userOption = scanner.nextInt();
            scanner.nextLine();
            Option option = Option.userOption(userOption);

            switch (option) {
                case EXIT -> System.out.println("Koniec programu");
                case ADD -> transactionDao.add();
                case MODIFY -> transactionDao.modify();
                case DELETE -> transactionDao.delete();
                case SHOW_INCOME -> transactionDao.showIncome();
                case SHOW_EXPENSE -> transactionDao.showExpense();
                default -> System.out.println("Zły wybór opcji");
            }
        } while (userOption != Option.EXIT.id);
    }

    private enum Option {
        EXIT(0, "Wyjście"),
        ADD(1, "Dodawanie transakcji"),
        MODIFY(2, "Modyfikacja transakcji"),
        DELETE(3, "Usuwanie transakcji"),
        SHOW_INCOME(4, "Wyświetlanie wszystkich przychodów"),
        SHOW_EXPENSE(5, "Wyświetlanie wszystkich wydatków");

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
