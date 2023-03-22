package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.userDtos.UserInfoDto;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "User API", description = "API for managing user profiles and information")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("profile")
    @Operation(summary = "Get user profile", description = "Get the profile information for the authenticated user")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        return userService.getUserDetail(getUsername(authentication));
    }

    @GetMapping("detail")
    @Operation(summary = "Get user detail", description = "Get the detailed information for a specific user by username")
    public ResponseEntity<User> getUserDetail(@Parameter(description = "Username of the user to retrieve") @RequestParam String username) {
        return userService.getUserDetail(username);
    }

    @PutMapping("")
    @Operation(summary = "Update user information", description = "Update the profile information for the authenticated user")
    public ResponseEntity<User> updateUser(Authentication authentication, @RequestBody @Schema(description = "User information to update") UserInfoDto dto) {
        return userService.updateUser(getUsername(authentication), dto);
    }

    @PostMapping("uploadImage")
    @Operation(summary = "Upload user profile image", description = "Upload a new profile image for the authenticated user")
    public ResponseEntity<String> uploadImage(Authentication authentication, @Parameter(description = "Image file to upload") @RequestParam("image") MultipartFile image) throws IOException {
        return userService.uploadImage(getUsername(authentication), image);
    }

    @PostMapping("voting")
    @Operation(summary = "Vote for a user", description = "Vote for another user by their user ID and the number of points to give")
    public ResponseEntity<?> voting(Authentication authentication, @Parameter(description = "ID of the user to vote for") @RequestParam String userId, @Parameter(description = "Number of points to give") @RequestParam Long point) {
        return userService.voting(getUsername(authentication), userId, point);
    }
}
