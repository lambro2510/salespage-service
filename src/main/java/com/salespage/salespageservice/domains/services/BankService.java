package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.PaymentDtos.CreatePaymentDto;
import com.salespage.salespageservice.app.dtos.bankDtos.BankAccountInfoRequest;
import com.salespage.salespageservice.app.dtos.bankDtos.BankDto;
import com.salespage.salespageservice.app.dtos.bankDtos.GenQrCodeDto;
import com.salespage.salespageservice.app.dtos.bankDtos.TransactionData;
import com.salespage.salespageservice.app.responses.BankResponse.BankAccountData;
import com.salespage.salespageservice.app.responses.BankResponse.BankListData;
import com.salespage.salespageservice.app.responses.BankResponse.QrData;
import com.salespage.salespageservice.app.responses.BankResponse.VietQrResponse;
import com.salespage.salespageservice.app.responses.transactionResponse.PaymentTransactionResponse;
import com.salespage.salespageservice.domains.entities.*;
import com.salespage.salespageservice.domains.entities.status.BankStatus;
import com.salespage.salespageservice.domains.entities.status.PaymentStatus;
import com.salespage.salespageservice.domains.entities.types.NotificationMessage;
import com.salespage.salespageservice.domains.entities.types.NotificationType;
import com.salespage.salespageservice.domains.exceptions.ResourceExitsException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.info.TpBankTransactionData;
import com.salespage.salespageservice.domains.producer.Producer;
import com.salespage.salespageservice.domains.utils.DateUtils;
import com.salespage.salespageservice.domains.utils.Helper;
import com.salespage.salespageservice.domains.utils.JsonParser;
import com.salespage.salespageservice.domains.utils.RequestUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
public class BankService extends BaseService {
    @Value("${casso.bank-acc-id}")
    private String BANKACCID;

    @Value("${casso.apikey}")
    private String APIKEY;

    @Value("${casso.url}")
    private String URL;

    @Value("${vietqr.api.client-id}")
    private String VIETQRCLIENTID;

    @Value("${vietqr.api.apikey}")
    private String VIETQRAPIKEY;

    @Value("${vietqr.api.url}")
    private String VIETQRURL;

    @Value("${tp-bank.account-no}")
    private String TPBANKACCOUNTNO;

    @Value("${tp-bank.api.url}")
    private String TPBANKURL;
    @Autowired
    Producer producer;

    @Autowired
    NotificationService notificationService;

    public void receiveBankTransaction(BankDto bankDto) {
        List<BankTransaction> bankTransactions = new ArrayList<>();
        for (TransactionData data : bankDto.getData()) {
            BankTransaction bankTransaction = new BankTransaction();
            bankTransaction.partnerFromTransactionData(data);
            bankTransactions.add(bankTransaction);
        }
        if (!bankTransactions.isEmpty()) {
            bankTransactionStorage.saveAll(bankTransactions);
        }
    }

    public List<BankTransaction> getAllTransaction() {
        return bankTransactionStorage.findAll();
    }

    public QrData genTransactionQr(String username, String paymentId) throws IOException {
        PaymentTransaction paymentTransaction = paymentTransactionStorage.findByIdAndUsername(paymentId, username);
        if (Objects.isNull(paymentTransaction)) throw new ResourceNotFoundException("Không tìm thấy giao dịch");
        GenQrCodeDto genQrCodeDto = new GenQrCodeDto();
        genQrCodeDto.setAccountNo(Long.parseLong(BANKACCID));
        genQrCodeDto.setAmount(paymentTransaction.getAmount());
        genQrCodeDto.setFormat("text");
        genQrCodeDto.setTemplate("LDP0k8f");
        genQrCodeDto.setAcqId(970422L);
        genQrCodeDto.setAccountName("Thanh toán mua hàng");
        genQrCodeDto.setAddInfo(username + paymentTransaction.getAmount());
        Map<String, String> header = new HashMap<>();
        VietQrResponse response = RequestUtil.request(HttpMethod.POST, VIETQRURL + "/v2/generate", VietQrResponse.class, genQrCodeDto, header);
        return JsonParser.entity(JsonParser.toJson(response.getData()), QrData.class);
    }

