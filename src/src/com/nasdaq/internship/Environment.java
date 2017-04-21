package src.com.nasdaq.internship;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

class Environment {
    private static Calendar cal = Calendar.getInstance();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static Locale locale = new Locale("en", "US");
    static DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(locale);
    private static BigInteger nextOrderID = BigInteger.ZERO;

    static {
        decimalFormat.setParseBigDecimal(true);
    }

    static String getCurrentTimeStamp() {
        return sdf.format(cal.getTime());
    }

    static BigInteger getNextOrderId() {
        nextOrderID = nextOrderID.add(BigInteger.ONE);
        return nextOrderID;
    }
}
