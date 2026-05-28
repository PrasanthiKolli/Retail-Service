package com.retail.rewards.controller;


import com.retail.rewards.dto.Reward;
import com.retail.rewards.service.RetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller contains APIs to fetch the rewards.
 */

@RestController
@RequestMapping("/rewards")
public class RetailerController {

    @Autowired
    RetailerService retailerService;

    /**
     * Retrives reward points for all customers.
     *
     * @return list of rewars for all customers
     */

    @GetMapping
    public ResponseEntity<List<Reward>> getRewards() {

        List<Reward> rewards = retailerService.getRewards();
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    /**
     * Fetch Reward points for a specific customer.
     *
     * @param customerId unique id of customer.
     * @return reward response for the customer.
     */

    @GetMapping("/{customerId}")
    public ResponseEntity<Reward> getRewardByCustomerId(@PathVariable String customerId) {

        Reward result = retailerService.getRewardByCustomerId(customerId);

        return ResponseEntity.ok(result);

    }

}
