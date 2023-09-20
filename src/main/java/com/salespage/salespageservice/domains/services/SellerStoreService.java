package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.storeDtos.SellerStoreDto;
import com.salespage.salespageservice.app.dtos.storeDtos.UpdateSellerStoreDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.storeResponse.SellerStoreResponse;
import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.info.AddressResult;
import com.salespage.salespageservice.domains.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SellerStoreService extends BaseService {

  @Autowired
  @Lazy
  private MapService mapService;

  public PageResponse<SellerStoreResponse> getAllSellerStore(String username, Pageable pageable) {
    Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
    Page<SellerStore> sellerStores = sellerStoreStorage.findByOwnerStoreName(username, newPageable);
    List<SellerStore> sellerStoreList = sellerStores.getContent();

    List<SellerStoreResponse> storeDataResponses = new ArrayList<>();
    for (SellerStore sellerStore : sellerStoreList) {
      SellerStoreResponse storeDataResponse = new SellerStoreResponse();
      storeDataResponse.assignFromSellerStore(sellerStore);
      storeDataResponses.add(storeDataResponse);
    }

    Page<SellerStoreResponse> pageStoreDataResponse = new PageImpl<>(storeDataResponses, newPageable, sellerStores.getTotalElements());
    return PageResponse.createFrom(pageStoreDataResponse);
  }


  public PageResponse<SellerStoreResponse> getAllStore(String storeId, String storeName, Pageable pageable) {
    Query query = new Query();
    if (storeId != null) {
      query.addCriteria(Criteria.where("id").is(storeId));
    }
    if (storeName != null) {
      query.addCriteria(Criteria.where("store_name").is(storeName));
    }
    Page<SellerStore> sellerStores = sellerStoreStorage.findAll(pageable);

    Page<SellerStoreResponse> pageStoreDataResponse = new PageImpl<>(sellerStores.stream().map(k -> {
      SellerStoreResponse response = new SellerStoreResponse();
      response.assignFromSellerStore(k);
      return response;
    }).collect(Collectors.toList()), pageable, sellerStores.getTotalElements());
    return PageResponse.createFrom(pageStoreDataResponse);
  }

  public void createStore(String username, SellerStoreDto sellerStoreDto) {
    SellerStore sellerStore = new SellerStore();
    sellerStore.assignFromSellerStoreDto(sellerStoreDto);
    sellerStore.setOwnerStoreName(username);
    setLocationOfStore(sellerStore, sellerStoreDto.getAddress());
    sellerStoreStorage.save(sellerStore);
  }

  public void updateStore(String username, UpdateSellerStoreDto dto) {
    SellerStore sellerStore = sellerStoreStorage.findById(dto.getStoreId());
    if (Objects.isNull(sellerStore)) throw new ResourceNotFoundException("Không tìm thấy cửa hàng này");
    sellerStore.assignFromSellerStoreDto(dto);
    sellerStore.setOwnerStoreName(username);
    setLocationOfStore(sellerStore, dto.getAddress());
    sellerStoreStorage.save(sellerStore);
  }

  public List<SellerStore> findIdsByStoreName(String storeName) {
    return sellerStoreStorage.findIdsByStoreName(storeName);
  }

  public List<SellerStore> findIdsByOwnerStoreName(String username) {
    return sellerStoreStorage.findIdsByOwnerStoreName(username);
  }

  public String uploadImage(String username, String storeId, MultipartFile multipartFile) throws IOException {
    SellerStore sellerStore = sellerStoreStorage.findById(storeId);
    if (Objects.isNull(sellerStore) || !sellerStore.getOwnerStoreName().equals(username))
      throw new AuthorizationException("Không có quyền truy cập");
    String imageUrl = googleDriver.uploadPublicImage(username + "-" + storeId, multipartFile.getName(), Helper.convertMultiPartToFile(multipartFile));
    sellerStore.setImageUrl(imageUrl);
    return imageUrl;
  }

  public SellerStoreResponse getStoreDetail(String storeId) {
    SellerStoreResponse storeDataResponse = new SellerStoreResponse();
    SellerStore sellerStore = sellerStoreStorage.findById(storeId);
    if (Objects.isNull(sellerStore)) throw new ResourceNotFoundException("Không tìm thấy của hàng này");
    storeDataResponse.assignFromSellerStore(sellerStore);
    return storeDataResponse;
  }

  public SellerStoreResponse getStoreDetail(String username, String storeId) {
    SellerStoreResponse sellerStoreResponse = new SellerStoreResponse();
    SellerStore sellerStore = sellerStoreStorage.findById(storeId);
    if (Objects.isNull(sellerStore)) throw new ResourceNotFoundException("Không tìm thấy của hàng này");
    if (!sellerStore.getStoreName().equals(username))
      throw new AuthorizationException("Không có quyền xem thông tin cửa hàng này");
    sellerStoreResponse.assignFromSellerStore(sellerStore);
    return sellerStoreResponse;
  }

  public void deleteStore(String username, String storeId) {
    SellerStore sellerStore = sellerStoreStorage.findById(storeId);
    if (Objects.isNull(sellerStore)) throw new ResourceNotFoundException("Không tìm thấy của hàng này");
    if (!sellerStore.getStoreName().equals(username))
      throw new AuthorizationException();

    sellerStoreStorage.delete(sellerStore);
  }

  public void setLocationOfStore(SellerStore sellerStore, String storeAddress){
    AddressResult address =  suggestAddressByAddress(sellerStore.getAddress());
    if(Objects.equals(address.getResults().get(0).getFormattedAddress(), storeAddress)){
      sellerStore.setStoreName(address.getResults().get(0).getFormattedAddress());
      sellerStore.setLocation(address.getResults().get(0).getGeometry().getLocation().getLat() + "," + address.getResults().get(0).getGeometry().getLocation().getLng());
    }
  }
}
