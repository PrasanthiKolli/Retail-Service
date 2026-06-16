package com.retail.rewards.service.impl;

import com.retail.rewards.dto.MonthlyReward;
import com.retail.rewards.dto.PageableReward;
import com.retail.rewards.dto.Reward;
import com.retail.rewards.exception.CustomerDataNotFoundException;
import com.retail.rewards.exception.PageNumberOutOfBoundException;
import com.retail.rewards.exception.ResourceNotFoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

/**
 * Implementation for RewardService
 * <p>
 * Contains business logic to :
 * -Filter transactions within lst 3 months.
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
        Optional<Customer> customer = customerRepository.findById(customerId);
        if(customer.isPresent()){
            return getReward(customer.get());
        }
        //exception when customer with id is not present.
        throw new ResourceNotFoundException("Customer with id " + customerId + " not found");
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
        // page out of bound
        if (page >= customerPage.getTotalPages()) {
            throw new PageNumberOutOfBoundException("Page " + page + " is out of bound");
        }
        List<Reward> rewards = new ArrayList<>(customerList.size());
        //Iterates through each customer and calculates their reward details.
        for (Customer customer : customerList) {
            Reward reward = getReward(customer);

            rewards.add(reward);
        }
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
    private Reward getReward(Customer customer) {
        long totalPoints = 0;
        Map<String, Long> monthlyPoints = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate threeMonthsAgo = today.minusMonths(noOfMonths);
        //fetches all transactions for the given customer within the last 3 months.
        List<Transaction> transactionList =
                transactionRepository.findByCustomerCustomerIdAndDateBetween(
                        customer.getCustomerId(),
                        threeMonthsAgo,
                        today
                );
        for (Transaction transaction : transactionList) {
            //Calculates reward points for each transaction
            long points = RetailerUtil.calculatePoints(transaction.getAmount());
            String yearMonth = YearMonth.from(transaction.getDate()).format(formatter);
            //Aggregates reward points based on year and month.
            monthlyPoints.put(
                    yearMonth,
                    monthlyPoints.getOrDefault(yearMonth, 0L) + points
            );

            totalPoints += points;
        }
        // Monthly rewards are grouped using a year-month format
        List<MonthlyReward> monthlyRewards = new ArrayList<>();
        for (String yearMonth : monthlyPoints.keySet()) {
            monthlyRewards.add(new MonthlyReward(yearMonth, monthlyPoints.get(yearMonth)));
        }
        Reward reward = new Reward();
        reward.setCustomerName(customer.getName());
        reward.setMonthlyRewards(monthlyRewards);
        reward.setCustomerId(customer.getCustomerId());
        reward.setTotalPoints(totalPoints);
        return reward;
    }
}
