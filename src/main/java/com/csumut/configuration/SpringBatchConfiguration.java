package com.csumut.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for spring batch.
 * 
 * @author UMUT
 *
 */
//@Configuration
//@EnableBatchProcessing
public class SpringBatchConfiguration {

	private static final String JOB_NAME_KEY = "Sample_Job";
	private static final String FIRST_STEP_NAME_KEY = "First_Step";
	private static final String SECOND_STEP_NAME_KEY = "Second_Step";
	private static final String THIRD_STEP_NAME_KEY = "Third_Step";
	
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    public SpringBatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
    	this.jobBuilderFactory = jobBuilderFactory;
    	this.stepBuilderFactory = stepBuilderFactory;
    }
	
	@Bean
	public Job sampleJob(Step firstStep, Step secondStep, Step thirdStep) {
		return jobBuilderFactory.get(JOB_NAME_KEY)
				.start(firstStep)
				.next(secondStep)
				.next(thirdStep).build();
	}
	
	@Bean
	public Step firstStep() {
		return stepBuilderFactory.get(FIRST_STEP_NAME_KEY)
				.tasklet(null)
				.build();
	}
	
	@Bean 
	public Step secondStep() {
		return stepBuilderFactory.get(SECOND_STEP_NAME_KEY)
				.tasklet(null)
				.build();
	}
	
	@Bean 
	public Step thirdStep() {
		return stepBuilderFactory.get(THIRD_STEP_NAME_KEY)
				.tasklet(null)
				.build();
	}
}
