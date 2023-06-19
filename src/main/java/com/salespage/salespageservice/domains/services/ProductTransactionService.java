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
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

  public PageResponse<ProductTransactionResponse> getAllTransaction(String username, String sellerUsername, String storeName, Date startDate, Date endDate, Pageable pageable) {
    Query query = new Query();
    query.addCriteria(Criteria.where("buyer_username").is(username));
    if (StringUtils.isNotBlank(sellerUsername)) {
      query.addCriteria(Criteria.where("seller_username").is(sellerUsername));
    }
    if (StringUtils.isNotBlank(storeName)) {
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
    return PageResponse.createFrom(pageResponse);
  }

  /*
   * Người dùng tạo 1 đơn hàng
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ProductTransactionResponse createProductTransaction(String username, ProductTransactionDto dto) {
    ProductTransactionResponse productTransactionResponse = new ProductTransactionResponse();
    Product product = productStorage.findProductById(dto.getProductId());
    if (username.equals(product.getSellerUsername()))
      throw new TransactionException(ErrorCode.TRANSACTION_EXCEPTION, "Bạn không thể mua mặt hàng này");

    SellerStore sellerStore = sellerStoreStorage.findById(product.getSellerStoreId());
    if(Objects.isNull(sellerStore)) throw new ResourceNotFoundException("Cửa hàng không tồn tại");

    User user = userStorage.findByUsername(username);
    if(Objects.isNull(user)) throw new ResourceNotFoundException("Người dùng không tồn tại");
    if(!user.updateBalance(false, product.getPrice().longValue())) throw new ResourceNotFoundException("Tài khoản của bạn không đủ tiền để thành toán mặt hàng này");

    ProductTransaction productTransaction = new ProductTransaction();
    productTransaction.setId(new ObjectId());
    productTransaction.createNewTransaction(username, dto);
    productTransaction.setSellerUsername(product.getSellerUsername());
    productTransaction.setStoreId(sellerStore.getId().toHexString());
    productTransaction.setStoreName(sellerStore.getStoreName());
    productTransaction.setProductName(product.getProductName());
    productTransaction.setPricePerProduct(product.getPrice());
    if (Objects.nonNull(dto.getVoucherCode())) {
      VoucherInfo voucherInfo = voucherCodeService.useVoucher(username, dto.getVoucherCode(), product.getId().toHexString(), product.getPrice().longValue());
      productTransaction.setVoucherInfo(voucherInfo);
      productTransaction.setIsUseVoucher(true);
    }
    productTransactionResponse.partnerFromProductTransaction(productTransaction);
    productTransactionStorage.save(productTransaction);
    return productTransactionResponse;
  }

  /*
   *Người dùng chỉnh sửa đơn hàng
   */
  public ProductTransactionResponse updateProductTransaction(String username, ProductTransactionInfoDto dto, String transactionId) {
    ProductTransactionResponse productTransactionResponse = new ProductTransactionResponse();
    ProductTransaction productTransaction = productTransactionStorage.findProductTransactionByIdInCache(transactionId);

    if (Objects.isNull(productTransaction)) throw new ResourceNotFoundException("Không tìm thấy đơn hàng");

    if (!productTransaction.getState().equals(ProductTransactionState.WAITING) && !productTransaction.getState().equals(ProductTransactionState.CANCEL))
      throw new TransactionException("Trạng thái hiện tại không thể cập nhật đơn hàng");

    productTransaction.updateTransaction(dto);
    productTransactionStorage.save(productTransaction);
    productTransactionResponse.partnerFromProductTransaction(productTransaction);

    //TODO Thêm vào kafka xử lý bất đồng bộ
    return productTransactionResponse;
  }


  public ProductTransaction cancelProductTransaction(String username, String transactionId) {
    ProductTransaction productTransaction = productTransactionStorage.findProductTransactionByIdInCache(transactionId);
    if (!username.equals(productTransaction.getBuyerUsername()))
      throw new TransactionException("Bạn không có quyền hủy đơn hàng này");

    productTransaction.setState(ProductTransactionState.CANCEL);

    productTransactionStorage.save(productTransaction);

    return productTransaction;
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

}
