package GUI;

public class HistoryMainData {
    private final String id;
    private final String date;
    private final String sum;
    private final String currency;
    private final String category;
    private final String subcategory;
    private final String comment;
    private final String pb;

    public HistoryMainData(String id, String date, String sum, String currency, String category, String subcategory, String comment, String pb) {
        this.id = id;
        this.date = date;
        this.sum = sum;
        this.currency = currency;
        this.category = category;
        this.subcategory = subcategory;
        this.comment = comment;
        this.pb = pb;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getSum() {
        return sum;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getComment() {
        return comment;
    }

    public String getPb() {
        return pb;
    }
}
