package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StockPositionTest {
    private Stock testStock;
    private StockPosition testPosition;

    @BeforeEach
    void runBefore() {
        testStock = new Stock("NVDA", 124.92);
        testPosition = new StockPosition(testStock, 3, 110);
    }

    @Test
    void testConstructor() {
        assertEquals(testStock, testPosition.getStock());
        assertEquals(3, testPosition.getQuantity());
        assertEquals(110, testPosition.getAverageCost());
    }

    @Test
    void testIncreasePosition() {
        testPosition.increasePosition(5, 120);
        assertEquals(8, testPosition.getQuantity());
        assertEquals(116.25, testPosition.getAverageCost());
    }

    @Test
    void testIncreasePositionMultipleTimes() {
        testPosition.increasePosition(5, 120);
        assertEquals(8, testPosition.getQuantity());
        assertEquals(116.25, testPosition.getAverageCost());

        testPosition.increasePosition(10, 125);
        assertEquals(18, testPosition.getQuantity());
        assertEquals(121.11, testPosition.getAverageCost());
    }

    @Test
    void testDecreasePosition() {
        testPosition.decreasePosition(1);
        assertEquals(2, testPosition.getQuantity());
        assertEquals(110, testPosition.getAverageCost());
    }

    @Test
    void testDecreasePositionMultipleTimes() {
        testPosition.decreasePosition(1);
        assertEquals(2, testPosition.getQuantity());
        assertEquals(110, testPosition.getAverageCost());

        testPosition.decreasePosition(1);
        assertEquals(1, testPosition.getQuantity());
        assertEquals(110, testPosition.getAverageCost());
    }

    @Test
    void testDecreasePositionToZero() {
        testPosition.decreasePosition(3);
        assertNull(testPosition);
    }

}
