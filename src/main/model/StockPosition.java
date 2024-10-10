package model;

import java.math.BigDecimal;

import utils.PriceUtils;

// Represents a stock position with a stock and the average cost of its shares
public class StockPosition {
    private Stock stock;        // reference to stock object, that has symbol and price
    private int quantity;       // total quantity of shares for this stock
    private BigDecimal totalCost; // total cost of all shares for this stock

    /**
     * SPECIFIES: Construct stock position to hold a specific stock and their average cost
     */
    public StockPosition(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
        this.totalCost = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    /** 
     * REQUIRES: quantity > 0
     * MODIFIES: this
     * EFFECTS: increase the stock position when buying more shares
     */
    public void increasePosition(int quantity) {
        BigDecimal addedCost = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
        this.totalCost = this.totalCost.add(addedCost);
        this.quantity += quantity;
    }

    /** 
     * REQUIRES: 0 < quantity <= this.quantity
     * MODIFIES: this
     * EFFECTS: decrease the stock position when selling existing shares
     */
    public void decreasePosition(int quantity) {
        if (quantity > 0 && quantity <= this.quantity) {
            BigDecimal decreasedValue = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
            this.totalCost = this.totalCost.subtract(decreasedValue);
            this.quantity -= quantity;
        }
    }

    public Stock getStock() {
        return this.stock;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public BigDecimal getTotalCost() {
        return this.totalCost;
    }

    public BigDecimal getAverageCost() {
        if (this.quantity > 0) {
            double averageCost = this.totalCost.doubleValue() / this.quantity;
            return PriceUtils.roundPrice(averageCost);
        }
        return PriceUtils.roundPrice(0);
    }
}
