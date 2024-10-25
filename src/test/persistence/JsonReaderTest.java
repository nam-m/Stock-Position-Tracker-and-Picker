package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import model.Account;

// Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonReaderTest.java

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderAccountWithEmptyPortfolio() {
        String testFilePath = "./data/testReaderAccountWithEmptyPortfolio.json";
        JsonReader reader = new JsonReader(testFilePath);
        try {
            JSONObject json = getJsonObjectFromFile(testFilePath);
            Account account = reader.read();
            checkAccountDetails(json, account);
            assertEquals(0, account.getPortfolio().getTotalStockPositions());
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
