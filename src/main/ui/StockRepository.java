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
    private static Map<String, Stock> stockData;
    
    // Static initializer block to load stocks from csv once before
    // any objects are created or methods are called
    static {
        stockData = new HashMap<>();
        try {
            loadStocksFromCSV("data/sp500_companies.csv");
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error while reading CSV: " + e.getMessage());
        }
    }

    // Load stocks from the CSV file
    /** 
     * REQUIRES: filePath not empty
     * EFFECTS: Parse stock symbol and current price from CSV to Stock
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


    // Get stock by symbol
    public static Stock getStockBySymbol(String symbol) {
        return stockData.get(symbol);
    }

    public static void addStock(Stock stock) {
        stockData.put(stock.getSymbol(), stock);
    }

    public static void clear() {
        stockData.clear();  // Clear all stocks from the repository
    }
}
