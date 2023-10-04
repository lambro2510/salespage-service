package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productTransactionDto.ListTransactionDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionInfoDto;
import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.app.responses.transactionResponse.ProductTransactionResponse;
import com.salespage.salespageservice.domains.entities.*;
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
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
  public void createProductTransaction(String username, List<ProductTransactionDto> dtos) {
    User user = userStorage.findByUsername(username);
    if (Objects.isNull(user)) {
      throw new ResourceNotFoundException("Người dùng không tồn tại");
    }

    for(ProductTransactionDto dto : dtos){
      ProductDetail productDetail = productDetailStorage.findById(dto.getProductDetailId());
      if(Objects.isNull(productDetail)){
        throw new TransactionException("Loại sản phẩm này không còn được bán");
      }
      Product product = productStorage.findProductById(productDetail.getProductId());
      if(Objects.isNull(product)){
        throw new TransactionException("Sản phẩm này không tồn tại");
      }
      if (username.equals(product.getSellerUsername()))
        throw new TransactionException(ErrorCode.NOT_ENOUGH_MONEY, "Bạn không thể mua mặt hàng này");

      SellerStore sellerStore = sellerStoreStorage.findById(dto.getStoreId());
      if (Objects.isNull(sellerStore)) throw new ResourceNotFoundException("Cửa hàng không tồn tại");


      ProductTransaction productTransaction = new ProductTransaction();
      productTransaction.setId(new ObjectId());
      productTransaction.createNewTransaction(username, dto);
      productTransaction.setSellerUsername(product.getSellerUsername());
      productTransaction.setStoreId(sellerStore.getId().toHexString());
      productTransaction.setProduct(product);
      productTransaction.setStore(sellerStore);
      productTransaction.setTotalPrice(productDetail.getSellPrice() * dto.getQuantity());
      if (StringUtils.isNotBlank(dto.getVoucherCode())) {
        VoucherInfo voucherInfo = voucherCodeService.useVoucher(username, dto.getVoucherCode(), productTransaction);
        updateUserBalance(user, voucherInfo.getPriceAfter().longValue());
      }else{
        updateUserBalance(user, productDetail.getSellPrice().longValue());
      }
      productTransactionStorage.save(productTransaction);
    }
    userStorage.save(user);
  }

  public void updateUserBalance(User user, Long amount){
    if (!user.updateBalance(false, amount))
      throw new TransactionException("Tài khoản của bạn không đủ tiền để thành toán. Vui lòng nạp thêm.");
  }
  /*
   *Người dùng chỉnh sửa đơn hàng
   */
  public void updateProductTransaction(String username, ProductTransactionInfoDto dto, String transactionId) {
    ProductTransactionResponse productTransactionResponse = new ProductTransactionResponse();
    ProductTransaction productTransaction = productTransactionStorage.findProductTransactionById(transactionId);
    if (Objects.isNull(productTransaction)) throw new ResourceNotFoundException("Không tìm thấy đơn hàng");
    if(!productTransaction.getBuyerUsername().equals(username)) throw new AuthorizationException();

    if (!productTransaction.getState().equals(ProductTransactionState.WAITING_STORE) && !productTransaction.getState().equals(ProductTransactionState.IN_CART))
      throw new TransactionException("Trạng thái hiện tại không thể cập nhật đơn hàng");

    productTransaction.updateTransaction(dto);
    productTransactionStorage.save(productTransaction);
    productTransactionResponse.partnerFromProductTransaction(productTransaction);

    //TODO Thêm vào kafka xử lý bất đồng bộ
  }


  public void cancelProductTransaction(String username, String transactionId) {
    ProductTransaction productTransaction = productTransactionStorage.findProductTransactionById(transactionId);
    if (!username.equals(productTransaction.getBuyerUsername()))
      throw new TransactionException("Bạn không có quyền hủy đơn hàng này");

    productTransaction.setState(ProductTransactionState.CANCEL);

    productTransactionStorage.save(productTransaction);

  }

  /*
   *Chuyển các đơn hàng có product bị xóa về trạng thái hủy bỏ
   */
  public void productTransactionCancel(String productId) {
    List<ProductTransaction> productTransactions = productTransactionStorage.findAllProductTransactionByProductId(productId);
    if (productTransactions.isEmpty()) return;

    productTransactions.forEach(transaction -> transaction.updateState(ProductTransactionState.CANCEL, "Sản phẩm đã ngừng bán hoặc không tồn tại"));
    productTransactionStorage.saveAll(productTransactions);

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

    Page<ProductTransaction> transactions = productTransactionStorage.findAll(query, pageable);
    Page<ProductTransactionResponse> responses = new PageImpl<>(transactions.getContent().stream().map(ProductTransaction::partnerToProductTransactionResponse).collect(Collectors.toList()), pageable, transactions.getTotalElements());
    return PageResponse.createFrom(responses);
  }

  public void acceptTransactionByStore(String username, String transactionId) {
    ProductTransaction productTransaction = productTransactionStorage.findProductTransactionById(transactionId);
    if(!productTransaction.getSellerUsername().equals(username)) throw new AuthorizationException();
    productTransaction.setState(ProductTransactionState.ACCEPT_STORE);
    productTransactionStorage.save(productTransaction);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
  public void confirmPayment(String username, List<ListTransactionDto> dto) {
    User user = userStorage.findByUsername(username);
    if(Objects.isNull(user)){
      throw new ResourceNotFoundException("Không tồn tại người dùng này");
    }
    List<ProductTransaction> productTransactions = productTransactionStorage.findByIdIn(dto.stream().map(ListTransactionDto::getTransactionId).collect(Collectors.toList()));
    Map<String, ListTransactionDto> tranMap = dto.stream().collect(Collectors.toMap(ListTransactionDto::getTransactionId, Function.identity()));
    if(productTransactions.size() > 10){
      throw new BadRequestException("Vượt quá số lượng sản phẩm trong giỏ hàng, vui long loại bớt sản phẩm");
    }
    Double totalMoney = 0D;
    for(ProductTransaction productTransaction : productTransactions){
      ListTransactionDto tranData = tranMap.get(productTransaction.getId().toHexString());
      if(tranData == null){
        throw new ResourceNotFoundException("Không tìm thấy giao dịch");
      }
      if(StringUtils.isNotBlank(tranData.getVoucherCode())){
        voucherCodeService.useVoucher(username, tranData.getVoucherCode(), productTransaction);
      }
      productTransaction.updateQuantity(tranData.getQuantity());
      voucherCodeService.useVoucher(username, tranData.getVoucherCode(), productTransaction);
      totalMoney += productTransaction.getTotalPrice();
      productTransaction.setState(ProductTransactionState.WAITING_STORE);
    }
    if(totalMoney > user.getBalance().getMoney()){
      throw new TransactionException(ErrorCode.NOT_ENOUGH_MONEY, "Tài khoản của bạn không đủ tiền");
    }
    user.getBalance().minusMoney(totalMoney.longValue());
    userStorage.save(user);
    productTransactionStorage.saveAll(productTransactions);
  }

  public List<ProductTransactionResponse> getTransactionInCart(String username) {
    List<ProductTransaction> productTransactions = productTransactionStorage.findProductTransactionByBuyerUsernameAndState(username, ProductTransactionState.IN_CART);
    return productTransactions.stream().map(ProductTransaction::partnerToProductTransactionResponse).collect(Collectors.toList());
  }

  public void deleteCart(String username, List<String> ids) {
    List<ProductTransaction> productTransactions = productTransactionStorage.findByIdInAndState(ids, ProductTransactionState.IN_CART);
    for(ProductTransaction productTransaction : productTransactions){
      if(!Objects.equals(productTransaction.getBuyerUsername(), username)){
        throw new AuthorizationException();
      }
    }
    productTransactionStorage.deleteAll(productTransactions);
  }

  public void updateQuantityOfTransaction(String username, String id, Long amount) {
    ProductTransaction productTransaction = productTransactionStorage.findProductTransactionById(id);
    if(Objects.isNull(productTransaction)){
      throw new ResourceNotFoundException("Giao dịch không tồn tại");
    }
    ProductTransactionState state = productTransaction.getState();
    if(state != ProductTransactionState.IN_CART && state != ProductTransactionState.WAITING_STORE){
      throw new BadRequestException("Trạng thái giao dịch hiện tại không thể cập nhật");
    }
  }
}
