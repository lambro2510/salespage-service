package com.salespage.salespageservice.domains.entities.types;

public enum ProductTransactionState {
  IN_CART,

  WAITING_STORE,

  ACCEPT_STORE,

  WAITING_SHIPPER,

  SHIPPER_PROCESSING,

  SHIPPER_COMPLETE,

  ALL_COMPLETE,

  CANCEL
}
