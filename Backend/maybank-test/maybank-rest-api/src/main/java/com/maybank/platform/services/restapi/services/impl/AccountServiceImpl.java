package com.maybank.platform.services.restapi.services.impl;

import com.maybank.platform.services.restapi.dao.AccountRepository;
import com.maybank.platform.services.restapi.model.Account;
import com.maybank.platform.services.restapi.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Map<String, Account> fetchAccounts(Set<String> uniqueAccountNumbers) {
        List<Account> accounts = accountRepository.findByAccountNumbers(uniqueAccountNumbers);
        Map<String, Account> accountMap = new HashMap<>();
        for (Account account : accounts) {
            accountMap.put(account.getAccountNumber(), account);
        }
        return accountMap;
    }
}
