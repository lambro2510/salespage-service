package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.voucherDtos.CreateVoucherStoreDto;
import com.salespage.salespageservice.app.dtos.voucherDtos.UpdateVoucherStoreDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.voucherResponse.UserVoucherResponse;
import com.salespage.salespageservice.app.responses.voucherResponse.VoucherStoreResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.VoucherCodeLimit;
import com.salespage.salespageservice.domains.entities.VoucherStore;
import com.salespage.salespageservice.domains.entities.types.ResponseType;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VoucherStoreService extends BaseService {

  @Autowired
  private VoucherCodeService voucherCodeService;

  public void createVoucherStore(String username, CreateVoucherStoreDto createVoucherStoreDto) {
    VoucherStore voucherStore = new VoucherStore();
    voucherStore.updatedVoucherStore(createVoucherStoreDto);
    voucherStore.setRefId(createVoucherStoreDto.getRefId());
    voucherStore.setDiscountType(createVoucherStoreDto.getDiscountType());
    voucherStore.setCreatedBy(username);
    voucherStoreStorage.save(voucherStore);
  }

  public ResponseEntity<?> updateVoucherStore(String username, UpdateVoucherStoreDto updateVoucherStoreDto, String voucherStoreId) {

    VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherStoreId);
    if (voucherStore == null) {
      throw new ResourceNotFoundException("Không tồn tại loại code này");
    }

    if (!Objects.equals(voucherStore.getCreatedBy(), username)) {
      throw new AuthorizationException("Bạn không có quyền chỉnh sửa loại code này");
    }

    voucherStore.updatedVoucherStore(updateVoucherStoreDto);
    voucherStore.setUpdatedAt(DateUtils.nowInMillis());
    voucherStoreStorage.save(voucherStore);
    return ResponseEntity.ok(ResponseType.UPDATED);
  }

  public void deleteVoucherStore(String username, String voucherStoreId) {
    VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherStoreId);
    if (voucherStore == null) {
      throw new ResourceNotFoundException("Không tồn tại loại code này");
    }

    if (!Objects.equals(voucherStore.getCreatedBy(), username)) {
      throw new AuthorizationException("Bạn không có quyền xóa loại code này");
    }

    voucherStoreStorage.deleteVoucherStoreById(voucherStoreId);
    voucherCodeService.deleteAllVoucherCodeInStore();
  }


  public PageResponse<VoucherStoreResponse> getAllVoucherStore(String username, Pageable pageable) {
    Page<VoucherStore> voucherStores = voucherStoreStorage.findVoucherStoreByCreatedBy(username, pageable);
    List<String> productIds = voucherStores.getContent()
        .stream().filter(k -> k.getVoucherStoreType() == VoucherStoreType.PRODUCT).map(VoucherStore::getRefId).collect(Collectors.toList());
    List<Product> products = productStorage.findByIdIn(productIds);
    Map<String, Product> productMap = products.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));

    List<String> storeIds = voucherStores.getContent()
        .stream().filter(k -> k.getVoucherStoreType() == VoucherStoreType.STORE).map(VoucherStore::getRefId).collect(Collectors.toList());
    List<SellerStore> stores = sellerStoreStorage.findByIdIn(storeIds);
    Map<String, SellerStore> storeMap = stores.stream().collect(Collectors.toMap(k -> k.getId().toHexString(), Function.identity()));
    List<VoucherStoreResponse> voucherStoreResponses = new ArrayList<>();
    for (VoucherStore voucherStore : voucherStores.getContent()) {
      VoucherStoreResponse response = new VoucherStoreResponse();
      response.setVoucherStoreName(voucherStore.getVoucherStoreName());
      response.setVoucherStoreStatus(voucherStore.getVoucherStoreStatus());
      response.setVoucherStoreType(voucherStore.getVoucherStoreType());
      response.setTotalQuantity(voucherStore.getVoucherStoreDetail().getQuantity());
      response.setTotalUsed(voucherStore.getVoucherStoreDetail().getQuantityUsed());
      response.setRefId(voucherStore.getRefId());
      response.setDiscountType(voucherStore.getDiscountType());
      response.setVoucherStoreId(voucherStore.getId().toHexString());
      response.setValue(voucherStore.getValue());
      if (response.getVoucherStoreType() == VoucherStoreType.STORE) {
        SellerStore sellerStore = storeMap.get(response.getRefId());
        if (sellerStore != null) {
          response.setName(sellerStore.getStoreName());
        }
      } else if (response.getVoucherStoreType() == VoucherStoreType.PRODUCT) {
        Product product = productMap.get(response.getRefId());
        if (product != null) {
          response.setName(product.getProductName());
        }
      }

      voucherStoreResponses.add(response);
    }
    return PageResponse.createFrom(new PageImpl<>(voucherStoreResponses, pageable, voucherStores.getTotalElements()));
  }

  public void updateQuantityOfVoucherStore(String voucherStoreId, Long totalQuantity, Long totalUsed, String username) {
    VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherStoreId);
    if (voucherStore == null) {
      throw new ResourceNotFoundException("Không tồn tại loại code này");
    }

    if (!Objects.equals(voucherStore.getCreatedBy(), username)) {
      throw new AuthorizationException("Bạn không có quyền xóa loại code này");
    }
    Long quantity = voucherStore.getVoucherStoreDetail().getQuantity();
    Long quantityUsed = voucherStore.getVoucherStoreDetail().getQuantityUsed();

    voucherStore.getVoucherStoreDetail().setQuantity(quantity + totalQuantity);
    voucherStore.getVoucherStoreDetail().setQuantityUsed(quantityUsed + totalUsed);
    voucherStoreStorage.save(voucherStore);
  }

  public VoucherStoreResponse getVoucherStoreDetail(String username, String id) {
    VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(id);
    if (voucherStore == null) {
      throw new ResourceNotFoundException("Không tồn tại kho voucher này");
    }
    if (!Objects.equals(voucherStore.getCreatedBy(), username)) {
      throw new AuthorizationException();
    }

    VoucherStoreResponse response = modelMapper.toVoucherStoreResponse(voucherStore);
    response.setTotalQuantity(voucherStore.getVoucherStoreDetail().getQuantity());
    response.setTotalUsed(voucherStore.getVoucherStoreDetail().getQuantityUsed());
    response.setRefId(voucherStore.getRefId());
    response.setDiscountType(voucherStore.getDiscountType());
    response.setVoucherStoreId(voucherStore.getId().toHexString());
    response.setValue(voucherStore.getValue());
    return response;
  }

  public List<UserVoucherResponse> getVoucherInProduct(String username, String productId) {
    List<UserVoucherResponse> responses = new ArrayList<>();
    Product product = productStorage.findProductById(productId);
    if (Objects.isNull(product)) throw new ResourceNotFoundException("Không tìm thấy sản phâm");
    List<VoucherStore> voucherStores = voucherStoreStorage.findByVoucherStoreTypeAndRefId(VoucherStoreType.PRODUCT, productId);
    for (String storeId : product.getSellerStoreIds()) {
      voucherStores.addAll(voucherStoreStorage.findByVoucherStoreTypeAndRefId(VoucherStoreType.STORE, storeId));
    }
    Map<String, VoucherCodeLimit> voucherCodeLimitMap = new HashMap<>();
    if (username != null) {
      List<String> voucherStoreIds = voucherStores.stream().map(k -> k.getId().toHexString()).collect(Collectors.toList());
      List<VoucherCodeLimit> voucherCodeLimit = voucherCodeLimitStorage.findByUsernameAndVoucherStoreIdIn(username, voucherStoreIds);
      voucherCodeLimitMap = voucherCodeLimit.stream().collect(Collectors.toMap(VoucherCodeLimit::getVoucherStoreId, Function.identity()));
    }
    for (VoucherStore voucherStore : voucherStores) {
      boolean isLimit = false;
      VoucherCodeLimit voucherCodeLimit = voucherCodeLimitMap.get(voucherStore.getId().toHexString());
      if (voucherCodeLimit != null) {
        isLimit = voucherCodeLimit.getNumberReceiveVoucher() > voucherStore.getVoucherStoreDetail().getMaxVoucherPerUser();
      }
      responses.add(UserVoucherResponse
          .builder()
          .voucherCodeId(null)
          .voucherStoreId(voucherStore.getId().toHexString())
          .voucherStoreName(voucherStore.getVoucherStoreName())
          .voucherCode(null)
          .discountType(voucherStore.getDiscountType())
          .storeType(voucherStore.getVoucherStoreType())
          .dayToExpireTime(null)
          .minPrice(voucherStore.getVoucherStoreDetail().getMinAblePrice())
          .maxPrice(voucherStore.getVoucherStoreDetail().getMaxAblePrice())
          .isLimited(isLimit)
          .value(voucherStore.getValue())
          .build());
    }
    return responses;
  }
}
