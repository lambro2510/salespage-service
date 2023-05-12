package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.storeDtos.SellerStoreDto;
import com.salespage.salespageservice.app.dtos.storeDtos.UpdateSellerStoreDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDataResponse;
import com.salespage.salespageservice.app.responses.storeResponse.StoreDataResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductTypeDetail;
import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.types.ResponseType;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.Helper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SellerStoreService extends BaseService {

  public ResponseEntity<PageResponse<StoreDataResponse>> getAllStore(String username, Pageable pageable) {
    Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
    Page<SellerStore> sellerStores = sellerStoreStorage.findByOwnerStoreName(username, newPageable);
    List<SellerStore> sellerStoreList = sellerStores.getContent();

    List<StoreDataResponse> storeDataResponses = new ArrayList<>();
    for (SellerStore sellerStore : sellerStoreList) {
      StoreDataResponse storeDataResponse = new StoreDataResponse();
      storeDataResponse.assignFromSellerStore(sellerStore);
      storeDataResponses.add(storeDataResponse);
    }

    Page<StoreDataResponse> pageStoreDataResponse = new PageImpl<>(storeDataResponses, newPageable, sellerStores.getTotalElements());
    return ResponseEntity.ok(PageResponse.createFrom(pageStoreDataResponse));
  }

  public ResponseEntity<PageResponse<StoreDataResponse>> getAllStore(String storeId, String storeName, Pageable pageable) {
    Query query = new Query();
    if (storeId != null) {
      query.addCriteria(Criteria.where("id").is(storeId));
    }
    if (storeName != null) {
      query.addCriteria(Criteria.where("store_name").is(storeName));
    }
    Page<SellerStore> sellerStores = sellerStoreStorage.findAll(pageable);
    List<SellerStore> sellerStoreList = sellerStores.getContent();

    List<StoreDataResponse> storeDataResponses = new ArrayList<>();
    for (SellerStore sellerStore : sellerStoreList) {
      StoreDataResponse storeDataResponse = new StoreDataResponse();
      storeDataResponse.assignFromSellerStore(sellerStore);
      List<Product> products = productStorage.findBySellerStoreId(sellerStore.getId().toHexString());
      List<ProductDataResponse> productDataResponses = new ArrayList<>();
      for (Product product : products) {
        ProductDataResponse productDataResponse = new ProductDataResponse();
        productDataResponse.assignFromProduct(product);
        productDataResponse.setStoreName(sellerStore.getStoreName());
        productDataResponses.add(productDataResponse);
        List<ProductTypeDetail> productTypeDetails = productTypeStorage.findByProductId(product.getId().toHexString());
        productDataResponse.setProductType(productTypeDetails.stream().map(ProductTypeDetail::getProductId).collect(Collectors.toList()));
      }
      storeDataResponse.setProductDataResponses(productDataResponses);
      storeDataResponses.add(storeDataResponse);
    }

    Page<StoreDataResponse> pageStoreDataResponse = new PageImpl<>(storeDataResponses, pageable, sellerStores.getTotalElements());
    return ResponseEntity.ok(PageResponse.createFrom(pageStoreDataResponse));
  }

  public ResponseEntity<?> createStore(String username, SellerStoreDto sellerStoreDto) {
    SellerStore sellerStore = new SellerStore();
    sellerStore.assignFromSellerStoreDto(sellerStoreDto);
    sellerStore.setOwnerStoreName(username);
    sellerStoreStorage.save(sellerStore);
    return ResponseEntity.ok(ResponseType.CREATED);
  }

  public ResponseEntity<?> updateStore(String username, UpdateSellerStoreDto dto) {
    SellerStore sellerStore = sellerStoreStorage.findById(dto.getStoreId());
    if (Objects.isNull(sellerStore)) throw new ResourceNotFoundException("Không tìm thấy cửa hàng này");
    sellerStore.assignFromSellerStoreDto(dto);
    sellerStore.setOwnerStoreName(username);
    sellerStoreStorage.save(sellerStore);
    return ResponseEntity.ok(ResponseType.UPDATED);
  }

  public List<SellerStore> findIdsByStoreName(String storeName) {
    return sellerStoreStorage.findIdsByStoreName(storeName);
  }

  public List<SellerStore> findIdsByOwnerStoreName(String username) {
    return sellerStoreStorage.findIdsByOwnerStoreName(username);
  }

  public ResponseEntity<?> uploadImage(String username, String storeId, MultipartFile multipartFile) throws IOException {
    SellerStore sellerStore = sellerStoreStorage.findById(storeId);
    if (Objects.isNull(sellerStore) || !sellerStore.getOwnerStoreName().equals(username))
      throw new AuthorizationException("Không có quyền truy cập");
    String imageUrl = googleDriver.uploadPublicImage(username + "-" + storeId, multipartFile.getName(), Helper.convertMultiPartToFile(multipartFile));
    sellerStore.setImageStoreUrl(imageUrl);
    return ResponseEntity.ok(imageUrl);
  }
}
