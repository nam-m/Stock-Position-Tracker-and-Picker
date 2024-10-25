package persistence;

import model.Account;
import model.Portfolio;
import model.Stock;
import model.StockPosition;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
// Represents a reader that reads account from JSON data stored in file

public class JsonReader {
    private String source;

    // REQUIRES: source is not empty
    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads account from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Account read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAccount(jsonObject);
    }

    // REQUIRES: source is not empty
    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // REQUIRES: jsonObject is not empty
    // EFFECTS: parses account from JSON object and returns it
    private Account parseAccount(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        double cashBalance = jsonObject.getDouble("balance");
        Account account = new Account(name, cashBalance);
        parsePortfolio(account, jsonObject);
        return account;
    }

    // REQUIRES: jsonObject is not empty
    // EFFECTS: parses portfolio from JSON object and returns it
    private Portfolio parsePortfolio(Account account, JSONObject jsonObject) {
        JSONObject portfolioJson = jsonObject.getJSONObject("portfolio");
        JSONObject positionsJson = portfolioJson.getJSONObject("positions");
        Portfolio portfolio = account.getPortfolio();
        // Iterate over each key-value pair in portfolioJson
        // key is stock symbol in stock position
        for (String symbol : positionsJson.keySet()) {
            JSONObject positionJson = positionsJson.getJSONObject(symbol);
            StockPosition stockPosition = parseStockPosition(positionJson);
            portfolio.getAllStockPositions().put(symbol, stockPosition);
        }
        return portfolio;
    }

    // REQUIRES: jsonObject is not empty
    // EFFECTS: parses stock position from JSON object and returns it
    private StockPosition parseStockPosition(JSONObject jsonObject) {
        String symbol = jsonObject.getString("symbol");
        int quantity = jsonObject.getInt("quantity");
        double averagePrice = jsonObject.getDouble("averagePrice");
        Stock stock = new Stock(symbol, averagePrice);
        return new StockPosition(stock, quantity);
    }
}
