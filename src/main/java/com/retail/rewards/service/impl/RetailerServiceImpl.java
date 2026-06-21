package com.retail.rewards.service.impl;

import com.retail.rewards.dto.MonthlyReward;
import com.retail.rewards.dto.PageableReward;
import com.retail.rewards.dto.Reward;
import com.retail.rewards.exception.CustomerDataNotFoundException;
import com.retail.rewards.exception.PageNumberOutOfBoundException;
import com.retail.rewards.entity.Customer;
import com.retail.rewards.entity.Transaction;
import com.retail.rewards.repository.CustomerRepository;
import com.retail.rewards.repository.TransactionRepository;
import com.retail.rewards.service.RetailerService;
import com.retail.rewards.util.RetailerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation for RewardService
 * <p>
 * Contains business logic to :
 * -Filter transactions within last 3 months.
 * -Calculate reward points.
 * -Aggregate monthly and total rewards for each customer.
 */
@Service
@Transactional(readOnly=true)
@Slf4j
public class RetailerServiceImpl implements RetailerService {

    @Value("${app.noOfMonths}")
    private int noOfMonths;

    private final CustomerRepository customerRepository;

    private final TransactionRepository transactionRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMMM");

    //constructor injection
    @Autowired
    public RetailerServiceImpl(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves reward points for specific customers.
     *
     * @param customerId customer identifier
     * @return reward response
     */
    @Override
    public Reward getRewardByCustomerId(Long customerId) {
        //fetch customer details based on id.
        log.info("Fetching reward for customerId={}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> {
                    log.error("Customer not found with id={}", customerId);
                   return new CustomerDataNotFoundException("Customer with id " + customerId + " not found ");
                });

        // Date range calculation
        LocalDate today = LocalDate.now();
        LocalDate threeMonthsAgo = today.minusMonths(noOfMonths);
        //fetching transaction list for the customer based on date range and customerId
        log.debug("Transaction date range: {} to {}", threeMonthsAgo, today);
        List<Transaction> transactionList = transactionRepository.findByCustomerCustomerIdAndDateBetween(customer.getCustomerId(),threeMonthsAgo,today);
        return buildReward(customer,transactionList);
    }


    /**
     * Retrieves a paginated list of reward details for all customers.
     *
     * @return page of reward responses
     * @throws CustomerDataNotFoundException if no customer data is found for the given page request
     */
    @Override
    public PageableReward getRewards(int page, int size) {
        //creates a pageable request with sorting based on customerId in ascending
        log.info("Fetching paginated rewards: page={}, size={}", page, size);
        Pageable request=PageRequest.of(page, size, Sort.by("customerId").ascending());

        //fetches customers from the database in a paginated format
        Page<Customer> customerPage = customerRepository.findAll(request);
        //No data in DB
        if (customerPage.getTotalElements() == 0) {
            log.error("No customers found in database");
            throw new CustomerDataNotFoundException("No customer data found");
        }
        // check if page number is out of bound
        if (page >= customerPage.getTotalPages()) {
            log.error("Requested page {} exceeds total pages {}", page, customerPage.getTotalPages());
            throw new PageNumberOutOfBoundException("Page " + page + " is out of bound as we have only "+customerPage.getTotalPages()+" pages");
        }
        List<Customer> customerList = customerPage.getContent();
        log.debug("Customers fetched: count={}", customerList.size());
        // date range calculation
        LocalDate today = LocalDate.now();
        LocalDate threeMonthsAgo = today.minusMonths(noOfMonths);
        //Extract customer IDs
        List<Long> customerIds = customerList.stream().map(Customer::getCustomerId).toList();

        //fetch transactionList based on customerIds
        List<Transaction> transactionList = transactionRepository.findByCustomerCustomerIdInAndDateBetween(customerIds,threeMonthsAgo,today);
        log.debug("Total transactions fetched for page: {}", transactionList.size());
        // group transactions by customerId
        Map<Long,List<Transaction>> transactionsByCustomer = transactionList.stream().collect(Collectors.groupingBy(t -> t.getCustomer().getCustomerId()));

        //build rewards
        List<Reward> rewards = customerList.stream().map(
                c-> buildReward(c,transactionsByCustomer.getOrDefault(c.getCustomerId(),List.of()))
        ).toList();

        //build pageable reward
        PageableReward pageableReward = new PageableReward();
        pageableReward.setCustomerList(rewards);
        pageableReward.setPageSize(customerPage.getSize());
        pageableReward.setCurrentPage(customerPage.getNumber()+1);
        pageableReward.setTotalElements(customerPage.getTotalElements());
        pageableReward.setTotalPages(customerPage.getTotalPages());
        log.info("Paginated rewards prepared successfully for page={}", page);
        return pageableReward;
    }

    /**
     * Calculates reward points for a given customer based on their recent transactions.
     *
     * @param customer customer details
     * @return reward response
     */
    private Reward buildReward(Customer customer,List<Transaction> transactions) {
        log.debug("Building reward for customerId={}, transactionsCount={}",
                customer.getCustomerId(), transactions.size());
        boolean hasTransactions = !transactions.isEmpty();
        //Log when no transactions
        if (!hasTransactions) {
            log.info("Customer with id={} and name={} has no transactions",
                    customer.getCustomerId(),
                    customer.getName());
        }

        // monthly points calculation
        Map<String, Long> monthlyPoints = transactions.stream().collect(Collectors.groupingBy(
                t -> YearMonth.from(t.getDate()).format(formatter),
                Collectors.summingLong(t ->RetailerUtil.calculatePoints(t.getAmount()))
        ));

        //convert to MonthlyReward list (sorted by month)
        List<MonthlyReward> monthlyRewards =monthlyPoints.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey()) // ensure order
                .map(m -> new MonthlyReward(m.getKey(),m.getValue()))
                .toList();

        // total points calculation
        long totalPoints = monthlyRewards.stream().mapToLong(MonthlyReward::getPoints).sum();

        //build reward
        Reward reward = new Reward();
        reward.setMonthlyRewards(monthlyRewards);
        reward.setCustomerName(customer.getName());
        reward.setCustomerId(customer.getCustomerId());
        reward.setTotalPoints(totalPoints);
        reward.setHasTransactions(hasTransactions);
        log.debug("Reward built: customerId={}, totalPoints={}",
                customer.getCustomerId(), totalPoints);

        return reward;
    }
}
