package se.callistaenterprise;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class Utils {

    public static final char NEW_LINE = '\n';
    public static final char QUOT = '\"';

    public static <T> T getLast(List<T> l) {
        return l.get(l.size() - 1);
    }

    public static Date tryParseDate(String text) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(text);
        } catch (ParseException e) {
            return null;
        }

    }

    public static Integer tryParseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Utils(){};

    public static String tryFormatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.format(date);
        } catch (NullPointerException e) {
            return null;
        }
    }
}
