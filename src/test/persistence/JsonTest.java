package persistence;

import model.Account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

public class JsonTest {

    // EFFECTS: loads the JSON content from file and returns it as JSONObject
    protected JSONObject getJsonObjectFromFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONObject(content);
    }

    // EFFECTS: checks that account fields are correctly saved in JSON
    protected void checkAccountDetails(JSONObject jsonObject, Account account) {
        assertTrue(jsonObject.getString("name").equals(account.getAccountName()));
        assertTrue(jsonObject.getString("cashBalance").equals(account.getCashBalance().toString()));
    }

    // EFFECTS: checks that portfolio and stock position fields are correctly saved in JSON
    protected void checkPortfolioDetails(JSONObject jsonObject, Account account) {
        JSONObject portfolioJson = jsonObject.getJSONObject("portfolio");
        JSONObject positionsJson = portfolioJson.getJSONObject("positions");

        // Loop through each stock position in the account and check its JSON equivalent
        account.getPortfolio().getAllStockPositions().forEach((symbol, position) -> {
            JSONObject stockJson = positionsJson.getJSONObject(symbol);
            assertEquals(position.getQuantity(), stockJson.getInt("quantity"));
            assertTrue(position.getAverageCost().toString().equals(stockJson.getString("averagePrice")));
        });
    }
}
