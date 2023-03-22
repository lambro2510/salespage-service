package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.userDtos.UserInfoDto;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("v1/api/user")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User API", description = "API quản lý thông tin và hồ sơ người dùng")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("profile")
    @Operation(summary = "Lấy thông tin hồ sơ người dùng", description = "Lấy thông tin hồ sơ cho người dùng đã xác thực")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    public ResponseEntity<User> getProfile(Authentication authentication) {
        return userService.getUserDetail(getUsername(authentication));
    }

    @GetMapping("detail")
    @Operation(summary = "Lấy thông tin chi tiết người dùng", description = "Lấy thông tin chi tiết cho một người dùng cụ thể theo tên đăng nhập")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    public ResponseEntity<User> getUserDetail(@Parameter(description = "Tên đăng nhập của người dùng cần lấy thông tin") @RequestParam String username) {
        return userService.getUserDetail(username);
    }

    @PutMapping("")
    @Operation(summary = "Cập nhật thông tin người dùng", description = "Cập nhật thông tin hồ sơ cho người dùng đã xác thực")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    public ResponseEntity<User> updateUser(Authentication authentication, @RequestBody @Schema(description = "Thông tin người dùng cần cập nhật") UserInfoDto dto) {
        return userService.updateUser(getUsername(authentication), dto);

    }

    @PostMapping("uploadImage")
    @Operation(summary = "Upload user profile image", description = "Upload a new profile image for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image upload successful"),
            @ApiResponse(responseCode = "400", description = "Invalid file format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> uploadImage(Authentication authentication, @Parameter(description = "Image file to upload") @RequestParam("image") MultipartFile image) throws IOException {
        return userService.uploadImage(getUsername(authentication), image);
    }

    @PostMapping("voting")
    @Operation(summary = "Vote for a user", description = "Vote for another user by their user ID and the number of points to give")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote successful"),
            @ApiResponse(responseCode = "400", description = "Invalid user ID or points"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> voting(Authentication authentication, @Parameter(description = "ID of the user to vote for") @RequestParam String userId, @Parameter(description = "Number of points to give") @RequestParam Long point) {
        return userService.voting(getUsername(authentication), userId, point);
    }

}
