package ui.button;

import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import model.Account;
import model.EventType;
import observer.Observer;
import persistence.JsonReader;

// Represent a button to load account
public class LoadAccountButton {
    private JButton button;
    private JsonReader jsonReader;

    // REQUIRES: account, label, location not null and label, location length > 0
    // EFFECTS: Construct a button and add action listener to handle load account
    public LoadAccountButton(Account account, String label, String location) {
        jsonReader = new JsonReader(location);
        button = new JButton(label);
        button.addActionListener(e -> handleLoadAccount(account, location));
    }

    // EFFECTS: Get load button
    public JButton getButton() {
        return button;
    }

    // EFFECTS: Load account and show dialogs on success/failure
    private void handleLoadAccount(Account account, String location) {
        try {
            loadAccount(account);
            JOptionPane.showMessageDialog(
                    null,
                    "Loaded account for " + account.getAccountName() + " from " + location,
                    "Load Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Unable to load from file: " + location,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: Load account from json
    private void loadAccount(Account account) {
        try {
            List<Observer> observers = account.getObservers();
            Account loadedAccount = jsonReader.read();
            account = loadedAccount;
            for (Observer observer : observers) {
                account.addObserver(observer);
            }
            account.notifyObservers(account, EventType.ACCOUNT_LOADED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
