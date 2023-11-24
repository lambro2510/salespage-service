package com.salespage.salespageservice.domains.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

  private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Ho_Chi_Minh");
  private static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");

  public static LocalDateTime now() {
    return LocalDateTime.now(ZONE_ID_UTC);
  }

  public static LocalDate nowDate() {
    ZonedDateTime utcDateTime = ZonedDateTime.now(ZONE_ID_UTC);
    return utcDateTime.toLocalDate();
  }

  public static String nowString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.now(DEFAULT_ZONE_ID);
    return dateTime.format(formatter);
  }

  public static String nowString(String pattern) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime dateTime = LocalDateTime.now(ZONE_ID_UTC);
    return dateTime.format(formatter);
  }

  public static LocalDateTime startOfDay() {
    return LocalDateTime.now(ZONE_ID_UTC).with(LocalTime.MIN);
  }


  public static Date toDate(final LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZONE_ID_UTC).toInstant());
  }

  public static String convertLocalDateTimeToString(LocalDateTime date, String pattern) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    return date.toLocalDate().format(dateTimeFormatter);
  }

  public static long nowInMillis() {
    ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
    Instant instant = Instant.now().atZone(vnZone).toInstant();
    return instant.toEpochMilli();
  }

  public static LocalDateTime convertToLocalDateTime(String dateString, String pattern) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    return LocalDateTime.parse(dateString, formatter);
  }

  public static Long convertLocalDateTimeToLong(LocalDateTime current) {

    Instant instant = current.atZone(ZONE_ID_UTC).toInstant();

    return instant.toEpochMilli();
  }

  public static LocalDateTime convertLongToLocalDateTime(Long timestamp) {
    Instant instant = Instant.ofEpochMilli(timestamp);
    return instant.atZone(DEFAULT_ZONE_ID).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
  }
}
