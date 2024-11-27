package model;

import java.math.BigDecimal;
import java.util.UUID;

import org.json.JSONObject;

import observer.Observable;
import persistence.Writable;
import ui.StockRepository;
import utils.PriceUtils;

/** 
 * Represents an account with id, owner name, transactions 
 * and stock holdings & balance
*/
public class Account extends Observable implements Writable {
    private final String id;        //account id
    private String name;            // account name
    private Portfolio portfolio;    // account portfolio
    private BigDecimal cashBalance; // total cash value available (dollars)

    /**
     * REQUIRES: initialDeposit > 0
     * EFFECTS: Construct an account with initial deposit and new portfolio
     */
    public Account(String name, double initialDeposit) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.portfolio = new Portfolio();
        this.cashBalance = PriceUtils.roundPrice(initialDeposit);
    }

    /**
     * REQUIRES: quantity > 0
     * MODIFIES: this
     * EFFECTS: buy stock from portfolio, and decrease cash balance by total cost
     */
    public void buyStock(String symbol, int quantity) {
        Stock stock = StockRepository.getStockBySymbol(symbol);
        double totalCost = stock.getPrice().doubleValue() * quantity;
        if (totalCost <= this.cashBalance.doubleValue()) {
            this.portfolio.buyStock(stock, quantity);
            this.cashBalance = PriceUtils.roundPrice(this.cashBalance.doubleValue() - totalCost);
        }
        notifyObservers(this, EventType.PORTFOLIO_CHANGED);
        notifyObservers(this, EventType.BALANCE_CHANGED);
        String shareString = quantity > 1 ? "shares" : "share";
        EventLog.getInstance().logEvent(new Event("Bought " + quantity + " " + shareString + " of " + symbol));
    }

    /**
     * REQUIRES: 0 < quantity <= quantity owned for in stock position
     * MODIFIES: this
     * EFFECTS: sell stock from portfolio, and increase cash balance by sell value
     */
    public void sellStock(String symbol, int quantity) {
        Stock stock = StockRepository.getStockBySymbol(symbol);
        this.portfolio.sellStock(stock, quantity);
        double sellValue = stock.getPrice().doubleValue() * quantity;
        this.cashBalance = PriceUtils.roundPrice(this.cashBalance.doubleValue() + sellValue);
        notifyObservers(this, EventType.PORTFOLIO_CHANGED);
        notifyObservers(this, EventType.BALANCE_CHANGED);
        String shareString = quantity > 1 ? "shares" : "share";
        EventLog.getInstance().logEvent(new Event("Sold " + quantity + " " + shareString + " of " + symbol));
    }

    /**
     * REQUIRES: depositValue > 0
     * MODIFIES: this
     * EFFECTS: increase cash balance by depositValue
     */
    public void deposit(double depositValue) {
        double newBalance = this.cashBalance.doubleValue() + depositValue;
        this.cashBalance = PriceUtils.roundPrice(newBalance);
        notifyObservers(this, EventType.BALANCE_CHANGED);
        EventLog.getInstance().logEvent(new Event("Deposited $" + depositValue));
    }

    /**
     * REQUIRES: 0 < withdrawValue <= cashBalance
     * MODIFIES: this
     * EFFECTS: decrease cash balance by withdrawValue
     */
    public void withdraw(double withdrawValue) {
        double newBalance = this.cashBalance.doubleValue() - withdrawValue;
        this.cashBalance = PriceUtils.roundPrice(newBalance);
        notifyObservers(this, EventType.BALANCE_CHANGED);
        EventLog.getInstance().logEvent(new Event("Withdrew $" + withdrawValue));
    }

    public String getAccountId() {
        return this.id;
    }

    public String getAccountName() {
        return this.name;
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    public BigDecimal getCashBalance() {
        return this.cashBalance;
    }

/**
     * EFFECTS: convert account data to json
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.getAccountName());
        json.put("balance", this.getCashBalance().toString());
        json.put("portfolio", this.getPortfolio().toJson());
        return json;
    }
}
