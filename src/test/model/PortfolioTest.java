package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PortfolioTest {
    private Portfolio portfolio;
    private Stock stock1;
    private Stock stock2;

    @BeforeEach
    void runBefore() {
        stock1 = new Stock("NVDA", 120);
        stock2 = new Stock("AAPL", 225);
        portfolio = new Portfolio();
    }

    @Test
    void testConstructor() {
        assertEquals(new BigDecimal("0.00"), portfolio.getTotalValue());
    }

    @Test
    void testGetTotalValue() {
        portfolio.buyStock(stock1, 4, 120);
        assertEquals(new BigDecimal("480.00"), portfolio.getTotalValue());
    }

    @Test
    void testBuyStockNewPosition() {
        portfolio.buyStock(stock1, 4, 120);
        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());
    }

    @Test
    void testBuyStockExistingPosition() {
        portfolio.buyStock(stock1, 4, 120);
        portfolio.buyStock(stock1, 6, 150);
        assertEquals(10, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("138.00"), portfolio.getStockPosition("NVDA").getAverageCost());
    }

    @Test
    void testBuyDifferentStocks() {
        portfolio.buyStock(stock1, 4, 120);
        portfolio.buyStock(stock2, 3, 220);

        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());
        
        assertEquals(3, portfolio.getStockPosition("AAPL").getQuantity());
        assertEquals(new BigDecimal("220.00"), portfolio.getStockPosition("AAPL").getAverageCost());
    }

    @Test
    void testSellStock() {
        portfolio.buyStock(stock1, 4, 120);
        portfolio.sellStock(stock1, 3);

        assertEquals(1, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());
    }

    @Test
    void testSellDifferentStocks() {
        portfolio.buyStock(stock1, 4, 120);
        portfolio.buyStock(stock2, 3, 220);
        portfolio.sellStock(stock1, 2);
        portfolio.sellStock(stock2, 2);

        assertEquals(2, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());

        assertEquals(1, portfolio.getStockPosition("AAPL").getQuantity());
        assertEquals(new BigDecimal("220.00"), portfolio.getStockPosition("AAPL").getAverageCost());
    }

    @Test
    void testSellNonexistentStock() {
        portfolio.sellStock(stock1, 1);

        assertEquals(0, portfolio.getTotalStockPositions());
        assertEquals(new BigDecimal("0.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellMoreThanOwnedStock() {
        portfolio.buyStock(stock1, 4, 120);
        portfolio.sellStock(stock1, 5);

        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());
    }

    @Test
    void testSellNegativeStock() {
        portfolio.buyStock(stock1, 4, 120);
        portfolio.sellStock(stock1, -5);

        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());
    }

    @Test
    void testSellAllStockPosition() {
        portfolio.buyStock(stock1, 4, 120);
        portfolio.sellStock(stock1, 4);

        assertNull(portfolio.getStockPosition("NVDA"));
        assertEquals(0, portfolio.getTotalStockPositions());
        assertEquals(new BigDecimal("0.00"), portfolio.getTotalValue());
    }
}
