package com.salespage.salespageservice.domains.utils;

public class CacheKey {
  public static final int HOUR = 3600;
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

  public static String getNumberProduct() {
    return prefix + "count:product";
  }

  public static String getFavoriteProduct(String username, String productId) {
    return prefix + "favorite:product:" + username + ":" + productId;
  }
}
