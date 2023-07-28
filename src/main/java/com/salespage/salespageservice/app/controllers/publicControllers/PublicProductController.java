package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.controllers.BaseController;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("api/v1/public/product")
@Tag(name = "Product", description = "Thông tin sản phẩm được bán")
public class PublicProductController extends BaseController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<BaseResponse> getAllProduct(
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) String ownerStoreUsername,
            @RequestParam(required = false) Long lte,
            @RequestParam(required = false) Long gte,
            Authentication authentication,
            Pageable pageable) {
        try{
            String sellerUsername = null;
            if (Objects.nonNull(authentication)) {
                if (getUserRoles(authentication).contains(UserRole.SELLER)) {
                    sellerUsername = getUsername(authentication);
                }
            }
            return successApi(productService.getAllProduct(sellerUsername, productId, productName, minPrice, maxPrice, storeName, ownerStoreUsername, lte, gte, pageable));

        }catch (Exception ex){
            return errorApi(ex.getMessage());
        }
        }

    @GetMapping("detail")
    public ResponseEntity<BaseResponse> getProductDetail(Authentication authentication, @RequestParam String productId) throws Exception {
        String username = null;
        if (Objects.nonNull(authentication)) {
            username = getUsername(authentication);
        }
        return successApi(productService.getProductDetail(username, productId));
    }

    @GetMapping("type")
    public ResponseEntity<BaseResponse> getAllActiveProductType() {
        return successApi(productService.getAllActiveProductType());
    }
}
