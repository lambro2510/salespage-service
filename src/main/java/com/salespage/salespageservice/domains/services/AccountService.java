package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.accountDtos.LoginDto;
import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.responses.JwtResponse;
import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.entities.types.UserState;
import com.salespage.salespageservice.domains.exceptions.AccountNotExistsException;
import com.salespage.salespageservice.domains.exceptions.ResourceExitsException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.info.TokenInfo;
import com.salespage.salespageservice.domains.producer.Producer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Log4j2
public class AccountService extends BaseService {
  @Autowired
  private UserService userService;

  @Autowired
  private Producer producer;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ResponseEntity<JwtResponse> signUp(SignUpDto dto) {

    if (!dto.getConfirmPassword().equals(dto.getPassword())) throw new ResourceExitsException("Invalid password");
    if (accountStorage.existByUsername(dto.getUsername())) throw new ResourceExitsException("User existed");

    Account account = new Account();
    account.createAccount(dto);
    accountStorage.save(account);

    userService.createUser(dto);

    return ResponseEntity.ok(new JwtResponse(account.getUsername(), jwtUtils.generateToken(new TokenInfo(account.getUsername(), account.getRole(), account.getState()))));
  }

  public ResponseEntity<JwtResponse> signIn(LoginDto dto) {
    Account account = accountStorage.findByUsername(dto.getUsername());
    if (account == null || !account.getUsername().equals(dto.getUsername()) || !BCrypt.checkpw(dto.getPassword(), account.getPassword()))
      throw new AccountNotExistsException("Invalid username or password");

    TokenInfo tokenInfo = new TokenInfo(account.getUsername(), account.getRole(), account.getState());
    String token = jwtUtils.generateToken(tokenInfo);
    accountStorage.saveTokenToRemoteCache(token);
    return ResponseEntity.ok(new JwtResponse(account.getUsername(), token));

  }

  public void createVerifyAccountCode(String username) {
    int max = 99999;
    int min = 10000;
    accountStorage.saveVerifyCode(username, Math.random() * (max - min + 1) + min);
  }

  public void verifyCode(String username, Integer code) {
    Integer verifyCode = accountStorage.getVerifyCode(username);

    if (Objects.isNull(verifyCode) || !verifyCode.equals(code))
      throw new ResourceNotFoundException("Invalid verify code");

    Account account = accountStorage.findByUsername(username);
    account.setState(UserState.VERIFIED);
    accountStorage.save(account);
  }
}
