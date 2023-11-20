package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Cart;
import com.salespage.salespageservice.domains.utils.CacheKey;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartStorage extends BaseStorage{
  public void save(Cart cart) {
    cartRepository.save(cart);
    remoteCacheManager.del(CacheKey.genListCartByUsername(cart.getUsername()));
  }

  public List<Cart> findByUsername(String username){
    String key = CacheKey.genListCartByUsername(username);
    List<Cart> carts = remoteCacheManager.getList(key, Cart.class);
    if(carts == null){
      carts = cartRepository.findByUsername(username);
      remoteCacheManager.set(key, carts);
    }
    return carts;
  }

  public Cart findById(String id){
    return cartRepository.findById(new ObjectId(id)).orElse(null);
  }

  public Long countByUsername(String username) {
    return cartRepository.countByUsername(username);
  }

  public void delete(Cart cart) {
    cartRepository.delete(cart);
  }

  public void deleteAll(List<Cart> deleteCard) {
    cartRepository.deleteAll(deleteCard);
  }
}
