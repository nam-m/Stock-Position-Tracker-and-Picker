package model;

// Represents a stock with its symbol and price (in dollars)
public class Stock {
    private String symbol;
    private double price;

    /**
     * REQUIRES: symbol has non-zero length, price > 0
     * EFFECTS: Construct a stock with its symbol and price
     */
    public Stock(String symbol, double price) {
        //stub
    }

    public String getSymbol() {
        return this.symbol;
    }

    public double getPrice() {
        return this.price;
    }
}
