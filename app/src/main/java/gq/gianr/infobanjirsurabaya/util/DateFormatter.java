package gq.gianr.infobanjirsurabaya.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by j on 17/03/2017.
 */

public class DateFormatter {
    private static final String DATE_FORMAT = "dd MMM yyyy HH:mm";

    public static String convertDate(Date date) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(date);
    }
}
