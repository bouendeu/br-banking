package com.itbcafrica.Dao;

import org.springframework.data.repository.CrudRepository;

import com.itbcafrica.domain.User;

public interface UserDao extends CrudRepository<User, Long> {

	User findByUsername(String username);
	User findByEmail(String email);
}
