package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.accountDtos.CheckInDto;
import com.salespage.salespageservice.app.dtos.accountDtos.LoginDto;
import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.responses.JwtResponse;
import com.salespage.salespageservice.domains.entities.*;
import com.salespage.salespageservice.domains.entities.types.OtpStatus;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.entities.types.UserState;
import com.salespage.salespageservice.domains.exceptions.AccountNotExistsException;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
import com.salespage.salespageservice.domains.exceptions.ResourceExitsException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.info.TokenInfo;
import com.salespage.salespageservice.domains.utils.DateUtils;
import com.salespage.salespageservice.domains.utils.EmailRequest;
import com.salespage.salespageservice.domains.utils.GoogleDriver;
import com.salespage.salespageservice.domains.utils.SmsUtils;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
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
  private GoogleDriver googleDriver;

  @Value("${twilio.open:false}")
  private boolean isCheckPhoneNumber;

  @Value("${twilio.token}")
  private String authToken;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public JwtResponse signUp(SignUpDto dto) {

    if (!dto.getConfirmPassword().equals(dto.getPassword())) throw new ResourceExitsException("Invalid password");
    if (accountStorage.existByUsername(dto.getUsername())) throw new ResourceExitsException("Người dùng đã tồn tại");
    if (userService.existByPhoneNumber(dto.getPhoneNumber())) throw new ResourceExitsException("Người dùng đã tồn tại");
    Account account = new Account();
    account.createAccount(dto);
    accountStorage.save(account);

    userService.createUser(dto);
    createVerifyCode(dto.getUsername());
    return new JwtResponse(account.getUsername(), null, jwtUtils.generateToken(new TokenInfo(account.getUsername(), account.getRole(), account.getState())), account.getRole());
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

    return new JwtResponse(account.getUsername(), null, jwtUtils.generateToken(new TokenInfo(account.getUsername(), account.getRole(), account.getState())), account.getRole());
  }

  public JwtResponse signIn(LoginDto dto) {

    Account account = accountStorage.findByUsername(dto.getUsername());
    if (account == null || !account.getUsername().equals(dto.getUsername()) || !BCrypt.checkpw(dto.getPassword(), account.getPassword()))
      throw new BadRequestException("Invalid username or password");
    User user = userStorage.findByUsername(dto.getUsername());
    TokenInfo tokenInfo = new TokenInfo(account.getUsername(), account.getRole(), account.getState());
    String token = jwtUtils.generateToken(tokenInfo);
    accountStorage.saveTokenToRemoteCache(account.getUsername(), token);
    return new JwtResponse(account.getUsername(), user.getImageUrl(), token, account.getRole());

  }


  public JwtResponse verifyCode(String phoneNumber, String code)  {
    User user = userStorage.findByPhoneNumber(phoneNumber);
    if (Objects.isNull(user)) {
      throw new ResourceNotFoundException("Không tồn tại người dùng này");
    }
    Integer verifyCode = accountStorage.getVerifyCode(user.getPhoneNumber());
    if (Objects.isNull(verifyCode))
      throw new ResourceNotFoundException("Invalid verify code");
    if(!Objects.equals(code, String.valueOf(verifyCode))){
      throw new BadRequestException("Sai mã xác minh");
    }
    Account account = accountStorage.findByUsername(user.getUsername());
    account.setState(UserState.VERIFIED);
    accountStorage.save(account);
    return new JwtResponse(account.getUsername(), user.getImageUrl(), jwtUtils.generateToken(new TokenInfo(account.getUsername(), account.getRole(), account.getState())), account.getRole());

  }

  public boolean checkAccount(String username) {
    Account account = accountStorage.findByUsername(username);
    if (account.getState().equals(UserState.NOT_VERIFIED)) {
      return true;
    } else {
      throw new BadRequestException("Tài khoản đã được xác minh");
    }
  }

  @Async("threadPoolTaskExecutor")
  public void createVerifyCode(String phone) {
    User user = userStorage.findByPhoneNumber(phone);
    if (Objects.isNull(user)) throw new AccountNotExistsException("Account not exist");
    int max = 99999;
    int min = 10000;
    int code = (int) (Math.random() * (max - min + 1) + min);
    Otp otp = new Otp(new ObjectId(), user.getPhoneNumber(), Integer.toString(code), OtpStatus.WAITING);
    otpStorage.saveVerifyCode(user.getUsername(), otp);
    accountStorage.saveOptToRemoteCache(phone, otp);
//    if (isCheckPhoneNumber) {
//      SmsUtils.sendMessage(Integer.toString(code), user.getPhoneNumber(), authToken);
//    }
//    EmailRequest.sendVerificationCode(user.getEmail(), Integer.toString(code));
  }

  public void changeShipMode(String username, List<UserRole> userRoles, CheckInDto dto) {
    Account account = accountStorage.findByUsername(username);
    if (Objects.nonNull(account) && hasUserRole(userRoles, UserRole.SHIPPER)) {
      Shipper shipper = shipperStorage.findByUsername(username);
      if (Objects.isNull(shipper)) throw new ResourceNotFoundException("Tài khoản chưa được xác minh");
      shipper.setLongitude(dto.getLongitude());
      shipper.setLatitude(dto.getLatitude());
      shipperStorage.save(shipper);
    } else {
      throw new ResourceNotFoundException("Tài khoản không tồn tại hoặc quyền không hợp lê");
    }
  }

  public void acceptProductTransaction(String username, List<UserRole> userRoles, String transactionId) {
  }

  public void checkIn(CheckInDto dto) {
    Account account = accountStorage.findByUsername(dto.getUsername());
    String today = DateUtils.nowString("dd/MM/yyyy");
    if (!today.equals(account.getLastLogin())) {
      account.setLastLogin(today);
      accountStorage.save(account);
      checkInDaily(account.getUsername());
    }
  }

  public void checkInDaily(String username) {
    String today = DateUtils.nowString("dd/MM/yyyy");
    CheckInDaily checkInDaily = checkInDailyStorage.findByUsernameAndDate(username, today);
    if (Objects.isNull(checkInDaily)) {
      checkInDaily = new CheckInDaily();
      checkInDaily.setDate(today);
      checkInDaily.setUsername(username);
      checkInDailyStorage.save(checkInDaily);
    }
  }
}
