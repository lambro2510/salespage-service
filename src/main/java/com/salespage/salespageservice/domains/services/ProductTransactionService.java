package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionInfoDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.transactionResponse.ProductTransactionResponse;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.entities.SellerStore;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.exceptions.TransactionException;
import com.salespage.salespageservice.domains.exceptions.info.ErrorCode;
import com.salespage.salespageservice.domains.producer.Producer;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
public class ProductTransactionService extends BaseService {

  @Autowired
  private Producer producer;

  @Autowired
  private VoucherCodeService voucherCodeService;

  public ResponseEntity<PageResponse<ProductTransactionResponse>> getAllTransaction(String username, String sellerUsername, String storeName, Date startDate, Date endDate, Pageable pageable) {
    Query query = new Query();
    query.addCriteria(Criteria.where("purchaser_username").is(username));
    if (sellerUsername != null) {
      query.addCriteria(Criteria.where("seller_username").is(sellerUsername));
    }
    if (storeName != null) {
      query.addCriteria(Criteria.where("store_name").is(storeName));
    }
    if (startDate != null) {
      query.addCriteria(Criteria.where("created_at").gte(startDate));
    }
    if (endDate != null) {
      query.addCriteria(Criteria.where("created_at").lte(endDate));
    }

    Page<ProductTransaction> productTransactions = productTransactionStorage.findAll(query, pageable);
    List<ProductTransactionResponse> productTransactionResponses = new ArrayList<>();
    for (ProductTransaction productTransaction : productTransactions.getContent()) {
      ProductTransactionResponse productTransactionResponse = new ProductTransactionResponse();
      productTransactionResponse.partnerFromProductTransaction(productTransaction);
      productTransactionResponses.add(productTransactionResponse);
    }
    Page<ProductTransactionResponse> pageResponse = new PageImpl<>(productTransactionResponses, pageable, productTransactions.getTotalElements());
    return ResponseEntity.ok(PageResponse.createFrom(pageResponse));
  }

  /*
   * Người dùng tạo 1 đơn hàng
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ResponseEntity<ProductTransactionResponse> createProductTransaction(String username, ProductTransactionDto dto) {
    ProductTransactionResponse productTransactionResponse = new ProductTransactionResponse();
    Product product = productStorage.findProductById(dto.getProductId());
    if (username.equals(product.getSellerUsername()))
      throw new TransactionException(ErrorCode.TRANSACTION_EXCEPTION, "Bạn không thể mua mặt hàng này");
    SellerStore sellerStore = sellerStoreStorage.findById(product.getSellerStoreId());

    ProductTransaction productTransaction = new ProductTransaction();
    productTransaction.setId(new ObjectId());
    productTransaction.createNewTransaction(username, dto);
    productTransaction.setSellerUsername(productTransaction.getSellerUsername());
    productTransaction.setStoreId(sellerStore.getId().toHexString());
    productTransaction.setStoreName(sellerStore.getStoreName());
    productTransaction.setProductName(product.getProductName());
    productTransaction.setPricePerProduct(product.getPrice());
    if (Objects.nonNull(dto.getVoucherCode())) {
      VoucherInfo voucherInfo = voucherCodeService.useVoucher(username, dto.getVoucherCode(), product.getId().toHexString(), product.getPrice().longValue());
      productTransaction.setVoucherInfo(voucherInfo);
    }
    productTransactionResponse.partnerFromProductTransaction(productTransaction);
    productTransactionStorage.save(productTransaction);
    return ResponseEntity.ok(productTransactionResponse);
  }

  /*
   *Người dùng chỉnh sửa đơn hàng
   */
  public ResponseEntity<ProductTransactionResponse> updateProductTransaction(String username, ProductTransactionInfoDto dto, String transactionId) {
    ProductTransactionResponse productTransactionResponse = new ProductTransactionResponse();
    ProductTransaction productTransaction = productTransactionStorage.findProductTransactionByIdInCache(transactionId);

    if (Objects.isNull(productTransaction)) throw new ResourceNotFoundException("Không tìm thấy đơn hàng");

    if (!productTransaction.getState().equals(ProductTransactionState.WAITING) && !productTransaction.getState().equals(ProductTransactionState.CANCEL))
      throw new TransactionException("Trạng thái hiện tại không thể cập nhật đơn hàng");

    productTransaction.updateTransaction(dto);
    productTransactionStorage.save(productTransaction);
    productTransactionResponse.partnerFromProductTransaction(productTransaction);

    //TODO Thêm vào kafka xử lý bất đồng bộ
    return ResponseEntity.ok(productTransactionResponse);
  }


  public ResponseEntity<ProductTransaction> cancelProductTransaction(String username, String transactionId) {
    ProductTransaction productTransaction = productTransactionStorage.findProductTransactionByIdInCache(transactionId);
    if (!username.equals(productTransaction.getPurchaserUsername()))
      throw new TransactionException("Bạn không có quyền hủy đơn hàng này");

    productTransaction.setState(ProductTransactionState.CANCEL);

    productTransactionStorage.save(productTransaction);

    return ResponseEntity.ok(productTransaction);
  }

  /*
   *Chuyển các đơn hàng có product bị xóa về trạng thái hủy bỏ
   */
  public void productTransactionCancel(String productId) {
    List<ProductTransaction> productTransactions = productTransactionStorage.findAllProductTransactionByProductId(productId);
    if (productTransactions.size() == 0) return;

    productTransactions.forEach(transaction -> transaction.updateState(ProductTransactionState.CANCEL, "Sản phẩm đã ngừng bán hoặc không tồn tại"));
    productTransactionStorage.saveAll(productTransactions);

  }


  private long parseToUsd(long money) {

    //TODO Lấy api tỉ lệ hối đoái
    return 0;
  }
}
