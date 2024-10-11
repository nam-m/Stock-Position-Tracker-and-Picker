package model;

import java.math.BigDecimal;

import utils.PriceUtils;

// Represents a stock position with a stock and the average cost of its shares
public class StockPosition {
    private Stock stock;            // reference to stock object, that has symbol and price
    private int quantity;           // total quantity of shares for this stock
    private BigDecimal totalCost;   // total cost of all shares for this stock

    /**
     * REQUIRES: quantity > 0
     * SPECIFIES: Construct stock position to hold a specific stock and their average cost
     */
    public StockPosition(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
        this.totalCost = PriceUtils.roundPrice(stock.getPrice().doubleValue() * quantity);
    }

    /** 
     * REQUIRES: quantity > 0
     * MODIFIES: this
     * EFFECTS: increase the stock position when buying more shares
     */
    public void increasePosition(int quantity) {
        double addedCost = stock.getPrice().doubleValue() * quantity;
        this.totalCost = PriceUtils.roundPrice(this.totalCost.doubleValue() + addedCost);
        this.quantity += quantity;
    }

    /** 
     * REQUIRES: 0 < quantity <= this.quantity
     * MODIFIES: this
     * EFFECTS: decrease the stock position when selling existing shares
     */
    public void decreasePosition(int quantity) {
        if (quantity > 0 && quantity <= this.quantity) {
            double decreasedValue = stock.getPrice().doubleValue() * quantity;
            this.totalCost = PriceUtils.roundPrice(this.totalCost.doubleValue() - decreasedValue);
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

    /**
     * SPECIFIES: return average cost of the stock position
     */
    public BigDecimal getAverageCost() {
        if (this.quantity > 0) {
            double averageCost = this.totalCost.doubleValue() / this.quantity;
            return PriceUtils.roundPrice(averageCost);
        }
        return PriceUtils.roundPrice(0);
    }
}
