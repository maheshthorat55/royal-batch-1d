package com.skillup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPreviousWinning {
	private Long userId; 
	private Integer salePoints;
	private Long winning;
	private Double commition;
	private Integer winningPercent;
	private Integer winningLimitUpto;
	private Long differanceWinning;
	private Long target;
	@Override
	public String toString() {
		return "UserPreviousWinning [userId=" + userId + ", salePoints=" + salePoints + ", winning=" + winning
				+ ", commition=" + commition + ", winningPercent=" + winningPercent + ", winningLimitUpto="
				+ winningLimitUpto + ", differanceWinning=" + differanceWinning + ", target=" + target + "]";
	}
	
}
