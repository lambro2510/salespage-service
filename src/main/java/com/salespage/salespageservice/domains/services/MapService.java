package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.domains.info.AddressResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapService extends BaseService{

  public List<String> suggestAddress(String address) {
    List<String> response = new ArrayList<>();
    AddressResult addressResults = suggestAddressByAddress(address);
    for (AddressResult.Result result : addressResults.getResults()){
      response.add(result.getFormattedAddress());
    }
    return response;
  }

}
