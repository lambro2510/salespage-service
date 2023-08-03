package com.salespage.salespageservice.domains.utils;

import com.salespage.salespageservice.domains.entities.types.FavoriteType;

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

  public static String getUserFavorite(String username, String refId, FavoriteType type) {
    return prefix + "favorite:" + username + ":" + type + ":" + refId;
  }

  public static String getOath2Key(String clientId) {
    return prefix + "key:casso:" + clientId;
  }

  public static String getTpBankToken() {
    return prefix + "tp-bank:token";
  }
}
