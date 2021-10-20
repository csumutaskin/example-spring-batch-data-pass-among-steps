package com.csumut.homeappliancegroups.service;

import org.springframework.stereotype.Service;

import com.csumut.homeappliancegroups.repository.HomeApplianceGroupRepository;

@Service
public class HomeApplianceGroupService {
	
	private HomeApplianceGroupRepository repository;
	
	public HomeApplianceGroupService(HomeApplianceGroupRepository repository) {
		this.repository = repository;
	}

	/**
	 * Deletes all tuples from the entity table.
	 * 
	 * @return true if deletion is successful 
	 */
	public Boolean deleteAllTuples() {
		repository.deleteAll();
		return Boolean.TRUE;
	}	
}
