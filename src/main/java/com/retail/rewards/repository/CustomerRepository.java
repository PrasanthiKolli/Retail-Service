package com.retail.rewards.repository;

import com.retail.rewards.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * Repository class responsible for providing Customer data.
 */

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    @NonNull
    Page<Customer> findAll(@NonNull Pageable pageable);

}

