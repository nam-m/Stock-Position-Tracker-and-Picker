package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(0, portfolio.getTotalValue());
    }

    @Test
    void testGetTotalValue() {
        portfolio.buyStock(stock1, 4, 120);
        assertEquals(480, portfolio.getTotalValue());
    }

    @Test
    void testBuyStock() {
        portfolio.buyStock(stock1, 4, 120);
        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(120, portfolio.getStockPosition("NVDA").getAverageCost());
    }

    @Test
    void testBuyDifferentStocks() {
        portfolio.buyStock(stock1, 4, 120);
        portfolio.buyStock(stock2, 3, 220);

        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(120, portfolio.getStockPosition("NVDA").getAverageCost());
        
        assertEquals(3, portfolio.getStockPosition("AAPL").getQuantity());
        assertEquals(220, portfolio.getStockPosition("AAPL").getAverageCost());
    }

    @Test
    void testSellStock() {
        portfolio.buyStock(stock1, 4, 120);
        portfolio.sellStock(stock1, 3);

        assertEquals(1, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(120, portfolio.getStockPosition("NVDA").getAverageCost());
    }

    @Test
    void testSellDifferentStocks() {
        portfolio.buyStock(stock1, 4, 120);
        portfolio.buyStock(stock2, 3, 220);
        portfolio.sellStock(stock1, 2);
        portfolio.sellStock(stock2, 2);

        assertEquals(2, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(120, portfolio.getStockPosition("NVDA").getAverageCost());

        assertEquals(1, portfolio.getStockPosition("AAPL").getQuantity());
        assertEquals(220, portfolio.getStockPosition("AAPL").getAverageCost());
    }

    @Test
    void testSellNonexistentStock() {
        portfolio.buyStock(stock2, 1, 220);
        portfolio.sellStock(stock1, 1);

        assertEquals(1, portfolio.getTotalStockPositions());
        assertEquals(220, portfolio.getTotalValue());
        assertEquals(1, portfolio.getStockPosition("AAPL").getQuantity());
        assertEquals(220, portfolio.getStockPosition("AAPL").getAverageCost());
    }
}
