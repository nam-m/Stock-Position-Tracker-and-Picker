package ui.button;

import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import model.Account;
import persistence.JsonWriter;

// Represent a button to load account
public class SaveAccountButton {
    private JButton button;
    private JsonWriter jsonWriter;

    // REQUIRES: account, label, location not null and label, location length > 0
    // EFFECTS: Construct a button and add action listener to handle save account
    public SaveAccountButton(Account account, String label, String location) {
        jsonWriter = new JsonWriter(location);
        button = new JButton(label);
        button.addActionListener(e -> handleSaveAccount(account, location));
    }

    // EFFECTS: Get save button
    public JButton getButton() {
        return button;
    }

    // EFFECTS: Save account and show dialogs on success/failure
    private void handleSaveAccount(Account account, String location) {
        try {
            saveAccount(account);  // Call your existing save method
            JOptionPane.showMessageDialog(
                    null,
                    "Saved account for " + account.getAccountName() + " to " + location,
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Unable to write to file: " + location,
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: Save account data to json
    private void saveAccount(Account account) {
        try {
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
