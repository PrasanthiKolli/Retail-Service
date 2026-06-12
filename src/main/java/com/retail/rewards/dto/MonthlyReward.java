package com.retail.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

/**
 * DTO representing monthly reward details for a specific month.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyReward {
    private String yearMonth;
    private double points;
}
