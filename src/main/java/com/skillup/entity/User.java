package com.skillup.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="user")
@EqualsAndHashCode(callSuper=false)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private long userId;
	
   	private String displayUserId;
   	
   	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   	@JoinColumn(name = "distributor_id", table = "user")
   	private DistributorSequence distbutorSeq;
   	
   	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   	@JoinColumn(name = "retailer_id", table = "user")
   	private RetailarSequence retailerSeq;
	
	@NotBlank(message = "userName is mandatory")
	private String userName;
	
	private String password;
	
	@Column(columnDefinition="varchar(255) DEFAULT '123123'")
	private String pointPassword;
	
	@OneToOne
	@JoinColumn(name = "reporting_user", table = "user")
	@JsonBackReference
	private User reportingUser;
	
	private int maxwining;
	private int winingDistribution;
	
	private int commition;
	
	@Column(columnDefinition="int DEFAULT 10")
	private int cancelLimit;
	
	@NotBlank(message = "type is mandatory")
	private String type;
	
	private int status;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	private String modifiedBy; 
	
	@Transient
	private String refernceUserId;
	
	private int winning_percent;
	
	private int winning_limit_upto;
	
}
