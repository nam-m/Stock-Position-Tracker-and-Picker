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

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final JPanel panel;
    private final JButton buyButton;
    private final JButton sellButton;
    private final JTable table;
    private final Account account;
    private final StockAppGUI gui;

    public ButtonEditor(JCheckBox checkBox, JTable table, Account account, StockAppGUI gui) {
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

    // EFFECTS: buy stock from input quantity dialog if quantity > 0 and totalCost <= getCashBalance
    private void handleBuy() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String symbol = (String) table.getValueAt(selectedRow, 0);
            String quantityStr = JOptionPane.showInputDialog(
                                    null, 
                                    "Enter quantity to buy:", 
                                    "Buy Stock", 
                                    JOptionPane.PLAIN_MESSAGE);

            try {
                if (quantityStr != null) {
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a positive quantity");
                        return;
                    }

                    Stock stock = StockRepository.getStockBySymbol(symbol);
                    BigDecimal totalCost = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
                    
                    if (totalCost.compareTo(account.getCashBalance()) > 0) {
                        JOptionPane.showMessageDialog(null, "Insufficient funds. Total cost: $" + totalCost);
                    } else {
                        account.buyStock(symbol, quantity);
                        gui.update();
                        JOptionPane.showMessageDialog(null, 
                                String.format("Bought %d shares of %s\nCash balance: $%.2f", 
                                quantity, symbol, account.getCashBalance().doubleValue()));
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number");
            }
        }
        fireEditingStopped();
    }

    // EFFECTS: sell stock from input quantity dialog if stock position exists 0 < quantity <= position.getQuantity
    private void handleSell() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String symbol = (String) table.getValueAt(selectedRow, 0);
            StockPosition position = account.getPortfolio().getStockPosition(symbol);
            
            if (position == null) {
                JOptionPane.showMessageDialog(null, "You don't own any shares of " + symbol);
                return;
            }
            
            String quantityStr = JOptionPane.showInputDialog(
                                    null, 
                                    "Enter quantity to sell (max " + position.getQuantity() + "):",
                                    "Sell Stock",
                                    JOptionPane.PLAIN_MESSAGE);
            try {
                if (quantityStr != null) {
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a positive quantity");
                        return;
                    }
                    if (quantity > position.getQuantity()) {
                        JOptionPane.showMessageDialog(null, "You can't sell more shares than you own");
                        return;
                    }

                    account.sellStock(symbol, quantity);
                    gui.update();
                    double balance = account.getCashBalance().doubleValue();
                    String msg = String.format("Sold %d shares of %s\nCash balance: $%.2f", quantity, symbol, balance);
                    JOptionPane.showMessageDialog(null, msg);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number");
            }
        }
        fireEditingStopped();
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