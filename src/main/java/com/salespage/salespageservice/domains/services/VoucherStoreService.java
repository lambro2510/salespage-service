package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.voucherDtos.CreateVoucherStoreDto;
import com.salespage.salespageservice.app.dtos.voucherDtos.UpdateVoucherStoreDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.voucherResponse.VoucherStoreResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.VoucherStore;
import com.salespage.salespageservice.domains.entities.types.ResponseType;
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
    //TODO cần phải kiểm tra để lấy tên product của các sản phẩm trong store
    List<Product> products = new ArrayList<>();
    Map<String, String> productMap = new HashMap<>();
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
    voucherStore.getVoucherStoreDetail().setQuantityUsed(quantity + quantityUsed);
    voucherStoreStorage.save(voucherStore);
  }
}
