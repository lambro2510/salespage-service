package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.voucherDtos.VoucherStoreDto;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.VoucherStore;
import com.salespage.salespageservice.domains.entities.types.ResponseType;
import com.salespage.salespageservice.domains.entities.types.VoucherStoreType;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.app.responses.voucherResponse.VoucherStoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VoucherStoreService extends BaseService {

  @Autowired
  private VoucherCodeService voucherCodeService;

  public ResponseEntity<?> createVoucherStore(String username, VoucherStoreDto voucherStoreDto) {
    VoucherStore voucherStore = new VoucherStore();
    voucherStore.updatedVoucherStore(voucherStoreDto);
    voucherStore.setCreatedAt(System.currentTimeMillis());
    voucherStore.setCreatedBy(username);
    voucherStoreStorage.save(voucherStore);
    return ResponseEntity.ok(ResponseType.CREATED.name());
  }

  public ResponseEntity<?> updateVoucherStore(String username, VoucherStoreDto voucherStoreDto, String voucherStoreId) {

    VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherStoreId);
    if (voucherStore == null) {
      throw new ResourceNotFoundException("Không tồn tại loại code này");
    }

    if (!Objects.equals(voucherStore.getCreatedBy(), username)) {
      throw new AuthorizationException("Bạn không có quyền chỉnh sửa loại code này");
    }

    voucherStore.updatedVoucherStore(voucherStoreDto);
    voucherStore.setUpdatedAt(System.currentTimeMillis());
    voucherStoreStorage.save(voucherStore);
    return ResponseEntity.ok(ResponseType.UPDATED);
  }

  public ResponseEntity<?> deleteVoucherStore(String username, String voucherStoreId) {
    VoucherStore voucherStore = voucherStoreStorage.findVoucherStoreById(voucherStoreId);
    if (voucherStore == null) {
      throw new ResourceNotFoundException("Không tồn tại loại code này");
    }

    if (!Objects.equals(voucherStore.getCreatedBy(), username)) {
      throw new AuthorizationException("Bạn không có quyền xóa loại code này");
    }

    voucherStoreStorage.deleteVoucherStoreById(voucherStoreId);
    voucherCodeService.deleteAllVoucherCodeInStore();
    return ResponseEntity.ok(ResponseType.DELETED);
  }


  public ResponseEntity<?> getAllVoucherStore(String username) {
    List<VoucherStore> voucherStoreList = voucherStoreStorage.findVoucherStoreByCreatedBy(username);
    //TODO cần phải kiểm tra để lấy tên product của các sản phẩm trong store
    List<Product> products = new ArrayList<>();
    Map<String, String> productMap = new HashMap<>();
    List<VoucherStoreResponse> voucherStoreResponses = new ArrayList<>();
    for (VoucherStore voucherStore : voucherStoreList) {
      VoucherStoreResponse response = new VoucherStoreResponse();
      response.setVoucherStoreName(voucherStore.getVoucherStoreName());
      response.setVoucherStoreStatus(voucherStore.getVoucherStoreStatus());
      response.setVoucherStoreType(voucherStore.getVoucherStoreType());
      response.setTotalQuantity(voucherStore.getVoucherStoreDetail().getQuantity());
      response.setTotalUsed(voucherStore.getVoucherStoreDetail().getQuantityUsed());
      response.setProductId(voucherStore.getProductId());
      response.setProductName(productMap.get(voucherStore.getProductId()));
      response.setVoucherStoreId(voucherStore.getId().toHexString());
      response.setValue(voucherStore.getValue());
      voucherStoreResponses.add(response);
    }
    return ResponseEntity.ok(voucherStoreResponses);
  }

  public void updateQuantityOfVoucherStore(String voucherStoreId, Long totalQuantity, Long totalUsed, String username){
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
    voucherStore.getVoucherStoreDetail().setQuantityUsed(quantity + quantityUsed);
    voucherStoreStorage.save(voucherStore);
  }
}
