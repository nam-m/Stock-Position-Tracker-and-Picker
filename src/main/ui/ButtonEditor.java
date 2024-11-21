package ui;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import model.Account;
import model.Stock;

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

    private void handleBuy() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String stockSymbol = (String) table.getValueAt(selectedRow, 0);
            // Call buyStock method with the symbol (and quantity if applicable)
            account.buyStock(stockSymbol, 1);
            JOptionPane.showMessageDialog(null, "Buy action performed for " + stockSymbol);
        }
        fireEditingStopped(); // Stop editing after the action
    }

    private void handleSell() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String stockSymbol = (String) table.getValueAt(selectedRow, 0);
            // Call sellStock method with the symbol (and quantity if applicable)
            account.sellStock(stockSymbol, 1);
            JOptionPane.showMessageDialog(null, "Sell action performed for " + stockSymbol);
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
