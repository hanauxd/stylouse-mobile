package lk.apiit.eea.stylouse.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringFormatter {
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String formatCurrency(double amount) {
        return String.format(Locale.getDefault(), "%.2f", amount);
    }

    public static String formatToken(String jwt) {
        return "Bearer ".concat(jwt);
    }
}
