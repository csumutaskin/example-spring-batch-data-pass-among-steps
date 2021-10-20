package com.csumut.batches.chunkprocessing.writers.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import com.csumut.homeappliancegroups.model.HomeApplianceGroup;

/**
 * Prepared Statement Value setter for ItemWriter.
 * 
 * @author UMUT
 *
 */
public class HomeApplianceGroupStatementSetter  implements ItemPreparedStatementSetter<HomeApplianceGroup> {
 
    @Override
    public void setValues(HomeApplianceGroup current, PreparedStatement preparedStatement) throws SQLException {        
        preparedStatement.setString(1, current.getCategory());
        preparedStatement.setLong(2, current.getCount());
        preparedStatement.setBigDecimal(3, current.getTotalPrice());
        preparedStatement.setString(4, current.getPercentage());
    }
}