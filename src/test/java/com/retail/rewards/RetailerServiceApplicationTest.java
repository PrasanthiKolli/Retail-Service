package com.retail.rewards;

import com.retail.rewards.entity.Customer;
import com.retail.rewards.entity.Transaction;
import com.retail.rewards.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RetailerServiceApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CustomerRepository customerRepository;

	@BeforeEach
	void setup() {
		customerRepository.deleteAll();

		// Sample transactions
		Transaction t1 = new Transaction();
		t1.setAmount(new BigDecimal("120")); // 90 points
		t1.setDate(LocalDate.now().minusDays(10));

		Transaction t2 = new Transaction();
		t2.setAmount(new BigDecimal("70")); // 20 points
		t2.setDate(LocalDate.now().minusDays(20));

		Customer customer = new Customer();
		customer.setCustomerId("C1");
		customer.setTransactions(List.of(t1, t2));

		customerRepository.save(customer);
	}

	@Test
	void shouldReturnAllCustomerRewards() throws Exception {
		mockMvc.perform(get("/rewards"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].customerId").value("C1"))
				.andExpect(jsonPath("$[0].totalPoints").value(110));

	}

	@Test
	void shouldReturnRewardByCustomerId() throws Exception {
		mockMvc.perform(get("/rewards/C1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.customerId").value("C1"))
				.andExpect(jsonPath("$.totalPoints").value(110));
	}

	@Test
	void shouldReturnNotFound_WhenCustomerDoesNotExist() throws Exception {
		mockMvc.perform(get("/rewards/C999"))
				.andExpect(status().is4xxClientError());
	}

	@Test
	void shouldHandleNoCustomers() throws Exception {
		customerRepository.deleteAll();

		mockMvc.perform(get("/rewards"))
				.andExpect(status().isNoContent());
	}


	@Test
	void shouldIgnoreOldTransactions() throws Exception {

		customerRepository.deleteAll();

		Transaction oldTxn = new Transaction();
		oldTxn.setAmount(new BigDecimal("200"));
		oldTxn.setDate(LocalDate.now().minusMonths(4)); // old

		Customer customer = new Customer();
		customer.setCustomerId("C2");
		customer.setTransactions(List.of(oldTxn));

		customerRepository.save(customer);

		mockMvc.perform(get("/rewards/C2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalPoints").value(0));
	}


	@Test
	void shouldHandleBoundaryValues() throws Exception {

		customerRepository.deleteAll();

		Transaction t1 = new Transaction();
		t1.setAmount(new BigDecimal("50")); // 0 points
		t1.setDate(LocalDate.now().minusDays(5));

		Transaction t2 = new Transaction();
		t2.setAmount(new BigDecimal("100")); // 50 points
		t2.setDate(LocalDate.now().minusDays(5));

		Customer customer = new Customer();
		customer.setCustomerId("C3");
		customer.setTransactions(List.of(t1, t2));

		customerRepository.save(customer);

		mockMvc.perform(get("/rewards/C3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalPoints").value(50));
	}
}