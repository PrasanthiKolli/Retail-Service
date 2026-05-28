package com.retail.rewards.service;

import com.retail.rewards.dto.Reward;

import java.util.List;

/**
 * Service interface for reward calculations
 */

public interface RetailerService {

    /**
     * calculates rewards for all customers.
     *
     * @return list of reward responses
     */
    List<Reward> getRewards();

    /**
     * calculates rewards for a specific customers.
     *
     * @param customerId customer identifier
     * @return reward response
     */
    Reward getRewardByCustomerId(String customerId);
}
