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
        stock = new Stock("NVDA", 110);
        position = new StockPosition(stock, 3);
    }

    @Test
    void testConstructor() {
        assertEquals(stock, position.getStock());
        assertEquals(3, position.getQuantity());
        assertEquals(new BigDecimal("330.00"), position.getTotalCost());
        assertEquals(new BigDecimal("110.00"), position.getAverageCost());
    }

    @Test
    void testIncreasePosition() {
        position.increasePosition(5);
        assertEquals(8, position.getQuantity());
        assertEquals(new BigDecimal("880.00"), position.getTotalCost());
        assertEquals(new BigDecimal("110.00"), position.getAverageCost());
    }

    @Test
    void testIncreasePositionMultipleTimes() {
        position.increasePosition(5);
        assertEquals(8, position.getQuantity());
        assertEquals(new BigDecimal("880.00"), position.getTotalCost());
        assertEquals(new BigDecimal("110.00"), position.getAverageCost());

        position.increasePosition(10);
        assertEquals(18, position.getQuantity());
        assertEquals(new BigDecimal("1980.00"), position.getTotalCost());
        assertEquals(new BigDecimal("110.00"), position.getAverageCost());
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
        assertEquals(new BigDecimal("110.00"), position.getAverageCost());

        position.decreasePosition(1);
        assertEquals(1, position.getQuantity());
        assertEquals(new BigDecimal("110.00"), position.getTotalCost());
        assertEquals(new BigDecimal("110.00"), position.getAverageCost());
    }

    @Test
    void testDecreaseNegativeQuantity() {
        position.decreasePosition(-2);
        assertEquals(3, position.getQuantity());
        assertEquals(new BigDecimal("330.00"), position.getTotalCost());
        assertEquals(new BigDecimal("110.00"), position.getAverageCost());
    }

    @Test
    void testDecreaseEqualToOwnedQuantity() {
        position.decreasePosition(3);
        assertEquals(0, position.getQuantity());
        assertEquals(new BigDecimal("0.00"), position.getTotalCost());
        assertEquals(new BigDecimal("0.00"), position.getAverageCost());
    }

    @Test
    void testDecreaseMoreThanOwnedQuantity() {
        position.decreasePosition(5);
        assertEquals(3, position.getQuantity());
        assertEquals(new BigDecimal("330.00"), position.getTotalCost());
        assertEquals(new BigDecimal("110.00"), position.getAverageCost());
    }
}
