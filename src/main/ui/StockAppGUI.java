package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class StockAppGUI {
    private final JFrame frame;
    private final JPanel sidebarPanel;
    private final JPanel mainPanel;
    private JTable stockTable;

    public StockAppGUI() {
        frame = new JFrame("Stock Picker");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        sidebarPanel = initSidebar();
        mainPanel = new JPanel(new BorderLayout());

        frame.add(sidebarPanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        
        // Default view
        showPortfolioPanel(); 

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // Initialize sidebar with buttons
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

     // Display Account panel
    private void showAccountPanel() {
        mainPanel.removeAll();
        mainPanel.add(new JLabel("Account Information"), BorderLayout.NORTH);

        // Account data display (for illustration)
        JTextArea accountDetails = new JTextArea("Name: John Doe\nBalance: $10,000.00");
        accountDetails.setEditable(false);
        mainPanel.add(accountDetails, BorderLayout.CENTER);

        refreshMainPanel();
    }

    // Display Portfolio panel with a stock table
    private void showPortfolioPanel() {
        mainPanel.removeAll();
        mainPanel.add(new JLabel("Portfolio"), BorderLayout.NORTH);

        // Initialize the stock table
        stockTable = createStockTable(getStockData());
        JScrollPane tableScrollPane = new JScrollPane(stockTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        refreshMainPanel();
    }

    // Display Stocks panel
    private void showStocksPanel() {
        mainPanel.removeAll();
        mainPanel.add(new JLabel("Stocks Overview"), BorderLayout.NORTH);

        // Example text content for stocks
        JTextArea stocksInfo = new JTextArea("Stock Information\nStock prices, trends, etc.");
        stocksInfo.setEditable(false);
        mainPanel.add(stocksInfo, BorderLayout.CENTER);

        refreshMainPanel();
    }

    // Refresh main panel after updating content
    private void refreshMainPanel() {
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Create stock table with columns: symbol, quantity, price, total value
    private JTable createStockTable(Object[][] data) {
        String[] columns = {"Symbol", "Quantity", "Price", "Total Value"};
        DefaultTableModel model = new DefaultTableModel(data, columns);
        return new JTable(model);
    }

    // Sample stock data (can replace with real data retrieval from Account/Portfolio classes)
    private Object[][] getStockData() {
        // Example data - replace this with real portfolio data
        List<Object[]> stockData = new ArrayList<>();
        stockData.add(new Object[]{"AAPL", 10, "150.00", "1500.00"});
        stockData.add(new Object[]{"GOOG", 5, "2000.00", "10000.00"});
        stockData.add(new Object[]{"AMZN", 3, "3500.00", "10500.00"});

        return stockData.toArray(new Object[0][0]);
    }

    public static void main(String[] args) {
        StockAppGUI gui = new StockAppGUI();
    }
}
