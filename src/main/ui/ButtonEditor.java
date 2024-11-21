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
    private JPanel panel = new JPanel();
    private JButton buyButton;
    private JButton sellButton;

    private JTable table;
    private Account account;

    public ButtonEditor(JCheckBox checkBox, JTable table, Account account) {
        this.table = table;
        this.account = account;

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        buyButton = new JButton("Buy");
        sellButton = new JButton("Sell");

        // Add action listeners for buttons
        buyButton.addActionListener(e -> handleBuy());
        sellButton.addActionListener(e -> handleSell());
        
        panel.add(buyButton);
        panel.add(sellButton);
    }

    /**
     * MODIFIES: this
     * EFFECTS: buy stock if totalCost > account.getCashBalance
     */
    private void handleBuy() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String symbol = (String) table.getValueAt(selectedRow, 0);
            // Call buyStock method with the symbol (and quantity if applicable)
            int quantity = 5;
            Stock stock = StockRepository.getStockBySymbol(symbol);
            BigDecimal totalCost = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
            if (totalCost.compareTo(account.getCashBalance()) > 0) {
                JOptionPane.showMessageDialog(null, "Cannot buy stock with total value of $" + totalCost + "\n");
            } else {
                account.buyStock(symbol, quantity);
                JOptionPane.showMessageDialog(null, "Bought " + quantity + " shares of " + symbol + "\n"
                        + "Cash balance: " + account.getCashBalance());
            }
            
        }
        fireEditingStopped(); // Stop editing after the action
    }

    /**
     * MODIFIES: this
     * EFFECTS: sell stock if position with selected stock is found
     */
    private void handleSell() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String symbol = (String) table.getValueAt(selectedRow, 0);
            int quantity = 2;
            StockPosition position = account.getPortfolio().getStockPosition(symbol);
            if (position == null) {
                JOptionPane.showMessageDialog(null, "Not found stock position for " + symbol);
            } else {
                // Call sellStock method with the symbol (and quantity if applicable)
                account.sellStock(symbol, quantity);
                JOptionPane.showMessageDialog(null, "Sold " + quantity + " shares of " + symbol + "\n"
                        + "Cash balance: " + account.getCashBalance());
            }
        }
        fireEditingStopped(); // Stop editing after the action
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null; // No actual value needs to be returned
    }
}
