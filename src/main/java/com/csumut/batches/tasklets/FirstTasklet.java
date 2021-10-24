package com.csumut.batches.tasklets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.csumut.batches.util.PromotionListenerKeyConstants;
import com.csumut.homeappliances.service.HomeApplianceService;

/**
 * A sample tasklet to demonstrate how information can be passed to future steps.
 * It passes the total number of items in Home Appliance table to the future steps.
 * <p>
 * ATTENTION: A WAY TO REACH A EXECUTION CONTEXT IS IMPLEMENTED HERE.
 * From a Spring Batch item/utility (Tasklet here) one way of reaching a step or job execution context is:
 * using the chunkContext. A chunkContext can reach the step context which encapsulates, and the job context
 * which encapsulates the step context. 
 * </p>
 * 
 * @author UMUT
 *
 */
@Component
public class FirstTasklet implements Tasklet {	
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private HomeApplianceService homeApplianceService;
	
	public FirstTasklet(HomeApplianceService homeApplianceService) {
		this.homeApplianceService = homeApplianceService;
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {		
		
		logger.info("FirstTasklet");
		Long homeAppliancesCount = homeApplianceService.getHomeAppliancesCount();
		
		ExecutionContext stepExecutionContext = getStepExecutionContext(chunkContext);
		stepExecutionContext.put(PromotionListenerKeyConstants.COUNT_OF_HOME_APPLIANCES_KEY, homeAppliancesCount);
				
		return RepeatStatus.FINISHED;
	}
	
	//Returns the current step's execution context.
	private ExecutionContext getStepExecutionContext(ChunkContext chunkContext) {
		return chunkContext.getStepContext().getStepExecution().getExecutionContext();
	}	
}
