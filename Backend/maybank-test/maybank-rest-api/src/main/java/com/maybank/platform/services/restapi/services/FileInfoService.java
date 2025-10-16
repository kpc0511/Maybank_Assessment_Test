package com.maybank.platform.services.restapi.services;

import com.maybank.platform.services.restapi.model.FileInfo;

import java.io.IOException;
import java.util.List;

public interface FileInfoService {
    void updateVersionFile(Long id);
    List<FileInfo> getPendingFiles(Integer version);
    void save(FileInfo fileInfo);
    void saveAll(List<FileInfo> list);
    List<FileInfo> scanDirectory(String directoryPath);
    void moveFilesToDirectory(String sourceDirectoryPath, String destinationDirectoryPath)
            throws IOException;
    void moveFileToDirectory(String sourceFilePath, String destinationDirectoryPath, FileInfo fileInfo)
            throws IOException;
}
