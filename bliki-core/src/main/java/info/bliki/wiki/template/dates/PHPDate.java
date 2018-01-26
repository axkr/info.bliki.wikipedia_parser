package info.bliki.wiki.template.dates;

import java.time.DayOfWeek;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static java.time.DayOfWeek.SUNDAY;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.Locale.ENGLISH;

/**
 * https://secure.php.net/manual/en/function.date.php
 */
public class PHPDate {
    private static final DateTimeFormatter RFC_2822 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", ENGLISH);
    private Map<Character, String> formatMapping;

    public PHPDate() {
        this.formatMapping = new HashMap<>();

        // Day of the month, 2 digits with leading zeros, 01 to 31
        formatMapping.put('d', "dd");

        // A textual representation of a day, three letters, Mon through Sun
        formatMapping.put('D', "E");

        // Day of the month without leading zeros, 1 to 31
        formatMapping.put('j', "d");

        // A full textual representation of the day of the week, Sunday through Saturday
        formatMapping.put('l', "EEEE");

        // ISO-8601 numeric representation of the day of the week, 1 (for Monday) through 7 (for Sunday)
        formatMapping.put('N', null);

        // English ordinal suffix for the day of the month, 2 characters, st, nd, rd or th.
        formatMapping.put('S', null);

        // Numeric representation of the day of the week, 0 (for Sunday) through 6 (for Saturday)
        formatMapping.put('w', null);

        // The day of the year (starting from 0), 0 through 365
        formatMapping.put('z', "D");

        // ISO-8601 week number of year, weeks starting on Monday, Example: 42 (the 42nd week in the year)
        formatMapping.put('W', "w");

        // A full textual representation of a month, such as January or March
        formatMapping.put('F', "MMMM");

        // Numeric representation of a month, with leading zeros, 01 through 12
        formatMapping.put('m', "MM");

        // A short textual representation of a month, three letters, Jan through Dec
        formatMapping.put('M', "MMM");

        // Numeric representation of a month, without leading zeros, 1 through 12
        formatMapping.put('n', "M");

        // Number of days in the given month, 28 through 31
        formatMapping.put('t', null);

        // Whether it's a leap year, 1 if it is a leap year, 0 otherwise.
        formatMapping.put('L', null);

        // ISO-8601 week-numbering year. Examples: 1999 or 2003
        formatMapping.put('o', "y");

        // A full numeric representation of a year, 4 digits. Examples: 1999 or 2003
        formatMapping.put('Y', "yyyy");

        // A two digit representation of a year
        formatMapping.put('y', "yy");

        // Lowercase Ante meridiem and Post meridiem. am or pm
        formatMapping.put('a', null);

        // Uppercase Ante meridiem and Post meridiem. AM or PM
        formatMapping.put('A', "a");

        // Swatch Internet time. 000 through 999
        formatMapping.put('B', null);

        // 12-hour format of an hour without leading zeros
        formatMapping.put('g', "h");

        // 24-hour format of an hour without leading zeros
        formatMapping.put('G', "H");

        // 12-hour format of an hour with leading zeros
        formatMapping.put('h', "hh");

        // 24-hour format of an hour with leading zeros
        formatMapping.put('H', "HH");

        // Minutes with leading zeros
        formatMapping.put('i', "mm");

        // Seconds, with leading zeros
        formatMapping.put('s', "ss");

        // Microseconds
        formatMapping.put('u', "SSS");

        // Milliseconds
        formatMapping.put('v', "e");

        // Timezone identifier. Examples: UTC, GMT, Atlantic/Azores
        formatMapping.put('e', "VV");

        // Whether or not the date is in daylight saving time. 1 if Daylight Saving Time, 0 otherwise.
        formatMapping.put('I', null);

        // Difference to Greenwich time (GMT) in hours. Example: +0200
        formatMapping.put('O', "ZZ");

        // Difference to Greenwich time (GMT) with colon between hours and minutes. Example: +02:00
        formatMapping.put('P', "xxx");

        // Timezone abbreviation. Examples: EST, MDT ...
        formatMapping.put('T', "zzzz");

        // Timezone offset in seconds. -43200 through 50400
        formatMapping.put('Z', null);

        // ISO 8601 date. 2004-02-12T15:19:21+00:00
        formatMapping.put('c', null);

        // RFC 2822 formatted date
        formatMapping.put('r', null);

        // Seconds since the Unix Epoch.
        formatMapping.put('U', null);
    }

    public String format(String format, ZonedDateTime time) {
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<format.length(); i++) {
            final char current = format.charAt(i);

            if (current == '\\') {
                if (i < format.length() - 1) {
                    builder.append(format.charAt(++i));
                }
            } else {
                String mapping = formatMapping.get(current);
                if (mapping != null) {
                    builder.append(time.format(DateTimeFormatter.ofPattern(mapping, ENGLISH)));
                } else {
                    builder.append(format(time, current));
                }
            }
        }
        return builder.toString();
    }

    private String format(ZonedDateTime time, char format) {
        switch (format) {
            case 'a':
                return DateTimeFormatter.ofPattern("a", ENGLISH).format(time).toLowerCase();
            case 'U':
                return String.valueOf(time.toEpochSecond());
            case 'r':
                return RFC_2822.format(time);
            case 'c':
                return ISO_OFFSET_DATE_TIME.format(time);
            case 't':
                return String.valueOf(YearMonth.from(time).lengthOfMonth());
            case 'L':
                return Year.from(time).isLeap() ? "1" : "0";
            case 'Z':
                return String.valueOf(ZoneOffset.from(time).getTotalSeconds());
            case 'N':
                return String.valueOf(DayOfWeek.from(time).getValue());
            case 'w':
                final DayOfWeek dayOfWeek = DayOfWeek.from(time);
                return String.valueOf(dayOfWeek == SUNDAY ? 0 : dayOfWeek.getValue());
            default:
                return String.valueOf(format);
        }
    }
}
