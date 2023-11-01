package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.SearchHistory;
import com.salespage.salespageservice.domains.entities.types.SearchType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SearchHistoryRepository extends MongoRepository<SearchHistory, ObjectId> {
  SearchHistory findByUsernameAndSearchTypeAndSearchData(String username, SearchType searchType, String productName);

  List<SearchHistory> findByUsernameOrderByCreatedAtDesc(String username);

  List<SearchHistory> findTop12ByUsernameOrderByCreatedAtDesc(String username);
}
