package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

import model.Account;
import model.Stock;
import model.StockPosition;
import persistence.JsonReader;
import persistence.JsonWriter;
import utils.InputHandler;

// Represents stock picker application
// Reference: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/main/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java

public class StockApp extends InputHandler {
    private static final String JSON_STORE = "./data/account.json";
    private Account account; // stock app account
    private Scanner input;   // console input
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    /**
     * EFFECTS: runs the stock picker application
    */
    public StockApp() {
        account = new Account("Henry", 10000);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
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
        System.out.println("Welcome to the Stock Tracker App!");
    }

    /**
     * EFFECTS: displays menu of options to user
    */
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tm -> manage account");
        System.out.println("\tb -> buy stock");
        System.out.println("\ts -> sell stock");
        System.out.println("\tv -> view portfolio");
        System.out.println("\tsave -> save account to json file");
        System.out.println("\tload -> load account from file");
        System.out.println("\tq -> quit");
    }

    /** 
     * MODIFIES: this
     * EFFECTS: processes user command
    */
    private void processCommand(String command) {
        switch (command) {
            case "m":
                accountMenu();
                break;
            case "b":
                doBuyStock();
                break;
            case "s":
                doSellStock();
                break;
            case "v":
                showPortfolio();
                break;
            case "save":
                saveAccount();
                break;
            case "load":
                loadAccount();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    /** 
     * EFFECTS: displays the account management menu
    */ 
    private void accountMenu() {
        boolean manageAccount = true;
        String accountCommand;

        while (manageAccount) {
            displayAccountMenu();
            accountCommand = input.nextLine().toLowerCase();

            if (accountCommand.equals("q")) {
                manageAccount = false;
            } else {
                processAccountCommand(accountCommand);
            }
        }
    }
    
    /**
     * MODIFIES: this
     * EFFECTS: processes user command for managing account
     */
    private void processAccountCommand(String command) {
        switch (command) {
            case "d":
                doDeposit();
                break;
            case "w":
                doWithdraw();
                break;
            case "b":
                showCashBalance();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    /**
     * EFFECTS: displays menu of account options to user
    */
    private void displayAccountMenu() {
        System.out.println("\nManage Account:");
        System.out.println("\td -> deposit");
        System.out.println("\tw -> withdraw");
        System.out.println("\tb -> view balance");
        System.out.println("\tq -> exit to main menu");
    }

    /**
     * MODIFIES: this
     * EFFECTS: buy stock
     */ 
    private void doBuyStock() {
        String symbol = getValidatedStockInput("Enter stock symbol:");

        int quantity = getValidatedIntegerInput("Enter quantity:");
        Stock stock = StockRepository.getStockBySymbol(symbol);
        BigDecimal totalCost = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
        if (totalCost.compareTo(account.getCashBalance()) > 0) {
            System.out.println("Cannot buy stock with total value of $" + totalCost + "\n");
        } else {
            account.buyStock(symbol, quantity);
            System.out.println("Bought " + quantity + " shares of " + symbol + "\n");
            // Retrieve the updated stock position from portfolio
            showStockPosition(account.getPortfolio().getStockPosition(symbol));
        }

        showCashBalance();
    }

    /**
     * MODIFIES: this
     * EFFECTS: sell stock
     */ 
    private void doSellStock() {
        String symbol = getValidatedStockInput("Enter stock symbol:");

        int quantity = getValidatedIntegerInput("Enter quantity:");
        StockPosition updatedPosition = account.getPortfolio().getStockPosition(symbol);
        if (updatedPosition == null) {
            System.out.println("Not found stock position for " + symbol);
        } else {
            if (quantity > updatedPosition.getQuantity()) {
                System.out.println("Cannot sell more than " + updatedPosition.getQuantity() + " shares\n");
            } else {
                account.sellStock(symbol, quantity);
                System.out.println("Sold " + quantity + " shares of " + symbol + "\n");
                // Retrieve the updated stock position from portfolio
                showStockPosition(updatedPosition);
            } 
        }

        showCashBalance();
    }

    /**
     * SPECIFIES: deposit into cash balance
     */
    private void doDeposit() {
        double depositValue = getValidatedDoubleInput("Enter amount to deposit:");
        account.deposit(depositValue);
        System.out.println("Deposited $" + depositValue + " to account.");
        showCashBalance();        
    }

    /**
     * SPECIFIES: withdraw from cash balance
     */
    private void doWithdraw() {
        double withdrawValue = getValidatedDoubleInput("Enter amount to withdraw:");
        if (withdrawValue > this.account.getCashBalance().doubleValue()) {
            System.out.println("Cannot withdraw more than cash balance");
        } else {
            account.withdraw(withdrawValue);
            System.out.println("Withdrew $" + withdrawValue + " from account.");
            showCashBalance();
        }
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
        System.out.println("Cash balance: $" + account.getCashBalance());
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

    // EFFECTS: saves the workroom to file
    private void saveAccount() {
        try {
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();
            System.out.println("Saved account for " + account.getAccountName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadAccount() {
        try {
            account = jsonReader.read();
            System.out.println("Loaded account for " + account.getAccountName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
