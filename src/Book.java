public class Book extends LibraryItem implements Borrowable {
    private String author;

    public Book(String id, String title, String author) {
        super(id, title);
        this.author = author;
    }

    @Override
    public String getType() {
        return "BOOK";
    }

    @Override
    public boolean isBorrowable() {
        return true;
    }

    @Override
    public int getFineRatePerDay() {
        return 2;
    }
}