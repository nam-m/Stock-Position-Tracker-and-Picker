package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Map;

import org.json.JSONObject;
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
        assertEquals(0, portfolio.getTotalStockPositions());
    }

    @Test
    void testGetTotalValue() {
        portfolio.buyStock(stock1, 4);
        assertEquals(new BigDecimal("480.00"), portfolio.getTotalValue());
    }

    @Test
    void testBuyStockNewPosition() {
        portfolio.buyStock(stock1, 4);
        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());
    }

    @Test
    void testBuyStockExistingPosition() {
        portfolio.buyStock(stock1, 4);
        portfolio.buyStock(stock1, 6);
        assertEquals(10, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("1200.00"), portfolio.getTotalValue());
    }

    @Test
    void testBuyDifferentStocks() {
        portfolio.buyStock(stock1, 4);
        portfolio.buyStock(stock2, 3);

        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());
        
        assertEquals(3, portfolio.getStockPosition("AAPL").getQuantity());
        assertEquals(new BigDecimal("225.00"), portfolio.getStockPosition("AAPL").getAverageCost());

        assertEquals(new BigDecimal("1155.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellStock() {
        portfolio.buyStock(stock1, 4);
        portfolio.sellStock(stock1, 3);

        assertEquals(1, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());
        assertEquals(new BigDecimal("120.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellDifferentStocks() {
        portfolio.buyStock(stock1, 4);
        portfolio.buyStock(stock2, 3);
        portfolio.sellStock(stock1, 1);
        portfolio.sellStock(stock2, 2);

        assertEquals(3, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());

        assertEquals(1, portfolio.getStockPosition("AAPL").getQuantity());
        assertEquals(new BigDecimal("225.00"), portfolio.getStockPosition("AAPL").getAverageCost());
        assertEquals(new BigDecimal("585.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellNonexistentStock() {
        portfolio.sellStock(stock1, 1);

        assertEquals(0, portfolio.getTotalStockPositions());
        assertEquals(new BigDecimal("0.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellMoreThanOwnedStock() {
        portfolio.buyStock(stock1, 4);
        portfolio.sellStock(stock1, 5);

        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("480.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellNegativeStock() {
        portfolio.buyStock(stock1, 4);
        portfolio.sellStock(stock1, -5);

        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("480.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellAllStockPosition() {
        portfolio.buyStock(stock1, 4);
        portfolio.sellStock(stock1, 4);

        assertNull(portfolio.getStockPosition("NVDA"));
        assertEquals(0, portfolio.getTotalStockPositions());
        assertEquals(new BigDecimal("0.00"), portfolio.getTotalValue());
    }

    @Test
    void testGetAllStockPositionsEmpty() {
        // Test when there are no stock positions
        Map<String, StockPosition> stockPositions = portfolio.getAllStockPositions();
        assertTrue(stockPositions.isEmpty(), "Stock positions should be empty initially");
    }

    @Test
    void testGetAllStockPositionsWithStocks() {

        portfolio.buyStock(stock1, 4);;
        portfolio.buyStock(stock2, 4);

        // Retrieve the stock positions and verify
        Map<String, StockPosition> stockPositions = portfolio.getAllStockPositions();

        assertEquals(2, stockPositions.size());
        assertTrue(stockPositions.containsKey(stock1.getSymbol()));
        assertTrue(stockPositions.containsKey(stock2.getSymbol()));
    }

    @Test
    void testToJson() {
        portfolio.buyStock(stock1, 4);
        portfolio.buyStock(stock2, 3);
        
        String expectedJson = "\"NVDA\":{\"symbol\":\"NVDA\",\"quantity\":4,\"averagePrice\":\"120.00\"}," 
                            + "\"AAPL\":{\"symbol\":\"AAPL\",\"quantity\":3,\"averagePrice\":\"225.00\"},";
        JSONObject jsonObject = portfolio.toJson();
        assertTrue(jsonObject.similar(expectedJson));
    }
}
