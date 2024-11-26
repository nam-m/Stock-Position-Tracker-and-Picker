package observer;

import model.AccountEvent;

public interface Observer {
    void update(AccountEvent event);
}
