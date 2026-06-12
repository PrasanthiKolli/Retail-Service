package com.retail.rewards.util;

import com.retail.rewards.exception.InvalidTransactionException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Utility class for calculating rewards.
 */
@Component
public class RetailerUtil {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal FIFTY = new BigDecimal("50");
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal TWO = new BigDecimal("2");
    /**
     * Calculates reward points based on transaction amount.
     *
     * @param amount transaction amount
     * @return reward points
     */
    public static double calculatePoints(BigDecimal amount) {

        // check if amount is negative
        if (amount.compareTo(ZERO)<0) {
            throw new InvalidTransactionException("Amount cannot be negative");
        }
        double points = 0;
        if (amount.compareTo(HUNDRED)>0) {
            points += amount.subtract(HUNDRED)
                    .multiply(TWO)
                    .add(FIFTY)
                    .intValue();
        } else if (amount.compareTo(FIFTY)>0) {
            points += amount.subtract(FIFTY).intValue();
        }
        return points;
    }
}
