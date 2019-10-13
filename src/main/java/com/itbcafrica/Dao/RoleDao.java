package com.itbcafrica.Dao;

import org.springframework.data.repository.CrudRepository;

import com.itbcafrica.domain.security.Role;

public interface RoleDao extends CrudRepository<Role, Long> {

	Role findByName(String string);

}
