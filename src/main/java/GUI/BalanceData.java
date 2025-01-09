package GUI;

public class BalanceData {
    private final String pb;
    private final String sum;
    private final String currency;

    public BalanceData(String pb, String sum, String currency) {
        this.pb = pb;
        this.sum = sum;
        this.currency = currency;
    }

    public String getPb() {
        return pb;
    }

    public String getSum() {
        return sum;
    }

    public String getCurrency() {
        return currency;
    }

}
