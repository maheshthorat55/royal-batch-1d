package com.skillup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TicketResult {
	private long totalSale;
	private long totalWin;
	private String tickets;
	private long userId;
	private long winDiff;
}
