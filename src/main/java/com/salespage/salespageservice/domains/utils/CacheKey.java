package com.salespage.salespageservice.domains.utils;

public class CacheKey {
  private static final String prefix = "sale:";

  public static String genSessionKey(String username) {
    return "session:" + username;
  }

  public static String getConfigKey(String key) {
    return prefix + "config:" + key;
  }


  public static String genToken(String token) {
    return "token:" + token;
  }

  public static String getVerifyUser(String username) {
    return prefix + "verify:user:" + username;
  }

  public static String listProduct(int pageIndex) {
    return prefix + "products" + pageIndex;

  }

  public static String getUserToken(String username) {
    return prefix + "token:" + username;
  }
}
