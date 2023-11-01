package com.salespage.salespageservice.domains.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class DateUtils {

  private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Ho_Chi_Minh");
  private static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");

  public static LocalDateTime now() {
    return LocalDateTime.now(ZONE_ID_UTC);
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

  public static Date asDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZONE_ID_UTC).toInstant());
  }

  public static LocalDateTime startOfDay() {
    return LocalDateTime.now(ZONE_ID_UTC).with(LocalTime.MIN);
  }

  public static LocalDateTime startOfDay(LocalDateTime dateTime) {
    return dateTime.with(LocalTime.MIN);
  }


  public static String startOfDayString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.now(ZONE_ID_UTC).with(LocalTime.MIN);
    return dateTime.format(formatter);
  }

  public static String startOfDayString(LocalDateTime dateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    dateTime.with(LocalTime.MIN);
    return dateTime.format(formatter);
  }

  public static LocalDateTime endOfDay() {
    return LocalDateTime.now(ZONE_ID_UTC).with(LocalTime.MAX);
  }

  public static LocalDateTime endOfDay(LocalDateTime dateTime) {
    return dateTime.with(LocalTime.MAX);
  }

  public static String endOfDayString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.now(ZONE_ID_UTC).with(LocalTime.MAX);
    return dateTime.format(formatter);
  }

  public static String endOfDayString(LocalDateTime dateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    dateTime.with(LocalTime.MAX);
    return dateTime.format(formatter);
  }

  public static boolean belongsToCurrentDay(final LocalDateTime localDateTime) {
    return localDateTime.isAfter(startOfDay()) && localDateTime.isBefore(endOfDay());
  }

  // note that week starts with Monday
  public static LocalDateTime startOfWeek(Integer value) {
    return LocalDateTime.now(DEFAULT_ZONE_ID)
        .minusWeeks(value)
        .with(LocalTime.MIN)
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
  }

  // note that week ends with Sunday
  public static LocalDateTime endOfWeek(Integer value) {
    return LocalDateTime.now(DEFAULT_ZONE_ID)
        .minusWeeks(value)
        .with(LocalTime.MAX)
        .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
  }

  public static LocalDateTime startOfMonth(Integer value) {
    return LocalDateTime.now(DEFAULT_ZONE_ID)
        .minusMonths(value)
        .with(LocalTime.MIN)
        .with(TemporalAdjusters.firstDayOfMonth());
  }

  public static LocalDateTime endOfMonth(Integer value) {
    return LocalDateTime.now(DEFAULT_ZONE_ID)
        .minusMonths(value)
        .with(LocalTime.MAX)
        .with(TemporalAdjusters.lastDayOfMonth());
  }

  public static long toMills(final LocalDateTime localDateTime) {
    return localDateTime.atZone(DEFAULT_ZONE_ID).toInstant().toEpochMilli();
  }

  public static Date toDate(final LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZONE_ID_UTC).toInstant());
  }

  public static String toString(final LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
  }

  public static String dateTimeToString(String dateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime datetime = LocalDateTime.parse(dateTime, formatter);
    return datetime.format(formatter);
  }

  public static String convertLocalDateToString(LocalDate date, String pattern) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    return date.format(dateTimeFormatter);
  }

  public static String convertLocalDateTimeToString(LocalDateTime dateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return dateTime.format(formatter);
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

  public static Long convertLocalDateToLong(LocalDate current) {
    LocalDateTime localDateTime = current.atStartOfDay();

    Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

    return instant.toEpochMilli();
  }

  public static LocalDateTime convertLongToLocalDateTime(Long timestamp) {

    Instant instant = Instant.ofEpochMilli(timestamp);

    return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
  }
}
