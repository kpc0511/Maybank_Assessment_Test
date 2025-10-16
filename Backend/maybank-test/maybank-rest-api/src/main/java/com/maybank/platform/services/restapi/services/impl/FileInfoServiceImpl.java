package com.maybank.platform.services.restapi.services.impl;

import cn.hutool.core.util.ObjectUtil;
import com.maybank.platform.services.restapi.annotation.EnablePerformanceLogger;
import com.maybank.platform.services.restapi.dao.FileInfoRepository;
import com.maybank.platform.services.restapi.model.FileInfo;
import com.maybank.platform.services.restapi.services.FileInfoService;
import com.maybank.platform.services.util.SnowflakeIdGenerator;
import com.maybank.platform.services.util.enums.EFileStatus;
import com.maybank.platform.services.util.enums.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileInfoServiceImpl implements FileInfoService {
    private final FileInfoRepository fileInfoRepository;

    @Override
    public void updateVersionFile(Long id) {
        fileInfoRepository.updateFileInfoById(id);
    }

    @Override
    public List<FileInfo> getPendingFiles(Integer version) {
        return fileInfoRepository.findByFileStatusAndVersionOrderByCreateDateAsc(EFileStatus.PENDING, version);
    }

    @Override
    @Transactional
    @EnablePerformanceLogger
    public void save(FileInfo fileInfo) {
        fileInfoRepository.save(fileInfo);
    }

    @Override
    @Transactional
    @EnablePerformanceLogger
    public void saveAll(List<FileInfo> list) {
        fileInfoRepository.saveAll(list);
    }

    @Override
    public List<FileInfo> scanDirectory(String directoryPath) {
        List<FileInfo> fileInfoList = new ArrayList<>();
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileInfoList.add(createFileInfo(file));
                    }
                }
            }
        }
        return fileInfoList;
    }

    @Override
    public void moveFilesToDirectory(String sourceDirectoryPath, String destinationDirectoryPath)
            throws IOException {
        File sourceDirectory = new File(sourceDirectoryPath);
        File destinationDirectory = new File(destinationDirectoryPath);

        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs();
        }

        File[] files = sourceDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    Path sourcePath = file.toPath();
                    Path destinationPath = new File(destinationDirectory, file.getName()).toPath();
                    Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    @Override
    public void moveFileToDirectory(String sourceFilePath, String destinationDirectoryPath,
                                    FileInfo fileInfo) throws IOException {
        File sourceFile = new File(sourceFilePath);
        File destinationDirectory = new File(destinationDirectoryPath);

        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs();
        }

        if (sourceFile.isFile()) {
            Path sourcePath = sourceFile.toPath();
            Path destinationPath = new File(destinationDirectory, sourceFile.getName()).toPath();
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            // Print filename and file size after moving
            String fileName = destinationPath.getFileName().toString();
            long fileSize = Files.size(destinationPath);
            log.info("File moved successfully. Filename:{}, Filesize:{} bytes", fileName, fileSize);
            if(ObjectUtil.isNotNull(fileInfo)) {
                fileInfo = updateFileComplete(fileInfo);
                log.info("File updated successfully. FileInfo:{}", fileInfo);
            }
        }
    }

    private FileInfo updateFileComplete(FileInfo fileInfo) {
        FileInfo newFileInfo = fileInfo.toBuilder().fileStatus(EFileStatus.COMPLETE).build();
        newFileInfo.setCreateBy(fileInfo.getCreateBy());
        newFileInfo.setCreateDate(fileInfo.getCreateDate());
        newFileInfo.setUpdateDate(new Date());
        newFileInfo.setStatus(1);
        newFileInfo.setVersion(fileInfo.getVersion());
        newFileInfo.setUpdateBy("PROCESS_SCHEDULER");
        newFileInfo = fileInfoRepository.save(newFileInfo);
        return newFileInfo;
    }

    private FileInfo createFileInfo(File file) {
        FileType fileType = this.getFileType(file);
        FileInfo fileInfo = FileInfo.builder()
                .id(SnowflakeIdGenerator.generateId())
                .fileSize(file.length()).fileStatus(EFileStatus.PENDING)
                .fileName(file.getName()).fileType(fileType)
                .build();
        fileInfo.setVersion(1);
        fileInfo.setStatus(0);
        fileInfo.setCreateDate(new Date());
        fileInfo.setCreateBy("SYSTEM");
        return fileInfo;
    }

    private FileType getFileType(File file) {
        String fileExtension = getFileExtension(file);
        try {
            return FileType.valueOf(fileExtension);  // Convert extension to FileType enum
        } catch (IllegalArgumentException e) {
            log.error("Unsupported file type: {}", fileExtension);
            return null; // Handle unsupported file types
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // Empty extension if no dot found
        }
        return fileName.substring(lastIndexOfDot + 1).toUpperCase();  // Convert to uppercase for consistency (e.g., TXT, CSV)
    }
}
