package com.csumut.batches.tasklets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.csumut.batches.util.DataHolder;
import com.csumut.batches.util.NotPromotedKeyConstants;
import com.csumut.batches.util.PromotionListenerKeyConstants;



/**
 * A sample tasklet to demonstrate how information can be passed to future steps.
 * Here we only read the data that is stored from previous steps.
 * Not a practical tasklet in real life.
 * 
 * @author UMUT
 *
 */
@Component
public class ThirdTasklet implements Tasklet {	
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private DataHolder dataHolder;
	
	public ThirdTasklet(DataHolder dataHolder) {
		this.dataHolder = dataHolder;
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {		
		
		logger.info("ThirdTasklet");
		
		ExecutionContext jobExecutionContext = getJobExecutionContext(chunkContext);
		logger.info("The total number of home application items can be retrieved from the JOB's execution context: {}",  
				jobExecutionContext.getLong(PromotionListenerKeyConstants.COUNT_OF_HOME_APPLIANCES_KEY));
		
		logger.info("A non promoted data is {} after retrieved from job's execution context", 
				jobExecutionContext.get(NotPromotedKeyConstants.NOT_PROMOTED_KEY));	
		
		logger.info("A non promoted data (put direcctly to the job's execution context) is: \"{}\" after retrieved from job's execution context", 
				jobExecutionContext.get(NotPromotedKeyConstants.NOT_PROMOTED_BUT_IN_JOB_EXECUTION_CONTEXT_KEY));	
		
		logger.info("Once set, the value of key: DATA_HOLDER_SAMPLE_STR is always available: {}", dataHolder.get(NotPromotedKeyConstants.DATA_HOLDER_SAMPLE_STR_KEY));
		
		return RepeatStatus.FINISHED;
	}
	
	//Returns the current job's execution context.
	private ExecutionContext getJobExecutionContext(ChunkContext chunkContext) {
		return chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
	}

}
