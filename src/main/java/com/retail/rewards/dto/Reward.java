package com.retail.rewards.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO representing reward response for a customer.
 * Includes:
 * - Customer Id
 * - Monthly reward points
 * - Total reward points
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reward {

    private String customerId;
    private List<MonthlyReward> monthlyRewards;
    private int totalPoints;
}
