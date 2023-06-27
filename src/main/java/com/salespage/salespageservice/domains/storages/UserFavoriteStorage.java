package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.UserFavorite;
import com.salespage.salespageservice.domains.entities.types.FavoriteType;
import com.salespage.salespageservice.domains.utils.CacheKey;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserFavoriteStorage extends BaseStorage {
    public UserFavorite findByUsernameAndRefIdAndFavoriteType(String username, String productId, FavoriteType type) throws Exception {
        UserFavorite userFavorite = remoteCacheManager.get(CacheKey.getUserFavorite(username, productId, type), UserFavorite.class);
        if (Objects.isNull(userFavorite)) {
            userFavorite = userFavoriteRepository.findByUsernameAndRefIdAndFavoriteType(username, productId);
            remoteCacheManager.set(CacheKey.getUserFavorite(username, productId, type), userFavorite, CacheKey.HOUR);
        }
        return userFavorite;
    }

    public void save(UserFavorite userFavorite) {
        userFavoriteRepository.save(userFavorite);
    }
}
