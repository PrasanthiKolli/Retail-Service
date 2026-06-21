package com.retail.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing monthly reward details for a specific month.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyReward {
    private String yearMonth;
    private Long points;
}
