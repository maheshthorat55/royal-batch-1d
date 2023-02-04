package com.skillup.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="pticket_details")
public class PTicketDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ticketDetailsId;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_ticket_id", referencedColumnName = "ticket_id")
	@JsonBackReference
    private PTicket ticket;
	
	private int ticketNumbers;
	private int quantity;
	private int multiplier;
	private int winingPoints;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate;

}
