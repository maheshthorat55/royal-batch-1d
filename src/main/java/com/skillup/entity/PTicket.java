package com.skillup.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="pticket")
public class PTicket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticket_id")
	private long ticketId;
	
	@ManyToOne
	@JoinColumn(name = "user_user_id", table = "pticket")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "game_game_id", table = "pticket")
	@JsonBackReference
	private PGame game;
	
	private String barcode;
	private double purchasePoints;
	private int quantity;
	private String drawTime;
	private Boolean isHigh;
	private Long advanceDraw;
	
	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Column(name = "claimed", nullable = false)
	@ColumnDefault(value = "false")
	@Generated(GenerationTime.INSERT)
	private Boolean claimed;
	
	@Column(name = "canceled", nullable = false)
	@ColumnDefault(value = "false")
	@Generated(GenerationTime.INSERT)
	private Boolean canceled;
	
	@Column(name = "type", nullable = false)
	@ColumnDefault(value = "1")
	@Generated(GenerationTime.INSERT)
	private Integer type;
	
	@OneToMany(targetEntity=TicketDetails.class, cascade = CascadeType.ALL, mappedBy = "ticket")
	private List<PTicketDetails> tickets = new ArrayList<>();

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate;
}
