package com.retail.rewards.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represnts a transaction mde by a customer
 * <p>
 * contains transaction amount and date.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private double amount;
    private LocalDate date;
}
