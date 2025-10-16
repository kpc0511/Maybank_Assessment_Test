package com.maybank.platform.services.restapi.task;

import com.maybank.platform.services.restapi.model.FileInfo;
import com.maybank.platform.services.restapi.services.FileInfoService;
import com.maybank.platform.services.restapi.services.TransactionService;
import com.maybank.platform.services.util.RedisUtil;
import com.maybank.platform.services.util.constants.GlobalConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessScheduler {
    private final FileInfoService fileInfoService;
    private final TransactionService transactionService;
    private final RedisUtil redisUtil;
    private final JobLauncher jobLauncher;
    private final Job transactionJob;

    @Value("${csv.destination.directory:}")
    private String fileDestinationPath = "";

    @Value("${csv.backup.directory:}")
    private String fileBackupPath = "";

    @Value("${csv.summary.directory:}")
    private String fileSummaryPath = "";

    @Scheduled(fixedRate = 60000) // Scan every 60 seconds
    public void processTxtData() {
        List<FileInfo> fileInfoList = fileInfoService.getPendingFiles(1);
        fileInfoList.forEach(fileInfo -> {
            String lockKey = GlobalConstants.FILE_INFO_LOCK_KEY + fileInfo.getId();
            if (!redisUtil.isLocked(lockKey)) {
                Boolean isLocked = redisUtil.setLock(lockKey, 3600);
                if (Boolean.TRUE.equals(isLocked)) {
                    try {
                        log.info("Starting batch job for file: {}", fileInfo.getFileName());

                        fileInfo.setVersion(2);
                        fileInfoService.updateVersionFile(fileInfo.getId());

                        // Build absolute file path
                        String filePath = fileDestinationPath + fileInfo.getFileName();
                        String fileType = fileInfo.getFileType().getKey();

                        JobParameters jobParameters = new JobParametersBuilder()
                                .addString("filePath", filePath)
                                .addString("fileSummaryPath", fileSummaryPath)
                                .addString("filename", fileInfo.getFileName())
                                .addString("fileType", fileType)
                                .addLong("fileId", fileInfo.getId())
                                .addLong("timestamp", System.currentTimeMillis())
                                .toJobParameters();
                        JobExecution execution = jobLauncher.run(transactionJob, jobParameters);
                        if (execution.getStatus() == BatchStatus.COMPLETED) {
                            logJobExecution(execution);
                            String destinationFile = fileBackupPath;
                            fileInfoService.moveFileToDirectory(filePath, destinationFile, fileInfo);
                        }
                        //transactionService.transactionSave(filePath, fileInfo);
                    } catch (Exception e) {
                        log.error("Failed to process file {}: {}", fileInfo.getFileName(), e.getMessage(), e);
                    } finally {
                        redisUtil.releaseLock(lockKey);
                    }
                }
            }
        });
    }

    private void logJobExecution(JobExecution execution) {
        execution.getStepExecutions().forEach(step -> {
            log.info("Step {} processed {} items (read: {}, written: {}, skipped: {})",
                    step.getStepName(),
                    step.getWriteCount(),
                    step.getReadCount(),
                    step.getWriteCount(),
                    step.getSkipCount());
        });
    }
}
