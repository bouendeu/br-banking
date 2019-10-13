package com.itbcafrica.Dao;

import org.springframework.data.repository.CrudRepository;

import com.itbcafrica.domain.Recipient;

public interface RecipientDao extends CrudRepository<Recipient, Long> {

	Recipient findByName(String recipientName);

	void deleteByName(String recipientName);

}
