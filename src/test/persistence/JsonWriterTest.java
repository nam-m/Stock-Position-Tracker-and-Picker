package persistence;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Account;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonWriterTest.java
class JsonWriterTest extends JsonTest {
    private Account account;
    private static final String TEST_FILE_PATH = "./data/testAccount.json";

    @BeforeEach
    void setUp() {
        account = new Account("Mary", 10000);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterAccountWithEmptyPortfolio() {
        try {
            JsonWriter writer = new JsonWriter(TEST_FILE_PATH);
            writer.open();
            writer.write(account);
            writer.close();

            // Verify the JSON content matches the expected account data
            JSONObject jsonObject = getJsonObjectFromFile(TEST_FILE_PATH);
            checkAccountDetails(jsonObject, account);
            checkPortfolioDetails(jsonObject, account);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterAccountWithGeneralPortfolio() {
        try {
            account.buyStock("APPL", 5);
            account.buyStock("META", 6);
            JsonWriter writer = new JsonWriter(TEST_FILE_PATH);
            writer.open();
            writer.write(account);
            writer.close();

            // Verify the JSON content matches the expected account data
            JSONObject jsonObject = getJsonObjectFromFile(TEST_FILE_PATH);
            checkAccountDetails(jsonObject, account);
            checkPortfolioDetails(jsonObject, account);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}