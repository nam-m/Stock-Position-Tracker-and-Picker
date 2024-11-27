package ui.button;

import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class CashBalanceButton {
    private JButton button;

    public CashBalanceButton(String label, ActionListener action) {
        button = new JButton(label);
        button.addActionListener(action);
    }

    public JButton getButton() {
        return button;
    }

    // EFFECTS: Shows a dialog for entering transaction amount
    protected String showTransactionDialog(String title, String message) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(message));
        JTextField textField = new JTextField(10);
        panel.add(textField);

        int result = JOptionPane.showConfirmDialog(null, panel, 
                title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
        if (result == JOptionPane.OK_OPTION) {
            return textField.getText().trim();
        }
        return null;
    }

    // EFFECTS: Shows an error message
    protected void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", 
                JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: Shows a success message
    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", 
                JOptionPane.INFORMATION_MESSAGE);
    }

    // // EFFECTS: Updates the balance display
    // private void updateBalanceDisplay() {
    //     balanceField.setText(formatCurrency(account.getCashBalance()));
    // }

    // EFFECTS: Formats currency values
    protected String formatCurrency(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(amount);
    }
}
