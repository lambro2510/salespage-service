package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.entities.types.LogType;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.info.OpenStreetMapResponse;
import com.salespage.salespageservice.domains.storages.*;
import com.salespage.salespageservice.domains.utils.GoogleDriver;
import com.salespage.salespageservice.domains.utils.JwtUtils;
import com.salespage.salespageservice.domains.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Objects;

public class BaseService {
  @Autowired
  protected AccountStorage accountStorage;

  @Autowired
  protected UserStorage userStorage;

  @Autowired
  protected ProductStorage productStorage;

  @Autowired
  protected ProductTransactionStorage productTransactionStorage;

  @Autowired
  protected VoucherStoreStorage voucherStoreStorage;

  @Autowired
  protected VoucherCodeStorage voucherCodeStorage;

  @Autowired
  protected VoucherCodeLimitStorage voucherCodeLimitStorage;

  @Autowired
  protected SellerStoreStorage sellerStoreStorage;

  @Autowired
  protected ProductTypeStorage productTypeStorage;

  @Autowired
  protected UserFavoriteStorage userFavoriteStorage;

  @Autowired
  protected SystemLogStorage systemLogStorage;

  @Autowired
  protected JwtUtils jwtUtils;

  @Autowired
  protected GoogleDriver googleDriver;

  @Autowired
  protected BankTransactionStorage bankTransactionStorage;

  @Autowired
  protected PaymentTransactionStorage paymentTransactionStorage;

  @Autowired
  protected NotificationStorage notificationStorage;

  @Autowired
  protected BankAccountStorage bankAccountStorage;

  @Autowired
  protected TransactionStatisticStorage transactionStatisticStorage;

  @Autowired
  protected CheckInDailyStorage checkInDailyStorage;

  @Autowired
  protected CheckInDailyStatisticStorage checkInDailyStatisticStorage;

  @Autowired
  protected ProductCategoryStorage productCategoryStorage;

  @Autowired
  protected TpBankTransactionStorage tpBankTransactionStorage;

  @Autowired
  protected StatisticCheckpointStorage statisticCheckpointStorage;

  @Autowired
  protected ConfigStorage configStorage;

  @Autowired
  protected ShipperStorage shipperStorage;

  @Autowired
  protected RatingStorage ratingStorage;

  @Autowired
  @Lazy
  private SystemLogService systemLogService;

  protected void writeLog(String message, String trace, LogType logType, String username) {
    systemLogService.createSystemLog(username, message, trace, logType);
  }

  protected boolean hasUserRole(List<UserRole> roles, UserRole role) {
    return roles.contains(role);
  }

  public OpenStreetMapResponse getOpenStreetMap(String lat, String lon, String address){
    StringBuilder url = new StringBuilder("https://nominatim.openstreetmap.org/search.php?");
    if(Objects.nonNull(lat) && Objects.nonNull(lon)){
      url.append("q=").append(lat).append(',').append(lon);
    }else if(Objects.nonNull(address)){
      url.append("q=").append(address);
    }
    url.append("polygon_geojson").append("1").append("format").append("json");
    return RequestUtil.request(HttpMethod.GET, url.toString(), OpenStreetMapResponse.class, null, null);
  }
}
