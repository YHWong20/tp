package seedu.binbash.item;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PerishableRetailItem extends RetailItem {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private LocalDate itemExpirationDate;

    public PerishableRetailItem(String itemName, String itemDescription, int itemQuantity,
                                LocalDate itemExpirationDate, double itemSalePrice, double itemCostPrice,
                                int itemThreshold) {
        super(itemName, itemDescription, itemQuantity, itemSalePrice, itemCostPrice, itemThreshold);
        this.itemExpirationDate = itemExpirationDate;
    }

    public String getItemExpirationDate() {
        return itemExpirationDate.format(DATE_TIME_FORMATTER);
    }

    public void setItemExpirationDate(LocalDate itemExpirationDate) {
        this.itemExpirationDate = itemExpirationDate;
    }

    public LocalDate getLocalDateItemExpirationDate() {
        return itemExpirationDate;
    }

    @Override
    public String toString() {
        return "[P]" + super.toString() + System.lineSeparator() +
                String.format("\texpiry date: %s", itemExpirationDate.format(DATE_TIME_FORMATTER));
    }
}
