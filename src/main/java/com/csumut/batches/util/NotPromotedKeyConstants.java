package com.csumut.batches.util;

/**
 * Contains the contstants that denote the keys that are not listened by
 * the promotion listener. 
 * 
 * @author UMUT
 *
 */
public class NotPromotedKeyConstants {
	
	private NotPromotedKeyConstants() {
		//hidden constructor
	}
	
	public static final String NOT_PROMOTED_KEY = "NOT_PROMOTED";	
	public static final String NOT_PROMOTED_BUT_IN_JOB_EXECUTION_CONTEXT_KEY = "NOT_PROMOTED_BUT_WILL_BE_PRESERVED";
	public static final String DATA_HOLDER_SAMPLE_STR_KEY = "DATA_HOLDER_SAMPLE_STR";
}
