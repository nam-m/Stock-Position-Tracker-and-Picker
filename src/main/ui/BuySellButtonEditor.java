package ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.math.BigDecimal;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import model.Account;
import model.Stock;
import model.StockPosition;
import utils.MessageHandler;

/** 
 * Represent buy and sell buttons that handle buying/selling stocks
*/
public class BuySellButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final JPanel panel;
    private final JButton buyButton;
    private final JButton sellButton;
    private final JTable table;
    private final Account account;
    private final StockAppGUI gui;

    // EFFECTS: Initialize buy and sell buttons and add them to a panel
    public BuySellButtonEditor(JCheckBox checkBox, JTable table, Account account, StockAppGUI gui) {
        this.table = table;
        this.account = account;
        this.gui = gui;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        buyButton = new JButton("Buy");
        sellButton = new JButton("Sell");

        buyButton.addActionListener(e -> handleBuy());
        sellButton.addActionListener(e -> handleSell());
        
        panel.add(buyButton);
        panel.add(sellButton);
    }

    // EFFECTS: Buy stock from input quantity dialog
    private void handleBuy() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        String symbol = (String) table.getValueAt(selectedRow, 0);
        String quantityStr = JOptionPane.showInputDialog(
                                null, 
                                "Enter quantity to buy:", 
                                "Buy Stock", 
                                JOptionPane.PLAIN_MESSAGE);
        try {
            if (validateAndPurchaseStock(symbol, quantityStr)) {
                gui.update();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number");
        } finally {
            fireEditingStopped();
        }
    }

    // EFFECTS: Sell stock from input quantity dialog
    private void handleSell() {
        if (table.getSelectedRow() == -1) {
            return;
        }
        String symbol = (String) table.getValueAt(table.getSelectedRow(), 0);
        StockPosition position = account.getPortfolio().getStockPosition(symbol);
        if (!validateExistingPosition(symbol)) {
            return;
        }
        String quantityStr = JOptionPane.showInputDialog(
                                null, 
                                "Enter quantity to sell (max " + position.getQuantity() + "):",
                                "Sell Stock",
                                JOptionPane.PLAIN_MESSAGE);
        try {
            if (validateAndSellStock(symbol, quantityStr, position)) {
                gui.update();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number");
        } finally {
            fireEditingStopped();
        }
    }

    // REQUIRES: symbol represents a valid stock
    // EFFECTS: Buy stock and return true if quantity is validated, false otherwise
    private boolean validateAndPurchaseStock(String symbol, String quantityStr) {
        if (quantityStr == null) {
            return false;
        }
        try {    
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                MessageHandler.showErrorMessage("Please enter a positive quantity");
                return false;
            }
            Stock stock = StockRepository.getStockBySymbol(symbol);
            BigDecimal totalCost = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
            if (totalCost.compareTo(account.getCashBalance()) > 0) {
                MessageHandler.showErrorMessage("Insufficient funds. Total cost: $" + totalCost);
                return false;
            }
            account.buyStock(symbol, quantity);
            MessageHandler.showPurchaseSuccessMessage(symbol, quantity, account.getCashBalance());
            return true;
        } catch (Exception e) {
            MessageHandler.showErrorMessage("Error processing purchase: " + e.getMessage());
            return false;
        }
    }

    // REQUIRES: symbol represents a valid stock
    // EFFECTS: Sell stock and return true if quantity is validated, false otherwise
    private boolean validateAndSellStock(String symbol, String quantityStr, StockPosition position) {
        if (quantityStr == null) {
            return false;
        }
        try {    
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                MessageHandler.showErrorMessage("Please enter a positive quantity");
                return false;
            }
            if (quantity > position.getQuantity()) {
                MessageHandler.showErrorMessage("Cannot sell more than " + position.getQuantity() + " shares");
                return false;
            }
            account.sellStock(symbol, quantity);
            MessageHandler.showSaleSuccessMessage(symbol, quantity, account.getCashBalance());
            return true;
        } catch (Exception e) {
            MessageHandler.showErrorMessage("Error processing sale: " + e.getMessage());
            return false;
        }
    }

    // EFFECTS: Return true if stock position with given symbol exists, false otherwise
    private boolean validateExistingPosition(String symbol) {
        StockPosition position = account.getPortfolio().getStockPosition(symbol);
        if (position == null) {
            JOptionPane.showMessageDialog(null, "You don't own any shares of " + symbol);
            return false;
        }
        return true;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, 
            boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}