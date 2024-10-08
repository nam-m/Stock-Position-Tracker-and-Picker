package model;

import java.math.BigDecimal;

import utils.PriceUtils;

// Represents a stock position with a stock and the average cost of its shares
public class StockPosition {
    private Stock stock;        // reference to stock object, that has symbol and price
    private int quantity;       // total quantity of a stock
    private BigDecimal averageCost; // average cost per share (dollars)

    /**
     * SPECIFIES: Construct stock position to hold a specific stock and their average cost
     */
    public StockPosition(Stock stock, int quantity, double averageCost) {
        this.stock = stock;
        this.quantity = quantity;
        this.averageCost = PriceUtils.roundPrice(averageCost);
    }

    /** 
     * REQUIRES: quantity > 0, pricePerShare > 0
     * MODIFIES: this
     * EFFECTS: increase the stock position when buying more shares
     */
    public void increasePosition(int quantity, double pricePerShare) {
        double increasedCost = quantity * pricePerShare;
        double totalCost = this.quantity * this.averageCost.doubleValue() + increasedCost;
        this.quantity += quantity;
        this.averageCost = PriceUtils.roundPrice(totalCost / this.quantity);
    }

    /** 
     * REQUIRES: quantity > 0
     * MODIFIES: this
     * EFFECTS: decrease the stock position when selling existing shares
     */
    public void decreasePosition(int quantity) {
        if (quantity > 0) {
            if (quantity > this.quantity) {
                System.out.println("Cannot sell more than owned quantity");
            }
            this.quantity -= quantity;
        }
    }

    public Stock getStock() {
        return this.stock;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public BigDecimal getAverageCost() {
        return this.averageCost;
    }
}
