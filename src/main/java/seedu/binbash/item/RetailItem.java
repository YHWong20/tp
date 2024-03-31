package seedu.binbash.item;

public class RetailItem extends Item {

    private double itemSalePrice;
    private int totalUnitsSold;
    public RetailItem(String itemName, String itemDescription, int itemQuantity,
                      double itemSalePrice, double itemCostPrice, Integer itemThreshold) {
        super(itemName, itemDescription, itemQuantity, itemCostPrice, itemThreshold);
        this.itemSalePrice = itemSalePrice;
        this.totalUnitsSold = 0;
    }

    public double getItemSalePrice() {
        return itemSalePrice;
    }

    public void setItemSalePrice(double itemSalePrice) {
        this.itemSalePrice = itemSalePrice;
    }

    public int getTotalUnitsSold() {
        return totalUnitsSold;
    }

    public void setTotalUnitsSold(int totalUnitsSold) {
        this.totalUnitsSold = totalUnitsSold;
    }

    @Override
    public String toString() {
        return "[R] " + super.toString() + System.lineSeparator()
                + String.format("\tsale price: $%.2f", itemSalePrice) + System.lineSeparator()
                + String.format("\tthreshold: %d", itemThreshold);
    }
}
