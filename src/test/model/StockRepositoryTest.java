package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.math.BigDecimal;

public class StockRepositoryTest {
    @BeforeAll
    static void setUp() {
        try {
            StockRepository.loadStocksFromCSV("data/sp500_companies.csv");
        } catch (IOException | CsvValidationException e) {
            fail("Failed to load stocks from CSV: " + e.getMessage());
        }
    }

    @Test
    void testGetStockBySymbolValid() {
        Stock stock = StockRepository.getStockBySymbol("AAPL");
        assertNotNull(stock);
        assertEquals("AAPL", stock.getSymbol());
        assertEquals(new BigDecimal("229.54"), stock.getPrice());
    }

    @Test
    void testGetStockBySymbolInvalid() {
        Stock unknownStock = StockRepository.getStockBySymbol("UNKNOWN");
        assertNull(unknownStock);
    }
}
