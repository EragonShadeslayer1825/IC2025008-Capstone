public class Staff extends Member {
    public Staff(String id, String name) {
        super(id, name);
    }

    @Override
    public int getMaxItems() {
        return 4;
    }

    @Override
    public int getLoanPeriodDays() {
        return 21;
    }
}