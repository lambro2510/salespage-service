package com.salespage.salespageservice.app.consumers;

import com.salespage.salespageservice.domains.entities.PaymentTransaction;
import com.salespage.salespageservice.domains.entities.types.NotificationMessage;
import com.salespage.salespageservice.domains.entities.types.NotificationType;
import com.salespage.salespageservice.domains.entities.types.PaymentType;
import com.salespage.salespageservice.domains.producer.Producer;
import com.salespage.salespageservice.domains.producer.TopicConfig;
import com.salespage.salespageservice.domains.services.BankService;
import com.salespage.salespageservice.domains.services.NotificationService;
import com.salespage.salespageservice.domains.utils.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Log4j2
public class ProductTransactionConsumer extends BankService {

    @Autowired
    private Producer producer;

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = TopicConfig.SALE_PAGE_PAYMENT_TRANSACTION)
    public void createPayment(String message) {
        log.debug("====> createPayment: {} " + message);
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        try {
            paymentTransaction = JsonParser.entity(message, PaymentTransaction.class);
            if (Objects.nonNull(paymentTransaction)) paymentTransactionStorage.save(paymentTransaction);
            if (paymentTransaction.getType().equals(PaymentType.IN)) {
                notificationService.createNotification(paymentTransaction.getUsername(), NotificationMessage.PAYMENT_IN_SUCCESS.getTittle(), NotificationMessage.PAYMENT_IN_SUCCESS.getMessage(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
            } else {
                notificationService.createNotification(paymentTransaction.getUsername(), NotificationMessage.PAYMENT_OUT_SUCCESS.getTittle(), NotificationMessage.PAYMENT_OUT_SUCCESS.getMessage(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
            }
        } catch (Exception e) {
            log.error("====> createPayment error: {} " + paymentTransaction);
//      producer.createPaymentTransaction(paymentTransaction);
        }
    }

    @KafkaListener(topics = "bizfly-7-453-RewardGift", groupId = "bizfly-7-453-RewardGift")
    public void receiveMessage(String message) {
        System.out.println("Received message from bizfly-7-453-RewardGift topic: " + message);
        // Xử lý tin nhắn nhận được từ Kafka
    }
}
