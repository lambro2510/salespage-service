package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionDto;
import com.salespage.salespageservice.app.dtos.productTransactionDto.ProductTransactionInfoDto;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.services.ProductTransactionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin
@RestController
@RequestMapping("v1/api/product-transaction")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Product Transaction API", description = "APIs for managing product transactions")
public class ProductTransactionController extends BaseController {

    @Autowired
    private ProductTransactionService productTransactionService;

    @PostMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product transaction created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductTransaction> createProductTransaction(Authentication authentication, @RequestBody ProductTransactionDto dto) {
        return productTransactionService.createProductTransaction(getUsername(authentication), dto);
    }

    @PutMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product transaction updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Product transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductTransaction> updateProductTransaction(Authentication authentication, @RequestBody ProductTransactionInfoDto dto) {
        return productTransactionService.updateProductTransaction(getUsername(authentication), dto);
    }

    @PutMapping("cancel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product transaction cancelled"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Product transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductTransaction> cancelProductTransaction(Authentication authentication, @RequestParam String transactionId) {
        return productTransactionService.cancelProductTransaction(getUsername(authentication), transactionId);
    }

}
