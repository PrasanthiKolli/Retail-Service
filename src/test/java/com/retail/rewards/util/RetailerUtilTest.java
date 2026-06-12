package com.retail.rewards.util;

import com.retail.rewards.exception.InvalidTransactionException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RetailerUtilTest {

    @Test
    void testCalculatePointsNegativeAmountThrowsException() {
        assertThrows(InvalidTransactionException.class, () -> RetailerUtil.calculatePoints(new BigDecimal("-10")));
    }

    @Test
    void testCalculatePointsLessThan50() {
        double points = RetailerUtil.calculatePoints(new BigDecimal("40"));
        assertEquals(0, points);
    }
    @Test
    void testCalculatePointsBetween50And100() {
        double points = RetailerUtil.calculatePoints(new BigDecimal("75"));
        // Expected: 75 - 50 = 25
        assertEquals(25, points);
    }

    @Test
    void testCalculatePointsExactly50() {
        double points = RetailerUtil.calculatePoints(new BigDecimal("50"));
        // Expected: 50 - 50 = 0
        assertEquals(0, points);
    }

    @Test
    void testCalculatePointsExactly100() {
        double points = RetailerUtil.calculatePoints(new BigDecimal("100"));
        // Expected: 100 - 50 = 50
        assertEquals(50, points);
    }

    @Test
    void testCalculatePointsGreaterThan100() {
        double points = RetailerUtil.calculatePoints(new BigDecimal("120"));
        // Expected: 2*(120-100) + 50 = 2*20 + 50 = 90
        assertEquals(90, points);
    }
}