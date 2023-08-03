package com.salespage.salespageservice.domains.utils;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class Helper {

  public static String ZONE_UTC = "UTC";
  public static String ZONE_DEFAULT = "Asia/Ho_Chi_Minh";

  public static List<String> convertObjectIdListToHexStringList(List<ObjectId> objectIdList) {
    return objectIdList.stream()
        .map(ObjectId::toHexString)
        .collect(Collectors.toList());
  }

  public static List<ObjectId> convertListStringToListObjectId(List<String> stringList) {
    return stringList.stream()
        .map(ObjectId::new)
        .collect(Collectors.toList());
  }

  public static java.io.File convertMultiPartToFile(MultipartFile file) throws IOException {
    java.io.File convFile = new java.io.File(Objects.requireNonNull(file.getOriginalFilename()));
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.close();
    return convFile;
  }

  public static String extractFileIdFromUrl(String imageUrl) {
    String[] parts = imageUrl.split("=");
    return parts[parts.length - 1];
  }

  public static String generateRandomString() {
    final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    final int STRING_LENGTH = 10;

    StringBuilder sb = new StringBuilder(STRING_LENGTH);
    Random random = new SecureRandom();
    for (int i = 0; i < STRING_LENGTH; i++) {
      sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
    }
    return sb.toString();
  }

  public static String genDescription(String username, String paymentId) {
    return username + paymentId;
  }

  public static long getStartTimeOfDay(LocalDate date) {
    LocalTime startTime = LocalTime.of(0, 0, 0, 0);
    LocalDateTime dateTime = LocalDateTime.of(date, startTime);
    return dateTime.atZone(ZoneId.of(ZONE_DEFAULT)).toInstant().toEpochMilli();
  }

  public static Long getEndTimeOfDay(LocalDate date) {
    LocalTime startTime = LocalTime.of(0, 0, 0, 0);
    LocalDateTime dateTime = LocalDateTime.of(date.plusDays(1), startTime);
    return dateTime.atZone(ZoneId.of(ZONE_DEFAULT)).toInstant().toEpochMilli() - 1;
  }

  public static Long getStartTimeOfWeek(LocalDate date) {
    LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDateTime mondayStartDateTime = startOfWeek.atStartOfDay();
    return mondayStartDateTime.atZone(ZoneId.of(ZONE_DEFAULT)).toInstant().toEpochMilli();
  }

  public static Long getEndTimeOfWeek(LocalDate date) {
    LocalDate startOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
    LocalDateTime mondayStartDateTime = startOfWeek.atStartOfDay();
    return mondayStartDateTime.atZone(ZoneId.of(ZONE_DEFAULT)).toInstant().toEpochMilli() - 1;
  }

  public static Long getStartTimeOfMonth(LocalDate date) {
    LocalDate startOfWeek = date.with(TemporalAdjusters.firstDayOfMonth());
    LocalDateTime mondayStartDateTime = startOfWeek.atStartOfDay();
    return mondayStartDateTime.atZone(ZoneId.of(ZONE_DEFAULT)).toInstant().toEpochMilli();
  }

  public static Long getEndTimeOfMonth(LocalDate date) {
    LocalDate startOfWeek = date.with(TemporalAdjusters.lastDayOfMonth());
    LocalDateTime mondayStartDateTime = startOfWeek.atStartOfDay();
    return mondayStartDateTime.atZone(ZoneId.of(ZONE_DEFAULT)).toInstant().toEpochMilli();
  }

  public static Long getStartTimeOfYear(LocalDate date) {
    LocalDate startOfWeek = date.with(TemporalAdjusters.firstDayOfYear());
    LocalDateTime mondayStartDateTime = startOfWeek.atStartOfDay();
    return mondayStartDateTime.atZone(ZoneId.of(ZONE_DEFAULT)).toInstant().toEpochMilli();
  }

  public static Long getEndTimeOfYear(LocalDate date) {
    LocalDate startOfWeek = date.with(TemporalAdjusters.lastDayOfYear());
    LocalDateTime mondayStartDateTime = startOfWeek.atStartOfDay();
    return mondayStartDateTime.atZone(ZoneId.of(ZONE_DEFAULT)).toInstant().toEpochMilli();
  }

  public static String getMonth(Date date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    return dateFormat.format(date);
  }

  public static String getDay(Date date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.format(date);
  }
}
