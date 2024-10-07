package model;

/** 
 * Represents an account with id, owner name, transactions 
 * and stock holdings & balance
*/
public class Account {
    private Portfolio portfolio; // account portfolio
    private double cashBalance; // total cash value available (dollars)

    /**
     * SPECIFIES: Construct an account with initial deposit and new portfolio
     */
    public Account(double initialDeposit) {
        //stub
    }

    public void buyStock(Stock stock, int quantity) {
        //stub
    }

    public void sellStock(Stock stock, int quantity) {
        //stub
    }

    public double getCashBalance() {
        return this.cashBalance;
    }

}
