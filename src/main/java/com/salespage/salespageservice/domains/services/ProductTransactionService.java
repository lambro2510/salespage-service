package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productTransactionDto.ListTransactionDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionInfoDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.transactionResponse.ProductTransactionDetailResponse;
import com.salespage.salespageservice.app.responses.transactionResponse.ProductTransactionResponse;
import com.salespage.salespageservice.domains.entities.*;
import com.salespage.salespageservice.domains.entities.infor.ComboInfo;
import com.salespage.salespageservice.domains.entities.infor.VoucherInfo;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class ProductTransactionService extends BaseService {

  @Autowired
  private Producer producer;

  @Autowired
  private VoucherCodeService voucherCodeService;

  @Autowired
  private ProductComboService productComboService;

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
    List<String> tranIds = productTransactions.getContent().stream().map(k -> k.getId().toHexString()).collect(Collectors.toList());
    List<ProductTransactionDetail> transactionDetails = productTransactionDetailStorage.findByTransactionIdIn(tranIds);
    Map<String, List<ProductTransactionDetail>> tranDetailsMap = transactionDetails.stream().collect(Collectors.groupingBy(ProductTransactionDetail::getTransactionId));

    List<ProductTransactionResponse> productTransactionResponses = new ArrayList<>();
    for(ProductTransaction productTransaction : productTransactions){
      ProductTransactionResponse transactionResponse = new ProductTransactionResponse();
      transactionResponse.partnerFromProductTransaction(productTransaction);
      List<ProductTransactionDetail> details = tranDetailsMap.get(productTransaction.getId().toHexString());
      List<ProductTransactionDetailResponse> detailResponse = modelMapper.ProductTransactionDetailResponse(details);
      transactionResponse.setDetails(detailResponse);
      productTransactionResponses.add(transactionResponse);

    }
    Page<ProductTransactionResponse> pageResponse = new PageImpl<>(productTransactionResponses, pageable, productTransactions.getTotalElements());
    return PageResponse.createFrom(pageResponse);
  }

  public void updateUserBalance(User user, Long amount){
    if (!user.updateBalance(false, amount))
      throw new TransactionException("Tài khoản của bạn không đủ tiền để thành toán. Vui lòng nạp thêm.");
  }


  public PageResponse<ProductTransactionResponse> getAllTransactionByUser(String username, String productId, String productName, String buyerName, String sellerStoreId, String sellerStoreName, ProductTransactionState state, Long lte, Long gte, Pageable pageable) {
    Query query = new Query();
    query.addCriteria(Criteria.where("seller_username").is(username));
    if (StringUtils.isNotBlank(productId)) {
      query.addCriteria(Criteria.where("_id").is(new ObjectId(productId)));
    }
    if (StringUtils.isNotBlank(productName)) {
      query.addCriteria(Criteria.where("product.product_name").is(productName));
    }
    if (StringUtils.isNotBlank(buyerName)) {
      query.addCriteria(Criteria.where("buyer_username").is(buyerName));
    }
    if (Objects.nonNull(state)) {
      query.addCriteria(Criteria.where("state").is(state));
    }
    if (StringUtils.isNotBlank(sellerStoreId)) {
      query.addCriteria(Criteria.where("store_id").is(new ObjectId(sellerStoreId)));
    }
    if (StringUtils.isNotBlank(sellerStoreName)) {
      query.addCriteria(Criteria.where("store.storeName").is(sellerStoreName));
    }
    if (Objects.nonNull(gte) && Objects.nonNull(lte)) {
      Criteria andCriteria = new Criteria().andOperator(
          Criteria.where("created_at").gte(gte),
          Criteria.where("created_at").lte(lte)
      );
      query.addCriteria(andCriteria);
    } else if (Objects.nonNull(gte)) {
      query.addCriteria(Criteria.where("created_at").gte(gte));
    } else if (Objects.nonNull(lte)) {
      query.addCriteria(Criteria.where("created_at").lte(lte));
    }

    return null;
  }



  public ProductTransaction buildProductTransaction(ObjectId id, User user, String note, ComboInfo comboInfo, List<ProductTransactionDetail> transactionDetails){
    return ProductTransaction.builder()
        .id(id)
        .buyerUsername(user.getUsername())
        .balance(user.getBalance().getMoney())
        .note(note)
        .comboInfo(comboInfo)
        .totalPrice(comboInfo.getSellPrice())
        .productTransactionDetails(transactionDetails)
        .build();
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void saveTransaction(ProductTransaction productTransaction, List<ProductTransactionDetail> transactionDetails){
    productTransactionStorage.save(productTransaction);
    productTransactionDetailStorage.saveAll(transactionDetails);
  }

  public ProductTransactionDetail buildProductTransactionDetail(String transactionId, ProductDetail productDetail, VoucherInfo voucher, String address, Long quantity, SellerStore store, String note) {
    return ProductTransactionDetail.builder()
        .transactionId(transactionId)
        .address(address)
        .quantity(quantity)
        .store(store)
        .storeId(store.getId().toHexString())
        .state(ProductTransactionState.WAITING_STORE)
        .note(note)
        .voucherInfo(voucher)
        .productDetail(productDetail)
        .totalPrice(voucher.getPriceAfter())
        .build();
  }
}
