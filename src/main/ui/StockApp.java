package ui;

import java.math.BigDecimal;
import java.util.Scanner;

import model.Account;
import model.Stock;
import model.StockPosition;

// Represents stock picker application
// Reference: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/main/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
public class StockApp {
    private Account account; // stock app account
    private Scanner input;   // console input

    /**
     * EFFECTS: runs the stock picker application
    */
     public StockApp() {
        runStockApp();
    }

    /**
     * MODIFIES: this
     * EFFECTS: processes user input
     */
    private void runStockApp() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.nextLine();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    /**
     * MODIFIES: this
     * EFFECTS: initializes accounts
     */
    private void init() {
        account = new Account("Henry", 10000);
        input = new Scanner(System.in);
        input.useDelimiter("\r?\n|\r");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tb -> buy stock");
        System.out.println("\ts -> sell stock");
        System.out.println("\tv -> view portfolio");
        System.out.println("\tq -> quit");
    }

    /** 
     * MODIFIES: this
     * EFFECTS: processes user command
    */
    private void processCommand(String command) {
        if (command.equals("b")) {
            doBuyStock();
        } else if (command.equals("s")) {
            doSellStock();
        } else if (command.equals("v")) {
            showPortfolio();
        } else {
            System.out.println("Selection not valid...");
        }
    }
    
    /**
     * MODIFIES: this
     * EFFECTS: buy stock
     */ 
    private void doBuyStock() {
        System.out.println("Enter stock symbol:");
        String symbol = input.nextLine().trim().toUpperCase();
        Stock stock = StockRepository.getStockBySymbol(symbol);
        
        if (stock == null) {
            System.out.println("Invalid stock symbol. Please try again");
        } else {
            System.out.println("Enter quantity:");
            int quantity = Integer.parseInt(input.nextLine().trim());

            if (quantity <= 0) {
                System.out.println("Cannot buy negative quantity\n");
            } else {
                BigDecimal totalCost = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
                if (totalCost.compareTo(account.getCashBalance()) > 0) {
                    System.out.println("Cannot buy stock with total value of $" + totalCost + "\n");
                } else {
                    account.buyStock(symbol, quantity);
                    System.out.println("Bought " + quantity + " shares of " + symbol + "\n");
                    // Retrieve the updated stock position from portfolio
                    showStockPosition(account.getPortfolio().getStockPosition(symbol));
                }
            }
        }
        showCashBalance();
    }

    /**
     * MODIFIES: this
     * EFFECTS: sell stock
     */ 
    private void doSellStock() {
        System.out.println("Enter stock symbol:");
        String symbol = input.nextLine().trim().toUpperCase();
        Stock stock = StockRepository.getStockBySymbol(symbol);
        
        if (stock == null) {
            System.out.println("Invalid stock symbol. Please try again");
        } else {
            System.out.println("Enter quantity:");
            int quantity = Integer.parseInt(input.nextLine().trim());

            if (quantity <= 0) {
                System.out.println("Cannot sell negative quantity\n");
            } else {
                StockPosition updatedPosition = account.getPortfolio().getStockPosition(symbol);
                if (quantity > updatedPosition.getQuantity()) {
                    System.out.println("Cannot sell more than " + updatedPosition.getQuantity() + " shares\n");
                } else {
                    account.sellStock(symbol, quantity);
                    System.out.println("Sold " + quantity + " shares of " + symbol + "\n");
                    // Retrieve the updated stock position from portfolio
                    showStockPosition(updatedPosition);
                } 
            }
        }
        showCashBalance();
    }

    /**
     * SPECIFIES: print stock position information
     */
    private void showStockPosition(StockPosition stockPosition) {
        if (stockPosition != null) {
            System.out.println("Updated stock position for " + stockPosition.getStock().getSymbol() + ": ");
            System.out.println("Quantity: " + stockPosition.getQuantity());
            System.out.println("Average Cost: $" + stockPosition.getAverageCost());
            System.out.println("Total Cost: $" + stockPosition.getTotalCost());
        } else {
            System.out.println("No update to stock position due to invalid transaction\n");
        }
    }

    /** 
     * SPECIFIES: print cash balance
     */
    private void showCashBalance() {
        System.out.println("Remaining cash balance: $" + account.getCashBalance());
    }
    
    /**
     * SPECIFIES: print portfolio with all stock position information
     */
    private void showPortfolio() {
        if (account.getPortfolio().getTotalStockPositions() == 0) {
            System.out.println("No owned stock positions");
        } else {
            System.out.println("Owned stock positions:");
            for (StockPosition position : account.getPortfolio().getAllStockPositions().values()) {
                String symbol = position.getStock().getSymbol();
                int quantity = position.getQuantity();
                BigDecimal totalCost = position.getTotalCost();
                BigDecimal averageCost = position.getAverageCost();

                System.out.println(symbol + " - " + quantity + " shares");
                System.out.println("  Total Value: $" + totalCost);
                System.out.println("  Average Cost: $" + averageCost);
            }
        }
    }
}
