package model;

import java.math.BigDecimal;

import org.json.JSONObject;

import persistence.Writable;
import utils.PriceUtils;

// Represents a stock with its symbol and price (in dollars)
public class Stock implements Writable {
    private String symbol;      // stock symbol
    private BigDecimal price;   // current stock price

    /**
     * REQUIRES: price > 0
     * EFFECTS: Construct a stock with its symbol and price
     */
    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = PriceUtils.roundPrice(price);
    }

    public String getSymbol() {
        return this.symbol;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    /**
     * EFFECTS: convert stock to json
     */
    @Override
    public JSONObject toJson() {
    } 
}
