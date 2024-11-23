package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import model.Account;
import model.Stock;
import model.StockPosition;
import persistence.JsonReader;
import persistence.JsonWriter;

/** 
 * GUI for Stock App
*/
public class StockAppGUI {
    private static final String JSON_STORE = "./data/account.json";
    private final JFrame frame;
    private final JPanel sidebarPanel;
    private final JPanel mainPanel;
    private final JPanel stockPanel;
    private JTable stockTable;
    private JTable portfolioTable;
    private DefaultTableModel portfolioModel;
    private DefaultTableModel stockModel;
    private JTextField balanceField;

    private Account account;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private ChartPanel chartPanel;

    // EFFECTS: Initialize account and create GUI application
    public StockAppGUI() {
        account = new Account("Henry", 10000);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        frame = new JFrame("Stock Picker");

        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        sidebarPanel = initializeSidebar();
        mainPanel = new JPanel(new BorderLayout());
        stockPanel = new JPanel(new BorderLayout());

        initializeStockTable();
        setupStockPanel();
        initializePortfolioTable();

        frame.add(sidebarPanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        
        // Default view
        showAccountPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // EFFECTS: Initialize stock table with column names, row height and buy/sell buttons under Actions column
    private void initializeStockTable() {
        String[] stockColumns = {"Symbol", "Price", "Actions"};
        Object[][] stockData = getAllStocksData();
        stockModel = new DefaultTableModel(stockData, stockColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        stockTable = new JTable(stockModel);
        stockTable.setRowHeight(30);
        stockTable.getColumn("Actions").setCellRenderer(new BuySellButtonRenderer());
        stockTable.getColumn("Actions").setCellEditor(
            new BuySellButtonEditor(new JCheckBox(), stockTable, account, this));
    }

    // EFFECTS: Initialize portfolio table with column names, row height and buy/sell buttons under Actions column
    private void initializePortfolioTable() {
        String[] portfolioColumns = {"Symbol", "Price", "Quantity", "Total Value", "Actions"};
        Object[][] portfolioData = getPortfolioStockData(account);
        portfolioModel = new DefaultTableModel(portfolioData, portfolioColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        portfolioTable = new JTable(portfolioModel);
        portfolioTable.setRowHeight(30);
        portfolioTable.getColumn("Actions").setCellRenderer(new BuySellButtonRenderer());
        portfolioTable.getColumn("Actions").setCellEditor(
            new BuySellButtonEditor(new JCheckBox(), portfolioTable, account, this));
    }

    // EFFECTS: Initialize sidebar with buttons
    private JPanel initializeSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(3, 1));
        sidebar.setPreferredSize(new Dimension(100, 0));

        JButton accountButton = new JButton("Account");
        JButton portfolioButton = new JButton("Portfolio");
        JButton stocksButton = new JButton("Stocks");

        accountButton.addActionListener(e -> showAccountPanel());
        portfolioButton.addActionListener(e -> showPortfolioPanel());
        stocksButton.addActionListener(e -> showStocksPanel());

        sidebar.add(accountButton);
        sidebar.add(portfolioButton);
        sidebar.add(stocksButton);

        return sidebar;
    }

    // EFFECTS: Display account panel
    private void showAccountPanel() {
        mainPanel.removeAll();
        // Create panel for form fields
        JPanel formPanel = createFormPanel();

        // Create chart panel
        chartPanel = createPieChart();
        chartPanel.setPreferredSize(new Dimension(300, 300));

        // Create a panel for the form and chart
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(chartPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();

        // Add panels to main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        refreshMainPanel();
    }

    // EFFECTS: Display Account panel
    // private void showAccountPanel() {
    //     mainPanel.removeAll();

    //     JPanel centerPanel = createCenterPanel();
    //     JPanel buttonPanel = createButtonPanel();

    //     mainPanel.add(centerPanel, BorderLayout.CENTER);
    //     mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    //     refreshMainPanel();
    // }

    // EFFECTS: Creates the center panel with the form and chart
    // private JPanel createCenterPanel() {
    //     JPanel formPanel = createFormPanel();
    //     JPanel chartPanel = createChartPanel();

    //     JPanel centerPanel = new JPanel(new BorderLayout());
    //     centerPanel.add(formPanel, BorderLayout.NORTH);
    //     centerPanel.add(chartPanel, BorderLayout.CENTER);
        
    //     return centerPanel;
    // }

    // EFFECTS: Creates the form panel for account name and balance
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        // Account Name field
        formPanel.add(new JLabel("Account Name: "));
        JTextField nameField = new JTextField(20);
        nameField.setEditable(false);
        nameField.setText(account.getAccountName());
        formPanel.add(nameField);

        // Balance field
        formPanel.add(new JLabel("Cash Balance: "));
        balanceField = new JTextField(20);
        balanceField.setEditable(false);
        balanceField.setText(account.getCashBalance().toString());
        formPanel.add(balanceField);

        return formPanel;
    }

    // EFFECTS: Creates the chart panel for displaying portfolio distribution
    // private JPanel createChartPanel() {
    //     JPanel chartPanel = createPieChart();
    //     chartPanel.setPreferredSize(new Dimension(300, 300));
    //     return chartPanel;
    // }

    // EFFECTS: Creates the button panel with deposit, withdraw, load, and save buttons
    private JPanel createButtonPanel() {
        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(e -> handleDeposit());

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(e -> handleWithdraw());

        JButton loadButton = new JButton("Load Data");
        loadButton.addActionListener(e -> handleLoadAccount());

        JButton saveButton = new JButton("Save Data");
        saveButton.addActionListener(e -> handleSaveAccount());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.add(depositButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(withdrawButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(loadButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    // EFFECTS: Creates and initializes the pie chart
    private ChartPanel createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        updateDataset(dataset);
        
        JFreeChart chart = ChartFactory.createPieChart(
                "Investment Portfolio", // chart title
                dataset,                       // data
                true,                         // include legend
                true,                         // tooltips
                false                         // URLs
        );
        PiePlot plot = (PiePlot) chart.getPlot();
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0} : {2}", new DecimalFormat("0"), new DecimalFormat("0%"));  
        plot.setLabelGenerator(labelGenerator);

        // Add spacing between sections
        plot.setSectionOutlinesVisible(true);
        
        // Customize the chart appearance
        chart.setBackgroundPaint(mainPanel.getBackground());
        
        return new ChartPanel(chart);
    }

    // EFFECTS: Updates the pie chart with current account data
    private void updatePieChart() {
        if (chartPanel != null) {
            JFreeChart chart = chartPanel.getChart();
            PiePlot plot = (PiePlot) chart.getPlot();
            DefaultPieDataset dataset = (DefaultPieDataset) plot.getDataset();
            updateDataset(dataset);
        }
    }

    // EFFECTS: Updates the dataset with current account information
    private void updateDataset(DefaultPieDataset dataset) {
        dataset.clear();
        
        // Add data to the dataset
        double cashBalance = account.getCashBalance().doubleValue();
        dataset.setValue("Cash", cashBalance);
        Map<String, StockPosition> positions = account.getPortfolio().getAllStockPositions();
        for (String symbol : positions.keySet()) {
            dataset.setValue(symbol, positions.get(symbol).getTotalCost());
        }
    }

    // EFFECTS: Handles the deposit transaction
    private void handleDeposit() {
        String input = showTransactionDialog("Deposit", "Enter amount to deposit:");
        if (input != null) {
            try {
                BigDecimal amount = new BigDecimal(input);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Please enter a positive amount.");
                    return;
                }
                
                account.deposit(amount.doubleValue());
                updateBalanceDisplay();
                updatePieChart();
                showSuccess("Successfully deposited " + formatCurrency(amount));
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            } catch (Exception e) {
                showError("Error processing deposit: " + e.getMessage());
            }
        }
    }

    // EFFECTS: Handles the withdraw transaction
    private void handleWithdraw() {
        String input = showTransactionDialog("Withdraw", "Enter amount to withdraw:");
        if (input != null) {
            try {
                BigDecimal amount = new BigDecimal(input);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Please enter a positive amount.");
                    return;
                }
                
                if (amount.compareTo(account.getCashBalance()) > 0) {
                    showError("Insufficient funds.");
                    return;
                }
                
                account.withdraw(amount.doubleValue());
                updateBalanceDisplay();
                updatePieChart();
                showSuccess("Successfully withdrew " + formatCurrency(amount));
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            } catch (Exception e) {
                showError("Error processing withdrawal: " + e.getMessage());
            }
        }
    }

    // EFFECTS: Shows a dialog for entering transaction amount
    private String showTransactionDialog(String title, String message) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(message));
        JTextField textField = new JTextField(10);
        panel.add(textField);

        int result = JOptionPane.showConfirmDialog(mainPanel, panel, 
                title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
        if (result == JOptionPane.OK_OPTION) {
            return textField.getText().trim();
        }
        return null;
    }

    // EFFECTS: Shows an error message
    private void showError(String message) {
        JOptionPane.showMessageDialog(mainPanel, message, "Error", 
                JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: Shows a success message
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(mainPanel, message, "Success", 
                JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS: Updates the balance display
    private void updateBalanceDisplay() {
        balanceField.setText(formatCurrency(account.getCashBalance()));
    }

    // EFFECTS: Formats currency values
    private String formatCurrency(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(amount);
    }

    // EFFECTS: Save account and show dialogs on success/failure
    private void handleSaveAccount() {
        try {
            saveAccount();  // Call your existing save method
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Saved account for " + account.getAccountName() + " to " + JSON_STORE,
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Unable to write to file: " + JSON_STORE,
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: Load account and show dialogs on success/failure
    private void handleLoadAccount() {
        try {
            loadAccount();  // Call your existing save method
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Loaded account for " + account.getAccountName() + " from " + JSON_STORE,
                    "Load Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Unable to load from file: " + JSON_STORE,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: Save account data to json
    private void saveAccount() {
        try {
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // EFFECTS: Load account from json
    private void loadAccount() {
        try {
            Account loadedAccount = jsonReader.read();
            this.account = loadedAccount;
            update();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // EFFECTS: Display portfolio panel with a stock table
    private void showPortfolioPanel() {
        mainPanel.removeAll();
        mainPanel.add(new JLabel("Portfolio"), BorderLayout.NORTH);
        JScrollPane tableScrollPane = new JScrollPane(portfolioTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        refreshMainPanel();
    }

    // EFFECTS: Display stock panel
    private void showStocksPanel() {
        mainPanel.removeAll();
        mainPanel.add(new JLabel("S&P 500 Stocks"), BorderLayout.NORTH);
        mainPanel.add(stockPanel, BorderLayout.CENTER);
        refreshMainPanel();
    }

    // EFFECTS: Set up stock panel
    private void setupStockPanel() {
        stockPanel.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(stockTable);
        stockPanel.add(scrollPane, BorderLayout.CENTER);
    }
    

    // EFFECTS: Retrieve all stock data from StockRepository
    private Object[][] getAllStocksData() {
        Map<String, Stock> stocks = StockRepository.getAllStocks();
        // Create columns for symbol and price
        Object[][] data = new Object[stocks.size()][3];
        
        // Get symbols as list and sort
        List<String> symbols = new ArrayList<>(stocks.keySet());
        symbols.sort(String::compareTo); 

        int row = 0;
        for (String symbol : symbols) {
            Stock stock = stocks.get(symbol);
            data[row][0] = stock.getSymbol();
            data[row][1] = stock.getPrice();
            data[row][2] = null;
            row++; 
        }
        return data;
    }

    // EFFECTS: Retrieve all portfolio stock positions
    private Object[][] getPortfolioStockData(Account account) {
        Map<String, StockPosition> positions = account.getPortfolio().getAllStockPositions();
        // Only create rows for positions with quantity > 0
        List<StockPosition> activePositions = positions.values().stream()
                .filter(position -> position.getQuantity() > 0)
                .collect(Collectors.toList());
        Object[][] data = new Object[activePositions.size()][5];

        int row = 0;
        for (StockPosition position : activePositions) {
            Stock stock = position.getStock();
            data[row][0] = stock.getSymbol();     
            data[row][1] = stock.getPrice();
            data[row][2] = position.getQuantity();
            data[row][3] = position.getTotalCost();
            data[row][4] = null;
            row++;
        }
        return data;
    }

    // EFFECTS: Refresh main panel after updating content
    private void refreshMainPanel() {
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // SPECIFIES Method to update both tables after transactions
    public void update() {
        SwingUtilities.invokeLater(() -> {
            // Update stock table
            stockModel.setRowCount(0);
            Object[][] newStockData = getAllStocksData();
            for (Object[] row : newStockData) {
                stockModel.addRow(row);
            }
            // Update portfolio table
            portfolioModel.setRowCount(0);
            Object[][] newPortfolioData = getPortfolioStockData(account);
            for (Object[] row : newPortfolioData) {
                portfolioModel.addRow(row);
            }
            // Update cash balance display if it exists
            if (balanceField != null) {
                double balance = account.getCashBalance().doubleValue(); // Assuming account has getCashBalance() method
                String formattedBalance = String.format("$%,.2f", balance);
                balanceField.setText(formattedBalance);
            }
            // Refresh the panels
            refreshMainPanel();
            // Update stock and portfolio table button editors
            updateButtonEditors();
            // Update pie chart if visible
            updatePieChart();
        });
    }
    
    // EFFECTS: Update stock and portfolio table button editors
    private void updateButtonEditors() {        
        portfolioTable.getColumn("Actions").setCellEditor(
            new BuySellButtonEditor(new JCheckBox(), portfolioTable, account, this));

        stockTable.getColumn("Actions").setCellEditor(
            new BuySellButtonEditor(new JCheckBox(), stockTable, account, this));
    }
}