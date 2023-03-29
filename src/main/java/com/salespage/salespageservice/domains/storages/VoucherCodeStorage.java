package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.VoucherCode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoucherCodeStorage extends BaseStorage{
  public void saveAll(List<VoucherCode> voucherCodes) {
    voucherCodeRepository.saveAll(voucherCodes);
  }

  public VoucherCode findByOwnerIdAndCode(String username, String code) {
    return voucherCodeRepository.findByOwnerIdAndCode(username, code);
  }
}
