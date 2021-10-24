package com.csumut.batches.chunkprocessing.readers.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.RowMapper;

import com.csumut.configuration.SpringBatchConfiguration;
import com.csumut.homeappliancegroups.model.HomeApplianceGroup;

/**
 * RowMapper for {@link HomeApplianceGroup} object. This row mapper will be consumed by the
 * {@link JdbcCursorItemReader} bean configured in {@link SpringBatchConfiguration} class
 * whose essential role is mapping the item data retrieved from the related db table to
 * the Java object. 
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
