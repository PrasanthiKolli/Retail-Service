package com.retail.rewards.util;

import com.retail.rewards.exception.InvalidTransactionException;
import org.springframework.stereotype.Component;

/**
 * Utility class for calculating rewards.
 */
@Component
public class RetailerUtil {

    /**
     * Calculates reward points based on transaction amount.
     *
     * @param amount trnsaction amount
     * @return reward points
     */

    public int calculatePoints(double amount) {
        if (amount < 0) {
            throw new InvalidTransactionException("Amount cannot be negative");
        }
        int points = 0;
        if (amount > 100) {
            points += (int) (2 * (amount - 100) + 50);
        } else if (amount >= 50) {
            points += (int) (amount - 50);
        }
        return points;
    }
}
