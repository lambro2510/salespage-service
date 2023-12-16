package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.dtos.userDtos.UserInfoDto;
import com.salespage.salespageservice.app.responses.UploadImageData;
import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.entities.ProductDetail;
import com.salespage.salespageservice.domains.entities.Rating;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import com.salespage.salespageservice.domains.entities.types.RatingType;
import com.salespage.salespageservice.domains.exceptions.*;
import com.salespage.salespageservice.domains.utils.Helper;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@Log4j2
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

  public Rate voting(String username, String votingUsername, Float point) {
    if (Objects.equals(username, votingUsername))
      throw new ResourceExitsException("Không thể tự đánh giá bản thân");
    User user = userStorage.findByUsername(votingUsername);

    if (user == null) throw new ResourceNotFoundException("Không tìm thấy người dùng này");

    Rating rating = ratingStorage.findByUsernameAndRefIdAndRatingType(username, user.getId().toHexString(), RatingType.USER);

    Rate rate = user.getRate();
    if (Objects.isNull(rating)) {
      rating = new Rating(new ObjectId(), username, user.getId().toHexString(), RatingType.USER, point, "");
      rate.processAddRatePoint(point);
    } else {
      rate.processUpdateRatePoint(rating.getPoint(), point);
    }

    user.setRate(rate);
    userStorage.save(user);
    ratingStorage.save(rating);
    return rate;

  }

  public UploadImageData uploadImage(String username, MultipartFile image) throws IOException {
    String imageUrl = googleDriver.uploadPublicImage("user-image", username, Helper.convertMultiPartToFile(image));
    User user = userStorage.findByUsername(username);
    user.setImageUrl(imageUrl);
    userStorage.save(user);
    return new UploadImageData(imageUrl);
  }

  public void minusBalance(User user, Double totalPrice) {
    log.debug("=====>minusBalance: username: {} balance: {} price: {}", user.getUsername(), user.getBalance(), totalPrice);
    if (user.getBalance().getMoney() < totalPrice) {
      throw new NotEnoughMoneyException();
    }
    user.getBalance().minusMoney(totalPrice);
  }

  public boolean updateTransaction() {
    List<ProductDetail> productTransactionDetailList = productDetailStorage.findAll();
    productTransactionDetailList.forEach(k -> k.setUsername("taikhoanbanhang"));
    productDetailStorage.saveAll(productTransactionDetailList);
    return true;
  }

  public boolean deleteAvatar(String username) {
    User user = userStorage.findByUsername(username);
    if (user == null) {
      throw new BadRequestException("Người dùng không tồn tại");
    }
    user.setImageUrl(null);
    userStorage.save(user);
    return true;
  }
}
