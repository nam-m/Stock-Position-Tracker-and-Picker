package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import model.Account;
import model.Stock;
import model.StockPosition;

public class StockAppGUI {
    private final JFrame frame;
    private final JPanel sidebarPanel;
    private final JPanel mainPanel;
    private final JPanel stockPanel;
    private JTable stockTable;
    private JTable portfolioTable;
    private DefaultTableModel portfolioModel;
    private DefaultTableModel stockModel;

    private Account account;

    public StockAppGUI() {
        account = new Account("Henry", 10000);
        frame = new JFrame("Stock Picker");

        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        initializeStockTable();
        initializePortfolioTable();

        sidebarPanel = initSidebar();
        mainPanel = new JPanel(new BorderLayout());
        stockPanel = new JPanel(new BorderLayout());
        
        setupStockPanel();

        frame.add(sidebarPanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        
        // Default view
        showStocksPanel(); 

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
        stockTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        stockTable.getColumn("Actions").setCellEditor(
            new ButtonEditor(new JCheckBox(), stockTable, account, this));
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
        portfolioTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        portfolioTable.getColumn("Actions").setCellEditor(
            new ButtonEditor(new JCheckBox(), portfolioTable, account, this));
    }

    // EFFECTS: Initialize sidebar with buttons
    private JPanel initSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(3, 1));
        sidebar.setPreferredSize(new Dimension(150, 0));

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

     // EFFECTS: Display Account panel
    private void showAccountPanel() {
        mainPanel.removeAll();
        mainPanel.add(new JLabel("Account Information"), BorderLayout.NORTH);

        // Account data display (for illustration)
        JTextArea accountDetails = new JTextArea("Name: John Doe\nBalance: $10,000.00");
        accountDetails.setEditable(false);
        mainPanel.add(accountDetails, BorderLayout.CENTER);

        refreshMainPanel();
    }

    // EFFECTS: Display Portfolio panel with a stock table
    private void showPortfolioPanel() {
        mainPanel.removeAll();
        mainPanel.add(new JLabel("Portfolio"), BorderLayout.NORTH);
        // JTable portfolioTable = createPortfolioTable();
        JScrollPane tableScrollPane = new JScrollPane(portfolioTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        refreshMainPanel();
    }

    // EFFECTS: Display Stocks panel
    private void showStocksPanel() {
        mainPanel.removeAll();
        mainPanel.add(new JLabel("S&P 500 Stocks"), BorderLayout.NORTH);
        mainPanel.add(stockPanel, BorderLayout.CENTER);
        refreshMainPanel();
    }

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
        int totalStockPositions = account.getPortfolio().getTotalStockPositions();
        Object[][] data = new Object[totalStockPositions][5];

        int row = 0;
        for (StockPosition position : account.getPortfolio().getAllStockPositions().values()) {
            Stock stock = position.getStock();
            data[row][0] = stock.getSymbol();               // Symbol
            data[row][1] = stock.getPrice();                // Current Price
            data[row][2] = position.getQuantity();          // Quantity Owned
            data[row][3] = position.getTotalCost();         // Total Cost
            data[row][4] = null;                            // Placeholder for Actions column
            row++;
        }
        return data;
    }

    // EFFECTS: Refresh main panel after updating content
    private void refreshMainPanel() {
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Method to update both tables after transactions
    public void updateTables() {
        SwingUtilities.invokeLater(() -> {
            // Update portfolio table
            portfolioModel.setRowCount(0);
            Object[][] newPortfolioData = getPortfolioStockData(account);
            for (Object[] row : newPortfolioData) {
                portfolioModel.addRow(row);
            }

            // Update cash balance display if it exists
            // updateCashBalance();

            // Refresh the panels
            refreshMainPanel();
        });
    }
}