package com.maybank.platform.services.restapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_customer_id", columnList = "customer_id"),
        @Index(name = "idx_account_number", columnList = "account_number"),
        @Index(name = "idx_description", columnList = "description")}
)
@Data
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseModel implements Serializable {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", nullable = false)
    private Account account;

    @Column(name = "trx_amount", nullable = false)
    private BigDecimal trxAmount;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "trx_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate trxDate;

    @Column(name = "trx_time", nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime trxTime;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;  // Assuming customer_id is part of transaction for quick search

    @Column(name = "reference_id", unique = true, nullable = false, length = 64)
    private String referenceId;

    // Optimistic locking version field
    @Version
    @Column(name = "version")
    private Integer version;
}
