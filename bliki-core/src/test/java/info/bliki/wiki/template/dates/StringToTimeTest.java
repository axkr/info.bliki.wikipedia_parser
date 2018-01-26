package info.bliki.wiki.template.dates;

import org.junit.Ignore;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringToTimeTest {
    @Test public void testNowPlus2Days() throws Exception {
        final Date date = (Date) StringToTime.date("now +2 days");
        assertThat(date).isAfter(new Date());
    }

    @Test public void testNowPlus2DaysWithSpace() throws Exception {
        final Date date = (Date) StringToTime.date("now + 2 days");
        assertThat(date).isAfter(new Date());
    }

    @Test public void testMySqlDateFormat() {
        Date now = now();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.DATE, 26);
        cal.set(Calendar.YEAR, 1981);
        cal.set(Calendar.HOUR_OF_DAY, 15);
        cal.set(Calendar.MINUTE, 26);
        cal.set(Calendar.SECOND, 3);
        cal.set(Calendar.MILLISECOND, 435);

        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("1981-10-26 15:26:03.435", now));
    }

	@Ignore @Test public void testISO8601() {
        Date now = now();
        Calendar cal = Calendar.getInstance();
		cal.setTime(now);

		cal.set(Calendar.MONTH, Calendar.OCTOBER);
		cal.set(Calendar.DATE, 26);
		cal.set(Calendar.YEAR, 1981);
		cal.set(Calendar.HOUR_OF_DAY, 15);
		cal.set(Calendar.MINUTE, 25);
		cal.set(Calendar.SECOND, 2);
		cal.set(Calendar.MILLISECOND, 435);

		assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("1981-10-26T15:26:03.435ZEST", now));
	}

    @Test public void test1200Seconds() {
        Date now = now();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + 1200);
        assertTrue(new Date(cal.getTimeInMillis()).equals(new StringToTime("+1200 s", now)));
        assertFalse(new Date(cal.getTimeInMillis()).equals(new StringToTime("+1 s", now)));
    }

    @Test public void testVariousExpressionsOfTimeOfDay() {
        Date now = now();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("11:59:59 PM", now));
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("23:59:59", now));

        cal.set(Calendar.SECOND, 0);
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("23:59", now));
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("11:59 PM", now));

        cal.set(Calendar.MILLISECOND, 123);
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("23:59:00.123", now));

        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.DATE, 26);
        cal.set(Calendar.YEAR, 1981);
        cal.set(Calendar.HOUR_OF_DAY, 15);
        cal.set(Calendar.MINUTE, 27);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("October 26, 1981 3:27:00 PM", now));

        cal.set(Calendar.HOUR, 5);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.AM_PM, Calendar.PM);
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("10/26/81 5PM", now));

        cal.setTime(now);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
        cal.set(Calendar.HOUR, 5);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, Calendar.PM);
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("tomorrow 5PM", now));

        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 2);
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("yesterday 5PM", now));
        assertEquals(new StringToTime("yesterday evening", now), new StringToTime("yesterday 5PM", now));
    }

    @Test public void testStaticMethods() {
        Date now = now();

        // timestamp
        Long time = (Long) StringToTime.time("now", now);
        assertEquals(new Date(now.getTime()), new Date(time));

        // calendar
        Calendar cal = (Calendar) StringToTime.cal("now", now);
        assertEquals(new Date(now.getTime()), new Date(cal.getTimeInMillis()));

        // date
        Date date = (Date) StringToTime.date("now", now);
        assertEquals(new Date(now.getTime()), date);
    }


    @Test public void testNow() {
        Date now = now();
        assertEquals(new Date(now.getTime()), new StringToTime("now", now));
    }

    @Test public void testToday() {
        Date now = now();
        assertEquals(new StringToTime("00:00:00.000", now), new StringToTime("today", now));
    }

    @Test public void testThisMorning() {
        Date now = now();
        assertEquals(new StringToTime("07:00:00.000", now), new StringToTime("this morning", now));
        assertEquals(new StringToTime("morning", now), new StringToTime("this morning", now));
    }

    @Test public void testNoon() {
        Date now = now();
        assertEquals(new StringToTime("12:00:00.000", now), new StringToTime("noon", now));
    }

    @Test public void testThisAfternoon() {
        Date now = now();
        assertEquals(new StringToTime("13:00:00.000", now), new StringToTime("this afternoon", now));
        assertEquals(new StringToTime("afternoon", now), new StringToTime("this afternoon", now));
    }

    @Test public void testThisEvening() {
        Date now = now();
        assertEquals(new StringToTime("17:00:00.000", now), new StringToTime("this evening", now));
        assertEquals(new StringToTime("evening", now), new StringToTime("this evening", now));
    }

    @Test public void testTonight() {
        Date now = now();
        assertEquals(StringToTime.time("20:00:00.000", now), StringToTime.time("tonight", now));
    }

    @Test public void testIncrements() {
        Date now = new Date(0);

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
        assertEquals(cal.getTimeInMillis(), StringToTime.time("+1 hour", now));

        cal.setTime(now);
        cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR) + 52);
        assertEquals(cal.getTimeInMillis(), StringToTime.time("+52 weeks", now));

        assertEquals(new StringToTime("1 year", now), new StringToTime("+1 year", now));

        assertEquals(new StringToTime("+1 year", now), new StringToTime("+12 months", now));

        assertEquals(new StringToTime("+1 year 6 months", now), new StringToTime("+18 months", now));

        assertEquals(new StringToTime("12 months 1 day 60 seconds", now), new StringToTime("1 year 24 hours 1 minute", now));
    }

    @Test public void testDecrements() {
        Date now = now();

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("-1 hour", now));
    }

    @Test public void testTomorrow() {
        Date now = now();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("tomorrow", now));
        assertEquals(new StringToTime("now +24 hours", now), new StringToTime("tomorrow", now));
    }

    @Test public void testTomorrowMorning() {
        Date now = now();
        assertEquals(new StringToTime("this morning +24 hours", now), new StringToTime("tomorrow morning", now));
    }

    @Test public void testTomorrowNoon() {
        Date now = now();
        assertEquals(new StringToTime("noon +24 hours", now), new StringToTime("tomorrow noon", now));
        assertEquals(new StringToTime("noon +24 hours", now), new StringToTime("noon tomorrow", now));
    }

    @Test public void testTomorrowAfternoon() {
        Date now = now();
        assertEquals(new StringToTime("this afternoon +24 hours", now), new StringToTime("tomorrow afternoon", now));
    }

    @Test public void testTomorrowEvening() {
        Date now = now();
        assertEquals(new StringToTime("this evening +24 hours", now), new StringToTime("tomorrow evening", now));
    }

    @Test public void testTomorrowNight() {
        Date now = now();
        assertEquals(new StringToTime("tonight +24 hours", now), new StringToTime("tomorrow night", now));
    }

    // e.g., October 26, 1981, or Oct 26, 1981, or 26 October 1981, or 26 Oct 1981, or 26 Oct 81
    @Test public void testLongHand() throws Exception {
        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("October 26, 1981"));
        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("Oct 26, 1981"));

        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("26 October 1981"));
        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("October 26, 1981"));
        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("October 26 1981"));
        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("26 Oct 1981"));
        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("26 Oct 81"));

        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("26 october 1981"));
        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("26 oct 1981"));
        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("26 oct 81"));

        assertEquals(new SimpleDateFormat("M/d/y").parse("1/1/2000"), new StringToTime("1 Jan 2000"));
        assertEquals(new SimpleDateFormat("M/d/y").parse("1/1/2000"), new StringToTime("1 Jan 00"));

        assertEquals(new SimpleDateFormat("M/d/y").parse("1/1/2000"), new StringToTime("1 jan 2000"));
        assertEquals(new SimpleDateFormat("M/d/y").parse("1/1/2000"), new StringToTime("1 jan 00"));
    }

    // e.g., 10/26/1981 or 10/26/81
    @Test public void testWithSlashesMonthFirst() throws Exception {
        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("10/26/1981"));
        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("10/26/81"));
    }

    // e.g., 1981/10/26
    @Test public void testWithSlashesYearFirst() throws Exception {
        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("1981/10/26"));
    }

    // e.g., 19811026
    @Test public void testYearMonthDay() throws Exception {
        assertEquals(new SimpleDateFormat("M/d/y").parse("10/26/1981"), new StringToTime("19811026"));
    }

    // e.g., 1981-10
    @Test public void testYearMonth() throws Exception {
        assertEquals(new SimpleDateFormat("M/y").parse("10/1981"), new StringToTime("1981-10"));
    }

    // e.g., October 26 and Oct 26
    @Ignore @Test public void testMonthAndDate() throws Exception {
        Date now = now();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.DATE, 26);
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("October 26", now));
        assertEquals(new StringToTime("Oct 26", now), new StringToTime("October 26", now));
    }

    // e.g., 10/26
    @Ignore @Test public void testWithSlashesMonthAndDate() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.DATE, 26);
        assertEquals(new Date(cal.getTimeInMillis()), new StringToTime("10/26"));
    }

    // e.g., October or Oct
    @Test public void testMonth() throws Exception {
        Date now = now();

        assertEquals(new StringToTime("October", now), new StringToTime("Oct", now));

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        // it should be this year
        assertEquals(cal.get(Calendar.YEAR), new StringToTime("January", now).getCal().get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.YEAR), new StringToTime("December", now).getCal().get(Calendar.YEAR));
    }

    @Ignore @Test public void testDayOfWeek() throws Exception {
        Date now = now();
        assertEquals(StringToTime.date("Friday", now), StringToTime.date("Fri", now));

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        // if today's day of the week is greater than or equal to our test day of the week (Wednesday)
        if (cal.get(Calendar.DAY_OF_WEEK) >= 3) // then the day of the week on the date returned should be next week
            assertEquals(cal.get(Calendar.WEEK_OF_YEAR) + 1, new StringToTime("Wednesday", now).getCal().get(Calendar.WEEK_OF_YEAR));
        else // otherwise, it should be this year
            assertEquals(cal.get(Calendar.WEEK_OF_YEAR), new StringToTime("Wednesday", now).getCal().get(Calendar.WEEK_OF_YEAR));
    }

    @Test public void testNext() {
        Date now = now();
        assertEquals(new StringToTime("next January 15", now), new StringToTime("Jan 15", now));
        assertEquals(new StringToTime("next Dec", now), new StringToTime("December", now));
        assertEquals(new StringToTime("next Sunday", now), new StringToTime("Sun", now));
        assertEquals(new StringToTime("next Sat", now), new StringToTime("Saturday", now));
    }

    @Test public void testLast() {
        Date now = now();
        assertEquals(new StringToTime("last January 15", now), new StringToTime("Jan 15 -1 year", now));
        assertEquals(new StringToTime("last Dec", now), new StringToTime("December -1 year", now));
        assertEquals(new StringToTime("last Sunday", now), new StringToTime("Sun -1 week", now));
        assertEquals(new StringToTime("last Sat", now), new StringToTime("Saturday -1 week", now));
    }

    private Date now() {
        return new Date(0);
    }
}
