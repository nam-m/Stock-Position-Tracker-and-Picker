package model;

import java.math.BigDecimal;
import java.util.UUID;

import ui.StockRepository;
import utils.PriceUtils;

/** 
 * Represents an account with id, owner name, transactions 
 * and stock holdings & balance
*/
public class Account {
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
     * SPECIFIES: buy stock from portfolio, and decrease cash balance by total cost
     */
    public void buyStock(String stockSymbol, int quantity) {
        Stock stock = StockRepository.getStockBySymbol(stockSymbol);
        BigDecimal totalCost = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
        if (totalCost.doubleValue() <= this.cashBalance.doubleValue()) {
            this.portfolio.buyStock(stock, quantity);
            this.cashBalance = this.cashBalance.subtract(totalCost);
        }
    }

    /**
     * REQUIRES: 0 < quantity <= quantity owned for in stock position
     * SPECIFIES: sell stock from portfolio, and increase cash balance by sell value
     */
    public void sellStock(String stockSymbol, int quantity) {
        Stock stock = StockRepository.getStockBySymbol(stockSymbol);
        if (quantity > 0 && quantity <= this.portfolio.getStockPosition(stock.getSymbol()).getQuantity()) {
            this.portfolio.sellStock(stock, quantity);
            BigDecimal sellValue = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
            this.cashBalance = this.cashBalance.add(sellValue);
        }
    }

    /**
     * REQUIRES: depositValue > 0
     * SPECIFIES: increase cash balance by depositValue
     */
    public void deposit(double depositValue) {
        this.cashBalance = this.cashBalance.add(BigDecimal.valueOf(depositValue));
    }

    /**
     * REQUIRES: 0 < withdrawValue <= cashBalance
     * SPECIFIES: decrease cash balance by withdrawValue
     */
    public void withdraw(double withdrawValue) {
        this.cashBalance = this.cashBalance.subtract(BigDecimal.valueOf(withdrawValue));
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

}
