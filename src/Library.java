import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Library {
    private int today = 0;
    private final Map<String, LibraryItem> items = new HashMap<>();
    private final Map<String, Member> members = new HashMap<>();
    private final Map<String, Loan> activeLoans = new HashMap<>();

    public Library() {
    }

    public void addBook(String id, String title, String author) {
        items.put(id, new Book(id, title, author));
    }

    public void addMagazine(String id, String title, String issue) {
        items.put(id, new Magazine(id, title, issue));
    }

    public void addReference(String id, String title, String author) {
        items.put(id, new ReferenceBook(id, title, author));
    }

    public void addMember(String id, String name, String type) {
        if (type.equals("STUDENT")) {
            members.put(id, new Student(id, name));
        } else if (type.equals("STAFF")) {
            members.put(id, new Staff(id, name));
        } else {
            members.put(id, new Faculty(id, name));
        }
    }

    public int borrow(String memberId, String itemId)
            throws ItemNotFoundException, MemberNotFoundException,
                   ItemNotAvailableException, BorrowLimitExceededException {
        LibraryItem item = findItem(itemId);
        Member member = findMember(memberId);

        if (!item.isBorrowable()) {
            throw new ItemNotAvailableException(itemId + " cannot be borrowed");
        }

        if (activeLoans.containsKey(itemId)) {
            throw new ItemNotAvailableException(itemId + " is on loan");
        }

        int memberLoanCount = 0;
        for (Loan loan : activeLoans.values()) {
            if (loan.getMember().getId().equals(memberId)) {
                memberLoanCount++;
            }
        }

        if (memberLoanCount >= member.getMaxItems()) {
            throw new BorrowLimitExceededException(
                    memberId + " has reached the limit of " + member.getMaxItems());
        }

        Borrowable borrowableItem = (Borrowable) item;
        int dueDay = today + member.getLoanPeriodDays();

        Loan loan = new Loan(
                item, member, today, dueDay, borrowableItem.getFineRatePerDay());
        activeLoans.put(itemId, loan);

        return dueDay;
    }

    public int returnItem(String itemId)
            throws ItemNotFoundException, ItemNotOnLoanException {
        findItem(itemId);

        Loan loan = activeLoans.get(itemId);
        if (loan == null) {
            throw new ItemNotOnLoanException(itemId + " is not on loan");
        }

        int fine = loan.fine(today);
        activeLoans.remove(itemId);
        return fine;
    }

    public int advance(int days) {
        today += days;
        return today;
    }

    public List<LibraryItem> search(String text) {
        String searchText = text.toLowerCase(Locale.ROOT);
        List<LibraryItem> results = new ArrayList<>();

        for (LibraryItem item : items.values()) {
            if (item.getTitle().toLowerCase(Locale.ROOT).contains(searchText)) {
                results.add(item);
            }
        }

        results.sort(Comparator.comparing(LibraryItem::getTitle)
                .thenComparing(LibraryItem::getId));
        return results;
    }

    public List<Loan> loansOf(String memberId) throws MemberNotFoundException {
        findMember(memberId);

        List<Loan> results = new ArrayList<>();
        for (Loan loan : activeLoans.values()) {
            if (loan.getMember().getId().equals(memberId)) {
                results.add(loan);
            }
        }

        results.sort(Comparator.comparingInt(Loan::getDueDay)
                .thenComparing(loan -> loan.getItem().getId()));
        return results;
    }

    public List<Loan> overdue() {
        List<Loan> results = new ArrayList<>();

        for (Loan loan : activeLoans.values()) {
            if (loan.isOverdue(today)) {
                results.add(loan);
            }
        }

        results.sort(Comparator
                .comparingInt((Loan loan) -> loan.daysLate(today))
                .reversed()
                .thenComparing(loan -> loan.getItem().getId()));
        return results;
    }

    public List<LibraryItem> inventory() {
        List<LibraryItem> results = new ArrayList<>(items.values());
        results.sort(Comparator.comparing(LibraryItem::getId));
        return results;
    }

    public String inventoryStatus(LibraryItem item) {
        if (!item.isBorrowable()) {
            return "NOT_BORROWABLE";
        }

        Loan loan = activeLoans.get(item.getId());
        if (loan == null) {
            return "AVAILABLE";
        }

        return "ON_LOAN|" + loan.getMember().getId();
    }

    private LibraryItem findItem(String id) throws ItemNotFoundException {
        LibraryItem item = items.get(id);
        if (item == null) {
            throw new ItemNotFoundException("item " + id + " not found");
        }
        return item;
    }

    private Member findMember(String id) throws MemberNotFoundException {
        Member member = members.get(id);
        if (member == null) {
            throw new MemberNotFoundException("member " + id + " not found");
        }
        return member;
    }

    public int getToday() {
    return today;
}

}