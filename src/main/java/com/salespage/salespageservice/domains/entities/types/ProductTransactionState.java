package com.salespage.salespageservice.domains.entities.types;

public enum ProductTransactionState {
    CANCEL, //Bị hủy
    WAITING, //Đang chờ shipper đến giao hàng
    PROGRESS, //Shipper đang giao hàng
    COMPLETE //Hoàn thành đơn hàng

}
