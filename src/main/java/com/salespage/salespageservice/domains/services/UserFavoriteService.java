package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.FavoriteResponse.FavoriteResponse;
import com.salespage.salespageservice.domains.entities.*;
import com.salespage.salespageservice.domains.entities.types.FavoriteType;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

  public List<FavoriteResponse> getListFavorite(String username, FavoriteType favoriteType) {
    List<UserFavorite> favorites = userFavoriteStorage.findByUsernameAndFavoriteType(username, favoriteType);
    List<String> refIds = favorites.stream().map(UserFavorite::getRefId).collect(Collectors.toList());
    List<FavoriteResponse> responses = new ArrayList<>();
    if(favoriteType.equals(FavoriteType.STORE)){
      List<SellerStore> sellerStores = sellerStoreStorage.findSellerStoreByIdIn(refIds);
      for(SellerStore sellerStore : sellerStores){
        FavoriteResponse response = new FavoriteResponse(favoriteType, sellerStore.getId().toHexString(), sellerStore.getStoreName());
        responses.add(response);
      }
    }else if(favoriteType.equals(FavoriteType.PRODUCT)){
      List<Product> products = productStorage.findByIdIn(refIds);
      for(Product product : products) {
        FavoriteResponse response = new FavoriteResponse(favoriteType, product.getId().toHexString(), product.getProductName());
        responses.add(response);
      }
    }else if(favoriteType.equals(FavoriteType.SELLER)){
      List<User> users = userStorage.findByIdIn(refIds);
      for(User user : users) {
        FavoriteResponse response = new FavoriteResponse(favoriteType, user.getId().toHexString(), user.getUsername());
        responses.add(response);
      }
    }else if(favoriteType.equals(FavoriteType.SHIPPER)){
      List<Shipper> shippers = shipperStorage.findByIdIn(refIds);
      for(Shipper shipper : shippers) {
        FavoriteResponse response = new FavoriteResponse(favoriteType, shipper.getId().toHexString(), shipper.getUsername());
        responses.add(response);
      }
    }
    return responses;
  }
}
