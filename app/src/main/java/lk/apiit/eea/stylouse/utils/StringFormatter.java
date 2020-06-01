package lk.apiit.eea.stylouse.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.format;
import static java.util.Locale.getDefault;

public class StringFormatter {
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd yyyy", getDefault());
        return dateFormat.format(date);
    }

    public static String formatDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, hh:mm a", getDefault());
        return dateFormat.format(date);
    }

    public static String formatCurrency(double amount) {
        return format(getDefault(), "%.2f", amount);
    }

    public static String formatToken(String jwt) {
        return "Bearer ".concat(jwt);
    }
}
