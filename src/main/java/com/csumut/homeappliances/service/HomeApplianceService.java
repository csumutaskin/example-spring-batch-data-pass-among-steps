package com.csumut.homeappliances.service;

import org.springframework.stereotype.Service;

import com.csumut.homeappliances.model.HomeAppliance;
import com.csumut.homeappliances.repository.HomeApplianceRepository;

/**
 * Sample Service Layer for HomeAppliance model.
 * @author UMUT
 *
 */
@Service
public class HomeApplianceService {

	private HomeApplianceRepository repository;
	
	public HomeApplianceService(HomeApplianceRepository repository) {
		this.repository = repository;
	}

	/**
	 * Retrieves all the HomeAppliances without any criteria.
	 * 
	 * @return an {@link Iterable} object of all the HomeAppliances. 
	 */
	public Iterable<HomeAppliance> getAllHomeAppliances() {
		return repository.findAll();
	}
	
	/**
	 * Retrieves the count of home appliance tuples in database.
	 * 
	 * @return number of tuples 
	 */
	public long getHomeAppliancesCount() {
		return repository.count();
	}
}
