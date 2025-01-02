package GUI;

public class HistoryTransferData {
    private final String id;
    private final String date;
    private final String sum;
    private final String currency;
    private final String pbFrom;
    private final String pbTo;
    private final String comment;

    public HistoryTransferData(String id, String date, String sum, String currency, String pbFrom, String pbTo, String comment) {
        this.id = id;
        this.date = date;
        this.sum = sum;
        this.currency = currency;
        this.pbFrom = pbFrom;
        this.pbTo = pbTo;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public String getReceiver() {
        return pbTo;
    }

    public String getSender() {
        return pbFrom;
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

}
