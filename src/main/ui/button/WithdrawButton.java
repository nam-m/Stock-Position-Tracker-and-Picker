package ui.button;

import java.math.BigDecimal;

import model.Account;

public class WithdrawButton extends CashBalanceButton {

    public WithdrawButton(Account account) {
        super("Withdraw", null);
        getButton().addActionListener(e -> handleWithdraw(account));
    }

    // EFFECTS: Handles the withdraw transaction
    private void handleWithdraw(Account account) {
        String input = showTransactionDialog("Withdraw", "Enter amount to withdraw:");
        if (input != null) {
            try {
                BigDecimal amount = new BigDecimal(input);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Please enter a positive amount.");
                    return;
                }
                
                if (amount.compareTo(account.getCashBalance()) > 0) {
                    showError("Insufficient funds.");
                    return;
                }
                
                account.withdraw(amount.doubleValue());
                // updateBalanceDisplay();
                showSuccess("Successfully withdrew " + formatCurrency(amount));
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            } catch (Exception e) {
                showError("Error processing withdrawal: " + e.getMessage());
            }
        }
    }

}
