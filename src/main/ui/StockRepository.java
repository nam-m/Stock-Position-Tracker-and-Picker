package ui;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import model.Stock;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Represents stock repository parsed from CSV
public class StockRepository {
    private static final String STOCK_CSV_FILE_PATH = "data/sp500_companies.csv"; // file path
    private static Map<String, Stock> stockData; // map of stock data
    
    // Static initializer block to load stocks from csv once before
    // any objects are created or methods are called
    static {
        stockData = new HashMap<>();
        try {
            loadStocksFromCSV(STOCK_CSV_FILE_PATH);
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error while reading CSV: " + e.getMessage());
        }
    }

    /** 
     * REQUIRES: filePath not empty
     * MODIFIES: stockData
     * EFFECTS: Parse stock symbol and current price from CSV to stockData
     */
    static void loadStocksFromCSV(String filePath) throws IOException, CsvValidationException {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)) 
                                  .withSkipLines(1) 
                                  .build(); 
            String[] line;
            while ((line = reader.readNext()) != null) {
                String symbol = line[1]; // Stock symbol
                double price = Double.parseDouble(line[6]); // Current price
                Stock stock = new Stock(symbol, price);
                stockData.put(symbol, stock);
            }
        } catch (IOException e) {
            throw new IOException("Error while reading CSV file", e);
        } catch (CsvValidationException e) {
            throw new CsvValidationException("Invalid line while reading CSV file");
        }
    }

    /**
     * SPECIFIES: Get stock by symbol from stock repository
     */
    public static Stock getStockBySymbol(String symbol) {
        return stockData.get(symbol);
    }

    /**
     * SPECIFIES: add stock to stock repository
     */
    public static void addStock(Stock stock) {
        stockData.put(stock.getSymbol(), stock);
    }

    /**
     * SPECIFIES: Clear all stocks from the repository
     */
    public static void clear() {
        stockData.clear();  
    }
}
