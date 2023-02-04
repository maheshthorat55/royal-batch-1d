package com.skillup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSaleBean {
	private Integer sale;
	private Long winning;
	private Integer ticketNumber;
	private Long userId;
	@Override
	public String toString() {
		return "UserSaleBean [sale=" + sale + ", winning=" + winning + ", ticketNumber=" + ticketNumber + ", userId="
				+ userId + "]";
	}
}
