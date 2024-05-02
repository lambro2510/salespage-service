package com.salespage.salespageservice.app.controllers.publicControllers;

import com.salespage.salespageservice.app.responses.UploadImageData;
import com.salespage.salespageservice.domains.services.UploadService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin
@RestController
@RequestMapping("api/v1/public/upload")
public class PublicUploadController {

  private final UploadService uploadService;

  public PublicUploadController(UploadService uploadService) {
    this.uploadService = uploadService;
  }

  @PostMapping(value = "")
  public ResponseEntity<UploadImageData> uploadImage(@RequestBody @NotNull @Valid MultipartFile file) {
    return ResponseEntity.ok(uploadService.processUpload(file));
  }
}
