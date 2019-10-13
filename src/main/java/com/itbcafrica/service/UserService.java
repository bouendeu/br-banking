package com.itbcafrica.service;

import java.util.Set;

import com.itbcafrica.domain.User;
import com.itbcafrica.domain.security.UserRole;

public interface UserService {

	void save(User user);

	User findByUsername(String username);

	User findByEmail(String email);

	boolean checkEmailExists(String email);

	boolean checkUsernameExists(String username);

	boolean checkUserExists(String username, String email);

	public User createUser(User user, Set<UserRole> userRoles);

	User saveUser(User user);

}