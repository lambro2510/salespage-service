package com.salespage.salespageservice.domains.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

  private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Ho_Chi_Minh");
  private static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");

  public static LocalDateTime now() {
    return LocalDateTime.now(DEFAULT_ZONE_ID);
  }

  public static LocalDate nowDate() {
    ZonedDateTime utcDateTime = ZonedDateTime.now(DEFAULT_ZONE_ID);
    return utcDateTime.toLocalDate();
  }

  public static String nowString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.now(DEFAULT_ZONE_ID);
    return dateTime.format(formatter);
  }

  public static LocalDateTime convertUtcToVietnamTime(LocalDateTime utcDateTime) {
    ZonedDateTime utcZonedDateTime = utcDateTime.atZone(DEFAULT_ZONE_ID);
    ZonedDateTime vietnamZonedDateTime = utcZonedDateTime.withZoneSameInstant(DEFAULT_ZONE_ID);

    return vietnamZonedDateTime.toLocalDateTime();
  }

  public static String nowString(String pattern) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime dateTime = LocalDateTime.now(DEFAULT_ZONE_ID);
    return dateTime.format(formatter);
  }

  public static LocalDateTime startOfDay() {
    return LocalDateTime.now(DEFAULT_ZONE_ID).with(LocalTime.MIN);
  }

  public static LocalDateTime startOfDayAtVn() {
    return LocalDateTime.now(ZONE_ID_UTC).with(LocalTime.MIN);
  }

  public static Date toDate(final LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
  }

  public static String convertLocalDateTimeToString(LocalDateTime date, String pattern) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    return date.toLocalDate().format(dateTimeFormatter);
  }

  public static long nowInMillis() {
    Instant instant = Instant.now().atZone(DEFAULT_ZONE_ID).toInstant();
    return instant.toEpochMilli();
  }

  public static LocalDateTime convertToLocalDateTime(String dateString, String pattern) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    return LocalDateTime.parse(dateString, formatter);
  }

  public static Long convertLocalDateTimeToLong(LocalDateTime current) {

    Instant instant = current.atZone(DEFAULT_ZONE_ID).toInstant();

    return instant.toEpochMilli();
  }

  public static LocalDateTime convertLongToLocalDateTime(Long timestamp) {
    Instant instant = Instant.ofEpochMilli(timestamp);
    return instant.atZone(DEFAULT_ZONE_ID).toLocalDateTime();
  }

  public static LocalDateTime nowAtVn() {
    return LocalDateTime.now(DEFAULT_ZONE_ID);
  }
}
