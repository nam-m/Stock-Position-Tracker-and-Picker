package persistence;

import model.Account;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(jsonObject.getString("name"), account.getAccountName());
        assertEquals(jsonObject.getString("cashBalance"), account.getCashBalance());
    }

    // EFFECTS: checks that portfolio and stock position fields are correctly saved in JSON
    protected void checkPortfolioDetails(JSONObject jsonObject, Account account) {
        JSONObject portfolioJson = jsonObject.getJSONObject("portfolio");
        JSONObject positionsJson = portfolioJson.getJSONObject("positions");

        // Loop through each stock position in the account and check its JSON equivalent
        account.getPortfolio().getAllStockPositions().forEach((symbol, position) -> {
            JSONObject stockJson = positionsJson.getJSONObject(symbol);
            assertEquals(symbol, stockJson.getString("symbol"));
            assertEquals(position.getQuantity(), stockJson.getInt("quantity"));
            assertEquals(position.getAverageCost(), stockJson.getString("averagePrice"));
        });
    }
}
