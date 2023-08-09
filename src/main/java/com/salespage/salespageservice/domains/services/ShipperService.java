package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.entities.Shipper;
import com.salespage.salespageservice.domains.entities.status.ShipperStatus;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ShipperService extends BaseService{

  public void findShipperForProduct() {
    List<ProductTransaction> productTransactions = productTransactionStorage.findProductTransactionByState(ProductTransactionState.WAITING_SHIPPER);
    for (ProductTransaction productTransaction : productTransactions) {
      Shipper shipper = shipperStorage.findFirstByShipModeAndAcceptTransaction(true, false);
      productTransaction.setShipperUsername(shipper.getUsername());
      productTransaction.setState(ProductTransactionState.SHIPPER_PROCESSING);
      productTransactionStorage.save(productTransaction);
    }
  }


  public void createShipperUser(String adminUser, String username) {
    Account account = accountStorage.findByUsername(adminUser);
    if(Objects.isNull(account) || !account.getRole().equals(UserRole.ADMIN)) throw new AuthorizationException();

    Shipper shipper = new Shipper();
    shipper.setUsername(username);
    shipper.setShipMode(false);
    shipper.setAcceptTransaction(false);
    shipper.setStatus(ShipperStatus.INACTIVATED);
    shipperStorage.save(shipper);
  }

  public void updateShipper(String adminUser, String username, ShipperStatus status){
    Account account = accountStorage.findByUsername(adminUser);
    if(Objects.isNull(account) || !account.getRole().equals(UserRole.ADMIN)) throw new AuthorizationException();

    Shipper shipper = shipperStorage.findByUsername(username);
    shipper.setStatus(status);
    shipperStorage.save(shipper);
  }

  public PageResponse<Shipper> getAllShipper(String username, Pageable pageable) {
    return null;
  }
}
