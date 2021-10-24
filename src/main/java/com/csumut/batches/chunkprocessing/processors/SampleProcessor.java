package com.csumut.batches.chunkprocessing.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;

import com.csumut.batches.util.PromotionListenerKeyConstants;
import com.csumut.homeappliancegroups.model.HomeApplianceGroup;

/**
 * This is a sample item processor that enriches the content of the HomeApplianceGroup tuple, and then returns it.
 * From the job's execution context the total number of devices is read and for each processed home appliance group
 * the percentage of that group w.r.t all is calculated.
 * <p>
 * This sample item processor does not seem to be an essential processing part of a step, however this demo is 
 * implemented to show, how you store data from previous steps and how you can read it from the future steps. 
 * </p>
 * <p>
 * ATTENTION: A WAY TO REACH A EXECUTION CONTEXT IS IMPLEMENTED HERE.
 * From a Spring Batch item/utility (Item Processor here) one way of reaching a step or job execution context is:
 * implementing the StepExecutionListener interface. And by reaching the execution context, you can reach any data 
 * that is stored in this execution context.
 * </p>
 * @author UMUT
 *
 */
public class SampleProcessor implements ItemProcessor<HomeApplianceGroup, HomeApplianceGroup>, StepExecutionListener{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private StepExecution stepExecution;
	
	@Override
	public HomeApplianceGroup process(HomeApplianceGroup item) throws Exception {
		ExecutionContext jobExecutionContext = getJobExecutionContext(stepExecution);
		Long totalDeviceCount = jobExecutionContext.getLong(PromotionListenerKeyConstants.COUNT_OF_HOME_APPLIANCES_KEY);
		
		Long percentage = 0l;
		if(totalDeviceCount > 0) {
			percentage = 100 * item.getCount() / totalDeviceCount;
		}
		item.setPercentage(String.valueOf(percentage));
		
		logger.info("SampleProcessor");
		return item;
	}
	
	//retrieves Job's ExecutionContext from current Step's ExecutionContext.
	private ExecutionContext getJobExecutionContext(StepExecution stepExecution) {
		return stepExecution.getJobExecution().getExecutionContext();	
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return stepExecution.getExitStatus();
	}	
}
