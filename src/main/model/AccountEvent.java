package model;

public class AccountEvent {
    private final EventType type;
    private final Account account;

    public AccountEvent(Account account, EventType type) {
        this.type = type;
        this.account = account;
    }

    public EventType getType() {
        return type; 
    }

    public Account getAccount() {
        return account; 
    }
}
