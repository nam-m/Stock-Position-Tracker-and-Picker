package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.jfree.chart.ChartPanel;
import model.Account;
import model.EventType;
import observer.Observer;
import persistence.JsonReader;
import persistence.JsonWriter;
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
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private PortfolioTable portfolioTableComponent;
    private StockTable stockTableComponent;
    private PieChartPanel pieChartPanel;
    private CashBalanceField balanceFieldComponent;

    // EFFECTS: Initialize account and create GUI application
    public StockAppGUI() {
        // account = new Account("Henry", 10000);
        account = new Account("Henry", 10000);
        portfolioTableComponent = new PortfolioTable(account);
        portfolioTable = portfolioTableComponent.getTable();
        stockTableComponent = new StockTable(account);
        stockTable = stockTableComponent.getTable();
        pieChartPanel = new PieChartPanel(account);
        chartPanel = pieChartPanel.getPanel();
        balanceFieldComponent = new CashBalanceField(account);
        balanceField = balanceFieldComponent.getTextField();

        account.addObserver(portfolioTableComponent);
        account.addObserver(stockTableComponent);
        account.addObserver(pieChartPanel);
        account.addObserver(balanceFieldComponent);

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        frame = new JFrame("Stock Picker");

        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        sidebarPanel = initializeSidebar();
        mainPanel = new JPanel(new BorderLayout());

        frame.add(sidebarPanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        
        // Default view
        showAccountPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
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
}