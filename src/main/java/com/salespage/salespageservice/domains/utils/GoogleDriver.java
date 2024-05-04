package com.salespage.salespageservice.domains.utils;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.salespage.salespageservice.domains.exceptions.BadRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

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

  public String uploadPublicImage(String folderName, String fileName, java.io.File filePath) {

    String folderId = createNewFolder(folderName);
    log.info("Folder iđ {{}} ", folderId);
    String fileId = null;
    try {
      File fileMetadata = new File();
      fileMetadata.setName(fileName);
      fileMetadata.setParents(List.of(folderId));

      // Check if a file with the same name already exists
      File existingFile = searchFileByName(fileName, folderId);

      // Set file permissions to be publicly readable
      Permission permission = new Permission();
      permission.setRole("reader");
      permission.setType("anyone");

      if (existingFile != null) {
        // Update the existing file
        deleteFile(existingFile.getId());
      }
      // Create a new file
      //      InputStream inputStream = new FileInputStream(filePath.getName());
      log.info("filePath: {}", filePath.getName());
      InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath.getPath());
      log.info("inputStream: {}", inputStream);
      assert inputStream != null;
      File file = googleDrive.files().create(fileMetadata,
              new InputStreamContent("image/jpeg", inputStream))
          .setFields("id").execute();

      fileId = file.getId();
      // Set file permissions using the fileId retrieved from the created file object
      googleDrive.permissions().create(fileId, permission).execute();

      log.info("Upload image success with id {{}}", fileId);
    } catch (Exception e) {
      log.error("==========> Can't upload image {{}} ", e);
    }
    log.info(getImageURL(fileId));
    return getImageURL(fileId);
  }

  public String uploadPublicImageNotDelete(String folderName, String fileName, java.io.File filePath) {
    String fileId = null;
    try {
      String folderId = getFolderIdByName(folderName);
      File fileMetadata = new File();
      fileMetadata.setName(fileName);
      fileMetadata.setParents(List.of(folderId));

      // Set file permissions to be publicly readable
      Permission permission = new Permission();
      permission.setRole("reader");
      permission.setType("anyone");

      // Create a new file
//      InputStream inputStream = new FileInputStream(filePath.getName());
      log.info("filePath: {}", filePath.getName());
      InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath.getPath());
      log.info("inputStream: {}", inputStream);
      File file = googleDrive.files().create(fileMetadata,
              new InputStreamContent("image/jpeg", inputStream))
          .setFields("id").execute();

      fileId = file.getId();
      // Set file permissions using the fileId retrieved from the created file object
      googleDrive.permissions().create(fileId, permission).execute();

      log.info("Upload image not delete success with id: ", fileId);
    } catch (Exception e) {
      log.error("==========> Can't upload image: ", e);
      throw new BadRequestException(e.getMessage());
    }
    return getImageURL(fileId);
  }

  public List<File> getAllFolders() throws IOException {
    List<File> allFiles = getAllGoogleDriveFiles();
    List<File> folders = allFiles.stream().filter(file -> "application/vnd.google-apps.folder".equals(file.getMimeType()))
        .collect(Collectors.toList());
    return folders;
  }

  public void deleteFolder(String folderId) {
    try {
      // Tìm tất cả các file và folder con trong folder cần xóa
      String query = "mimeType='application/vnd.google-apps.folder' and trashed = false and parents in '" + folderId + "'";
      FileList fileList = googleDrive.files().list().setQ(query).execute();

      // Xóa tất cả các file và folder con trong folder cần xóa
      for (File file : fileList.getFiles()) {
        if (file.getMimeType().equals("application/vnd.google-apps.folder")) {
          log.debug("delete with file id {{}}", file.getId());
          deleteFolder(file.getId());
        } else {
          googleDrive.files().delete(file.getId()).execute();
        }
      }
      // Xóa folder cần xóa
      googleDrive.files().delete(folderId).execute();
    } catch (Exception e) {
      log.error("==========> Can't delete folder: " + e);
    }
  }

  public void deleteFile(String fileId) {
    try {
      googleDrive.files().delete(fileId).execute();
      log.info("File with id: " + fileId + " has been deleted successfully");
    } catch (IOException e) {
      log.error("Error deleting file with id: " + fileId + ". Error: " + e);
    }
  }

  public void deleteAllFile(List<String> fileIds) {
    for (String fileId : fileIds) {
      deleteFile(fileId);
    }
  }

  public void deleteFolderByName(String folderName) throws IOException {
    List<File> folders = getAllFolders();
    File folder = folders.stream().filter(f -> folderName.equals(f.getName())).findFirst().orElse(null);
    if (folder != null) {
      deleteFolder(folder.getId());
    } else {
      log.error("==========> Can't find folder with name " + folderName);
    }
  }

  public String getFolderIdByName(String folderName) throws IOException {
    List<File> folders = getAllFolders();
    File folder = folders.stream().filter(f -> folderName.equals(f.getName())).findFirst().orElse(null);
    if (folder != null) {
      log.info("==========> Find folder with name: " + folderName);
      return folder.getId();
    } else {
      log.info("==========> Can't find folder with name: " + folderName + " -> create folder");
      return createNewFolder(folderName);
    }
  }

  public String getImageURL(String fileId) {
    return "https://lh3.google.com/u/0/d/" + fileId;
  }

  private File searchFileByName(String fileName, String folderId) throws IOException {
    String query = "name='" + fileName + "' and trashed = false and parents in '" + folderId + "'";
    FileList result = googleDrive.files().list().setQ(query).execute();
    List<File> files = result.getFiles();
    if (files == null || files.isEmpty()) {
      return null;
    } else {
      return files.get(0);
    }
  }


}
