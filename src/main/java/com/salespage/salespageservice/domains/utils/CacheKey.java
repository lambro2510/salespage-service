package com.salespage.salespageservice.domains.utils;

public class CacheKey {
  private static final String prefix = "sale:";

  public static String genSessionKey(String uuid) {
    return "session:" + uuid;
  }

  public static String getConfigKey(String key) {
    return prefix + "config:" + key;
  }


  public static String genToken(String token) {
    return "token:" + token;
  }

  public static String getVerifyUser(String username) {
    return "verify:user:" + username;
  }
}
