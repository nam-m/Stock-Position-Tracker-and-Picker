package model;

import java.math.BigDecimal;

import utils.PriceUtils;

// Represents a stock position with a stock and the average cost of its shares
public class StockPosition {
    private Stock stock;        // reference to stock object, that has symbol and price
    private int quantity;       // total quantity of shares for this stock
    private BigDecimal totalCost; // total cost of all shares for this stock
    private BigDecimal averageCost; // average cost of all shares for this stock

    /**
     * SPECIFIES: Construct stock position to hold a specific stock and their average cost
     */
    public StockPosition(Stock stock, int quantity, double pricePerShare) {
        this.stock = stock;
        this.quantity = quantity;
        this.totalCost = PriceUtils.roundPrice(this.quantity * pricePerShare);
        this.averageCost = PriceUtils.roundPrice(this.totalCost.doubleValue() / this.quantity);
    }

    /** 
     * REQUIRES: quantity > 0, pricePerShare > 0
     * MODIFIES: this
     * EFFECTS: increase the stock position when buying more shares
     */
    public void increasePosition(int quantity, double pricePerShare) {
        if (quantity > 0 && pricePerShare > 0) {
            double increasedCost = quantity * pricePerShare;
            this.quantity += quantity;
            this.totalCost = PriceUtils.roundPrice(this.totalCost.doubleValue() + increasedCost);
            this.averageCost = PriceUtils.roundPrice(this.totalCost.doubleValue() / this.quantity);
        }
    }

    /** 
     * REQUIRES: 0 < quantity <= this.quantity
     * MODIFIES: this
     * EFFECTS: decrease the stock position when selling existing shares
     */
    public void decreasePosition(int quantity) {
        if (quantity > 0 && quantity <= this.quantity) {
            this.quantity -= quantity;
            double decreasedValue = quantity * this.averageCost.doubleValue();
            this.totalCost = PriceUtils.roundPrice(this.totalCost.doubleValue() - decreasedValue);
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
        return this.averageCost;
    }
}
