package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StockPositionTest {
    private StockPosition testPosition;
    private Stock testStock;

    @BeforeEach
    void runBefore() {
        testStock = new Stock("NVDA", 124.92);
        testPosition = new StockPosition(testStock, 3, 110);
    }

    @Test
    void testConstructor() {
        assertEquals(testStock, testPosition.getStock());
        assertEquals(3, testPosition.getQuantity());
        assertEquals(new BigDecimal("110.00"), testPosition.getAverageCost());
    }

    @Test
    void testIncreasePosition() {
        testPosition.increasePosition(5, 120);
        assertEquals(8, testPosition.getQuantity());
        assertEquals(new BigDecimal("116.25"), testPosition.getAverageCost());
    }

    @Test
    void testIncreasePositionMultipleTimes() {
        testPosition.increasePosition(5, 120);
        assertEquals(8, testPosition.getQuantity());
        assertEquals(new BigDecimal("116.25"), testPosition.getAverageCost());

        testPosition.increasePosition(10, 125);
        assertEquals(18, testPosition.getQuantity());
        assertEquals(new BigDecimal("121.11"), testPosition.getAverageCost());
    }

    @Test
    void testIncreaseNegativeQuantity() {
        testPosition.increasePosition(-2, 130);
        assertEquals(3, testPosition.getQuantity());
        assertEquals(new BigDecimal("110.00"), testPosition.getAverageCost());
    }

    @Test
    void testIncreaseNegativeSharePrice() {
        testPosition.increasePosition(2, -130);
        assertEquals(3, testPosition.getQuantity());
        assertEquals(new BigDecimal("110.00"), testPosition.getAverageCost());
    }

    @Test
    void testDecreasePosition() {
        testPosition.decreasePosition(1);
        assertEquals(2, testPosition.getQuantity());
        assertEquals(new BigDecimal("110.00"), testPosition.getAverageCost());
    }

    @Test
    void testDecreasePositionMultipleTimes() {
        testPosition.decreasePosition(1);
        assertEquals(2, testPosition.getQuantity());
        assertEquals(new BigDecimal("110.00"), testPosition.getAverageCost());

        testPosition.decreasePosition(1);
        assertEquals(1, testPosition.getQuantity());
        assertEquals(new BigDecimal("110.00"), testPosition.getAverageCost());
    }

    @Test
    void testDecreaseNegativeQuantity() {
        testPosition.decreasePosition(-2);
        assertEquals(3, testPosition.getQuantity());
        assertEquals(new BigDecimal("110.00"), testPosition.getAverageCost());
    }

    @Test
    void testDecreaseMoreThanOwnedQuantity() {
        testPosition.decreasePosition(5);
        assertEquals(3, testPosition.getQuantity());
        assertEquals(new BigDecimal("110.00"), testPosition.getAverageCost());
    }
}
