package com.salespage.salespageservice.domains.utils;

import com.salespage.salespageservice.domains.entities.types.ActiveState;
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

  public static String genSellerStoreId(String storeId) {
    return prefix + ":store:storeId:" + storeId;
  }

  public static String genSellerStoreName(String storeName) {
    return prefix + ":store:storeName:" + storeName;
  }

  public static String genProductDetail(String detailId) {
    return prefix + ":product:productDetail:detailId:" + detailId;
  }

  public static String genProductDetailByProductId(String productId) {
    return prefix + ":product:productDetail:detailId:" + productId;
  }

  public static String genProductByProductId(String productId) {
    return prefix + ":product:productId:" + productId;
  }

  public static String genProductCategoryById(String id) {
    return prefix + ":category:categoryId:" + id;
  }

  public static String genVoucherCodeById(String voucherCodeId) {
    return prefix + ":voucher:code:voucherCodeId:" + voucherCodeId;
  }

  public static String genVoucherStoreById(String voucherStoreId) {
    return prefix + ":voucher:store:voucherStoreId:" + voucherStoreId;
  }

  public static String genProductComboByIdAndState(String comboId, ActiveState activeState) {
    return prefix + ":product:combo:id:" + comboId + ":state:" + activeState;
  }

  public static String genProductComboByIds(List<String> comboIds) {
    return prefix + ":product:combo:id:" + StringUtils.join(comboIds, ",");
  }

  public static String genHotProduct() {
    return prefix + ":product:hot:";
  }

  public static String genProductDetailByIdIn(List<String> ids) {
    return prefix + ":product:detail:ids:" + StringUtils.join(ids, ',');
  }

  public static String genProductByIdIn(List<String> ids) {
    return prefix + ":product:ids:" + StringUtils.join(ids, ',');
  }

  public static String genProductCategoryByIdIn(List<String> ids) {
    return prefix + ":product:category:ids:" + StringUtils.join(ids, ',');
  }

  public static String genSellerStoreByIdIn(List<String> ids) {
    return prefix + ":product:store:ids:" + StringUtils.join(ids, ',');
  }

  public static String genVoucherCodeByIdInAndUsername(List<String> ids, String username) {
    return prefix + ":product:store:username:" + username + ":ids" + StringUtils.join(ids, ',');
  }

  public static String genProductDetailByProductIdIn(List<String> ids) {
    return prefix + ":product:detail:productIds:" + StringUtils.join(ids, ',');
  }

  public static String genListUserOfProductDetail(String productDetailId, Long start, Long end) {
    return prefix + ":statistic:pd:id:" + productDetailId + ":s:" + start + ":e:" + end ;
  }
}
