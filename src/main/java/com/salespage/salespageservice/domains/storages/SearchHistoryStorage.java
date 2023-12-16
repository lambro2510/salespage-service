package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.SearchHistory;
import com.salespage.salespageservice.domains.entities.types.SearchType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchHistoryStorage extends BaseStorage {
  public SearchHistory findByUsernameAndSearchTypeAndSearchData(String username, SearchType searchType, String productName) {
    return searchHistoryRepository.findByUsernameAndSearchTypeAndSearchData(username, searchType, productName);
  }

  public void save(SearchHistory searchHistory) {
    searchHistoryRepository.save(searchHistory);

  }

  public List<SearchHistory> findByUsernameOrderByCreatedAtDesc(String username) {
    return searchHistoryRepository.findByUsernameOrderByCreatedAtDesc(username);
  }

  public List<SearchHistory> findTop12ByUsernameOrderByCreatedAtDesc(String username) {
    return searchHistoryRepository.findTop12ByUsernameOrderByCreatedAtDesc(username);

  }
}
