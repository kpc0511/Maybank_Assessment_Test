package com.maybank.platform.services.restapi.helper;

import com.maybank.platform.services.restapi.model.Account;
import com.maybank.platform.services.restapi.model.Transaction;
import com.maybank.platform.services.restapi.services.AccountService;
import com.maybank.platform.services.util.SnowflakeIdGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@AllArgsConstructor
public class TXTParser implements Parser {
    private final AccountService accountService;

    @Override
    public List<Transaction> parse(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        Set<String> uniqueAccountNumbers = this.collectUniqueAccountNumbers(filePath);
        Map<String, Account> accountMap = accountService.fetchAccounts(uniqueAccountNumbers);

        if (CollectionUtils.isNotEmpty(accountMap.values())) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                br.readLine(); // Skip header line
                while ((line = br.readLine()) != null) {
                    Transaction transaction = createTransaction(line, accountMap);
                    if (transaction != null) {
                        transactions.add(transaction);
                    }
                }
            } catch (IOException e) {
                log.error("Error reading the file: {}", e.getMessage());
            }
        }
        return transactions;
    }

    private Transaction createTransaction(String line, Map<String, Account> accountMap) {
        String[] values = line.split("\\|");
        if (values.length != 6) {
            log.warn("Skipping invalid line: {}", line);
            return null; // Skip this line if the format is not as expected
        }

        try {
            Account account = accountMap.get(values[0].trim());
            if (account == null) {
                log.warn("No account found for account number: {}", values[0].trim());
                return null; // Skip if account not found
            }

            Transaction transaction = new Transaction();
            transaction.setAccount(account);
            transaction.setTrxAmount(new BigDecimal(values[1].trim()));
            transaction.setDescription(values[2].trim());
            transaction.setTrxDate(LocalDate.parse(values[3].trim()));
            transaction.setTrxTime(LocalTime.parse(values[4].trim()));
            transaction.setCustomerId(Long.parseLong(values[5].trim()));
            transaction.setVersion(0);
            transaction.setCreateBy("SYSTEM");
            transaction.setCreateDate(new Date());
            transaction.setId(SnowflakeIdGenerator.generateId()); // Generate Snowflake ID

            return transaction;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            log.error("Error parsing transaction from line: {}. Error: {}", line, e.getMessage());
            return null; // Return null if there was an error
        }
    }

    private Set<String> collectUniqueAccountNumbers(String filePath) {
        Set<String> uniqueAccountNumbers = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\\|");
                if (fields.length < 6) {
                    continue; // Skip invalid lines
                }
                uniqueAccountNumbers.add(fields[0].trim());
            }
        } catch (IOException e) {
            log.error("Error reading account numbers from file: {}", e.getMessage());
        }
        return uniqueAccountNumbers;
    }
}
