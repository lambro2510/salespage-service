package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.dtos.userDtos.UserInfoDto;
import com.salespage.salespageservice.domains.entities.User;
import com.salespage.salespageservice.domains.exceptions.AccountNotExistsException;
import com.salespage.salespageservice.domains.exceptions.ResourceExitsException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.GoogleDriver;
import com.salespage.salespageservice.domains.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class UserService extends BaseService {

    @Autowired
    private GoogleDriver googleDriver;

    public void createUser(SignUpDto dto) {
        User user = new User();
        user.createUser(dto);
        userStorage.save(user);
    }

    public ResponseEntity<User> updateUser(String username, UserInfoDto dto) {
        User user = userStorage.findByUsername(username);
        if (Objects.isNull(user)) throw new AccountNotExistsException("User not exist");

        user.updateUser(dto);
        userStorage.save(user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<User> getUserDetail(String username) {
        User user = userStorage.findByUsername(username);
        if (Objects.isNull(user)) throw new AccountNotExistsException("User not exist");

        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> voting(String username, String votingUsername, Long point) {
        if (Objects.equals(username, votingUsername))
            throw new ResourceExitsException("Không thể tự đánh giá bản thân");
        User user = userStorage.findByUsername(votingUsername);

        if (user == null) throw new ResourceNotFoundException("Không tìm thấy người dùng này");

        user.getRate().processRatePoint(point);

        userStorage.save(user);

        return ResponseEntity.ok(user.getRate());
    }


    public ResponseEntity<String> uploadImage(String username, MultipartFile image) throws IOException {
        String imageUrl = googleDriver.uploadPublicImage(googleDriver.getFolderIdByName("user-image"), username, Helper.convertMultiPartToFile(image));
        User user = userStorage.findByUsername(username);
        user.setImageUrl(imageUrl);
        userStorage.save(user);
        return ResponseEntity.ok("Upload success");
    }

}
