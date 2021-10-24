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

	public JobRest(HomeApplianceGroupService homeApplianceGroupService, JobLauncher jobLauncher, Job sampleJob) {
		this.homeApplianceGroupService = homeApplianceGroupService;
		this.jobLauncher = jobLauncher;
		this.sampleJob = sampleJob;
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
}