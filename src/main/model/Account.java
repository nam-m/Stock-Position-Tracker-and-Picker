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
    private final String id;
    private String name;
    private Portfolio portfolio; // account portfolio
    private BigDecimal cashBalance; // total cash value available (dollars)

    /**
     * SPECIFIES: Construct an account with initial deposit and new portfolio
     */
    public Account(String name, double initialDeposit) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.portfolio = new Portfolio();
        this.cashBalance = PriceUtils.roundPrice(initialDeposit);
    }

    public void buyStock(String stockSymbol, int quantity) {
        Stock stock = StockRepository.getStockBySymbol(stockSymbol);
        System.out.println(stock.getSymbol());
        BigDecimal totalCost = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
        if (totalCost.doubleValue() <= this.cashBalance.doubleValue()) {
            this.portfolio.buyStock(stock, quantity);
            this.cashBalance = this.cashBalance.subtract(totalCost);
        }
    }

    public void sellStock(String stockSymbol, int quantity) {
        Stock stock = StockRepository.getStockBySymbol(stockSymbol);
        if (quantity > 0 && quantity <= this.portfolio.getStockPosition(stock.getSymbol()).getQuantity()) {
            this.portfolio.sellStock(stock, quantity);
            BigDecimal sellValue = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
            this.cashBalance = this.cashBalance.add(sellValue);
        }
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
