package seedu.binbash.inventory;

import seedu.binbash.comparators.ItemComparatorByExpiryDate;
import seedu.binbash.item.Item;
import seedu.binbash.item.OperationalItem;
import seedu.binbash.item.PerishableOperationalItem;
import seedu.binbash.item.PerishableRetailItem;
import seedu.binbash.item.RetailItem;
import seedu.binbash.command.RestockCommand;
import seedu.binbash.logger.BinBashLogger;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import static java.util.stream.Collectors.toList;

public class ItemList {
    private static final BinBashLogger logger = new BinBashLogger(ItemList.class.getName());
    private double totalRevenue;
    private double totalCost;
    private final ArrayList<Item> itemList;
    private ArrayList<Integer> sortedOrder;
    private SearchAssistant searchAssistant;

    public ItemList() {
        this.itemList = new ArrayList<Item>();
        this.sortedOrder = new ArrayList<Integer>();
        this.totalRevenue = 0;
        this.totalCost = 0;
        searchAssistant = new SearchAssistant();
    }

    public double getTotalRevenue() {
        double totalRevenue = 0;

        for (Item item: itemList) {
            if (item instanceof RetailItem) {
                // Downcast made only after checking if item is a RetailItem (and below) object.
                // TODO: Add an assert statement to verify code logic.
                RetailItem retailItem = (RetailItem) item;
                totalRevenue += (retailItem.getTotalUnitsSold() * retailItem.getItemSalePrice());
            }
        }

        return totalRevenue;
    }

    public double getTotalCost() {
        double totalCost = 0;

        for (Item item: itemList) {
            totalCost += (item.getTotalUnitsPurchased() * item.getItemCostPrice());
        }

        return totalCost;
    }

    public String getProfitMargin() {
        double totalCost = getTotalCost();
        double totalRevenue = getTotalRevenue();
        double netProfit = totalRevenue - totalCost;

        String output =
                String.format("Here are your metrics: " + System.lineSeparator()
                        + "\tTotal Cost: %.2f" + System.lineSeparator()
                        + "\tTotal Revenue: %.2f" + System.lineSeparator()
                        + "\tNet Profit: %.2f" + System.lineSeparator(),
                        totalCost,
                        totalRevenue,
                        netProfit);
        return output;
    }

