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
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->new CustomerDataNotFoundException("Customer with id "+customerId+" not found "));

        // Date range calculation
        LocalDate today = LocalDate.now();
        LocalDate threeMonthsAgo = today.minusMonths(noOfMonths);
        //fetching transaction list for the customer based on date range and customerId
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
        Pageable request=PageRequest.of(page, size, Sort.by("customerId").ascending());

        //fetches customers from the database in a paginated format
        Page<Customer> customerPage = customerRepository.findAll(request);
        List<Customer> customerList = customerPage.getContent();
        //No data in DB
        if (customerPage.getTotalElements() == 0) {
            throw new CustomerDataNotFoundException("No customer data found");
        }
        // check if page number is out of bound
        if (page >= customerPage.getTotalPages()) {
            throw new PageNumberOutOfBoundException("Page " + page + " is out of bound as we have only "+customerPage.getTotalPages()+" pages");
        }
        // date range calcuation
        LocalDate today = LocalDate.now();
        LocalDate threeMonthsAgo = today.minusMonths(noOfMonths);
        //Extract customer IDs
        List<Long> customerIds = customerList.stream().map(Customer::getCustomerId).toList();

        //fetch transactionList based on customerIds
        List<Transaction> transactionList = transactionRepository.findByCustomerCustomerIdInAndDateBetween(customerIds,threeMonthsAgo,today);
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
        return pageableReward;
    }

    /**
     * Calculates reward points for a given customer based on their recent transactions.
     *
     * @param customer customer details
     * @return reward response
     */
    private Reward buildReward(Customer customer,List<Transaction> transactions) {
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

        return reward;
    }
}
