package model;

import java.math.BigDecimal;
import java.util.UUID;

import org.json.JSONObject;

import persistence.Writable;
import ui.StockRepository;
import utils.PriceUtils;

/** 
 * Represents an account with id, owner name, transactions 
 * and stock holdings & balance
*/
public class Account implements Writable {
    private final String id;        //account id
    private String name;            // account name
    private Portfolio portfolio;    // account portfolio
    private BigDecimal cashBalance; // total cash value available (dollars)

    /**
     * REQUIRES: initialDeposit > 0
     * SPECIFIES: Construct an account with initial deposit and new portfolio
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
     * SPECIFIES: buy stock from portfolio, and decrease cash balance by total cost
     */
    public void buyStock(String stockSymbol, int quantity) {
        Stock stock = StockRepository.getStockBySymbol(stockSymbol);
        double totalCost = stock.getPrice().doubleValue() * quantity;
        if (totalCost <= this.cashBalance.doubleValue()) {
            this.portfolio.buyStock(stock, quantity);
            this.cashBalance = PriceUtils.roundPrice(this.cashBalance.doubleValue() - totalCost);
        }
    }

    /**
     * REQUIRES: 0 < quantity <= quantity owned for in stock position
     * MODIFIES: this
     * SPECIFIES: sell stock from portfolio, and increase cash balance by sell value
     */
    public void sellStock(String stockSymbol, int quantity) {
        Stock stock = StockRepository.getStockBySymbol(stockSymbol);
        this.portfolio.sellStock(stock, quantity);
        double sellValue = stock.getPrice().doubleValue() * quantity;
        this.cashBalance = PriceUtils.roundPrice(this.cashBalance.doubleValue() + sellValue);
    }

    /**
     * REQUIRES: depositValue > 0
     * MODIFIES: this
     * SPECIFIES: increase cash balance by depositValue
     */
    public void deposit(double depositValue) {
        double newBalance = this.cashBalance.doubleValue() + depositValue;
        this.cashBalance = PriceUtils.roundPrice(newBalance);
    }

    /**
     * REQUIRES: 0 < withdrawValue <= cashBalance
     * MODIFIES: this
     * SPECIFIES: decrease cash balance by withdrawValue
     */
    public void withdraw(double withdrawValue) {
        double newBalance = this.cashBalance.doubleValue() - withdrawValue;
        this.cashBalance = PriceUtils.roundPrice(newBalance);
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
        json.put("cashBalance", this.getCashBalance().toString());
        json.put("portfolio", this.getPortfolio().toJson());
        return json;
    }

}
