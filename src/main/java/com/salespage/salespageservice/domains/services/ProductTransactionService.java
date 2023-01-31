package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionInfoDto;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.exceptions.TransactionException;
import com.salespage.salespageservice.domains.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class ProductTransactionService extends BaseService {

    @Autowired
    private Producer producer;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<ProductTransaction> createProductTransaction(String username, ProductTransactionDto dto) {
        User user = userStorage.findByUsername(username);
        ProductTransaction productTransaction = new ProductTransaction();
        productTransaction.createNewTransaction(username, dto);

        productTransactionStorage.save(productTransaction);

        //TODO Thêm vào kafka xử lý bất đồng bộ
        return ResponseEntity.ok(productTransaction);
    }

    public ResponseEntity<ProductTransaction> updateProductTransaction(String username, ProductTransactionInfoDto dto) {
        ProductTransaction productTransaction = productTransactionStorage.findProductTransactionByIdInCache(dto.getTransactionId());

        if (Objects.isNull(productTransaction)) throw new ResourceNotFoundException("Không tìm thấy đơn hàng");

        if (!productTransaction.getState().equals(ProductTransactionState.WAITING) && !productTransaction.getState().equals(ProductTransactionState.CANCEL))
            throw new TransactionException("Trạng thái hiện tại không thể cập nhật đơn hàng");

        productTransaction.updateTransaction(dto);
        productTransactionStorage.save(productTransaction);

        //TODO Thêm vào kafka xử lý bất đồng bộ
        return ResponseEntity.ok(productTransaction);
    }


    public ResponseEntity<ProductTransaction> cancelProductTransaction(String username, String transactionId) {
        ProductTransaction productTransaction = productTransactionStorage.findProductTransactionByIdInCache(transactionId);
        if (!username.equals(productTransaction.getPurchaserUsername()))
            throw new TransactionException("Bạn không có quyền hủy đơn hàng này");

        productTransaction.setState(ProductTransactionState.CANCEL);

        productTransactionStorage.save(productTransaction);

        return ResponseEntity.ok(productTransaction);
    }

    public void productTransactionCancel(String productId){
        List<ProductTransaction> productTransactions = productStorage.findAllProductById(productId);
        if(productTransactions.size() == 0) return ;

        productTransactions.forEach(transaction -> transaction.updateState(ProductTransactionState.CANCEL));
        productStorage.saveAll(productTransactions);

    }



    private long parseToUsd(long money) {

        //TODO Lấy api tỉ lệ hối đoái
        return 0;
    }
}
