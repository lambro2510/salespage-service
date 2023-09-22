package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.Shipper;
import com.salespage.salespageservice.domains.utils.Helper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShipperStorage extends BaseStorage{
  public Shipper findByUsername(String username) {
    return shipperRepository.findByUsername(username);
  }

  public void save(Shipper shipper) {
    shipperRepository.save(shipper);
  }

  public Shipper findFirstByShipModeAndAcceptTransaction(boolean shipMode, boolean acceptTransaction) {
    return shipperRepository.findFirstByShipModeAndAcceptTransaction(shipMode, acceptTransaction);
  }

  public List<Shipper> findByShipModeAndAcceptTransaction(boolean shipMode, boolean acceptTransaction) {
    return shipperRepository.findByShipModeAndAcceptTransaction(shipMode, acceptTransaction);
  }

  public void saveAll(List<Shipper> freeShippers) {
    shipperRepository.saveAll(freeShippers);
  }

  public List<Shipper> findByIdIn(List<String> refIds) {
    return shipperRepository.findByIdIn(Helper.convertListStringToListObjectId(refIds));
  }
}
