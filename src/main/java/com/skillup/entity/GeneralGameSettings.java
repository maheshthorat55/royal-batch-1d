package com.skillup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="general_settings")
@NoArgsConstructor
@AllArgsConstructor
public class GeneralGameSettings {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "setting_id")
	private long settingId;
	private int winningPercentage;
	private String winningNumber;
	private long gameId;
	private String notificationMessage;
	private int resultType;
	private Integer hourlyLogic;
	private int pwinningPercentage;
	private String winningNumber3D;
	private String winningNumber1D;
}
