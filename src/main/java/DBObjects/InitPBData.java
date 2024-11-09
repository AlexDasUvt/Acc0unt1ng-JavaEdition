package DBObjects;

public class InitPBData {
    public String PersonBank;
    public Double Sum;
    public String Currency;

    public InitPBData(String PersonBank, Double Sum, String Currency) {
        this.PersonBank = PersonBank;
        this.Sum = Sum;
        this.Currency = Currency;
    }

    public InitPBData(String line) {
        String[] fields = line.split(",");
        this.PersonBank = fields[0];
        this.Sum = Double.parseDouble(fields[1]);
        this.Currency = fields[2];
    }
}
