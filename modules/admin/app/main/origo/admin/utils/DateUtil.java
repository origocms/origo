package main.origo.admin.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.i18n.Messages;

import java.util.Date;

public class DateUtil {

    public static String formatDateIfNotNull(Date date) {
        if (date != null) {
            String datePattern = Messages.get("date.format");
            return DateTimeFormat.forPattern(datePattern).print(new DateTime(date));
        }
        return "";
    }

    public static String formatTimeIfNotNull(Date date) {
        if (date != null) {
            String datePattern = Messages.get("time.format");
            return DateTimeFormat.forPattern(datePattern).print(new DateTime(date));
        }
        return "";
    }

    public static Date parseDate(String dateValue, String timeValue) {
        if (StringUtils.isNotBlank(dateValue)) {
            String datePattern = Messages.get("date.format");
            DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(datePattern);
            DateTime date = dateFormatter.parseDateTime(dateValue);

            if (StringUtils.isNotBlank(timeValue)) {
                String timePattern = Messages.get("time.format");
                DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(timePattern);
                DateTime time = timeFormatter.parseDateTime(timeValue);

                return DateTime.now().withDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth()).withTime(time.getHourOfDay(), time.getMinuteOfHour(), 0, 0).toDate();
            }
            return DateTime.now().withDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth()).toDate();
        }

        return null;
    }

    public static Date parseDate(String dateValue) {
        if (StringUtils.isNotBlank(dateValue)) {
            String datePattern = Messages.get("date.format");
            DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(datePattern);
            DateTime date = dateFormatter.parseDateTime(dateValue);

            return DateTime.now().withDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth()).toDate();
        }

        return null;
    }


}
