package com.skillup.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultTicket {
	private long gameId;
	private long compId;
	private long userId;
	private int ticketNumder;
	private int quantity;
	private int totalSalePoints;
	private int winingPoints;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultTicket other = (ResultTicket) obj;
		return ticketNumder == other.ticketNumder;
	}
	@Override
	public int hashCode() {
		return Objects.hash(ticketNumder);
	}
	public ResultTicket(int ticketNumber, int findComp, int winingPoints2) {
		this.ticketNumder=ticketNumber;
		this.compId=findComp;
		this.winingPoints=winingPoints2;
	}
}

