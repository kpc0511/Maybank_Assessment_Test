package com.maybank.platform.services.restapi.config;

import com.maybank.platform.services.restapi.dao.AccountRepository;
import com.maybank.platform.services.restapi.dao.TransactionRepository;
import com.maybank.platform.services.restapi.factory.TransactionReaderFactory;
import com.maybank.platform.services.restapi.factory.TransactionReaderFactorySelector;
import com.maybank.platform.services.restapi.listener.TransactionJobListener;
import com.maybank.platform.services.restapi.model.Account;
import com.maybank.platform.services.restapi.model.Transaction;
import com.maybank.platform.services.restapi.model.dto.TransactionCsvDto;
import com.maybank.platform.services.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class TransactionBatchConfig {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public List<Transaction> duplicateRecords() {
        return new ArrayList<>();
    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setCorePoolSize(4);
//        taskExecutor.setMaxPoolSize(8);
//        taskExecutor.setQueueCapacity(20);
//        taskExecutor.setThreadNamePrefix("batch-thread-");
//        taskExecutor.initialize();

        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
//        jobLauncher.setTaskExecutor(taskExecutor);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    @StepScope
    public ItemProcessor<TransactionCsvDto, Transaction> processor(AccountRepository accountRepository,
                                                                   @Value("#{jobParameters['filePath']}") String filePath,
                                                                   List<Transaction> duplicateRecords) throws IOException {
        Set<String> accountNumbers = extractAccountNumbers(filePath);

        // Prefetch all accounts from DB
        Map<String, Account> accountMap = accountRepository.findByAccountNumbers(accountNumbers)
                .stream()
                .collect(Collectors.toMap(Account::getAccountNumber, a -> a));

        Set<String> duplicateCheck = new HashSet<>();
        return dto -> {
            Account account = accountMap.get(dto.getAccountNumber());
            if (account == null) {
                throw new IllegalArgumentException("Invalid account: " + dto.getAccountNumber());
            }

            String key = dto.getAccountNumber() + "|" +
                    dto.getTrxAmount() + "|" +
                    dto.getTrxDate() + "|" +
                    dto.getTrxTime() + "|" +
                    dto.getDescription().trim().toLowerCase() + "|" +
                    dto.getCustomerId();

            if (duplicateCheck.contains(key)) {
                Transaction dup = Transaction.builder()
                        .account(account)
                        .trxAmount(dto.getTrxAmount())
                        .customerId(dto.getCustomerId())
                        .description(dto.getDescription())
                        .trxDate(dto.getTrxDate())
                        .trxTime(dto.getTrxTime())
                        .description(dto.getDescription())
                        .build();
                duplicateRecords.add(dup);
                return null; // skip inserting duplicate
            }
            duplicateCheck.add(key);

            return Transaction.builder()
                    .id(SnowflakeIdGenerator.generateId())
                    .trxAmount(dto.getTrxAmount())
                    .description(dto.getDescription())
                    .trxDate(dto.getTrxDate())
                    .trxTime(dto.getTrxTime())
                    .customerId(dto.getCustomerId())
                    .account(account)
                    .referenceId(UUID.randomUUID().toString())
                    .version(1)
                    .build();
        };
    }

    @Bean
    @StepScope
    public FlatFileItemReader<TransactionCsvDto> reader(
            @Value("#{jobParameters['filePath']}") String filePath,
            @Value("#{jobParameters['fileType']}") String fileType,
            TransactionReaderFactorySelector factorySelector) throws Exception {

        TransactionReaderFactory<TransactionCsvDto> factory = factorySelector.getReader(fileType);
        return factory.createReader(filePath);
//        FlatFileItemReader<TransactionCsvDto> reader = new FlatFileItemReader<>();
//        reader.setResource(new FileSystemResource(filePath));
//        reader.setLinesToSkip(1);
//        reader.setLineMapper(lineMapper());
//        return reader;
    }

    private LineMapper<TransactionCsvDto> lineMapper() {
        DefaultLineMapper<TransactionCsvDto> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer("|");
        tokenizer.setNames("accountNumber", "trxAmount", "description", "trxDate", "trxTime", "customerId");

        lineMapper.setLineTokenizer(tokenizer);

        lineMapper.setFieldSetMapper(fieldSet -> {
            TransactionCsvDto dto = new TransactionCsvDto();
            dto.setAccountNumber(fieldSet.readString("accountNumber"));
            dto.setTrxAmount(fieldSet.readBigDecimal("trxAmount"));
            dto.setDescription(fieldSet.readString("description"));
            dto.setTrxDate(LocalDate.parse(fieldSet.readString("trxDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dto.setTrxTime(LocalTime.parse(fieldSet.readString("trxTime"), DateTimeFormatter.ofPattern("HH:mm:ss")));
            dto.setCustomerId(fieldSet.readLong("customerId"));
            return dto;
        });

        return lineMapper;
    }

    @Bean
    public RepositoryItemWriter<Transaction> writer() {
        RepositoryItemWriter<Transaction> writer = new RepositoryItemWriter<>();
        writer.setRepository(transactionRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public SkipPolicy duplicateSkipPolicy() {
        return (throwable, skipCount) -> throwable instanceof DataIntegrityViolationException;
    }

    @Bean
    public Step transactionStep(FlatFileItemReader<TransactionCsvDto> reader,
                                RepositoryItemWriter<Transaction> writer,
                                SkipPolicy duplicateSkipPolicy,
                                ItemProcessor<TransactionCsvDto, Transaction> processor) {
        return new StepBuilder("transactionStep", jobRepository)
                .<TransactionCsvDto, Transaction>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(duplicateSkipPolicy)
                .build();
    }

    @Bean
    public Job transactionJob(Step transactionStep, List<Transaction> duplicateRecords) {
        return new JobBuilder("transactionJob", jobRepository)
                .start(transactionStep)
                .listener(new TransactionJobListener(duplicateRecords))
                .build();
    }

    // Pre-read all account numbers from CSV file
    private Set<String> extractAccountNumbers(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .skip(1) // skip header
                .map(line -> line.split("\\|")[0]) // first column = accountNumber
                .collect(Collectors.toSet()); // use Set to remove duplicates
    }
}
