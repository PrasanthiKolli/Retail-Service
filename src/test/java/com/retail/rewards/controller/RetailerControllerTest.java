package com.retail.rewards.controller;


import com.retail.rewards.dto.MonthlyReward;
import com.retail.rewards.dto.PageableReward;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


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
        MonthlyReward janReward = new MonthlyReward("2026-January", 50);
        MonthlyReward febReward = new MonthlyReward("2026-February", 70);

        Reward reward1 = new Reward(1L,"Alice", Arrays.asList(janReward, febReward), 120);
        Reward reward2 = new Reward(2L,"Bob", Collections.singletonList(new MonthlyReward("March", 200)), 200);
        Reward reward3 = new Reward(3L,"Alice", Arrays.asList(janReward, febReward), 120);
        Reward reward4 = new Reward(4L,"Bob", Collections.singletonList(new MonthlyReward("March", 200)), 200);
        List<Reward> rewards = Arrays.asList(reward1, reward2,reward3,reward4);
        PageableReward pageableReward = new PageableReward();
        pageableReward.setCustomerList(rewards);
        pageableReward.setTotalElements(4);
        pageableReward.setPageSize(2);
        pageableReward.setTotalPages(2);
        pageableReward.setCurrentPage(1);
        when(retailerService.getRewards(0,2)).thenReturn(pageableReward);

        // Act
        ResponseEntity<PageableReward> response = retailerController.getRewards(0,2);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getPageSize());
        assertEquals(1,response.getBody().getCurrentPage());
        assertEquals(4,response.getBody().getTotalElements());
        assertEquals(2,response.getBody().getTotalPages());
        verify(retailerService, times(1)).getRewards(0,2);
    }

    @Test
    void testGetRewardByCustomerId() {
        // Arrange
        MonthlyReward aprReward = new MonthlyReward("2026-April", 150);
        Reward reward1 = new Reward(1L,"Alice", Arrays.asList(aprReward), 150);

        when(retailerService.getRewardByCustomerId(1L)).thenReturn(reward1);

        // Act
        ResponseEntity<Reward> response = retailerController.getRewardByCustomerId(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getCustomerId());
        assertEquals(150, response.getBody().getTotalPoints());
        assertEquals("2026-April", response.getBody().getMonthlyRewards().get(0).getYearMonth());
        verify(retailerService, times(1)).getRewardByCustomerId(1L);
    }
}
