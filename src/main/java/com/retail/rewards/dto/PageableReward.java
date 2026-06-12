package com.retail.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * contains customer reward details in the form of pages
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableReward {
    private List<Reward> customerList;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}
