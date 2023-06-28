package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.UserFavoriteDtos.UserFavoriteDto;
import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.entities.types.FavoriteType;
import com.salespage.salespageservice.domains.services.UserFavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/favorite")
public class FavoriteProductController extends BaseController {

    @Autowired
    UserFavoriteService userFavoriteService;

    @PostMapping("")
    @Operation(summary = "Yêu thích sản phẩm, cửa hàng, người bản", description = "Yêu thích sản phẩm hoặc cửa hàng, người bán")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thêm vào danh sách yêu thích thành công thành công"),
            @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "401", description = "Không được phép"),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
    })
    public ResponseEntity<BaseResponse> createUpdateUserFavorite(Authentication authentication, @RequestBody UserFavoriteDto dto) {
        try {
            userFavoriteService.createAndUpdateUserFavorite(getUsername(authentication), dto.getRefId(), dto.getFavoriteType(), dto.getStar(), dto.getIsLike());
            return successApi("Thêm vào danh sách yêu thích thành công");
        } catch (Exception ex) {
            return errorApi(ex.getMessage());
        }
    }

    @GetMapping("")
    @Operation(summary = "Danh sách yêu thích", description = "Yêu thích sản phẩm hoặc cửa hàng, người bán")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "400", description = "Đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "401", description = "Không được phép"),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
    })
    public ResponseEntity<BaseResponse> getUpdateUserFavorite(Authentication authentication, @RequestParam FavoriteType favoriteType) {
        try {
            return successApi(userFavoriteService.getListFavorite(getUsername(authentication),favoriteType));
        } catch (Exception ex) {
            return errorApi(ex.getMessage());
        }
    }
}
