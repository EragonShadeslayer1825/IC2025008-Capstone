import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\|", -1);

            try {
                switch (parts[0]) {
                    case "ADD_BOOK":
                        library.addBook(parts[1], parts[2], parts[3]);
                        System.out.println("OK");
                        break;

                    case "ADD_MAGAZINE":
                        library.addMagazine(parts[1], parts[2], parts[3]);
                        System.out.println("OK");
                        break;

                    case "ADD_REFERENCE":
                        library.addReference(parts[1], parts[2], parts[3]);
                        System.out.println("OK");
                        break;

                    case "ADD_MEMBER":
                        library.addMember(parts[1], parts[2], parts[3]);
                        System.out.println("OK");
                        break;

                    case "BORROW":
                        int dueDay = library.borrow(parts[1], parts[2]);
                        System.out.println("OK due on day " + dueDay);
                        break;

                    case "RETURN":
                        int fine = library.returnItem(parts[1]);
                        System.out.println("OK fine " + fine);
                        break;

                    case "ADVANCE":
                        int today = library.advance(Integer.parseInt(parts[1]));
                        System.out.println("OK today is day " + today);
                        break;

                    case "SEARCH":
                        List<LibraryItem> matches = library.search(parts[1]);
                        if (matches.isEmpty()) {
                            System.out.println("NONE");
                        } else {
                            for (LibraryItem item : matches) {
                                System.out.println(
                                        item.getId() + "|" + item.getType() + "|" + item.getTitle());
                            }
                        }
                        break;

                    case "LOANS":
                        List<Loan> memberLoans = library.loansOf(parts[1]);
                        if (memberLoans.isEmpty()) {
                            System.out.println("NONE");
                        } else {
                            for (Loan loan : memberLoans) {
                                LibraryItem item = loan.getItem();
                                System.out.println(
                                        item.getId() + "|" + item.getTitle() + "|" + loan.getDueDay());
                            }
                        }
                        break;

                    case "OVERDUE":
                        List<Loan> overdueLoans = library.overdue();
                        if (overdueLoans.isEmpty()) {
                            System.out.println("NONE");
                        } else {
                            for (Loan loan : overdueLoans) {
                                System.out.println(
                                        loan.getItem().getId() + "|"
                                                + loan.getMember().getId() + "|"
                                                + loan.daysLate(library.getToday()) + "|"
                                                + loan.fine(library.getToday()));
                            }
                        }
                        break;

                    case "INVENTORY":
                        List<LibraryItem> allItems = library.inventory();
                        if (allItems.isEmpty()) {
                            System.out.println("NONE");
                        } else {
                            for (LibraryItem item : allItems) {
                                System.out.println(
                                        item.getId() + "|" + item.getType() + "|"
                                                + library.inventoryStatus(item));
                            }
                        }
                        break;

                    default:
                        break;
                }
            } catch (LibraryException e) {
                System.out.println("ERROR " + e.getMessage());
            }
        }
    }
}