public class Magazine extends LibraryItem implements Borrowable {
    private String issue;

    public Magazine(String id, String title, String issue) {
        super(id, title);
        this.issue = issue;
    }

    @Override
    public String getType() {
        return "MAGAZINE";
    }

    @Override
    public boolean isBorrowable() {
        return true;
    }

    @Override
    public int getFineRatePerDay() {
        return 1;
    }
}