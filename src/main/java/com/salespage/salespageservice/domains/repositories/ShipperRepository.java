package com.salespage.salespageservice.domains.repositories;

import com.salespage.salespageservice.domains.entities.Shipper;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipperRepository extends MongoRepository<Shipper, ObjectId> {
  Shipper findByUsername(String username);

  Shipper findFirstByShipModeAndAcceptTransaction(boolean shipMode, boolean acceptTransaction);
}
