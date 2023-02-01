package com.salespage.salespageservice.domains.utils;



import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

  public static String ZONE_UTC = "UTC";
  public static String ZONE_DEFAULT = "Asia/Ho_Chi_Minh";

  public static boolean regexPhoneNumber(String phoneNumber) {
    if (phoneNumber == null) {
      return false;
    }
    String regex = "^0[37859]{1}[0-9]{1}\\d{7}$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.find();
  }

  public static boolean regexEmail(String email) {
    if (email == null) {
      return false;
    }
    String regex =
            "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(email);
    return matcher.find();
  }

  public static double round(double value) {
    if (value - Math.round(value) == 0) return (long) value;
    return Math.round(value + 0.5);
  }

  public static LocalDateTime getNowDateTimeAtDefault() {
    return LocalDateTime.now(ZoneId.of(ZONE_DEFAULT));
  }

  public static String boDauTiengViet(String str) {
    str = str.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
    str = str.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
    str = str.replaceAll("ì|í|ị|ỉ|ĩ", "i");
    str = str.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
    str = str.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
    str = str.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
    str = str.replaceAll("đ", "d");

    str = str.replaceAll("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
    str = str.replaceAll("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
    str = str.replaceAll("Ì|Í|Ị|Ỉ|Ĩ", "I");
    str = str.replaceAll("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
    str = str.replaceAll("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
    str = str.replaceAll("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
    str = str.replaceAll("Đ", "D");
    return str;
  }

  public static long getNowMillisAtUtc() {
    return System.currentTimeMillis();
  }

  public static String randomString(int length) {
    int len = length; // Độ dài của String random
    byte[] array = new byte[len];
    new Random().nextBytes(array);
    return new String(array, StandardCharsets.UTF_8);
  }

}
