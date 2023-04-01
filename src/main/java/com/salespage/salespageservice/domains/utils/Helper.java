package com.salespage.salespageservice.domains.utils;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Helper {

  public static String ZONE_UTC = "UTC";
  public static String ZONE_DEFAULT = "Asia/Ho_Chi_Minh";

  public static List<String> convertObjectIdListToHexStringList(List<ObjectId> objectIdList) {
    return objectIdList.stream()
        .map(ObjectId::toHexString)
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
}
