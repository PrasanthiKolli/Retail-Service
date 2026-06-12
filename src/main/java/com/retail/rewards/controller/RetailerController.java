package com.retail.rewards.controller;


import com.retail.rewards.dto.PageableReward;
import com.retail.rewards.dto.Reward;
import com.retail.rewards.service.RetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller contains APIs to fetch the rewards.
 */

@RestController
@RequestMapping("/rewards")
public class RetailerController {

    @Autowired
    RetailerService retailerService;

    /**
     * Retrieve reward points for all customers in the form of pages.
     *
     * @return list of rewards for all customers
     */

    @GetMapping()
    public ResponseEntity<PageableReward> getRewards(@RequestParam(defaultValue ="0") int page, @RequestParam(defaultValue = "5") int size) {

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
    public ResponseEntity<Reward> getRewardByCustomerId(@PathVariable Long customerId) {

        Reward result = retailerService.getRewardByCustomerId(customerId);

        return ResponseEntity.ok(result);

    }

}
