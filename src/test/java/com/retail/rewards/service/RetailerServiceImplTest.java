package com.retail.rewards.service;

import com.retail.rewards.dto.MonthlyReward;
import com.retail.rewards.dto.Reward;
import com.retail.rewards.exception.CustomerDataNotFoundException;
import com.retail.rewards.exception.ResourceNotFoundException;
import com.retail.rewards.entity.Customer;
import com.retail.rewards.entity.Transaction;
import com.retail.rewards.repository.CustomerRepository;
import com.retail.rewards.service.impl.RetailerServiceImpl;
import com.retail.rewards.util.RetailerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class RetailerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RetailerUtil retailerUtil;

    @InjectMocks
    private RetailerServiceImpl retailerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Transaction t1 = new Transaction(1L,new BigDecimal("120"), LocalDate.of(2026, 3, 15));
        Transaction t2 = new Transaction(2L,new BigDecimal("80"), LocalDate.of(2026, 4, 10));
        Transaction t3 = new Transaction(2L,new BigDecimal("180"), LocalDate.of(2026, 8, 10));
        Transaction t4 = new Transaction(2L,new BigDecimal("80"), LocalDate.of(2025, 4, 10));

        customer = new Customer("CUST1", Arrays.asList(t1, t2, t3, t4));
    }

    @Test
    void testGetRewardsSuccess() {
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
        when(retailerUtil.calculatePoints(new BigDecimal("120"))).thenReturn(90); // >100 case
        when(retailerUtil.calculatePoints(new BigDecimal("80"))).thenReturn(30);  // between 50–100 case

        List<Reward> rewards = retailerService.getRewards();

        assertEquals(1, rewards.size());
        Reward reward = rewards.get(0);
        assertEquals("CUST1", reward.getCustomerId());
        assertEquals(120, reward.getTotalPoints());
        assertEquals(2, reward.getMonthlyRewards().size());
        assertTrue(reward.getMonthlyRewards().stream()
                .map(MonthlyReward::getMonth)
                .anyMatch(m -> m.equalsIgnoreCase("MARCH")));
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetRewardsNoCustomersThrowsException() {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(CustomerDataNotFoundException.class, () -> retailerService.getRewards());
    }

    @Test
    void testGetRewardByCustomerIdSuccess() {
        when(customerRepository.findById("CUST1")).thenReturn(Optional.ofNullable(customer));
        when(retailerUtil.calculatePoints(new BigDecimal("120"))).thenReturn(90);
        when(retailerUtil.calculatePoints(new BigDecimal("80"))).thenReturn(30);

        Reward reward = retailerService.getRewardByCustomerId("CUST1");

        assertEquals("CUST1", reward.getCustomerId());
        assertEquals(120, reward.getTotalPoints());
        assertEquals(2, reward.getMonthlyRewards().size());
    }

    @Test
    void testGetRewardByCustomerIdNotFoundThrowsException() {
        Customer otherCustomer = new Customer("OTHER", Collections.emptyList());
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(otherCustomer));

        assertThrows(ResourceNotFoundException.class, () -> retailerService.getRewardByCustomerId("CUST1"));
    }
}
