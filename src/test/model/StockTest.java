package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

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
    void testUpdatePrice() {
        stock.setPrice(200.5);
        assertEquals(new BigDecimal("200.50"), stock.getPrice());
    }
}
