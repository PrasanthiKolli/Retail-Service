package com.retail.rewards.service.impl;

import com.retail.rewards.dto.MonthlyReward;
import com.retail.rewards.dto.Reward;
import com.retail.rewards.exception.CustomerDataNotFoundException;
import com.retail.rewards.exception.ResourceNotFoundException;
import com.retail.rewards.model.Customer;
import com.retail.rewards.model.Transaction;
import com.retail.rewards.repository.CustomerRepository;
import com.retail.rewards.service.RetailerService;
import com.retail.rewards.util.RetailerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation for RewardService
 * <p>
 * Contains business logic to :
 * -Filter transactions within lst 3 months.
 * -Calculate reward points.
 * -Aggregate monthly ad total rewards for each customer.
 */
@Service
public class RetailerServiceImpl implements RetailerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RetailerUtil retailerUtil;

    /**
     * Calculate rewards for all customers.
     *
     * @return list of reward responses
     */
    @Override
    public List<Reward> getRewards() {
        List<Customer> customerList = customerRepository.getAllCustomers();
        if (customerList == null || customerList.isEmpty()) {
            throw new CustomerDataNotFoundException("No customer data found");
        }
        List<Reward> rewards = new ArrayList<>();

        for (Customer customer : customerList) {
            Reward reward = getReward(customer);

            rewards.add(reward);
        }
        return rewards;
    }

    /**
     * Calculate rewards for specific customers.
     *
     * @param customerId customer identifier
     * @return reward response
     */

    @Override
    public Reward getRewardByCustomerId(String customerId) {
        List<Customer> customerList = customerRepository.getAllCustomers();
        if (customerList == null || customerList.isEmpty()) {
            throw new CustomerDataNotFoundException("No customer data found");
        }
        for (Customer customer : customerList) {
            if (customer.getCustomerId().equalsIgnoreCase(customerId)) {
                return getReward(customer);
            }
        }
        throw new ResourceNotFoundException("Customer with id " + customerId + " not found");
    }

    /**
     * calculate reward points.
     *
     * @param customer customer details
     * @return reward response
     */

    private Reward getReward(Customer customer) {
        int totalPoints = 0;
        Map<String, Integer> monthlyPoints = new HashMap<>();

        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        for (Transaction transaction : customer.getTransactions()) {
            //filter only last 3 months transactions
            if (transaction.getDate().isBefore(threeMonthsAgo) || transaction.getDate().isAfter(LocalDate.now())) {
                continue; // skip transactions older than 3 months
            }
            int points = retailerUtil.calculatePoints(transaction.getAmount());
            String month = transaction.getDate().getMonth().toString();

            monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
            totalPoints += points;
        }

        List<MonthlyReward> monthlyRewards = new ArrayList<>();
        for (String month : monthlyPoints.keySet()) {
            monthlyRewards.add(new MonthlyReward(month, monthlyPoints.get(month)));
        }
        Reward reward = new Reward();
        reward.setMonthlyRewards(monthlyRewards);
        reward.setCustomerId(customer.getCustomerId());
        reward.setTotalPoints(totalPoints);
        return reward;
    }
}
