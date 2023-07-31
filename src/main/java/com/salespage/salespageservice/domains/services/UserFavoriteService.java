package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.entities.UserFavorite;
import com.salespage.salespageservice.domains.entities.types.FavoriteType;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserFavoriteService extends BaseService {
    public void createAndUpdateUserFavorite(String username, String refId, FavoriteType type, Boolean isLike) throws Exception {
        User user = userStorage.findByUsername(username);
        if (Objects.isNull(user)) throw new ResourceNotFoundException("Không tồn tại người dùng này");
        UserFavorite userFavorite = userFavoriteStorage.findByUsernameAndRefIdAndFavoriteType(username, refId, type);
        if (Objects.isNull(userFavorite)) userFavorite = new UserFavorite();

        if (type.equals(FavoriteType.PRODUCT)) {
            if (!productStorage.isExistByProductId(refId))
                throw new ResourceNotFoundException("Không tồn tại sản phẩm này");
        }

        if (type.equals(FavoriteType.STORE)) {
            if (!sellerStoreStorage.isExistByStoreId(refId))
                throw new ResourceNotFoundException("Không tồn tại cửa hàng này");
        }

        if (type.equals(FavoriteType.SELLER)) {
            User favoriteUser = userStorage.findUserById(refId);
            if (Objects.isNull(favoriteUser)) throw new ResourceNotFoundException("Không tồn tại người bán này này");
            if (!accountStorage.existByUsernameAndRole(favoriteUser.getUsername(), UserRole.SELLER))
                throw new ResourceNotFoundException("Người dùng không hợp lệ");
        }

        if (type.equals(FavoriteType.SHIPPER)) {
            User favoriteUser = userStorage.findUserById(refId);
            if (Objects.isNull(favoriteUser)) throw new ResourceNotFoundException("Không tồn tại người bán này này");
            if (!accountStorage.existByUsernameAndRole(favoriteUser.getUsername(), UserRole.SHIPPER))
                throw new ResourceNotFoundException("Người dùng không hợp lệ");
        }
        userFavorite.setUsername(username);
        userFavorite.setRefId(refId);
        userFavorite.setLike(isLike);
        userFavorite.setFavoriteType(type);
        userFavoriteStorage.save(userFavorite);
    }

    public List<UserFavorite> getListFavorite(String username, FavoriteType favoriteType) {
        return userFavoriteStorage.findByUsernameAndFavoriteType(username, favoriteType);
    }
}
