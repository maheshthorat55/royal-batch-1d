package com.skillup.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="presult_calculations")
@AllArgsConstructor
@NoArgsConstructor
public class PResultCalculations {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long resultCalId;
	
	private long gameId;
	private long userId;
	private int ticketNumber;
	private int totalSalePoints;
	private int winingPoints;
	private long ticketId;
	
	@Transient
	private String userName;
	
	public PResultCalculations(long gameId, long userId, int ticketNumber, 
					int totalSalePoints, int winingPoints, long ticketId) {
		super();
		this.gameId = gameId;
		this.userId = userId;
		this.ticketNumber = ticketNumber;
		this.totalSalePoints = totalSalePoints;
		this.winingPoints = winingPoints;
		this.ticketId = ticketId;
	}

}
