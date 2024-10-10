package model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import utils.PriceUtils;

// Represents a portfolio that manages buying/selling stocks
// and adjusting stock positions if needed
public class Portfolio {
    private Map<String, StockPosition> positions; // map of stock symbol to StockPosition

    /**
     * EFFECTS: construct portfolio for stock position management
     */
    public Portfolio() {
        this.positions = new HashMap<>();
    }

    /**
     * REQUIRES: quantity > 0, pricePerShare > 0
     * MODIFIES: this
     * EFFECTS: buy stock and add to existing position or create new one
     */
    public void buyStock(Stock stock, int quantity) {
        StockPosition position = positions.get(stock.getSymbol());
        // Increase position if there is already one
        // or create a new one
        if (position != null) {
            position.increasePosition(quantity);
        } else {
            positions.put(stock.getSymbol(), new StockPosition(stock, quantity));
        }
    }

    /**
     * REQUIRES: quantity > 0
     * MODIFIES: this
     * EFFECTS: sell stock and reduce from existing position or remove it
     */
    public void sellStock(Stock stock, int quantity) {
        StockPosition position = positions.get(stock.getSymbol());
        if (position != null) {
            if (quantity > 0 && quantity < position.getQuantity()) {
                position.decreasePosition(quantity);
            } else if (quantity == position.getQuantity()) {
                positions.remove(stock.getSymbol());
            }
        }
    }

    /**
     * EFFECTS: get total value of all stock positions
     */
    public BigDecimal getTotalValue() {
        double totalValue = 0;
        for (StockPosition position : this.positions.values()) {
            totalValue += position.getTotalCost().doubleValue();
        }
        return PriceUtils.roundPrice(totalValue);
    }

    /**
     * EFFECTS: get stock position based on stock symbol
     */
    public StockPosition getStockPosition(String symbol) {
        return this.positions.get(symbol);
    }

    /**
     * EFFECTS: get total number of stock positions
     */
    public int getTotalStockPositions() {
        return this.positions.size();
    }
}
