package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.accountDtos.LoginDto;
import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.responses.JwtResponse;
import com.salespage.salespageservice.domains.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("v1/api/account")
public class AccountController {

  @Autowired
  private AccountService accountService;

  @PostMapping("sign-up")
  public ResponseEntity<JwtResponse> signUp(@RequestBody SignUpDto dto) {
    return accountService.signUp(dto);
  }

  @PostMapping("sign-in")
  public ResponseEntity<JwtResponse> login(@RequestBody LoginDto dto) {
    return accountService.signIn(dto);
  }
}
