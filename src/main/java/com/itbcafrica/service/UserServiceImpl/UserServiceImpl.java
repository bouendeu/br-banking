package com.itbcafrica.service.UserServiceImpl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.itbcafrica.Dao.RoleDao;
import com.itbcafrica.Dao.UserDao;
import com.itbcafrica.domain.User;
import com.itbcafrica.domain.security.UserRole;
import com.itbcafrica.service.AccountService;
import com.itbcafrica.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	@Autowired
	UserDao userDao;

	@Autowired
	private AccountService accountService;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public void save(User user) {
		userDao.save(user);
	}

	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
	public boolean checkUserExists(String username, String email) {
		return checkUsernameExists(username) || checkEmailExists(email) ? true : false;
	}

	public boolean checkEmailExists(String email) {

		return findByEmail(email) != null ? true : false;
	}

	public boolean checkUsernameExists(String username) {

		return findByUsername(username) != null ? true : false;
	}

	public User createUser(User user, Set<UserRole> userRoles) {

		User localUser = userDao.findByUsername(user.getUsername());
		if (localUser != null) {
			LOG.info("user with username {}already exist,nothing will be done. " + user.getUsername());
		} else {
			String encryptedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encryptedPassword);
			for (UserRole ur : userRoles) {
				roleDao.save(ur.getRole());
			}
			user.getUserRoles().addAll(userRoles);
			user.setPrimaryAccount(accountService.createPrimaryAccount());
			user.setSavingsAccount(accountService.createSavingAccount());
			localUser = userDao.save(user);
		}

		return localUser;

	}

	public User saveUser(User user) {
		return userDao.save(user);
	}
}
