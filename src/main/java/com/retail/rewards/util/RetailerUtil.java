package com.retail.rewards.util;

import com.retail.rewards.exception.InvalidTransactionException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * Utility class for calculating rewards.
 */
@Slf4j
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
    public static Long calculatePoints(BigDecimal amount) {
        log.debug("calculating points based on the transaction amount: {}",amount);
        // check if amount is negative
        if (amount.compareTo(ZERO) < 0) {
            log.warn("transaction amount cannot be negative");
            throw new InvalidTransactionException("Amount cannot be negative");
        }
        long points = 0L;
        if (amount.compareTo(HUNDRED) > 0) {
            points += amount.subtract(HUNDRED)
                    .multiply(TWO)
                    .add(FIFTY).longValue();
        } else if (amount.compareTo(FIFTY) > 0) {
            points += amount.subtract(FIFTY).longValue();
        }
        log.debug("calculatePoints with result={}", points);
        return points;
    }
}
