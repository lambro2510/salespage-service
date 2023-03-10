package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.accountDtos.LoginDto;
import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.responses.JwtResponse;
import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.entities.types.UserState;
import com.salespage.salespageservice.domains.exceptions.AccountNotExistsException;
import com.salespage.salespageservice.domains.exceptions.ResourceExitsException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.info.TokenInfo;
import com.salespage.salespageservice.domains.producer.Producer;
import com.salespage.salespageservice.domains.utils.EmailRequest;
import com.salespage.salespageservice.domains.utils.GoogleDriver;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Objects;

@Service
@Log4j2
public class AccountService extends BaseService {
  @Autowired
  private UserService userService;

  @Autowired
  private Producer producer;

  @Autowired
  private GoogleDriver googleDriver;

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

  public ResponseEntity<JwtResponse> signIn(LoginDto dto) throws IOException {

    Account account = accountStorage.findByUsername(dto.getUsername());
    if (account == null || !account.getUsername().equals(dto.getUsername()) || !BCrypt.checkpw(dto.getPassword(), account.getPassword()))
      throw new AccountNotExistsException("Invalid username or password");

    TokenInfo tokenInfo = new TokenInfo(account.getUsername(), account.getRole(), account.getState());
    String token = jwtUtils.generateToken(tokenInfo);
    accountStorage.saveTokenToRemoteCache(account.getUsername(), token);
    return ResponseEntity.ok(new JwtResponse(account.getUsername(), token));

  }


  public ResponseEntity<String> verifyCode(String username, int code) {
    Integer verifyCode = Integer.valueOf(accountStorage.getVerifyCode(username));
    if (!verifyCode.equals(code))
      throw new ResourceNotFoundException("Invalid verify code");

    Account account = accountStorage.findByUsername(username);
    account.setState(UserState.VERIFIED);
    accountStorage.save(account);
    return ResponseEntity.ok("Verify success");
  }

  public ResponseEntity<String> createVerifyCode(String username) {
    User user = userStorage.findByUsername(username);
    if (Objects.isNull(user)) throw new AccountNotExistsException("Account not exist");
    int max = 99999;
    int min = 10000;
    String code = Math.random() * (max - min + 1) + min + " ";
    accountStorage.saveVerifyCode(username, code);
    EmailRequest.sendVerificationCode(user.getEmail(), code);
    return ResponseEntity.ok("Create verify code successful");
  }
}
