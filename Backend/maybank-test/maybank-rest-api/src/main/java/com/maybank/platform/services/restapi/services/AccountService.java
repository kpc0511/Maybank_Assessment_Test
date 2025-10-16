package com.maybank.platform.services.restapi.services;

import com.maybank.platform.services.restapi.model.Account;
import java.util.Map;
import java.util.Set;

public interface AccountService {
    Map<String, Account> fetchAccounts(Set<String> uniqueAccountNumbers);
}
