package com.itbcafrica.service.UserServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itbcafrica.Dao.AppointmentDao;
import com.itbcafrica.domain.Appointment;
import com.itbcafrica.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentDao appointmentDao;

	public Appointment createAppointment(Appointment appointment) {
		return appointmentDao.save(appointment);
	}

	public List<Appointment> findAll() {
		return (List<Appointment>) appointmentDao.findAll();
	}

	public Appointment findAppointment(Long id) {
		return appointmentDao.findById(id).orElse(new Appointment());
	}

	public void confirmAppointment(Long id) {
		Appointment appointment = findAppointment(id);
		appointment.setConfirmed(true);
		appointmentDao.save(appointment);
	}

}
