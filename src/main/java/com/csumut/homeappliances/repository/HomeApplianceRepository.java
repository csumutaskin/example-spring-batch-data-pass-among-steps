package com.csumut.homeappliances.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.csumut.homeappliances.model.HomeAppliance;

/**
 * Repository Layer for {@link HomeAppliance} entity. 
 * 
 * @author UMUT
 *
 */
@Repository
public interface HomeApplianceRepository extends CrudRepository<HomeAppliance, Long> {
}