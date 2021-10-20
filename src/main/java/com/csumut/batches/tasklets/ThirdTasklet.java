package com.csumut.batches.tasklets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import com.csumut.batches.util.PromotionListenerKeyConstants;



/**
 * A sample tasklet to demonstrate how information can be passed to future steps.
 * 
 * @author UMUT
 *
 */
public class ThirdTasklet implements Tasklet {	
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {		
		
		ExecutionContext jobExecutionContext = getJobExecutionContext(chunkContext);
		//logger.info("First Tasklet started running at : {}",  jobExecutionContext.getString(PromotionListenerKeyConstants.COUNT_OF_HOME_APPLIANCES_KEY));
		
		logger.info("ThirdTasklet");
		return RepeatStatus.FINISHED;
	}
	
	//Returns the current job's execution context.
	private ExecutionContext getJobExecutionContext(ChunkContext chunkContext) {
		return chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
	}

}
