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
import javax.swing.table.DefaultTableModel;

import model.Stock;

public class StockAppGUI {
    private final JFrame frame;
    private final JPanel sidebarPanel;
    private final JPanel mainPanel;
    private final JPanel stockPanel;
    private JTable stockTable;

    public StockAppGUI() {
        frame = new JFrame("Stock Picker");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        sidebarPanel = initSidebar();
        mainPanel = new JPanel(new BorderLayout());
        stockPanel = new JPanel();
        setupStockPanel();

        frame.add(sidebarPanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        
        // Default view
        showStocksPanel(); 

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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

        // Initialize the stock table
        // stockTable = createStockTable(getAllStocksData());
        JScrollPane tableScrollPane = new JScrollPane(stockTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        refreshMainPanel();
    }

    // EFFECTS: Display Stocks panel
    private void showStocksPanel() {
        mainPanel.removeAll();
        mainPanel.add(new JLabel("S&P 500 Stocks"), BorderLayout.NORTH);
        mainPanel.add(stockPanel, BorderLayout.CENTER);

        // Create stock table and add to panel
        // JTable stockTable = createStockTable();
        // stockPanel.add(new JScrollPane(stockTable), BorderLayout.CENTER);
        refreshMainPanel();
    }

    private void setupStockPanel() {
        stockPanel.setLayout(new BorderLayout());
    
        // Create and add the stock table
        JTable stockTable = createStockTable();
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


    // EFFECTS: Refresh main panel after updating content
    private void refreshMainPanel() {
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // EFFECTS: Create stock table with columns: symbol, quantity, price, total value
    private JTable createStockTable() {
        String[] columnNames = {"Symbol", "Price", "Actions"};
        Object[][] stockData = getAllStocksData();
        DefaultTableModel model = new DefaultTableModel(stockData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only make the "Actions" column editable
                return column == 2;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(30);
        // Add custom renderer and editor for the "Actions" column
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        // Editor requires a checkBox
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox())); 

        return table;
    }

    public static void main(String[] args) {
        StockAppGUI gui = new StockAppGUI();
    }
}
