package com.salespage.salespageservice.domains.utils;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
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

  public String uploadPublicImage(String folderId, String fileName, java.io.File filePath) {
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
      googleDrive.permissions().create(fileId, permission).execute();

      if (existingFile != null) {
        // Update the existing file
        deleteFile(existingFile.getId());
      }
      // Create a new file
      File file = googleDrive.files().create(fileMetadata,
                      new InputStreamContent("image/jpeg", new FileInputStream(filePath)))
              .setFields("id").execute();
      fileId = file.getId();
      log.info("Upload image success with id: " + fileId);
    } catch (Exception e) {
      log.error("==========> Can't upload image: " + e);
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
      return folder.getId();
    } else {
      log.error("==========> Can't find folder with name " + folderName);
      return null;
    }
  }

  public String getImageURL(String fileId) {
    return "https://drive.google.com/u/0/uc?id=" + fileId;
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
