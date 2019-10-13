package com.itbcafrica.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class PrimaryTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String description;
	private Date date;
	private String type;
	private String status;
	private double amount;
	private BigDecimal availableBalance;
	
	@ManyToOne
	@JoinColumn(name = "primary_account_id")
	private PrimaryAccount primaryAccount;

	public PrimaryTransaction(String description, Date date, String type, String status, double amount,
			BigDecimal availableBalance, PrimaryAccount primaryAccount) {
		super();
		this.description = description;
		this.date = date;
		this.type = type;
		this.status = status;
		this.amount = amount;
		this.availableBalance = availableBalance;
		this.primaryAccount = primaryAccount;
	}
	
	
	
}
