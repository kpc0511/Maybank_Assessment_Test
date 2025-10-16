package com.maybank.platform.services.restapi.listener;

import com.maybank.platform.services.restapi.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class TransactionJobListener implements JobExecutionListener {
    private final List<Transaction> duplicateRecords;
    private String fileSummaryPath;
    private String filename;

    public TransactionJobListener(List<Transaction> duplicateRecords) {
        this.duplicateRecords = duplicateRecords;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Transaction Job Started...");
        this.fileSummaryPath = jobExecution.getJobParameters().getString("fileSummaryPath");
        this.filename = jobExecution.getJobParameters().getString("filename") + "_summary.txt";
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Transaction Job Completed...");
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fileSummaryPath + filename, true))) {
                LocalDateTime processTime = LocalDateTime.now(); // or jobExecution.getEndTime().toInstant()

                pw.println("Transaction Summary Report");
                pw.println("Process Date & Time: " + processTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                pw.println("---------------------------------------------------");
                StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
                long totalRecords = stepExecution.getReadCount();
                long insertedCount = stepExecution.getWriteCount();
                int duplicateCount = duplicateRecords.size();
                pw.println("Summary:");
                pw.println("Total Records in CSV: " + totalRecords);
                pw.println("Inserted Records: " + insertedCount);
                pw.println("Duplicated Records: " + duplicateCount);
                for (Transaction txn : duplicateRecords) {
                    pw.println(String.format("%s | %s | %s | %s | %s",
                            txn.getAccount().getAccountNumber(),
                            txn.getTrxAmount(),
                            txn.getDescription(),
                            txn.getTrxDate(),
                            txn.getTrxTime()));
                }
                pw.println("---------------------------------------------------");
            } catch (Exception e) {
                log.error("Error while writing summary to file", e);
            }
        }
    }
}
