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
    private Stock nvdaStock;
    private Stock aaplStock;

    @BeforeEach
    void runBefore() {
        nvdaStock = new Stock("NVDA", 120);
        aaplStock = new Stock("AAPL", 225);
        portfolio = new Portfolio();
    }

    @Test
    void testConstructor() {
        assertEquals(new BigDecimal("0.00"), portfolio.getTotalValue());
        assertEquals(0, portfolio.getTotalStockPositions());
    }

    @Test
    void testGetTotalValue() {
        portfolio.buyStock(nvdaStock, 4);
        assertEquals(new BigDecimal("480.00"), portfolio.getTotalValue());
    }

    @Test
    void testBuyStockNewPosition() {
        portfolio.buyStock(nvdaStock, 4);
        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());
    }

    @Test
    void testBuyStockExistingPosition() {
        portfolio.buyStock(nvdaStock, 4);
        portfolio.buyStock(nvdaStock, 6);
        assertEquals(10, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("1200.00"), portfolio.getTotalValue());
    }

    @Test
    void testBuyDifferentStocks() {
        portfolio.buyStock(nvdaStock, 4);
        portfolio.buyStock(aaplStock, 3);

        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());
        
        assertEquals(3, portfolio.getStockPosition("AAPL").getQuantity());
        assertEquals(new BigDecimal("225.00"), portfolio.getStockPosition("AAPL").getAverageCost());

        assertEquals(new BigDecimal("1155.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellStock() {
        portfolio.buyStock(nvdaStock, 4);
        portfolio.sellStock(nvdaStock, 3);

        assertEquals(1, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());
        assertEquals(new BigDecimal("120.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellDifferentStocks() {
        portfolio.buyStock(nvdaStock, 4);
        portfolio.buyStock(aaplStock, 3);
        portfolio.sellStock(nvdaStock, 1);
        portfolio.sellStock(aaplStock, 2);

        assertEquals(3, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("120.00"), portfolio.getStockPosition("NVDA").getAverageCost());

        assertEquals(1, portfolio.getStockPosition("AAPL").getQuantity());
        assertEquals(new BigDecimal("225.00"), portfolio.getStockPosition("AAPL").getAverageCost());
        assertEquals(new BigDecimal("585.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellNonexistentStock() {
        portfolio.sellStock(nvdaStock, 1);

        assertEquals(0, portfolio.getTotalStockPositions());
        assertEquals(new BigDecimal("0.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellMoreThanOwnedStock() {
        portfolio.buyStock(nvdaStock, 4);
        portfolio.sellStock(nvdaStock, 5);

        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("480.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellNegativeStock() {
        portfolio.buyStock(nvdaStock, 4);
        portfolio.sellStock(nvdaStock, -5);

        assertEquals(4, portfolio.getStockPosition("NVDA").getQuantity());
        assertEquals(new BigDecimal("480.00"), portfolio.getTotalValue());
    }

    @Test
    void testSellAllStockPosition() {
        portfolio.buyStock(nvdaStock, 4);
        portfolio.sellStock(nvdaStock, 4);

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

        portfolio.buyStock(nvdaStock, 4);;
        portfolio.buyStock(aaplStock, 4);

        // Retrieve the stock positions and verify
        Map<String, StockPosition> stockPositions = portfolio.getAllStockPositions();

        assertEquals(2, stockPositions.size());
        assertTrue(stockPositions.containsKey(nvdaStock.getSymbol()));
        assertTrue(stockPositions.containsKey(aaplStock.getSymbol()));
    }

    @Test
    void testToJson() {
        portfolio.buyStock(aaplStock, 3);
        portfolio.buyStock(nvdaStock, 4);
        
        String expectedString = "{\"positions\":" 
                                    + "{\"AAPL\":{\"symbol\":\"AAPL\",\"quantity\":3," 
                                    + "\"averagePrice\":\"225.00\"}," 
                                    + "\"NVDA\":{\"symbol\":\"NVDA\",\"quantity\":4," 
                                    + "\"averagePrice\":\"120.00\"}}}";
        JSONObject expectedJson = new JSONObject(expectedString);
        JSONObject jsonObject = portfolio.toJson();
        System.out.println(jsonObject.toString());
        assertTrue(jsonObject.similar(expectedJson));
    }
}
