package com.retail.rewards.service;

import com.retail.rewards.dto.PageableReward;
import com.retail.rewards.dto.Reward;
import com.retail.rewards.entity.Customer;
import com.retail.rewards.entity.Transaction;
import com.retail.rewards.exception.CustomerDataNotFoundException;
import com.retail.rewards.exception.PageNumberOutOfBoundException;
import com.retail.rewards.exception.ResourceNotFoundException;
import com.retail.rewards.repository.CustomerRepository;
import com.retail.rewards.repository.TransactionRepository;
import com.retail.rewards.service.impl.RetailerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;


class RetailerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RetailerServiceImpl retailerService;

    private Customer customer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Alice");
    }

    // SUCCESS CASE - getRewardByCustomerId
    @Test
    void testGetRewardByCustomerId_success() {

        when(customerRepository.findById("1"))
                .thenReturn(Optional.of(customer));

        List<Transaction> transactions = List.of(
                createTxn(120, 10),
                createTxn(200, 20)
        );

        when(transactionRepository.findByCustomerCustomerIdAndDateAfter(
                eq(1L), any(LocalDate.class)))
                .thenReturn(transactions);

        Reward result = retailerService.getRewardByCustomerId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCustomerId());
        assertEquals("Alice", result.getCustomerName());
        assertTrue(result.getTotalPoints() > 0);
    }

    // EXCEPTION CASE - customer not found
    @Test
    void testGetRewardByCustomerId_notFound() {

        when(customerRepository.findById("1"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> retailerService.getRewardByCustomerId(1L));
    }

    // PAGINATION SUCCESS
    @Test
    void testGetRewards_success() {

        List<Customer> customers = List.of(customer);

        Page<Customer> page = new PageImpl<>(customers,
                PageRequest.of(0, 5),
                1);

        when(customerRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        when(transactionRepository.findByCustomerCustomerIdAndDateAfter(
                eq(1L), any(LocalDate.class)))
                .thenReturn(List.of(createTxn(150, 5)));

        PageableReward result = retailerService.getRewards(0, 5);

        assertNotNull(result);
        assertEquals(1, result.getCustomerList().size());
        assertEquals(1, result.getCurrentPage()); // +1 logic
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testPaginationError() {

        List<Customer> customers = List.of(customer);

        Page<Customer> page = new PageImpl<>(customers,
                PageRequest.of(0, 5),
                1);

        when(customerRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        assertThrows(PageNumberOutOfBoundException.class,
                () -> retailerService.getRewards(10, 5));
    }

    // PAGINATION - EMPTY CASE
    @Test
    void testGetRewards_empty() {

        Page<Customer> page = new PageImpl<>(Collections.emptyList());

        when(customerRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        assertThrows(CustomerDataNotFoundException.class,
                () -> retailerService.getRewards(0, 5));
    }

    // MULTIPLE MONTH AGGREGATION
    @Test
    void testMonthlyAggregation() {

        when(customerRepository.findById("1"))
                .thenReturn(Optional.of(customer));

        List<Transaction> transactions = Arrays.asList(
                createTxn(120, 5),   // current month
                createTxn(130, 6),   // same month
                createTxn(200, 40)   // previous month
        );

        when(transactionRepository.findByCustomerCustomerIdAndDateAfter(
                eq(1L), any(LocalDate.class)))
                .thenReturn(transactions);

        Reward result = retailerService.getRewardByCustomerId(1L);

        assertEquals(1L, result.getCustomerId());
        assertTrue(result.getMonthlyRewards().size() >= 1);
        assertTrue(result.getTotalPoints() > 0);
    }

    // HELPER METHOD
    private Transaction createTxn(double amount, int daysAgo) {
        Transaction t = new Transaction();
        t.setAmount(BigDecimal.valueOf(amount));
        t.setDate(LocalDate.now().minusDays(daysAgo));
        t.setCustomer(customer);
        return t;
    }
}


