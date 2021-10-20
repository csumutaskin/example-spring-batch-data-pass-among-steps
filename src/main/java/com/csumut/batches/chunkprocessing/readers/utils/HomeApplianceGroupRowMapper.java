package com.csumut.batches.chunkprocessing.readers.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.csumut.homeappliancegroups.model.HomeApplianceGroup;

/**
 * RowMapper for {@link HomeApplianceGroup} entity.
 * 
 * @author UMUT
 *
 */
public class HomeApplianceGroupRowMapper implements RowMapper<HomeApplianceGroup> {
	
	@Override
	public HomeApplianceGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
		HomeApplianceGroup current = new HomeApplianceGroup();
		current.setCategory(rs.getString("CATEGORY"));
		current.setCount(rs.getLong("TOT_COUNT"));
		current.setTotalPrice(rs.getBigDecimal("TOT_PRICE"));
		return current;
	}
}
