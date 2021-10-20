package com.csumut.batches.controller;

import org.springframework.batch.core.Job;
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

	@GetMapping("/triggerJob")
	@ResponseBody
	public Boolean trigger() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {

		jobLauncher.run(sampleJob, new JobParametersBuilder().toJobParameters());
		return Boolean.TRUE;
	}

	@GetMapping("/cleanTable")
	@ResponseBody
	public Boolean clean() {
		return homeApplianceGroupService.deleteAllTuples();

	}
}