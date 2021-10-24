package com.csumut.batches.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.csumut.batches.util.DataHolder;
import com.csumut.batches.util.NotPromotedKeyConstants;
import com.csumut.homeappliancegroups.service.HomeApplianceGroupService;

/**
 * This rest end point set is used to trigger the sample job again and if you want to clean the destination database table for 
 * a clearer demonstration. 
 * 
 * @author UMUT
 *
 */
@RestController
@RequestMapping("/job/")
public class JobRest {

	private HomeApplianceGroupService homeApplianceGroupService;
	private JobLauncher jobLauncher;
	private Job sampleJob;
	private DataHolder dataHolder;

	public JobRest(HomeApplianceGroupService homeApplianceGroupService, JobLauncher jobLauncher, Job sampleJob, DataHolder dataHolder) {
		this.homeApplianceGroupService = homeApplianceGroupService;
		this.jobLauncher = jobLauncher;
		this.sampleJob = sampleJob;
		this.dataHolder = dataHolder;
	}

	@GetMapping("/triggerSampleJob")
	@ResponseBody
	public String trigger() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {

		JobExecution run = jobLauncher.run(sampleJob, new JobParametersBuilder().toJobParameters());
		return run.getId() + " : " + run.getExitStatus().toString();
	}

	@GetMapping("/cleanTable")
	@ResponseBody
	public Boolean clean() {
		return homeApplianceGroupService.deleteAllTuples();

	}
	
	/**
	 * This useless rest end point is to show that:
	 * Once the key value pair is set in a spring component holder bean, 
	 * it is always available even if its out of the Spring Batch Job's Scope
	 * like here.
	 * 
	 * @return the stored value of key : DATA_HOLDER_SAMPLE_STR in Data Holder Bean.
	 */
	@GetMapping("/read")
	@ResponseBody
	public String retrieveDataWithoutJobExecutionContext() {
		return String.valueOf(dataHolder.get(NotPromotedKeyConstants.DATA_HOLDER_SAMPLE_STR_KEY));
	}
}