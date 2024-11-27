package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.jfree.chart.ChartPanel;
import model.Account;
import model.Event;
import model.EventLog;
import ui.button.DepositButton;
import ui.button.LoadAccountButton;
import ui.button.SaveAccountButton;
import ui.button.WithdrawButton;

/** 
 * GUI for Stock App
*/
public class StockAppGUI {
    private static final String JSON_STORE = "./data/account.json";
    private Account account;
    private final JFrame frame;
    private final JPanel sidebarPanel;
    private final JPanel mainPanel;
    private JTable stockTable;
    private JTable portfolioTable;
    private JTextField balanceField;
    private ChartPanel chartPanel;

    private PortfolioTable portfolioTableComponent;
    private StockTable stockTableComponent;
    private PieChartPanel pieChartPanel;
    private CashBalanceField balanceFieldComponent;

    // EFFECTS: Initialize account and create GUI application
    public StockAppGUI() {
        account = new Account("Henry", 10000);
        initializeComponents();

        account.addObserver(portfolioTableComponent);
        account.addObserver(stockTableComponent);
        account.addObserver(pieChartPanel);
        account.addObserver(balanceFieldComponent);

        frame = new JFrame("Stock Picker");

        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        sidebarPanel = createSidebarPanel();
        mainPanel = new JPanel(new BorderLayout());

        frame.add(sidebarPanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        // Add WindowListener to handle close event
        frame.addWindowListener(windowClosingEvent());
        
        // Default view
        showAccountPanel();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // EFFECTS: Initialize components
    private void initializeComponents() {
        portfolioTableComponent = new PortfolioTable(account);
        portfolioTable = portfolioTableComponent.getTable();

        stockTableComponent = new StockTable(account);
        stockTable = stockTableComponent.getTable();

        pieChartPanel = new PieChartPanel(account);
        chartPanel = pieChartPanel.getPanel();

        balanceFieldComponent = new CashBalanceField(account);
        balanceField = balanceFieldComponent.getTextField();
    }

    // EFFECTS: Initialize sidebar with buttons
    private JPanel createSidebarPanel() {
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

    // EFFECTS: Display Account panel
    private void showAccountPanel() {
        mainPanel.removeAll();

        JPanel centerPanel = createCenterPanel();
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        refreshMainPanel();
    }

    // EFFECTS: Creates the center panel with the form and chart
    private JPanel createCenterPanel() {
        JPanel formPanel = createFormPanel();
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(chartPanel, BorderLayout.CENTER);
        
        return centerPanel;
    }

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
        formPanel.add(balanceField);

        return formPanel;
    }

    // EFFECTS: Creates the button panel with deposit, withdraw, load, and save buttons
    private JPanel createButtonPanel() {
        DepositButton depositButtonComponent = new DepositButton(account);
        JButton depositButton = depositButtonComponent.getButton();

        WithdrawButton withdrawButtonComponent = new WithdrawButton(account);
        JButton withdrawButton = withdrawButtonComponent.getButton();

        LoadAccountButton loadButtonComponent = new LoadAccountButton(account, "Load Data", JSON_STORE);
        JButton loadButton = loadButtonComponent.getButton();

        SaveAccountButton saveButtonComponent = new SaveAccountButton(account, "Save Data", JSON_STORE);
        JButton saveButton = saveButtonComponent.getButton();

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

    // EFFECTS: Display portfolio panel with a stock table
    private void showPortfolioPanel() {
        mainPanel.removeAll();
        mainPanel.add(new JLabel("Portfolio"), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(portfolioTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        refreshMainPanel();
    }

    // EFFECTS: Display stock panel
    private void showStocksPanel() {
        mainPanel.removeAll();
        mainPanel.add(new JLabel("S&P 500 Stocks"), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(stockTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        refreshMainPanel();
    }   

    // EFFECTS: Refresh main panel after updating content
    private void refreshMainPanel() {
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // EFFECTS: Create a window adapter to receive window closing event
    private WindowAdapter windowClosingEvent() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event);
                }
            }
        };
    }
}