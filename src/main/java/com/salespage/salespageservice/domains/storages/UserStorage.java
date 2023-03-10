package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.User;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class UserStorage extends BaseStorage {
    public void save(User user) {
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserById(String userId) {
        return userRepository.findUserById(new ObjectId(userId));
    }
}
