package com.salespage.salespageservice.app.responses.BankResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BankListData {

    @Schema(description = "Bank ID", example = "21")
    Long id;

    @Schema(description = "Bank name", example = "Ngân hàng TMCP Quân đội")
    String name;

    @Schema(description = "Bank code", example = "MB")
    String code;

    @Schema(description = "Bank BIN", example = "970422")
    String bin;

    @Schema(description = "Bank short name", example = "MBBank")
    String shortName;

    @Schema(description = "Bank logo URL", example = "https://api.vietqr.io/img/MB.png")
    String logo;

    @Schema(description = "Transfer supported", example = "1")
    Long transferSupported;

    @Schema(description = "Lookup supported", example = "1")
    Long lookupSupported;

    @Schema(description = "Bank short name", example = "MBBank")
    @JsonProperty("short_name")
    String shortname;

    @Schema(description = "Support level", example = "3")
    Long support;

    @Schema(description = "Is transfer supported", example = "1")
    Long isTransfer;

    @Schema(description = "Bank SWIFT code", example = "MSCBVNVX")
    @JsonProperty("swift_code")
    String swiftCode;
}
