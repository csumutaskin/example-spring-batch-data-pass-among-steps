package com.csumut.batches.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * A sample data holder bean that holds key value data as part of its state.
 * 
 * @author UMUT
 *
 */
@Component
public class DataHolder {
	
	private Map<String, Object> dataMap;
	
	public DataHolder() {
		dataMap = new ConcurrentHashMap<>();
	}
	
	/**
	 * Stores key value data as part of the component's state.
	 * @param key key string to store
	 * @param value value object to store
	 */
	public void put(String key, Object value) {
		dataMap.put(key, value);
	}
	
	/**
	 * Retrieves the value that matches the key.
	 * @param key key to seek the value
	 * @return the value that matches the key
	 */
	public Object get(String key) {
		return dataMap.get(key);
	}
}
