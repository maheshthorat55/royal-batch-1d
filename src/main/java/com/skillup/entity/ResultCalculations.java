package com.skillup.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="result_calculations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultCalculations {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long resultCalId;
	
	private long gameId;
	private long compId;
	private long userId;
	private int ticketNumber;
	private int quantity;
	private int totalSalePoints;
	private int winingPoints;
	private long ticketId;
	
	public ResultCalculations(long gameId, long compId, long userId, int ticketNumber, int quantity,
			int totalSalePoints, int winingPoints, long ticketId) {
		super();
		this.gameId = gameId;
		this.compId = compId;
		this.userId = userId;
		this.ticketNumber = ticketNumber;
		this.quantity = quantity;
		this.totalSalePoints = totalSalePoints;
		this.winingPoints = winingPoints;
		this.ticketId = ticketId;
	}

}
