package org.openmrs.module.nursingactivity.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.module.nursingactivity.dao.NursingActivityScheduleDao;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;
import org.openmrs.module.nursingactivity.model.NursingActivityStatus;
import org.openmrs.module.nursingactivity.model.NursingActivityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NursingActivityScheduleDaoImpl implements NursingActivityScheduleDao {

	private SessionFactory sessionFactory;

	@Autowired
	public NursingActivityScheduleDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<NursingActivitySchedule> getScheduleEntriesForPatient(Patient patient, Date startDate, Date endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NursingActivitySchedule.class);
		criteria.add(Restrictions.eq("patient", patient));
		criteria.add(Restrictions.between("scheduleTime", startDate, endDate));
		return criteria.list();
	}

	@Override
	public NursingActivitySchedule saveSchedule(NursingActivitySchedule schedule) {
		Session currentSession = sessionFactory.getCurrentSession();
		if (null == schedule.getScheduleId()) {
			currentSession.save(schedule);
		} else {
			currentSession.merge(schedule);
		}
		
		return schedule;
	}

	@Override
	public List<NursingActivityType> getNursingActivityTypes() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NursingActivityType.class);
		return criteria.list();
	}

	@Override
	public List<NursingActivityStatus> getNursingActivityStatus() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NursingActivityStatus.class);
		return criteria.list();
	}

}
