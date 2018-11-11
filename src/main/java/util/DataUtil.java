package util;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DataUtil {

    public static String getDataIso(Date data) {
        if (data != null) {
            DateTime dateTime = new DateTime(data);
            DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
            return fmt.print(dateTime);
        } else {
            return "";
        }
    }

    public static Date getData(String data) {
        DateTime dateTime = new DateTime(data);
        return dateTime.toDate();
    }

}
