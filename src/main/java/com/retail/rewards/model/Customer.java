package com.retail.rewards.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represent a Customer
 * Customer contains id and list of transactions.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private String customerId;
    private List<Transaction> transactions;
}
