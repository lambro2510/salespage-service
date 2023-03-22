package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.accountDtos.LoginDto;
import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.responses.JwtResponse;
import com.salespage.salespageservice.domains.services.AccountService;
import com.salespage.salespageservice.domains.services.BaseService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("v1/api/account")
public class AccountController extends BaseController {

  @Autowired
  private AccountService accountService;

  @PostMapping("sign-up")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully created an account"),
      @ApiResponse(responseCode = "400", description = "Invalid input, please check the request body"),
      @ApiResponse(responseCode = "409", description = "Account with this email/username already exists"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<JwtResponse> signUp(@RequestBody @Valid SignUpDto dto) {
    return accountService.signUp(dto);
  }

  @PostMapping("sign-in")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully authenticated the account"),
      @ApiResponse(responseCode = "400", description = "Invalid input, please check the request body"),
      @ApiResponse(responseCode = "401", description = "Unauthorized, please check your credentials"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginDto dto) throws IOException {
    return accountService.signIn(dto);
  }

  @PostMapping("verify-code")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully created a verification code"),
      @ApiResponse(responseCode = "401", description = "Unauthorized, please check your authentication token"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<String> createVerifyCode(Authentication authentication) {
    return accountService.createVerifyCode(getUsername(authentication));
  }

  @PostMapping("verify")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully verified the verification code"),
      @ApiResponse(responseCode = "401", description = "Unauthorized, please check your authentication token"),
      @ApiResponse(responseCode = "400", description = "Invalid input, please check the request parameters"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<String> verifyCode(Authentication authentication, @RequestParam Integer code) {
    return accountService.verifyCode(getUsername(authentication), code);
  }

}