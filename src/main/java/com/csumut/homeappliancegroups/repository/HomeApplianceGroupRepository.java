package com.csumut.homeappliancegroups.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.csumut.homeappliancegroups.model.HomeApplianceGroup;

/**
 * Repository Layer for {@link HomeApplianceGroup} entity. 
 * 
 * @author UMUT
 *
 */
@Repository
public interface HomeApplianceGroupRepository extends CrudRepository<HomeApplianceGroup, Long> {
}
