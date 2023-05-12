package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.FavoriteProduct;
import com.salespage.salespageservice.domains.utils.CacheKey;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FavoriteProductStorage extends BaseStorage {
  public FavoriteProduct findByUsernameAndProductId(String username, String productId) throws Exception {
    FavoriteProduct favoriteProduct = remoteCacheManager.get(CacheKey.getFavoriteProduct(username, productId), FavoriteProduct.class);
    if (Objects.isNull(favoriteProduct)) {
      favoriteProduct = favoriteProductRepository.findByUsernameAndProductId(username, productId);
      remoteCacheManager.set(CacheKey.getFavoriteProduct(username, productId), favoriteProduct, CacheKey.HOUR);
    }
    return favoriteProduct;
  }
}
