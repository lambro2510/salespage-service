package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.responses.UploadImageData;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Log4j2
public class UploadService extends BaseService {

  @Value("${upload.folder}")
  private String uploadFolder;

  @Value("${upload.url}")
  private String uploadUrl;

  public UploadImageData processUpload(MultipartFile file) {
    if (file.isEmpty()) {
      throw new ResourceNotFoundException("Chưa chọn file");
    }
    String fileName = uploadFolder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
    try (OutputStream outputStream = new FileOutputStream(fileName)) {
      // Convert data to bytes
      byte[] bytes = file.getBytes();
      // Write bytes to the output stream
      outputStream.write(bytes);
      System.out.println("File '" + fileName + "' written successfully.");
    } catch (IOException e) {
      System.err.println("Error writing to file: " + e.getMessage());
    }
    return new UploadImageData(uploadUrl + fileName);
  }

}
