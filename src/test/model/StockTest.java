package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StockTest {
    private Stock testStock;

    @BeforeEach
    void runBefore() {
        testStock = new Stock("AAPL", 226.8);
    }

    @Test
    void testConstructor() {
        assertEquals("AAPL", testStock.getSymbol());
        assertEquals(new BigDecimal("226.80"), testStock.getPrice());
    }

    @Test
    void testUpdatePrice() {
        testStock.setPrice(200.5);
        assertEquals(new BigDecimal("200.50"), testStock.getPrice());
    }
}
