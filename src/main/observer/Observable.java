package observer;

import java.util.ArrayList;
import java.util.List;

import model.Account;
import model.AccountEvent;
import model.EventType;

public abstract class Observable {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Account account, EventType eventType) {
        AccountEvent event = new AccountEvent(account, eventType);
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    public List<Observer> getObservers() {
        return observers;
    }
}
