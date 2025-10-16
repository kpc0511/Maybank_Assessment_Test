package com.maybank.platform.services.restapi.dao;

import com.maybank.platform.services.restapi.model.Transaction;
import com.maybank.platform.services.restapi.model.dto.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT new com.maybank.platform.services.restapi.model.dto.TransactionDto(t.id, t.account.accountNumber, t.trxAmount, t.description, t.trxDate, t.trxTime, t.customerId, t.version) " +
            "FROM Transaction t WHERE t.id = :id")
    TransactionDto getTransactionById(@Param("id") Long id);

    @Query("SELECT new com.maybank.platform.services.restapi.model.dto.TransactionDto(t.id, t.account.accountNumber, t.trxAmount, t.description, t.trxDate, t.trxTime, t.customerId, t.version) " +
            "FROM Transaction t WHERE " +
            "(:customerId IS NULL OR t.customerId = :customerId) AND " +
            "(:description IS NULL OR t.description LIKE CONCAT(:description, '%'))")
    Page<TransactionDto> searchTransactions(
            @Param("customerId") Long customerId,
            @Param("description") String description,
            Pageable pageable);

    @Query("SELECT new com.maybank.platform.services.restapi.model.dto.TransactionDto(t.id, t.account.accountNumber, t.trxAmount, t.description, t.trxDate, t.trxTime, t.customerId, t.version) " +
            "FROM Transaction t WHERE " +
            "(:customerId IS NULL OR t.customerId = :customerId) AND " +
            "(t.account.accountNumber IN :accountNumbers) AND " +
            "(:description IS NULL OR t.description LIKE CONCAT(:description, '%'))")
    Page<TransactionDto> searchTransactions(
            @Param("customerId") Long customerId,
            @Param("accountNumbers") List<String> accountNumbers,
            @Param("description") String description,
            Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Transaction t SET t.description = :description, t.version = t.version + 1 WHERE t.id = :id AND t.version = :currentVersion")
    int updateTransactionDescription(@Param("id") Long id,
                                     @Param("description") String description,
                                     @Param("currentVersion") Integer currentVersion);
}
