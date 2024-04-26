package com.salespage.salespageservice.app.responses.BankResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MbBankTransaction {

  @Schema(description = "Trạng thái giao dịch thành công hay không", example = "true")
  private boolean success;

  @Schema(description = "Thông điệp về kết quả giao dịch", example = "Giao dịch thành công")
  private String message;

  @Schema(description = "Danh sách các giao dịch ngân hàng")
  private List<Transaction> data;

  @Data
  public static class Transaction {

    @Schema(description = "Ngày ghi sổ giao dịch", example = "2023-08-07 10:30:00")
    private String postingDate;

    @Schema(description = "Mô tả bổ sung", example = "Thanh toan QR NAP12848 - Ma giao di ch/ Trace 707690 ")
    private String addDescription;

    @Schema(description = "Mô tả bổ sung", example = "Thanh toan QR NAP12848 - Ma giao di ch/ Trace 707690 ")
    private String tracingType;

    @Schema(description = "pos", example = "Thanh toan QR NAP12848 - Ma giao di ch/ Trace 707690 ")
    private String pos;

    @Schema(description = "Ngày thực hiện giao dịch", example = "2023-08-07 09:45:00")
    private String transactionDate;

    @Schema(description = "Ngày đáo hạn (nếu có)", example = "2023-09-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    @Schema(description = "Số tài khoản ngân hàng", example = "1234567890")
    private String accountNo;

    @Schema(description = "Số tiền ghi có", example = "5000.0")
    private Double creditAmount;

    @Schema(description = "Số tiền ghi nợ", example = "3000.0")
    private Double debitAmount;

    @Schema(description = "Loại tiền tệ", example = "VND")
    private String currency;

    @Schema(description = "Mô tả giao dịch", example = "Giao dịch mua hàng")
    private String description;

    @Schema(description = "Số dư khả dụng", example = "8000.0")
    private Double availableBalance;

    @Schema(description = "Số tài khoản người thụ hưởng", example = "12345678901234")
    private String beneficiaryAccount;

    @Schema(description = "Số tham chiếu", example = "REF12345")
    private String refNo;

    @Schema(description = "Tên người thụ hưởng", example = "Nguyễn Văn B")
    private String benAccountName;

    @Schema(description = "Tên ngân hàng", example = "XYZ Bank")
    private String bankName;

    @Schema(description = "Số tài khoản người thụ hưởng", example = "5678901234")
    private String benAccountNo;

    @Schema(description = "Loại giao dịch", example = "CHUYENTIEN")
    private String transactionType;

    @Schema(description = "ID tài liệu giao dịch", example = "DOC123")
    private String docId;
  }
}
