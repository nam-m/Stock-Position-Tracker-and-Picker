package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Account;
import model.Portfolio;
import model.Stock;
import model.StockPosition;
import ui.StockRepository;

// Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonReaderTest.java

public class JsonReaderTest extends JsonTest {
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("Henry", 10000);
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            account = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderAccountWithEmptyPortfolio() {
        String testFilePath = "./data/testReaderAccountWithEmptyPortfolio";
        JsonReader reader = new JsonReader(testFilePath);
        try {
            JSONObject json = getJsonObjectFromFile(testFilePath);
            Account account = reader.read();
            checkAccountDetails(json, account);
            checkPortfolioDetails(json, account);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderAccountWithGeneralPortfolio() {
        String testFilePath = "./data/testReaderAccountWithGeneralPortfolio.json";
        JsonReader reader = new JsonReader(testFilePath);
        try {
            JSONObject json = getJsonObjectFromFile(testFilePath);
            Account account = reader.read();
            checkAccountDetails(json, account);
            checkPortfolioDetails(json, account);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
