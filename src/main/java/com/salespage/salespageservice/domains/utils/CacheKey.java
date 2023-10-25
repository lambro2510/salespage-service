package com.salespage.salespageservice.domains.utils;

import com.salespage.salespageservice.domains.entities.types.FavoriteType;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
    return prefix + "tp-bank:token:";
  }

  public static String genProductComboDetailProductId(String productId) {
    return prefix + ":product:combo:detail:productId:" + productId;
  }

  public static String genProductComboDetailComboId(String comboId) {
    return prefix + ":product:combo:detail:comboId:" + comboId;
  }

  public static String genProductComboDetailProductIdIn(List<String> ids) {
    return prefix + ":product:combo:detail:productIds:" + StringUtils.join(ids, ',');
  }

  public static String genListCartByUsername(String username) {
    return prefix + ":cart:username:" + username;
  }
}
