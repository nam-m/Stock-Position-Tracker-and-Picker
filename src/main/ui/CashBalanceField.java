package ui;

import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.Account;
import model.AccountEvent;
import model.EventType;
import observer.Observer;

public class CashBalanceField implements Observer {
    private JTextField balanceField;

    public CashBalanceField(Account account) {
        balanceField = new JTextField(20);
        balanceField.setEditable(false);
        balanceField.setText(account.getCashBalance().toString());
    }

    @Override
    public void update(AccountEvent event) {
        if (event.getType() == EventType.ACCOUNT_LOADED
                || event.getType() == EventType.BALANCE_CHANGED
                || event.getType() == EventType.PORTFOLIO_CHANGED) {
            updateBalanceField(event.getAccount());
        }
    }

    // EFFECTS: Update the balance display
    private void updateBalanceField(Account account) {
        SwingUtilities.invokeLater(() -> {
            // double balance = account.getCashBalance().doubleValue(); // Assuming account has getCashBalance() method
            // String formattedBalance = String.format("$%,.2f", balance);
            balanceField.setText(formatCurrency(account.getCashBalance()));
        });
    }

    // EFFECTS: Format currency values
    private String formatCurrency(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(amount);
    }

    public JTextField getTextField() {
        return balanceField;
    }
}
