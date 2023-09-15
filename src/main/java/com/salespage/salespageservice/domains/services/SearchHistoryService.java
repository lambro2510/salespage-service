package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.entities.SearchHistory;
import com.salespage.salespageservice.domains.entities.types.SearchType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SearchHistoryService extends BaseService{

  @Async("threadPoolTaskExecutor")
  public void updateSearchHistory(String username, String productName, String storeName, String sellerUsername) {
    if(Objects.nonNull(productName)){
      SearchHistory searchHistory = searchHistoryStorage.findByUsernameAndSearchTypeAndSearchData(username, SearchType.PRODUCT_NAME, productName);
      searchHistoryStorage.save(searchHistory);
    }
    if(Objects.nonNull(storeName)){
      SearchHistory searchHistory = searchHistoryStorage.findByUsernameAndSearchTypeAndSearchData(username, SearchType.STORE_NAME, storeName);
      searchHistoryStorage.save(searchHistory);
    }
    if(Objects.nonNull(sellerUsername)){
      SearchHistory searchHistory = searchHistoryStorage.findByUsernameAndSearchTypeAndSearchData(username, SearchType.SELLER_USERNAME, sellerUsername);
      searchHistoryStorage.save(searchHistory);
    }
  }
}
