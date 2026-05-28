package com.retail.rewards.repository;

import com.retail.rewards.model.Customer;
import com.retail.rewards.model.Transaction;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Respository class responsible for providing transaction data.
 * Currently uses mock data representing transactions within 3 month period.
 */

@Repository
public class CustomerRepository {

    /**
     * fetch all customers along with their transaction history.
     *
     * @return list of customers
     */

    public List<Customer> getAllCustomers() {
        return List.of(
                new Customer("C1", List.of(
                        new Transaction(120, LocalDate.of(2026, 5, 10)),
                        new Transaction(75, LocalDate.of(2026, 4, 14)),
                        new Transaction(200, LocalDate.of(2026, 3, 17))
                )),
                new Customer("C2", List.of(
                        new Transaction(130, LocalDate.of(2026, 4, 10)),
                        new Transaction(140, LocalDate.of(2026, 4, 14)),
                        new Transaction(120, LocalDate.of(2026, 3, 17))
                ))
        );
    }
}
