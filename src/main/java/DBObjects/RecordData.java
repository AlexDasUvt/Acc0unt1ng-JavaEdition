package DBObjects;

import Interfaces.Validatable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RecordData implements Validatable {
    public String date;
    public String category;
    public String subCategory;
    public String personBank;
    public String personBankTo;
    public Double sum;
    public String currency;
    public String comment;

    @JsonCreator
    public RecordData(@JsonProperty("date") String date,
                      @JsonProperty("category") String category,
                      @JsonProperty("subCategory") String subCategory,
                      @JsonProperty("personBank") String personBank,
                      @JsonProperty("personBankTo") String personBankTo,  // Optional field
                      @JsonProperty("sum") Double sum,
                      @JsonProperty("currency") String currency,
                      @JsonProperty("comment") String comment) {
        this.date = date;
        this.category = category;
        this.subCategory = subCategory;
        this.personBank = personBank;
        this.personBankTo = personBankTo;
        this.sum = sum;
        this.currency = currency;
        this.comment = comment == null || comment.equals(" ") ? " " : comment;
    }

    @Override
    public boolean isValid() {
        return (date != null && !date.isEmpty()) &&
               (category != null && !category.isEmpty()) &&
               (personBank != null && !personBank.isEmpty()) &&
               (currency != null && !currency.isEmpty()) &&
               (sum != 0);
    }

    public boolean isValidTransfer() {
        return (date != null && !date.isEmpty()) &&
               (personBank != null && !personBank.isEmpty()) &&
               (personBankTo != null && !personBankTo.isEmpty()) &&
               (currency != null && !currency.isEmpty()) &&
               (sum != 0) &&
               (sum > 0);
    }
}
