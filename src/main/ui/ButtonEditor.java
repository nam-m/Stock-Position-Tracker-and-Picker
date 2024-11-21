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

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel = new JPanel();
    private JButton buyButton;
    private JButton sellButton;

    public ButtonEditor(JCheckBox checkBox) {
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
        // System.out.println("Buy button clicked");
        // stopCellEditing(); // Stop editing to finalize the action
        JOptionPane.showMessageDialog(null, "Buy action performed!");
        fireEditingStopped(); // Stop editing after the action
    }

    private void handleSell() {
        // System.out.println("Sell button clicked");
        // stopCellEditing();
        JOptionPane.showMessageDialog(null, "Sell action performed!");
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
