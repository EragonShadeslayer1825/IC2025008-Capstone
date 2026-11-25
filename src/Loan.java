public class Loan {
    private final LibraryItem item;
    private final Member member;
    private final int borrowedDay;
    private final int dueDay;
    private final int finePerDay;

    public Loan(
            LibraryItem item,
            Member member,
            int borrowedDay,
            int dueDay,
            int finePerDay) {
        this.item = item;
        this.member = member;
        this.borrowedDay = borrowedDay;
        this.dueDay = dueDay;
        this.finePerDay = finePerDay;
    }

    public LibraryItem getItem() {
        return item;
    }

    public Member getMember() {
        return member;
    }

    public int getDueDay() {
        return dueDay;
    }

    public int daysLate(int today) {
        if (today <= dueDay) {
            return 0;
        }
        return today - dueDay;
    }

    public int fine(int today) {
        return daysLate(today) * finePerDay;
    }

    public boolean isOverdue(int today) {
        return today > dueDay;
    }
}