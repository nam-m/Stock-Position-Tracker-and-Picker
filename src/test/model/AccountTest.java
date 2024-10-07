package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {
    private Account account;
    private Stock stock1;
    private Stock stock2;

    @BeforeEach
    void runBefore() {
        account = new Account(10000);
        stock1 = new Stock("NVDA", 120);
        stock2 = new Stock("AAPL", 225);
    }

    @Test
    void testConstructor() {
        assertEquals(10000, account.getCashBalance());
    }

    @Test
    void testBuyStock() {
        account.buyStock(stock1, 5);
        assertEquals(9400, account.getCashBalance());
    }

    @Test
    void testBuyMultipleStocks() {
        account.buyStock(stock1, 5);
        account.buyStock(stock2, 5);
        
        assertEquals(8275, account.getCashBalance());
    }

    @Test
    void testBuyStockWithInsufficientBalance() {
        account.buyStock(stock1, 100);
        // There is no change to cash balance due to insufficient balance
        assertEquals(10000, account.getCashBalance());
    }

    @Test
    void testSellStock() {
        account.sellStock(stock1, 5);
        assertEquals(10600, account.getCashBalance());
    }

    @Test
    void testSellMultipleStocks() {
        account.sellStock(stock1, 5);
        account.sellStock(stock2, 5);
        
        assertEquals(11725, account.getCashBalance());
    }
}
