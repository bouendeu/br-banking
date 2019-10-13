package com.itbcafrica.Dao;

import org.springframework.data.repository.CrudRepository;

import com.itbcafrica.domain.SavingsAccount;

public interface SavingsAccountDao extends CrudRepository<SavingsAccount, Long> {
	SavingsAccount findByAccountNumber(int accountNumber);
}
