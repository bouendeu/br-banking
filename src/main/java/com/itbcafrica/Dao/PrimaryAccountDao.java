package com.itbcafrica.Dao;

import org.springframework.data.repository.CrudRepository;

import com.itbcafrica.domain.PrimaryAccount;

public interface PrimaryAccountDao extends CrudRepository<PrimaryAccount, Long> {
	PrimaryAccount findByAccountNumber(int accountNumber);
}
