package utils;

import java.math.BigDecimal;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

// Utility methods for different UI contexts
public class MessageHandler {
    // EFFECTS: show error message for gui/terminal context
    public static void showErrorMessage(String message) {
        if (SwingUtilities.isEventDispatchThread()) {
            // GUI context
            JOptionPane.showMessageDialog(null, message);
        } else {
            // Terminal context
            System.out.println(message);
        }
    }

    // EFFECTS: show success purchase message for gui/terminal context
    public static void showPurchaseSuccessMessage(String symbol, int quantity, BigDecimal balance) {
        if (SwingUtilities.isEventDispatchThread()) {
            // GUI context
            String msg = String.format("Bought %d shares of %s\nCash balance: $%.2f", 
                    quantity, symbol, balance.doubleValue());
            JOptionPane.showMessageDialog(null, msg);
        } else {
            // Terminal context
            System.out.println("Bought " + quantity + " shares of " + symbol);
        }
    }

    // EFFECTS: show success sale message for gui/terminal context
    public static void showSaleSuccessMessage(String symbol, int quantity, BigDecimal balance) {
        if (SwingUtilities.isEventDispatchThread()) {
            // GUI context
            String msg = String.format("Sold %d shares of %s\nCash balance: $%.2f", 
                    quantity, symbol, balance.doubleValue());
            JOptionPane.showMessageDialog(null, msg);
        } else {
            // Terminal context
            System.out.println("Sold " + quantity + " shares of " + symbol);
        }
    }
}
