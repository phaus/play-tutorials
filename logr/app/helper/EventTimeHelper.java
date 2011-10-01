/**
 * EventTimeHelper
 * 29.09.2011
 * @author Philipp Haußleiter
 *
 */
package helper;

import java.util.Calendar;
import models.EventTime;
import play.Logger;

public class EventTimeHelper {

    private final static String DATE1 = "[29/Sep/2011:01:21:45 +0200]";
    private final static String DATE2 = "Sep 29 01:52:41";
    private final static String DATE3 = "2011-09-29 01:26:41";
    public static Calendar cal = Calendar.getInstance();
    public final static String MONTHS[] = {
        "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
    };

    public static EventTime getEventTimeFromString(String dateString) {
        dateString = dateString.trim();
        //Logger.info("searching for timestamp: "+dateString);
        String parts1[];
        String parts2[];
        String parts3[];
        if (dateString.length() == DATE1.length()) {
            dateString = dateString.replace("[", "").replace("]", "");
            parts1 = dateString.split(" ");
            parts2 = parts1[0].split(":");
            parts3 = parts2[0].split("/");
            /*
            Logger.info("creating:"     + getYear(parts3[2]) + "-"
                                        + getMonthNumber(parts3[1]) + "-"
                                        + getDay(parts3[0]) + "-"
                                        + getHour(parts2[1]) + "-"
                                        + getMinute(parts2[2]) + "-"
                                        + getSecond(parts2[3]) + "-"
                                        + getMillisecond(""));
             */
            return EventTime.findOrCreateWith(
                    getYear(parts3[2]),
                    getMonthNumber(parts3[1]),
                    getDay(parts3[0]),
                    getHour(parts2[1]),
                    getMinute(parts2[2]),
                    getSecond(parts2[3]),
                    getMillisecond(""));
        }
        if (dateString.length() == DATE2.length()) {
            parts1 = dateString.split(" ");
            parts2 = parts1[2].split(":");
            /*
            Logger.info("creating:"     + getYear("") + "-"
                                        + getMonthNumber(parts1[0]) + "-"
                                        + getDay(parts1[1]) + "-"
                                        + getHour(parts2[0]) + "-"
                                        + getMinute(parts2[1]) + "-"
                                        + getSecond(parts2[2]) + "-"
                                        + getMillisecond(""));
             */
            return EventTime.findOrCreateWith(
                    getYear(""),
                    getMonthNumber(parts1[0]),
                    getDay(parts1[1]),
                    getHour(parts2[0]),
                    getMinute(parts2[1]),
                    getSecond(parts2[2]),
                    getMillisecond(""));
        }
        return null;
    }

    private static int getYear(String yearString) {
        return getInt(yearString, cal.get(Calendar.YEAR));
    }

    private static int getMonth(String monthString) {
        return getInt(monthString, cal.get(Calendar.MONTH));
    }

    private static int getDay(String dayString) {
        return getInt(dayString, cal.get(Calendar.DAY_OF_MONTH));
    }

    private static int getHour(String hourString) {
        return getInt(hourString, cal.get(Calendar.HOUR_OF_DAY));
    }

    private static int getMinute(String minuteString) {
        return getInt(minuteString, cal.get(Calendar.MINUTE));
    }

    private static int getSecond(String secondString) {
        return getInt(secondString, cal.get(Calendar.SECOND));
    }

    private static int getInt(String valueString, int defaultValue) {
        int value;
        try {
            value = Integer.parseInt(valueString);
        } catch (NumberFormatException ex) {
            value = defaultValue;
        }
        return value;
    }

    private static long getMillisecond(String msString) {
        long millisecond;
        try {
            millisecond = Long.parseLong(msString);
        } catch (NumberFormatException ex) {
            millisecond = 0000L;
        }
        return millisecond;
    }

    private static int getMonthNumber(String monthString) {
        for (int i = 1; i < MONTHS.length; i++) {
            if (monthString.toUpperCase().equals(MONTHS[i])) {
                return (i + 1);
            }
        }
        return 1;
    }
}
