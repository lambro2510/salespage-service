package com.salespage.salespageservice.domains.entities.types;

public enum ProductTransactionState {
  NEW,

  WAITING_STORE,

  WAITING_SHIPPER,

  SHIPPER_PROCESSING,

  SHIPPER_COMPLETE,

  ALL_COMPLETE,

  CANCEL
}
