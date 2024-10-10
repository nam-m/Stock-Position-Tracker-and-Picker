package utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import model.Stock;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Represents stock repository parsed from CSV
public class StockRepository {
    private Map<String, Stock> stockData = new HashMap<>();

    // Load stocks from the CSV file
    public void loadStocksFromCSV(String filePath) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            reader.readNext(); // Skip the header
            while ((line = reader.readNext()) != null) {
                String symbol = line[1]; // Stock symbol
                double price = Double.parseDouble(line[6]); // Current price
                Stock stock = new Stock(symbol, price);
                stockData.put(symbol, stock);
            }
        } catch (IOException e) {
            System.out.println("Error while reading CSV file");
        } catch (CsvValidationException e) {
            System.out.println("Invalid line while reading CSV file");
        }
    }

    // Get stock by symbol
    public Stock getStockBySymbol(String symbol) {
        return stockData.get(symbol);
    }
}
