package seedu.binbash.item;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Perishable Operational Item in the ItemList (inventory).
 * Operational Items are Items meant for internal use, to ensure business functionality.
 * These Items are not meant to be sold, and thus, have no sale price.
 * Perishable Operational Items have an expiration date.
 */
public class PerishableOperationalItem extends OperationalItem {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private LocalDate itemExpirationDate;

    public PerishableOperationalItem(String itemName, String itemDescription, int itemQuantity,
                                     LocalDate itemExpirationDate, double itemCostPrice) {
        this(itemName, itemDescription, itemQuantity, itemExpirationDate, itemCostPrice, -1);
    }

    public PerishableOperationalItem(String itemName, String itemDescription, int itemQuantity,
                                     LocalDate itemExpirationDate, double itemCostPrice, int itemThreshold) {
        super(itemName, itemDescription, itemQuantity, itemCostPrice, itemThreshold);
        this.itemExpirationDate = itemExpirationDate;
    }

    /**
     * Returns the expiration date of the Item.
     *
     * @return Item expiration date, formatted as a String in dd-MM-yyyy format.
     */
    public String getItemExpirationDate() {
        return itemExpirationDate.format(DATE_TIME_FORMATTER);
    }

    /**
     * Returns the expiration date of the Item as a LocalDate object.
     *
     * @return Item expiration date (as LocalDate object).
     */
    public LocalDate getLocalDateItemExpirationDate() {
        return itemExpirationDate;
    }

    /**
     * Sets the Item expiration date.
     *
     * @param itemExpirationDate The new expiration date of the Item.
     */
    public void setItemExpirationDate(LocalDate itemExpirationDate) {
        this.itemExpirationDate = itemExpirationDate;
    }

    @Override
    public String toString() {
        return "[P]" + super.toString() + System.lineSeparator() +
                String.format("\texpiry date: %s", itemExpirationDate.format(DATE_TIME_FORMATTER));
    }
}
