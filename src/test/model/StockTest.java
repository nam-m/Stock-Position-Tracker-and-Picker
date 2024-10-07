package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(226.8, testStock.getPrice());
    }

    @Test
    void testUpdatePrice() {
        testStock.setPrice(200.5);
        assertEquals(200.5, testStock.getPrice());
    }
}
