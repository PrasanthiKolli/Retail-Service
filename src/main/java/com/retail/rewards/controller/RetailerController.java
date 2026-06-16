package com.retail.rewards.controller;


import com.retail.rewards.dto.PageableReward;
import com.retail.rewards.dto.Reward;
import com.retail.rewards.service.RetailerService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller contains APIs to fetch the rewards.
 */

@RestController
@RequestMapping("/rewards")
@Validated
public class RetailerController {

    // constructor injection
    private final RetailerService retailerService;

    @Autowired
    public RetailerController(RetailerService retailerService){
        this.retailerService = retailerService;
    }

    /**
     * Retrieve reward points for all customers in the form of pages.
     *
     * @return list of rewards for all customers
     */

    @GetMapping()
    public ResponseEntity<PageableReward> getRewards(@RequestParam(defaultValue ="0") @Min(value = 0,message = "Page index cannot be negative") int page, @RequestParam(defaultValue = "5") @Min(value = 1, message = "Size must be at least 1") @Max(100) int size) {

        PageableReward reward = retailerService.getRewards(page, size);
        return new ResponseEntity<>(reward, HttpStatus.OK);
    }

    /**
     * Fetch Reward points for a specific customer.
     *
     * @param customerId unique id of customer.
     * @return reward response for the customer.
     */

    @GetMapping("/{customerId}")
    public ResponseEntity<Reward> getRewardByCustomerId(@PathVariable @Min(1) Long customerId) {

        Reward result = retailerService.getRewardByCustomerId(customerId);

        return ResponseEntity.ok(result);

    }

}
