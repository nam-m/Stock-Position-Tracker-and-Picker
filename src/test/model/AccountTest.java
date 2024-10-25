package model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.json.JSONObject;
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
        assertEquals(5, account.getPortfolio().getStockPosition("AAPL").getQuantity());
    }

    @Test
    void testBuyMultipleStocks() {
        account.buyStock("AAPL", 5);
        account.buyStock("NVDA", 10);
        
        assertEquals(new BigDecimal("7400.00"), account.getCashBalance());
        assertEquals(5, account.getPortfolio().getStockPosition("AAPL").getQuantity());
        assertEquals(10, account.getPortfolio().getStockPosition("NVDA").getQuantity());
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
        assertEquals(5, account.getPortfolio().getStockPosition("AAPL").getQuantity());
    }

    @Test
    void testSellMultipleStocks() {
        account.buyStock("AAPL", 7);
        account.buyStock("NVDA", 8);

        account.sellStock("AAPL", 5);
        account.sellStock("NVDA", 4);
        
        assertEquals(new BigDecimal("8960.00"), account.getCashBalance());
        assertEquals(2, account.getPortfolio().getStockPosition("AAPL").getQuantity());
        assertEquals(4, account.getPortfolio().getStockPosition("NVDA").getQuantity());
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

    @Test
    void testToJson() {
        account.buyStock("AAPL", 5);
        account.buyStock("NVDA", 10);
        
        String expectedString = "{\"name\":\"Henry\",\"balance\":\"7400.00\",\"portfolio\":{\"positions\":" 
                            + "{\"AAPL\":{\"symbol\":\"AAPL\",\"quantity\":5,\"averagePrice\":\"220.00\"}," 
                            + "\"NVDA\":{\"symbol\":\"NVDA\",\"quantity\":10,\"averagePrice\":\"150.00\"}}}}";
        JSONObject expectedJson = new JSONObject(expectedString);
        JSONObject json = account.toJson();
        System.out.println(json.toString());
        assertTrue(json.similar(expectedJson));
    }
}
