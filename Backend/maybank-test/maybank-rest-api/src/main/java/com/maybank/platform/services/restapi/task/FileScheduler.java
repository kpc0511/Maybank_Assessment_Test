package com.maybank.platform.services.restapi.task;

import com.maybank.platform.services.restapi.model.FileInfo;
import com.maybank.platform.services.restapi.services.FileInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileScheduler {
    private final FileInfoService fileInfoService;

    @Value("${csv.source.directory:}")
    private String csvFileSourcePath = "";

    @Value("${csv.destination.directory:}")
    private String csvFileDestinationPath = "";

    @Scheduled(fixedRate = 60000) // Scan every 60 seconds
    public void scanCSVFiles() {
        List<FileInfo> fileInfoList = fileInfoService.scanDirectory(csvFileSourcePath);
        if(!CollectionUtils.isEmpty(fileInfoList)) {
            fileInfoList.forEach(fileInfo -> {
                fileInfoService.save(fileInfo);
                try {
                    String sourceFile = csvFileSourcePath+fileInfo.getFileName();
                    String destinationFile = csvFileDestinationPath;
                    fileInfoService.moveFileToDirectory(sourceFile, destinationFile, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }
    }
}
