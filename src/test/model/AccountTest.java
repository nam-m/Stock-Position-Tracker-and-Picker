package model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ui.StockRepository;

public class AccountTest {
    private Account account;

    @BeforeEach
    void runBefore() {
        StockRepository.addStock(new Stock("AAPL", 220));
        StockRepository.addStock(new Stock("NVDA", 150));
        account = new Account("Henry", 10000);
    }

    @AfterEach
    void runAfter() {
        StockRepository.clear();  // Clear the stock repository after each test
    }

    @Test
    void testConstructor() {
        assertNotNull(account.getAccountId());
        assertEquals("Henry", account.getAccountName());
        assertEquals(new BigDecimal("10000.00"), account.getCashBalance());
    }

    @Test
    void testBuyStock() {
        account.buyStock("AAPL", 5);
        assertEquals(new BigDecimal("8900.00"), account.getCashBalance());
    }

    @Test
    void testBuyMultipleStocks() {
        account.buyStock("AAPL", 5);
        account.buyStock("NVDA", 10);
        
        assertEquals(new BigDecimal("7400.00"), account.getCashBalance());
    }

    @Test
    void testBuyStockWithInsufficientBalance() {
        account.buyStock("AAPL", 100);
        // There is no change to cash balance due to insufficient balance
        assertEquals(new BigDecimal("10000.00"), account.getCashBalance());
    }

    @Test
    void testSellStock() {
        account.buyStock("AAPL", 10);
        account.sellStock("AAPL", 5);
        assertEquals(new BigDecimal("8900.00"), account.getCashBalance());
    }

    @Test
    void testSellMultipleStocks() {
        account.buyStock("AAPL", 7);
        account.buyStock("NVDA", 8);

        account.sellStock("AAPL", 5);
        account.sellStock("NVDA", 4);
        
        assertEquals(new BigDecimal("8960.00"), account.getCashBalance());
    }

    @Test
    void testSellMoreThanOwnedStock() {
        account.buyStock("AAPL", 6);
        account.sellStock("AAPL", 10);

        assertEquals(new BigDecimal("8680.00"), account.getCashBalance());
    }

    @Test
    void testSellNegativeStock() {
        account.buyStock("AAPL", 4);
        account.sellStock("AAPL", -5);

        assertEquals(4, account.getPortfolio().getStockPosition("AAPL").getQuantity());
        assertEquals(new BigDecimal("9120.00"), account.getCashBalance());
    }

    @Test
    void testDeposit() {
        account.deposit(5000);
        assertEquals(new BigDecimal("15000.00"), account.getCashBalance());
    }

    @Test
    void testWithdraw() {
        account.withdraw(5000);
        assertEquals(new BigDecimal("5000.00"), account.getCashBalance());
    }
}
