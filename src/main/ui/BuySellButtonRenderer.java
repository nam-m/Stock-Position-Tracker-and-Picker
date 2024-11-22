package ui;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Represents buy and sell button render for JTable cell
 */
public class BuySellButtonRenderer extends JPanel implements TableCellRenderer {
    private JButton buyButton;
    private JButton sellButton;

    // EFFECTS: Create render with layout containing buy and sell buttons
    public BuySellButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0)); // Layout for buttons
        buyButton = new JButton("Buy");
        sellButton = new JButton("Sell");
        add(buyButton);
        add(sellButton);
    }

    // EFFECTS: Set background when button renderer cell is selected
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        return this;
    }
}
