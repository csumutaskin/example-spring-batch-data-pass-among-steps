package com.csumut.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * A sample item reader implementation that reads data from a HSQLDB an in-memory DBMS.
 * 
 * @author UMUT
 */
public class SampleReader implements ItemReader<String> {

	@Override
	public String read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
		
		
		return null;
	}

}
