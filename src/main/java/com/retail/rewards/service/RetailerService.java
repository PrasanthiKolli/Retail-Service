package com.retail.rewards.service;

import com.retail.rewards.dto.PageableReward;
import com.retail.rewards.dto.Reward;

import java.util.List;

/**
 * Service interface for reward calculations
 */

public interface RetailerService {

    /**
     * calculates rewards for a specific customers.
     *
     * @param customerId customer identifier
     * @return reward response
     */
    Reward getRewardByCustomerId(Long customerId);

    /**
     * calculates rewards for all customers.
     *
     * @return list of reward responses
     */
    PageableReward getRewards(int page, int size);
}
