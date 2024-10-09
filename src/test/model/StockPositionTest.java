package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StockPositionTest {
    private StockPosition position;
    private Stock stock;

    @BeforeEach
    void runBefore() {
        stock = new Stock("NVDA", 124.92);
        position = new StockPosition(stock, 3, 110);
    }

    @Test
    void testConstructor() {
        assertEquals(stock, position.getStock());
        assertEquals(3, position.getQuantity());
        assertEquals(new BigDecimal("330.00"), position.getTotalCost());
    }

    @Test
    void testIncreasePosition() {
        position.increasePosition(5, 120);
        assertEquals(8, position.getQuantity());
        assertEquals(new BigDecimal("930.00"), position.getTotalCost());
    }

    @Test
    void testIncreasePositionMultipleTimes() {
        position.increasePosition(5, 120);
        assertEquals(8, position.getQuantity());
        assertEquals(new BigDecimal("930.00"), position.getTotalCost());
        assertEquals(new BigDecimal("116.25"), position.getAverageCost());

        position.increasePosition(10, 125);
        assertEquals(18, position.getQuantity());
        assertEquals(new BigDecimal("2180.00"), position.getTotalCost());
        assertEquals(new BigDecimal("121.11"), position.getAverageCost());
    }

    @Test
    void testIncreaseNegativeQuantity() {
        position.increasePosition(-2, 130);
        assertEquals(3, position.getQuantity());
        assertEquals(new BigDecimal("330.00"), position.getTotalCost());
    }

    @Test
    void testIncreaseNegativeSharePrice() {
        position.increasePosition(2, -130);
        assertEquals(3, position.getQuantity());
        assertEquals(new BigDecimal("330.00"), position.getTotalCost());
    }

    @Test
    void testDecreasePosition() {
        position.decreasePosition(1);
        assertEquals(2, position.getQuantity());
        assertEquals(new BigDecimal("110.00"), position.getAverageCost());
    }

    @Test
    void testDecreasePositionMultipleTimes() {
        position.decreasePosition(1);
        assertEquals(2, position.getQuantity());
        assertEquals(new BigDecimal("220.00"), position.getTotalCost());

        position.decreasePosition(1);
        assertEquals(1, position.getQuantity());
        assertEquals(new BigDecimal("110.00"), position.getTotalCost());
    }

    @Test
    void testDecreaseNegativeQuantity() {
        position.decreasePosition(-2);
        assertEquals(3, position.getQuantity());
        assertEquals(new BigDecimal("330.00"), position.getTotalCost());
    }

    @Test
    void testDecreaseMoreThanOwnedQuantity() {
        position.decreasePosition(5);
        assertEquals(3, position.getQuantity());
        assertEquals(new BigDecimal("110.00"), position.getTotalCost());
    }
}
