package com.retail.rewards.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO representing reward response for a customer.
 * Includes:
 * - Customer Id
 * - Customer Name
 * - Monthly reward points
 * - Total reward points
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reward {

    private Long customerId;
    private String customerName;
    private List<MonthlyReward> monthlyRewards;
    private double totalPoints;
}