    public SearchAssistant getSearchAssistant() {
        searchAssistant.setFoundItems(itemList);
        return searchAssistant;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public int getItemCount() {
        return itemList.size();
    }

    public String addItem(String itemType, String itemName, String itemDescription, int itemQuantity,
                          LocalDate itemExpirationDate, double itemSalePrice, double itemCostPrice) {
        Item item;
        if (itemType.equals("retail") && !itemExpirationDate.equals(LocalDate.MIN)) {
            // Perishable Retail Item
            item = new PerishableRetailItem(itemName, itemDescription, itemQuantity,
                    itemExpirationDate, itemSalePrice, itemCostPrice);
        } else if (itemType.equals("retail") && itemExpirationDate.equals(LocalDate.MIN)) {
            // Non-perishable Retail Item
            item = new RetailItem(itemName, itemDescription, itemQuantity, itemSalePrice, itemCostPrice);
        } else if (itemType.equals("operational") && !itemExpirationDate.equals(LocalDate.MIN)) {
            // Perishable Operational Item
            item = new PerishableOperationalItem(itemName, itemDescription, itemQuantity,
                    itemExpirationDate, itemCostPrice);
        } else {
            // Non-perishable Operational Item
            item = new OperationalItem(itemName, itemDescription, itemQuantity, itemCostPrice);
        }

        int beforeSize = itemList.size();
        sortedOrder.add(beforeSize);
        itemList.add(item);
        assert itemList.size() == (beforeSize + 1);
        assert sortedOrder.size() == (beforeSize + 1);

        String output = "Noted! I have added the following item into your inventory:" + System.lineSeparator()
                + System.lineSeparator() + item;
        return output;
    }

    public String updateItemQuantity(String itemName, int itemQuantity, String command) {
        String output = "Sorry, I can't find the item you are looking for.";

        for (Item item : itemList) {
            int newQuantity = item.getItemQuantity();

            if (!item.getItemName().trim().equals(itemName.trim())) {
                continue;
            }

            // Restocking item consists of (i) Updating itemQuantity, (ii) Updating totalUnitsPurchased
            if (command.trim().equals(RestockCommand.COMMAND.trim())) {
                newQuantity += itemQuantity;

                int totalUnitsPurchased = item.getTotalUnitsPurchased();
                item.setTotalUnitsPurchased(totalUnitsPurchased + itemQuantity);

            // Selling item consists of (i) Updating itemQuantity, (ii) Updating totalUnitsSold
            } else {
                newQuantity -= itemQuantity;

                // Stinky downcast?
                // I'm going off the assertion that only retail items (and its subclasses) can be sold.
                // TODO: Add an assert statement to verify code logic.
                RetailItem retailItem = (RetailItem)item;

                int totalUnitsSold = retailItem.getTotalUnitsSold();
                retailItem.setTotalUnitsSold(totalUnitsSold + itemQuantity);
            }
            item.setItemQuantity(newQuantity);
            output = "Great! I have updated the quantity of the item for you:" + System.lineSeparator()
                    + System.lineSeparator() + item;

        }
        return output;
    }

    /**
     * Deletes an item from the inventory by identifying the item using its index.
     *
     * @param index index of the item to be deleted.
     * @return the message indicating which item was deleted.
     */
    public String deleteItem(int index) {
        logger.info("Attempting to delete an item");
        int beforeSize = itemList.size();
        Item tempItem = itemList.remove(index - 1);
        assert itemList.size() == (beforeSize - 1);

        String output = "Got it! I've removed the following item:" + System.lineSeparator()
                + System.lineSeparator() + tempItem;
        logger.info("An item has been deleted");
        return output;
    }

    /**
     * Deletes an item from the inventory by identifying the item using its name.
     *
     * @param keyword the name of the item to be deleted.
     * @return the message indicating which item was deleted.
     */
    public String deleteItem(String keyword) {
        int targetIndex = -1;
        Item currentItem;
        for (int i = 0; i < itemList.size(); i ++) {
            currentItem = itemList.get(i);
            if (currentItem.getItemName().trim().equals(keyword)) {
                logger.info("first matching item at index " + i + " found.");
                targetIndex = i + 1;
                break;
            }
        }

        if (targetIndex == -1) {
            logger.info("No matching item was found, no item was deleted.");
            String output = "Item not found! Nothing was deleted!";
            return output;
        }

        return deleteItem(targetIndex);
    }

    /**
     * Returns a string representation of all the items in the list. Each item's string
     * representation is obtained by calling its `toString` method.
     *
     * @return A concatenated string of all item representations in the list, each on a new line.
     */
    public String printList(List<Item> itemList) {
        int index = 1;
        String output = "";

        sortedOrder.clear();
        for (Item item: itemList) {
            sortedOrder.add(index - 1);
            output += index + ". " + item.toString() + System.lineSeparator() + System.lineSeparator();
            index++;
        }

        return output;
    }

    public String printListSortedByExpiryDate(List<Item> itemList) {
        int index = 1;
        String output = "";
        ArrayList<Item> sortedList = (ArrayList<Item>) itemList.stream()
                .filter((t) -> t instanceof PerishableOperationalItem || t instanceof PerishableRetailItem)
                .sorted(new ItemComparatorByExpiryDate())
                .collect(toList());

        sortedOrder.clear();
        for (int i = 0; i < sortedList.size(); i++) {
            sortedOrder.add(itemList.indexOf(sortedList.get(i)));
        }

        for (Item item: sortedList) {
            output += index + ". " + item.toString() + System.lineSeparator() + System.lineSeparator();
            index++;
        }

        return output;
    }

    @Override
    public String toString() {
        return itemList.toString();
    }
}
