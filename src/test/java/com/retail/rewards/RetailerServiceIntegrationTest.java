package com.retail.rewards;

import com.retail.rewards.entity.Customer;
import com.retail.rewards.entity.Transaction;
import com.retail.rewards.repository.CustomerRepository;
import com.retail.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Integration test class

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RetailerServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Customer customer;

    @BeforeEach
    void setup() {

        // Correct order
        transactionRepository.deleteAll();
        customerRepository.deleteAll();

        customer = new Customer();
        customer.setName("Alice");

        customer = customerRepository.save(customer);

        Transaction t1 = new Transaction();
        t1.setAmount(new BigDecimal("120")); // 90 points
        t1.setDate(LocalDate.now().minusDays(10));
        t1.setCustomer(customer);

        Transaction t2 = new Transaction();
        t2.setAmount(new BigDecimal("70")); // 20 points
        t2.setDate(LocalDate.now().minusDays(20));
        t2.setCustomer(customer);

        transactionRepository.saveAll(Arrays.asList(t1, t2));
    }

    @Test
    void shouldReturnAllCustomerRewards() throws Exception {

        mockMvc.perform(get("/rewards?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerList[0].customerId")
                        .value(customer.getCustomerId()))
                .andExpect(jsonPath("$.customerList[0].totalPoints")
                        .value(110));
    }

    @Test
    void shouldReturnRewardByCustomerId() throws Exception {

        mockMvc.perform(get("/rewards/" + customer.getCustomerId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId")
                        .value(customer.getCustomerId()))
                .andExpect(jsonPath("$.totalPoints").value(110));
    }

    @Test
    void shouldReturnNotFound_WhenCustomerDoesNotExist() throws Exception {
        mockMvc.perform(get("/rewards/999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldIgnoreOldTransactions() throws Exception {

        transactionRepository.deleteAll();
        customerRepository.deleteAll();

        Customer c = new Customer();
        c.setName("Bob");
        c = customerRepository.save(c);

        Transaction oldTxn = new Transaction();
        oldTxn.setAmount(new BigDecimal("200"));
        oldTxn.setDate(LocalDate.now().minusMonths(4)); // older than 3 months
        oldTxn.setCustomer(c);

        transactionRepository.save(oldTxn);

        mockMvc.perform(get("/rewards/" + c.getCustomerId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPoints").value(0));
    }

    // TEST 6
    @Test
    void shouldHandleBoundaryValues() throws Exception {

        transactionRepository.deleteAll();
        customerRepository.deleteAll();

        Customer c = new Customer();
        c.setName("Charlie");
        c = customerRepository.save(c);

        Transaction t1 = new Transaction();
        t1.setAmount(new BigDecimal("50"));  // 0 points
        t1.setDate(LocalDate.now().minusDays(5));
        t1.setCustomer(c);

        Transaction t2 = new Transaction();
        t2.setAmount(new BigDecimal("100")); // 50 points
        t2.setDate(LocalDate.now().minusDays(5));
        t2.setCustomer(c);

        transactionRepository.saveAll(Arrays.asList(t1, t2));

        mockMvc.perform(get("/rewards/" + c.getCustomerId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPoints").value(50));
    }
}