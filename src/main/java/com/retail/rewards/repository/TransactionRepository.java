package com.retail.rewards.repository;

import com.retail.rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
/**
 * Repository class responsible for providing transaction data.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    //List of last 3 months transactions based on customerId.
    List<Transaction> findByCustomerCustomerIdAndDateAfter(Long customerId, LocalDate threeMonthsAgo);
}
