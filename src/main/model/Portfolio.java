package model;

import java.util.Map;

// Represents a portfolio that manages buying/selling stocks
// and adjusting stock positions if needed
public class Portfolio {
    private Map<String, StockPosition> stockPositions; // map of stock symbol to StockPosition

    /**
     * EFFECTS: construct portfolio for stock position management
     */
    public Portfolio() {
        //stub
    }

    /**
     * REQUIRES: quantity > 0, pricePerShare > 0
     * MODIFIES: this
     * EFFECTS: buy stock and add to existing position or create new one
     */
    public void buyStock(Stock stock, int quantity, double pricePerShare) {
        //stub
    }

    /**
     * REQUIRES: quantity > 0
     * MODIFIES: this
     * EFFECTS: sell stock and reduce from existing position or remove it
     */
    public void sellStock(Stock stock, int quantity) {
        //stub
    }

    /**
     * EFFECTS: get total value of all stock positions
     */
    public double getTotalValue() {
        //stub
    }
}
