package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.dtos.accountDtos.ShipperStatusDto;
import com.salespage.salespageservice.domains.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/shipper")
public class ShipperController extends BaseController{

    @Autowired
    private AccountService accountService;

    @PostMapping("status")
    private ResponseEntity<?> changeModeOn(Authentication authentication, @RequestBody ShipperStatusDto dto){
        try{
            accountService.changeShipMode(getUsername(authentication), getUserRoles(authentication), dto);
            return successApi("Chuyển trạng thái thành công");
        }catch (Exception ex){
            return errorApi(ex.getMessage());
        }
    }

}