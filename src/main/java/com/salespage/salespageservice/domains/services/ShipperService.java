package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.PageResponse;
import com.salespage.salespageservice.domains.entities.Account;
import com.salespage.salespageservice.domains.entities.ProductTransaction;
import com.salespage.salespageservice.domains.entities.Shipper;
import com.salespage.salespageservice.domains.entities.status.ShipperStatus;
import com.salespage.salespageservice.domains.entities.types.ProductTransactionState;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.entities.types.VehicleType;
import com.salespage.salespageservice.domains.exceptions.AuthorizationException;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
import com.salespage.salespageservice.domains.info.DistanceMatrixResult;
import com.salespage.salespageservice.domains.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ShipperService extends BaseService{

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public void findShipperForProduct() {
    List<Shipper> freeShippers = shipperStorage.findByShipModeAndAcceptTransaction(true, false);
//    List<ProductTransaction> productTransactions = productTransactionStorage.findProductTransactionByState(ProductTransactionState.ACCEPT_STORE);
    for(Shipper shipper : freeShippers){
//      for(ProductTransaction productTransaction: productTransactions){
//        String shipperLocation = shipper.getLatitude() + ',' + shipper.getLongitude();
//        DistanceMatrixResult.Distance distance = getDistance(shipperLocation, productTransaction.getStore().getLocation(), shipper.getVehicleType().getValue());
//        if(VehicleType.CAR == shipper.getVehicleType()){
//          if(distance.getValue() < 10000){
//            shipper.setAcceptTransaction(true);
//          }
//        }
//      }
    }
    shipperStorage.saveAll(freeShippers);
//    productTransactionStorage.saveAll(productTransactions);
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
