package com.skillup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleResult {
	private long userId;
	private String saleDate;
	private double salePoints;
	private String displayUserId;
	private String userName;
	private double commition;
	private double dCommition;
	private int userType;
	private long reportingUser;
	private long gameId;
	private double totalWinning;
	private double netPoints;
	@Builder.Default
	private int winPercentage=0;
	@Builder.Default
	private int maxWinning=0;
}
