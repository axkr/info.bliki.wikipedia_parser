package info.bliki.wiki.template.dates;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PHPDateTest {
    private PHPDate phpDate;
    private ZonedDateTime referenceDate;

    @Before
    public void setUp() throws Exception {
        phpDate = new PHPDate();
        referenceDate = ZonedDateTime.parse("2018-02-04T01:06+01:00");
    }

    @Test public void testDayOfMonth() {
        assertThat(phpDate.format("d", referenceDate)).isEqualTo("04");
    }

    @Test public void testDayOfMonthTextual() {
        assertThat(phpDate.format("D", referenceDate)).isEqualTo("Sun");
    }

    @Test public void testDayOfMonthWithoutLeadingZero() {
        assertThat(phpDate.format("j", referenceDate)).isEqualTo("4");
    }

    @Test public void testDayOfWeekTextual() {
        assertThat(phpDate.format("l", referenceDate)).isEqualTo("Sunday");
    }

    @Test public void testDayOfWeekISO8601Numerical() {
        assertThat(phpDate.format("N", referenceDate)).isEqualTo("7");
    }

    @Test public void testDayOfWeekNumericalZeroBased() {
        assertThat(phpDate.format("w", referenceDate)).isEqualTo("0");
    }

    @Test public void testDayOfYear() {
        assertThat(phpDate.format("z", referenceDate)).isEqualTo("35");
    }

    @Test public void testWeekOfYear() {
        assertThat(phpDate.format("W", referenceDate)).isEqualTo("6");
    }

    @Test public void testMonthTextual() {
        assertThat(phpDate.format("F", referenceDate)).isEqualTo("February");
    }

    @Test public void testMonthNumericalWithLeadingZeros() {
        assertThat(phpDate.format("m", referenceDate)).isEqualTo("02");
    }

    @Test public void testMonthTextualShort() {
        assertThat(phpDate.format ("M", referenceDate)).isEqualTo("Feb");
    }

    @Test public void testMonthNumericalWithoutLeadingZeros() {
        assertThat(phpDate.format("n", referenceDate)).isEqualTo("2");
    }

    @Test public void testNumberOfDaysInGivenMonth() {
        assertThat(phpDate.format("t", referenceDate)).isEqualTo("28");
    }

    @Test public void testLeapYear() {
        assertThat(phpDate.format("L", referenceDate)).isEqualTo("0");
    }

    @Test public void testWeekNumberingYear() {
        assertThat(phpDate.format("o", referenceDate)).isEqualTo("2018");
    }

    @Test public void testFullNumericYear() {
        assertThat(phpDate.format("Y", referenceDate)).isEqualTo("2018");
    }

    @Test public void testTwoDigitYear() {
        assertThat(phpDate.format("y", referenceDate)).isEqualTo("18");
    }

    @Test public void testLowerCaseAMPM() {
        assertThat(phpDate.format("a", referenceDate)).isEqualTo("am");
    }

    @Test public void testUpperCaseAMPM() {
        assertThat(phpDate.format("A", referenceDate)).isEqualTo("AM");
    }

    @Test public void testTwelveHourFormatWithoutLeadingZeros() {
        assertThat(phpDate.format("g", referenceDate)).isEqualTo("1");
    }

    @Test public void testTwentyFourHourFormatWithoutLeadingZeros() {
        assertThat(phpDate.format("G", referenceDate)).isEqualTo("1");
    }

    @Test public void testTwelveHourFormatWithLeadingZeros() {
        assertThat(phpDate.format("h", referenceDate)).isEqualTo("01");
    }

    @Test public void testTwentyFourHourFormatWithLeadingZeros() {
        assertThat(phpDate.format("H", referenceDate)).isEqualTo("01");
    }

    @Test public void testMinutesWithLeadingZeros() {
        assertThat(phpDate.format("i", referenceDate)).isEqualTo("06");
    }

    @Test public void testSecondsWithLeadingZeros() {
        assertThat(phpDate.format("s", referenceDate)).isEqualTo("00");
    }

    @Ignore @Test public void testMicroSeconds() {
        assertThat(phpDate.format("u", referenceDate)).isEqualTo("00");
    }

    @Test public void testMilliSeconds() {
        assertThat(phpDate.format("u", referenceDate)).isEqualTo("000");
    }

    @Test public void testTimeZoneIdentifier() {
        assertThat(phpDate.format("e", referenceDate)).isEqualTo("+01:00");
    }

    @Test public void testDifferenceToGMTInHours() {
        assertThat(phpDate.format("O", referenceDate)).isEqualTo("+0100");
    }

    @Test public void testDifferenceToGMTInHoursColon() {
        assertThat(phpDate.format("P", referenceDate)).isEqualTo("+01:00");
    }

    @Test public void testTimeZoneAbbreviation() {
        assertThat(phpDate.format("T", referenceDate)).isEqualTo("+01:00");
    }

    @Test public void testTimeZoneOffsetInSeconds() {
        assertThat(phpDate.format("Z", referenceDate)).isEqualTo("3600");
    }

    @Test public void testISO8601() {
        assertThat(phpDate.format("c", referenceDate)).isEqualTo("2018-02-04T01:06:00+01:00");
    }

    @Test public void testRFC2822() {
        assertThat(phpDate.format("r", referenceDate)).isEqualTo("Sun, 04 Feb 2018 01:06:00 +0100");
    }

    @Test public void testSecondsSinceUnixEpoch() {
        assertThat(phpDate.format("U", referenceDate)).isEqualTo("1517702760");
    }

    @Test public void testComplexDate() {
        assertThat(phpDate.format("'''Y''' F j", referenceDate)).isEqualTo("'''2018''' February 4");
    }

    @Test public void testQuoting() {
        assertThat(phpDate.format("Y \\q\\u\\o\\t\\e\\", referenceDate)).isEqualTo("2018 quote");
    }
}
