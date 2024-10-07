package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceUtils {
    // Method to round to 2 decimal places
    public static BigDecimal roundPrice(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        return bd.setScale(2, RoundingMode.HALF_UP);  // Always round to 2 decimal places
    }
}
