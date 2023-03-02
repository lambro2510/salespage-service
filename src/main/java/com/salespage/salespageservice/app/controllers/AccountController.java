package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.accountDtos.LoginDto;
import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.responses.JwtResponse;
import com.salespage.salespageservice.domains.services.AccountService;
import com.salespage.salespageservice.domains.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(origins = "https://salepage-fontend-yygsn.appengine.bfcplatform.vn/", allowedHeaders = "*")
@RestController
@RequestMapping("v1/api/account")
public class AccountController extends BaseController {

  @Autowired
  private AccountService accountService;

  @PostMapping("sign-up")
  public ResponseEntity<JwtResponse> signUp(@RequestBody @Valid SignUpDto dto) {
    return accountService.signUp(dto);
  }

  @PostMapping("sign-in")
  public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginDto dto) throws IOException {
    return accountService.signIn(dto);
  }

  @PostMapping("verify-code")
  public ResponseEntity<String> createVerifyCode(Authentication authentication) {
    return accountService.createVerifyCode(getUsername(authentication));
  }

  @PostMapping("verify")
  public ResponseEntity<String> verifyCode(Authentication authentication, @RequestParam Integer code) {
    return accountService.verifyCode(getUsername(authentication), code);
  }
}
