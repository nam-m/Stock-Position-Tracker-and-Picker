package ui.button;

import java.math.BigDecimal;

import model.Account;

public class DepositButton extends CashBalanceButton {

    public DepositButton(Account account) {
        super("Deposit", null);
        getButton().addActionListener(e -> handleWithdraw(account));
    }

    // EFFECTS: Handles the withdraw transaction
    private void handleWithdraw(Account account) {
        String input = showTransactionDialog("Deposit", "Enter amount to deposit:");
        if (input != null) {
            try {
                BigDecimal amount = new BigDecimal(input);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Please enter a positive amount.");
                    return;
                }
                
                account.deposit(amount.doubleValue());
                // updateBalanceDisplay();
                showSuccess("Successfully deposited " + formatCurrency(amount));
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            } catch (Exception e) {
                showError("Error processing deposit: " + e.getMessage());
            }
        }
    }
}
