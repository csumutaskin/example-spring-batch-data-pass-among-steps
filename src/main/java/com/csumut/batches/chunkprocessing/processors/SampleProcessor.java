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

public class SampleProcessor implements ItemProcessor<HomeApplianceGroup, HomeApplianceGroup>, StepExecutionListener{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private StepExecution stepExecution;
	
	@Override
	public HomeApplianceGroup process(HomeApplianceGroup item) throws Exception {
		ExecutionContext jobExecutionContext = getJobExecutionContext(stepExecution);
		Long totalDeviceCount = Long.valueOf(jobExecutionContext.getString(PromotionListenerKeyConstants.COUNT_OF_HOME_APPLIANCES_KEY));
		
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
