package com.retail.rewards.repository;

import com.retail.rewards.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Respository class responsible for providing transaction data.
 */

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String> {

    @EntityGraph(attributePaths = "transactions")
    List<Customer> findAll();

}
