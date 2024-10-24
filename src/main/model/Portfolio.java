package model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import persistence.Writable;
import utils.PriceUtils;

// Represents a portfolio that manages buying/selling stocks
// and adjusting stock positions if needed
public class Portfolio implements Writable {
    private Map<String, StockPosition> positions; // map of stock symbol to StockPosition

    /**
     * EFFECTS: construct portfolio for stock position management
     */
    public Portfolio() {
        this.positions = new HashMap<>();
    }

    /**
     * REQUIRES: quantity > 0
     * MODIFIES: this
     * EFFECTS: buy stock and add to existing position or create new one
     */
    public void buyStock(Stock stock, int quantity) {
        StockPosition position = this.positions.get(stock.getSymbol());
        if (position != null) {
            position.increasePosition(quantity);
        } else {
            this.positions.put(stock.getSymbol(), new StockPosition(stock, quantity));
        }
    }

    /**
     * REQUIRES: quantity > 0 AND quantity <= quantity of the stock position
     * MODIFIES: this
     * EFFECTS: sell stock and reduce from existing position or remove it
     */
    public void sellStock(Stock stock, int quantity) {
        StockPosition position = this.positions.get(stock.getSymbol());
        if (position != null) {
            if (quantity > 0 && quantity < position.getQuantity()) {
                position.decreasePosition(quantity);
            } else if (quantity == position.getQuantity()) {
                this.positions.remove(stock.getSymbol());
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
     * EFFECTS: get all stock positions in portfolio
     */
    public Map<String, StockPosition> getAllStockPositions() {
        return this.positions;
    }

    /**
     * EFFECTS: get total number of stock positions
     */
    public int getTotalStockPositions() {
        return this.positions.size();
    }

    /**
     * EFFECTS: convert portfolio to json
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONObject positionsJson = new JSONObject();

        for (Map.Entry<String, StockPosition> entry : positions.entrySet()) {
            String symbol = entry.getKey();
            StockPosition position = entry.getValue();
            positionsJson.put(symbol, position.toJson());  // Use StockPosition's toJson method
        }

        json.put("positions", positionsJson);
        return json;
    }
}
