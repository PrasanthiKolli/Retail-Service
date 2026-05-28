package com.retail.rewards.service;

import com.retail.rewards.dto.MonthlyReward;
import com.retail.rewards.dto.Reward;
import com.retail.rewards.exception.CustomerDataNotFoundException;
import com.retail.rewards.exception.ResourceNotFoundException;
import com.retail.rewards.model.Customer;
import com.retail.rewards.model.Transaction;
import com.retail.rewards.repository.CustomerRepository;
import com.retail.rewards.service.impl.RetailerServiceImpl;
import com.retail.rewards.util.RetailerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        Transaction t1 = new Transaction(120.0, LocalDate.of(2026, 3, 15));
        Transaction t2 = new Transaction(80.0, LocalDate.of(2026, 4, 10));
        customer = new Customer("CUST1", Arrays.asList(t1, t2));
    }

    @Test
    void testGetRewardsSuccess() {
        when(customerRepository.getAllCustomers()).thenReturn(Collections.singletonList(customer));
        when(retailerUtil.calculatePoints(120.0)).thenReturn(90); // >100 case
        when(retailerUtil.calculatePoints(80.0)).thenReturn(30);  // between 50–100 case

        List<Reward> rewards = retailerService.getRewards();

        assertEquals(1, rewards.size());
        Reward reward = rewards.get(0);
        assertEquals("CUST1", reward.getCustomerId());
        assertEquals(120, reward.getTotalPoints());
        assertEquals(2, reward.getMonthlyRewards().size());
        assertTrue(reward.getMonthlyRewards().stream()
                .map(MonthlyReward::getMonth)
                .anyMatch(m -> m.equalsIgnoreCase("MARCH")));
        verify(customerRepository, times(1)).getAllCustomers();
    }

    @Test
    void testGetRewardsNoCustomersThrowsException() {
        when(customerRepository.getAllCustomers()).thenReturn(Collections.emptyList());
        assertThrows(CustomerDataNotFoundException.class, () -> retailerService.getRewards());
    }

    @Test
    void testGetRewardByCustomerIdSuccess() {
        when(customerRepository.getAllCustomers()).thenReturn(Collections.singletonList(customer));
        when(retailerUtil.calculatePoints(120.0)).thenReturn(90);
        when(retailerUtil.calculatePoints(80.0)).thenReturn(30);

        Reward reward = retailerService.getRewardByCustomerId("CUST1");

        assertEquals("CUST1", reward.getCustomerId());
        assertEquals(120, reward.getTotalPoints());
        assertEquals(2, reward.getMonthlyRewards().size());
        verify(customerRepository, times(1)).getAllCustomers();
    }

    @Test
    void testGetRewardByCustomerIdNoCustomersThrowsException() {
        when(customerRepository.getAllCustomers()).thenReturn(null);
        assertThrows(CustomerDataNotFoundException.class, () -> retailerService.getRewardByCustomerId("CUST1"));
    }

    @Test
    void testGetRewardByCustomerIdNotFoundThrowsException() {
        Customer otherCustomer = new Customer("OTHER", Collections.emptyList());
        when(customerRepository.getAllCustomers()).thenReturn(Collections.singletonList(otherCustomer));

        assertThrows(ResourceNotFoundException.class, () -> retailerService.getRewardByCustomerId("CUST1"));
    }
}
