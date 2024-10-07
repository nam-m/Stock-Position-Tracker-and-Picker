package model;

// Represents a stock position with a stock and the average cost of its shares
public class StockPosition {
    private Stock stock;        // reference to stock object, that has symbol and price
    private int quantity;       // total quantity of a stock
    private double averageCost; // average cost per share (dollars)

    /**
     * SPECIFIES: Construct stock position to hold a specific stock and their average cost
     */
    public StockPosition(Stock stock, double averageCost) {
        //stub
    }

    public Stock getStock() {
        return this.stock;
    }

    public int getNumberOfShares() {
        return this.quantity;
    }

    public double getAverageCost() {
        return this.averageCost;
    }
}
