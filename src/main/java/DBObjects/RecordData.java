package DBObjects;

import Interfaces.Readable;

public class RecordData implements Readable {
    public String date;
    public String category;
    public String subCategory;
    public String personBank;
    public Double sum;
    public String currency;
    public String comment;
    private Boolean isEmpty = true;

    public RecordData(String date, String category, String subCategory, String personBank, Double sum, String currency, String comment) {
        this.date = date;
        this.category = category;
        this.subCategory = subCategory;
        this.personBank = personBank;
        this.sum = sum;
        this.currency = currency;
        if (comment == null || comment.equals(" ")) {
            this.comment = " ";
        } else {
            this.comment = comment;
        }
        isEmpty = false;
    }

    @Override
    public Boolean isReadable() {
        return !isEmpty;
    }

    public boolean isValid() {
        return (date != null && !date.isEmpty()) &&
               (category != null && !category.isEmpty()) &&
               (personBank != null && !personBank.isEmpty()) &&
               (currency != null && !currency.isEmpty()) &&
               (sum != 0);
    }
}
