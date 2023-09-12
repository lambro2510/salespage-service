package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.accountDtos.LoginDto;
import com.salespage.salespageservice.app.dtos.accountDtos.ShipperStatusDto;
import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.responses.JwtResponse;
import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.entities.Shipper;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.entities.types.UserState;
import com.salespage.salespageservice.domains.exceptions.AccountNotExistsException;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
import com.salespage.salespageservice.domains.exceptions.ResourceExitsException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.info.DistanceMatrixResult;
import com.salespage.salespageservice.domains.info.TokenInfo;
import com.salespage.salespageservice.domains.producer.Producer;
import com.salespage.salespageservice.domains.utils.EmailRequest;
import com.salespage.salespageservice.domains.utils.GoogleDriver;
import com.salespage.salespageservice.domains.utils.RequestUtil;
import com.salespage.salespageservice.domains.utils.SmsUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
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

  @Value("${twilio.open:false}")
  private boolean isCheckPhoneNumber;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public JwtResponse signUp(SignUpDto dto) {

    if (!dto.getConfirmPassword().equals(dto.getPassword())) throw new ResourceExitsException("Invalid password");
    if (accountStorage.existByUsername(dto.getUsername())) throw new ResourceExitsException("User existed");

    Account account = new Account();
    account.createAccount(dto);
    accountStorage.save(account);

    userService.createUser(dto);
    createVerifyCode(dto.getUsername());
    return new JwtResponse(account.getUsername(), jwtUtils.generateToken(new TokenInfo(account.getUsername(), account.getRole(), account.getState())), account.getRole());
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public JwtResponse createdAdminRole() {
    String username = "admin-he-thong";
    String password = "admin-he-thong";
    if (accountStorage.existByUsername(username)) throw new ResourceExitsException("User existed");

    Account account = new Account();
    account.setRole(UserRole.ADMIN);
    account.setSalt(BCrypt.gensalt());
    account.setUsername(username);
    account.setPassword(BCrypt.hashpw(password, account.getSalt()));
    account.setState(UserState.VERIFIED);
    accountStorage.save(account);

    userService.createUserAdmin(account);

    return new JwtResponse(account.getUsername(), jwtUtils.generateToken(new TokenInfo(account.getUsername(), account.getRole(), account.getState())), account.getRole());
  }

  public JwtResponse signIn(LoginDto dto) throws IOException {

    Account account = accountStorage.findByUsername(dto.getUsername());
    if (account == null || !account.getUsername().equals(dto.getUsername()) || !BCrypt.checkpw(dto.getPassword(), account.getPassword()))
      throw new AccountNotExistsException("Invalid username or password");

    TokenInfo tokenInfo = new TokenInfo(account.getUsername(), account.getRole(), account.getState());
    String token = jwtUtils.generateToken(tokenInfo);
    accountStorage.saveTokenToRemoteCache(account.getUsername(), token);
    return new JwtResponse(account.getUsername(), token, account.getRole());

  }


  public void verifyCode(String username, int code) {
    Integer verifyCode = Integer.valueOf(accountStorage.getVerifyCode(username));
    if (!verifyCode.equals(code))
      throw new ResourceNotFoundException("Invalid verify code");

    Account account = accountStorage.findByUsername(username);
    account.setState(UserState.VERIFIED);
    accountStorage.save(account);
  }

  public boolean checkAccount(String username){
    Account account = accountStorage.findByUsername(username);
    if(account.getState().equals(UserState.NOT_VERIFIED)){
      return true;
    }else{
      throw new BadRequestException("Tài khoản đã được xác minh");
    }
  }

  public void createVerifyCode(String username) {
    User user = userStorage.findByUsername(username);
    if (Objects.isNull(user)) throw new AccountNotExistsException("Account not exist");
    int max = 99999;
    int min = 10000;
    String code = Math.random() * (max - min + 1) + min + " ";
    accountStorage.saveVerifyCode(username, code);
    if(isCheckPhoneNumber){
      SmsUtils.sendMessage(code, user.getPhoneNumber());
    }
    EmailRequest.sendVerificationCode(user.getEmail(), code);
  }

  public void changeShipMode(String username, List<UserRole> userRoles, ShipperStatusDto dto) {
    Account account = accountStorage.findByUsername(username);
    if (Objects.nonNull(account) && hasUserRole(userRoles, UserRole.SHIPPER)) {
      Shipper shipper = shipperStorage.findByUsername(username);
      if(Objects.isNull(shipper)) throw new ResourceNotFoundException("Tài khoản chưa được xác minh");
      shipper.setShipMode(dto.getStatus());
      shipper.setLongitude(dto.getLongitude());
      shipper.setLatitude(dto.getLatitude());
      shipperStorage.save(shipper);
    } else {
      throw new ResourceNotFoundException("Tài khoản không tồn tại hoặc quyền không hợp lê");
    }
  }

  public void acceptProductTransaction(String username, List<UserRole> userRoles, String transactionId) {
  }


}
