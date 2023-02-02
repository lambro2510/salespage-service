package com.salespage.salespageservice.domains.utils;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Log4j2
@Component
public class GoogleDriver {

  @Autowired
  private Drive googleDrive;

  public List<File> getAllGoogleDriveFiles() throws IOException {
    FileList result = googleDrive.files().list()
            .setFields("nextPageToken, files(id, name, parents, mimeType)")
            .execute();
    return result.getFiles();
  }

  public String createNewFolder(String folderName) {
    try {
      File fileMetadata = new File();
      fileMetadata.setName(folderName);
      fileMetadata.setMimeType("application/vnd.google-apps.folder");
      File file = googleDrive.files().create(fileMetadata).setFields("id").execute();
      log.info("========> create folder success" + file.getId());
      return file.getId();
    } catch (Exception e) {
      log.error("==========> Can't create folder: " + e);
    }
    return null;
  }

}
