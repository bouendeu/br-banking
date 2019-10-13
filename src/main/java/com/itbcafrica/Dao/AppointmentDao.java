package com.itbcafrica.Dao;

import org.springframework.data.repository.CrudRepository;

import com.itbcafrica.domain.Appointment;

public interface AppointmentDao extends CrudRepository<Appointment, Long> {

}
