package model;

import java.math.BigDecimal;

import utils.PriceUtils;

// Represents a stock with its symbol and price (in dollars)
public class Stock {
    private String symbol; // stock symbol
    private BigDecimal price;  // current stock price

    /**
     * REQUIRES: symbol has non-zero length, price > 0
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
}
