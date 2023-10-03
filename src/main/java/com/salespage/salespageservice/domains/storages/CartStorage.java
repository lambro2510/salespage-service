package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Cart;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartStorage extends BaseStorage{
  public void save(Cart cart) {
    cartRepository.save(cart);
  }

  public List<Cart> findByUsername(String username){
    return cartRepository.findByUsername(username);
  }

  public Cart findById(String id){
    return cartRepository.findById(new ObjectId(id)).get();
  }

  public Long countByUsername(String username) {
    return cartRepository.countByUsername(username);
  }

  public void delete(Cart cart) {
    cartRepository.delete(cart);
  }
}
