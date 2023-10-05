package com.salespage.salespageservice.domains.storages;


import com.salespage.salespageservice.domains.entities.ProductStatistic;
import com.salespage.salespageservice.domains.entities.SearchHistory;
import com.salespage.salespageservice.domains.repositories.*;
import com.salespage.salespageservice.domains.utils.RemoteCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class BaseStorage {

  @Autowired
  protected AccountRepository accountRepository;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected ProductTransactionRepository productTransactionRepository;

  @Autowired
  protected ProductRepository productRepository;

  @Autowired
  protected VoucherStoreRepository voucherStoreRepository;

  @Autowired
  protected VoucherCodeRepository voucherCodeRepository;

  @Autowired
  protected SellerStoreRepository sellerStoreRepository;

  @Autowired
  protected SystemLogRepository systemLogRepository;

  @Autowired
  protected VoucherCodeLimitRepository voucherCodeLimitRepository;

  @Autowired
  protected ProductTypeRepository productTypeRepository;

  @Autowired
  protected ProductTypeDetailRepository productTypeDetailRepository;

  @Autowired
  protected UserFavoriteRepository userFavoriteRepository;

  @Autowired
  protected BankTransactionRepository bankTransactionRepository;

  @Autowired
  protected PaymentTransactionRepository paymentTransactionRepository;

  @Autowired
  protected BankAccountRepository bankAccountRepository;

  @Autowired
  protected NotificationRepository notificationRepository;

  @Autowired
  protected TransactionStatisticRepository transactionStatisticRepository;

  @Autowired
  protected CheckInDailyRepository checkInDailyRepository;

  @Autowired
  protected CheckInDailyStatisticRepository checkInDailyStatisticRepository;

  @Autowired
  protected ProductCategoryRepository productCategoryRepository;

  @Autowired
  protected TpBankTransactionRepository tpBankTransactionRepository;

  @Autowired
  protected RatingRepository ratingRepository;

  @Autowired
  protected StatisticCheckpointRepository statisticCheckpointRepository;

  @Autowired
  protected ConfigRepository configRepository;

  @Autowired
  protected ShipperRepository shipperRepository;

  @Autowired
  protected ProductStatisticRepository productStatisticRepository;

  @Autowired
  protected OtpRepository otpRepository;

  @Autowired
  protected SearchHistoryRepository searchHistoryRepository;

  @Autowired
  protected CartRepository cartRepository;

  @Autowired
  protected ProductDetailRepository productDetailRepository;

  @Autowired
  protected ProductComboDetailRepository productComboDetailRepository;

  @Autowired
  protected ProductComboRepository productComboRepository;

  @Autowired
  protected ProductTransactionDetailRepository productTransactionDetailRepository;

  @Autowired
  protected MongoTemplate mongoTemplate;

  @Autowired
  protected RemoteCacheManager remoteCacheManager;
}
