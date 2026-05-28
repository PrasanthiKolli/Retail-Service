package com.retail.rewards.controller;


import com.retail.rewards.dto.MonthlyReward;
import com.retail.rewards.dto.Reward;
import com.retail.rewards.service.RetailerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class RetailerControllerTest {

    @Mock
    private RetailerService retailerService;

    @InjectMocks
    private RetailerController retailerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRewards() {
        // Arrange
        MonthlyReward janReward = new MonthlyReward("January", 50);
        MonthlyReward febReward = new MonthlyReward("February", 70);

        Reward reward1 = new Reward("CUST1", Arrays.asList(janReward, febReward), 120);
        Reward reward2 = new Reward("CUST2", Collections.singletonList(new MonthlyReward("March", 200)), 200);
        List<Reward> rewards = Arrays.asList(reward1, reward2);

        when(retailerService.getRewards()).thenReturn(rewards);

        // Act
        ResponseEntity<List<Reward>> response = retailerController.getRewards();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("CUST1", response.getBody().get(0).getCustomerId());
        verify(retailerService, times(1)).getRewards();
    }

    @Test
    void testGetRewardByCustomerId() {
        // Arrange
        MonthlyReward aprReward = new MonthlyReward("April", 150);
        Reward reward = new Reward("CUST123", Collections.singletonList(aprReward), 150);

        when(retailerService.getRewardByCustomerId("CUST123")).thenReturn(reward);

        // Act
        ResponseEntity<Reward> response = retailerController.getRewardByCustomerId("CUST123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CUST123", response.getBody().getCustomerId());
        assertEquals(150, response.getBody().getTotalPoints());
        assertEquals("April", response.getBody().getMonthlyRewards().get(0).getMonth());
        verify(retailerService, times(1)).getRewardByCustomerId("CUST123");
    }
}
