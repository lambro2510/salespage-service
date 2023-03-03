package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.userDtos.UserInfoDto;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("v1/api/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        return userService.getUserDetail(getUsername(authentication));
    }

    @GetMapping("detail")
    public ResponseEntity<User> getUserDetail(@RequestParam String username) {
        return userService.getUserDetail(username);
    }

    @PutMapping("")
    public ResponseEntity<User> updateUser(Authentication authentication, @RequestBody UserInfoDto dto) {
        return userService.updateUser(getUsername(authentication), dto);
    }

    @PostMapping("uploadImage")
    public ResponseEntity<String> uploadImage(Authentication authentication, @RequestParam("image") MultipartFile image) throws IOException {
        return userService.uploadImage(getUsername(authentication), image);
    }
    
    @PostMapping("voting")
    public ResponseEntity<?> voting(Authentication authentication, @RequestParam String userId, @RequestParam Long point) {
        return userService.voting(getUsername(authentication), userId, point);
    }
}
