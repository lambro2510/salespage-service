package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.dtos.userDtos.UserInfoDto;
import com.salespage.salespageservice.app.responses.transactionResponse.PaymentTransactionResponse;
import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.entities.PaymentTransaction;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.exceptions.AccountNotExistsException;
import com.salespage.salespageservice.domains.exceptions.ResourceExitsException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.Helper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService extends BaseService {


  public void createUser(SignUpDto dto) {
    User user = new User();
    user.createUser(dto);
    userStorage.save(user);
  }

  public void createUserAdmin(Account account) {
    User user = new User();
    user.createUserAdmin(account);
    userStorage.save(user);
  }

  public User updateUser(String username, UserInfoDto dto) {
    User user = userStorage.findByUsername(username);
    if (Objects.isNull(user)) throw new AccountNotExistsException("User not exist");

    user.updateUser(dto);
    userStorage.save(user);
    return user;
  }

  public User getUserDetail(String username) {
    User user = userStorage.findByUsername(username);
    if (Objects.isNull(user)) throw new AccountNotExistsException("User not exist");

    return user;
  }

  public void voting(String username, String votingUsername, Long point) {
    if (Objects.equals(username, votingUsername))
      throw new ResourceExitsException("Không thể tự đánh giá bản thân");
    User user = userStorage.findByUsername(votingUsername);

    if (user == null) throw new ResourceNotFoundException("Không tìm thấy người dùng này");

    user.getRate().processRatePoint(point);

    userStorage.save(user);

  }


  public void uploadImage(String username, MultipartFile image) throws IOException {
    String imageUrl = googleDriver.uploadPublicImage("user-image", username, Helper.convertMultiPartToFile(image));
    User user = userStorage.findByUsername(username);
    user.setImageUrl(imageUrl);
    userStorage.save(user);
  }

  public List<PaymentTransactionResponse> paymentTransactionOfUser(String username){
    return paymentTransactionStorage.findByUsername(username).stream().map(PaymentTransaction::partnerToPaymentTransactionResponse).collect(Collectors.toList());
  }
}
