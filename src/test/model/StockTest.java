package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StockTest {
    private Stock stock;

    @BeforeEach
    void runBefore() {
        stock = new Stock("AAPL", 226.8);
    }

    @Test
    void testConstructor() {
        assertEquals("AAPL", stock.getSymbol());
        assertEquals(new BigDecimal("226.80"), stock.getPrice());
    }

    @Test
    void testToJson() {
        String expectedJson = "{\"symbol\":\"AAPL\",\"price\":\"226.80\"}";
        JSONObject jsonObject = stock.toJson();
        assertTrue(jsonObject.similar(expectedJson));
    }
}
