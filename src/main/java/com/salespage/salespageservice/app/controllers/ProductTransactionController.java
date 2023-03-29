package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionInfoDto;
import com.salespage.salespageservice.app.responses.transactionResponse.ProductTransactionResponse;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.services.ProductTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin
@RestController
@RequestMapping("v1/api/product-transaction")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Quản lý giao dịch của người dùng", description = "Quản lý các giao dịch mua bán của người dùng")
public class ProductTransactionController extends BaseController {

    @Autowired
    private ProductTransactionService productTransactionService;

    @PostMapping("")
    @Operation(summary = "Tạo giao dịch sản phẩm", description = "Tạo một giao dịch sản phẩm mới với thông tin đã cho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Giao dịch sản phẩm đã được tạo"),
            @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "401", description = "Không được phép"),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
    })
    public ResponseEntity<ProductTransactionResponse> createProductTransaction(
            Authentication authentication,
            @RequestBody @Valid ProductTransactionDto dto) {
        return productTransactionService.createProductTransaction(getUsername(authentication), dto);
    }

    @PutMapping("")
    @Operation(summary = "Cập nhật giao dịch sản phẩm", description = "Cập nhật giao dịch sản phẩm với thông tin đã cho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật giao dịch sản phẩm thành công"),
            @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "401", description = "Không được phép"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy giao dịch sản phẩm"),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
    })
    public ResponseEntity<ProductTransactionResponse> updateProductTransaction(Authentication authentication, @RequestParam String transactionId, @RequestBody ProductTransactionInfoDto dto) {
        return productTransactionService.updateProductTransaction(getUsername(authentication), dto,transactionId);
    }

    @PutMapping("cancel")
    @Operation(summary = "Hủy bỏ giao dịch sản phẩm", description = "Hủy bỏ giao dịch sản phẩm với mã giao dịch tương ứng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Giao dịch sản phẩm đã được hủy bỏ"),
            @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "401", description = "Không được phép"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy giao dịch sản phẩm"),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
    })
    public ResponseEntity<ProductTransaction> cancelProductTransaction(Authentication authentication, @RequestParam String transactionId) {
        return productTransactionService.cancelProductTransaction(getUsername(authentication), transactionId);
    }

}
