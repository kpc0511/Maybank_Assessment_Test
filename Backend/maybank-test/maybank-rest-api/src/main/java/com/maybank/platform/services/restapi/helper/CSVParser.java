package com.maybank.platform.services.restapi.helper;

import com.maybank.platform.services.restapi.model.Account;
import com.maybank.platform.services.restapi.model.Transaction;
import com.maybank.platform.services.restapi.services.AccountService;
import com.maybank.platform.services.restapi.vo.CSVData;
import com.maybank.platform.services.util.SnowflakeIdGenerator;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class CSVParser implements Parser{
    private final AccountService accountService;

    @Override
    public List<Transaction> parse(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        Path path = Paths.get(filePath);
        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<CSVData> csvToBean = new CsvToBeanBuilder<CSVData>(reader)
                    .withSkipLines(1)
                    .withType(CSVData.class)
                    .withSeparator('|')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<CSVData> tempList = csvToBean.parse();
            Set<String> uniqueAccountNumbers = this.getDistinctAccountNumbers(tempList);
            Map<String, Account> accountMap = accountService.fetchAccounts(uniqueAccountNumbers);
            tempList.forEach(temp -> {
                transactions.add(this.createTransaction(temp, accountMap));
            });
        } catch (IOException e) {
            log.error("Error reading the file: {}", e.getMessage());
        }
        return transactions;
    }

    private Transaction createTransaction(CSVData csvData, Map<String, Account> accountMap) {
        try {
            Account account = accountMap.get(csvData.getAccountNumber().trim());
            if (account == null) {
                log.warn("No account found for account number: {}", csvData.getAccountNumber().trim());
                return null;
            }

            Transaction transaction = new Transaction();
            transaction.setAccount(account);
            transaction.setTrxAmount(BigDecimal.valueOf(csvData.getTrxAmount()));
            transaction.setDescription(csvData.getDescription().trim());
            transaction.setTrxDate(LocalDate.parse(csvData.getTrxDate().trim()));
            transaction.setTrxTime(LocalTime.parse(csvData.getTrxTime().trim()));
            transaction.setCustomerId(Long.parseLong(csvData.getCustomerId().trim()));
            transaction.setVersion(0);
            transaction.setCreateBy("SYSTEM");
            transaction.setCreateDate(new Date());
            transaction.setId(SnowflakeIdGenerator.generateId());

            return transaction;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            log.error("Error parsing transaction. Error: {}", e.getMessage());
            return null;
        }
    }

    private Set<String> getDistinctAccountNumbers(List<CSVData> tempList) {
        // Extract distinct account numbers using Stream and collect into a Set
        // Extract accountNumber
        return tempList.stream()
                .map(CSVData::getAccountNumber)
                .collect(Collectors.toSet());     // Collect as a Set (which automatically handles uniqueness)
    }
}