    public void asyncTransaction() {
        JSONObject json = new JSONObject();
        json.put("bank_acc_id", BANKACCID);
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Apikey " + APIKEY);
        String response = RequestUtil.request(HttpMethod.POST, URL + "/v2/sync", json, header).toString();
        log.info(response);
    }

    public String createPayment(String username, CreatePaymentDto dto) {
        BankAccount bankAccount = bankAccountStorage.findBankAccountById(dto.getBankAccountId());
        if (Objects.isNull(bankAccount)) throw new ResourceNotFoundException("Không tìm thấy ngân hàng liên kết này");
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        ObjectId id = new ObjectId();
        paymentTransaction.setId(id);
        paymentTransaction.setUsername(username);
        paymentTransaction.setPaymentStatus(PaymentStatus.WAITING);
        paymentTransaction.setBankAccountId(dto.getBankAccountId());
        paymentTransaction.setAmount(dto.getAmount());
        producer.createPaymentTransaction(paymentTransaction);
//    paymentTransactionStorage.save(paymentTransaction);
        return id.toHexString();
    }

    @Transactional(noRollbackFor = {ResourceNotFoundException.class})
    public String confirmPayment(String username, String paymentId) throws Exception {
        try {
            User user = userStorage.findByUsername(username);
            if (Objects.isNull(user)) throw new ResourceNotFoundException("Không tồn tại người dùng này");
            PaymentTransaction paymentTransaction = paymentTransactionStorage.findByIdAndUsernameAndPaymentStatus(paymentId, username, PaymentStatus.WAITING);
            if (Objects.isNull(paymentTransaction))
                throw new ResourceNotFoundException("Giao dịch không tồn tại hoặc đã được thanh toán");
            else {
                BankTransaction bankTransaction = bankTransactionStorage.findByDescription(Helper.genDescription(username, paymentId));
                if (Objects.isNull(bankTransaction)) return "Giao dịch đang được xử lý";
                if (user.updateBalance(true, bankTransaction.getAmount())) {

                    paymentTransaction.setPaymentStatus(PaymentStatus.RESOLVE);
                    String message;
                    if (bankTransaction.getAmount() >= 0) {
                        message = "Tài khoản của bạn được cộng " + bankTransaction.getAmount();
                        notificationService.createNotification(username, NotificationMessage.CHANGE_STATUS_PAYMENT_RESOLVE_IN.getTittle(), NotificationMessage.CHANGE_STATUS_PAYMENT_RESOLVE_IN.getMessage(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
                    } else {
                        message = "Tài khoản của bạn bị trừ " + Math.abs(bankTransaction.getAmount());
                        notificationService.createNotification(username, NotificationMessage.CHANGE_STATUS_PAYMENT_RESOLVE_OUT.getTittle(), NotificationMessage.CHANGE_STATUS_PAYMENT_RESOLVE_OUT.getMessage(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
                    }
                    paymentTransaction.setDescription(message);
                    paymentTransactionStorage.save(paymentTransaction);
                    userStorage.save(user);
                } else {
                    paymentTransaction.setPaymentStatus(PaymentStatus.CANCEL);
                    paymentTransaction.setDescription("Giao dịch đã bị hủy bỏ do tài khoản của bạn không đủ tiền");
                    notificationService.createNotification(username, NotificationMessage.CHANGE_STATUS_PAYMENT_RESOLVE_OUT_ERR.getTittle(), NotificationMessage.CHANGE_STATUS_PAYMENT_RESOLVE_OUT_ERR.getMessage(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
                    paymentTransactionStorage.save(paymentTransaction);
                }
            }
        } catch (ResourceNotFoundException ex) {
            return ex.getMessage();
        }

        return "Xử lý giao dịch thành công";
    }

    public List<BankListData> getListBank() throws IOException {
        VietQrResponse response = RequestUtil.request(HttpMethod.GET, VIETQRURL + "/v2/banks", VietQrResponse.class, null, new HashMap<>());
        if (Objects.isNull(response))
            throw new ResourceNotFoundException("Lỗi hệ thống, không lấy được danh sách ngân hàng");
        if (!Objects.equals(response.getCode(), "00")) throw new ResourceNotFoundException(response.getDesc());
        log.info(response);
        return JsonParser.arrayList(JsonParser.toJson(response.getData()), BankListData.class);
    }

    public BankAccountData getBankAccountData(String bin, String accountNo) throws IOException {
        Map<String, String> header = new HashMap<>();
        header.put("x-client-id", VIETQRCLIENTID);
        header.put("x-api-key", VIETQRAPIKEY);
        BankAccountInfoRequest request = new BankAccountInfoRequest();
        request.setBin(bin);
        request.setAccountNumber(accountNo);
        VietQrResponse response = RequestUtil.request(HttpMethod.POST, VIETQRURL + "/v2/lookup", VietQrResponse.class, request, header);
        if (Objects.isNull(response))
            throw new ResourceNotFoundException("Lỗi hệ thống, không lấy được thông tin tài khoản ngân hàng");
        if (!Objects.equals(response.getCode(), "00")) throw new ResourceNotFoundException(response.getDesc());
        log.info("----getBankAccountData: " + response);
        return JsonParser.entity(JsonParser.toJson(response.getData()), BankAccountData.class);
    }

    @Transactional
    public void cancelPayment(String username, String paymentId) {
        PaymentTransaction paymentTransaction = paymentTransactionStorage.findByIdAndUsername(paymentId, username);
        if (Objects.isNull(paymentTransaction)) throw new ResourceNotFoundException("Không tìm thấy giao dịch");
        if (paymentTransaction.getPaymentStatus().equals(PaymentStatus.RESOLVE))
            throw new ResourceExitsException("Giao dịch đã hoàn thành không thể hủy bỏ");
        paymentTransaction.setPaymentStatus(PaymentStatus.CANCEL);
        notificationService.createNotification(username, "Hủy bỏ giao dịch", "Bạn đã hủy bỏ giao dịch với số giao dịch là: " + paymentId + ". Thông tin giao dịch: " + paymentTransaction.getDescription(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
        paymentTransactionStorage.save(paymentTransaction);
    }

    @Transactional(noRollbackFor = {ResourceNotFoundException.class})
    public void checkNotResolveTransaction() throws Exception {
        List<PaymentTransaction> paymentTransactions = paymentTransactionStorage.findByPaymentStatus(PaymentStatus.WAITING);
        log.info("----checkNotResolveTransaction----: " + paymentTransactions.size() + " paymentTransactions chua duoc xu ly");
        for (PaymentTransaction paymentTransaction : paymentTransactions) {
            if (paymentTransaction.createdOneDayPeriod()) {
                paymentTransaction.setPaymentStatus(PaymentStatus.PENDING);
                paymentTransaction.setDescription("Giao dịch không được xử lý");
                notificationService.createNotification(paymentTransaction.getUsername(), NotificationMessage.CHANGE_STATUS_PAYMENT_PENDING.getTittle(), NotificationMessage.CHANGE_STATUS_PAYMENT_PENDING.getMessage(), NotificationType.PAYMENT_TRANSACTION, paymentTransaction.getId().toHexString());
                paymentTransactionStorage.save(paymentTransaction);
            }
            confirmPayment(paymentTransaction.getUsername(), paymentTransaction.getId().toHexString());
        }
    }

    public void linkBankAccount(String username, BankAccountInfoRequest request) throws Exception {
        List<BankListData> bankListData = getListBank();
        Map<String, BankListData> bankMap = bankListData.stream()
                .collect(Collectors.toMap(BankListData::getBin, Function.identity()));

        BankListData bankData = bankMap.get(request.getBin());
        if (Objects.isNull(bankData)) throw new ResourceNotFoundException("Ngân hàng không được hỗ trợ");

        BankAccount bankAccount = bankAccountStorage.findByBankIdAndAccountNo(bankData.getId(), request.getAccountNumber());
        if (Objects.nonNull(bankAccount)) throw new ResourceExitsException("Tài khoản đã được liên kết");
        else bankAccount = new BankAccount();

        BankAccountData bankAccountData = getBankAccountData(request.getBin(), request.getAccountNumber());
        if (Objects.isNull(bankAccountData)) throw new Exception("Tài khoản ngân hàng không hợp lệ");

        bankAccount.setAccountNo(request.getAccountNumber());
        bankAccount.setBankName(bankData.getShortName());
        bankAccount.setBankFullName(bankData.getName());
        bankAccount.setUsername(username);
        bankAccount.setBankId(bankData.getId());
        bankAccount.setBankLogoUrl(bankData.getLogo());
        bankAccount.setBankAccountName(bankAccountData.getAccountName());
        bankAccount.setStatus(BankStatus.ACTIVE);
        bankAccount.setMoneyIn(0D);
        bankAccount.setMoneyOut(0D);
        bankAccountStorage.save(bankAccount);
    }

    public List<PaymentTransactionResponse> getPayment(String username) {
        List<PaymentTransactionResponse> responses = new ArrayList<>();
        List<PaymentTransaction> paymentTransactions = paymentTransactionStorage.findByUsername(username);
        List<String> bankAccountIds = paymentTransactions.stream().map(PaymentTransaction::getBankAccountId).collect(Collectors.toList());
        Map<String, BankAccount> bankAccountMap = bankAccountStorage.findBankAccountByIdIn(bankAccountIds).stream().collect(Collectors.toMap(BankAccount::getIdStr, Function.identity()));
        for (PaymentTransaction paymentTransaction : paymentTransactions) {
            PaymentTransactionResponse paymentTransactionResponse = paymentTransaction.partnerToPaymentTransactionResponse();
            BankAccount bankAccount = bankAccountMap.get(paymentTransaction.getBankAccountId());
            if (Objects.isNull(bankAccount)) continue;
            paymentTransactionResponse.setBankAccountName(bankAccount.getBankAccountName());
            paymentTransactionResponse.setBankName(bankAccount.getBankName());
            paymentTransactionResponse.setBankAccountNo(bankAccount.getAccountNo());
            responses.add(paymentTransactionResponse);
        }
        return responses;
    }

    public TpBankTransactionData getBankTransaction(String fromDate, String toDate) throws Exception {
        String token = bankAccountStorage.getTokenFromRemoteCache();
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + token);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountNo", TPBANKACCOUNTNO);
        jsonObject.put("currency", "VND");
        jsonObject.put("fromDate", fromDate);
        jsonObject.put("keyword", "");
        jsonObject.put("toDate", toDate);
        return RequestUtil.request(HttpMethod.POST,
            TPBANKURL + "/api/smart-search-presentation-service/v1/account-transactions/find",
            TpBankTransactionData.class,
            jsonObject,
            header
            );
    }

    public void saveTpBankTransactionToday() throws Exception {
        LocalDate now = LocalDate.now();
        String fromDate = DateUtils.convertLocalDateToString(now.minusDays(1), "yyyyMMdd");
        String toDate = DateUtils.convertLocalDateToString(now, "yyyyMMdd");
        TpBankTransactionData tpBankTransactionData = getBankTransaction(fromDate, toDate);
        for(TpBankTransactionData.TpBankTransactionInfo info : tpBankTransactionData.getTransactionInfos()){
            TpBankTransaction tpBankTransaction = tpBankTransactionStorage.findByTransId(info.getId());
            if(Objects.isNull(tpBankTransaction)){
                tpBankTransaction = new TpBankTransaction();
                tpBankTransaction.fromTpBankTransactionInfo(info);
            }
            tpBankTransactionStorage.save(tpBankTransaction);
        }
    }

    public void saveTpBankTransactionPeriodDay() throws Exception {
        LocalDate now = LocalDate.now();
        String fromDate = DateUtils.convertLocalDateToString(now.minusDays(2), "yyyyMMdd");
        String toDate = DateUtils.convertLocalDateToString(now.minusDays(1), "yyyyMMdd");
        TpBankTransactionData tpBankTransactionData = getBankTransaction(fromDate, toDate);
        for(TpBankTransactionData.TpBankTransactionInfo info : tpBankTransactionData.getTransactionInfos()){
            TpBankTransaction tpBankTransaction = tpBankTransactionStorage.findByTransId(info.getId());
            if(Objects.isNull(tpBankTransaction)){
                tpBankTransaction = new TpBankTransaction();
                tpBankTransaction.fromTpBankTransactionInfo(info);
            }
            tpBankTransactionStorage.save(tpBankTransaction);
        }
    }
}
