package com.csumut.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.csumut.batches.chunkprocessing.processors.SampleProcessor;
import com.csumut.batches.chunkprocessing.readers.utils.HomeApplianceGroupRowMapper;
import com.csumut.batches.chunkprocessing.writers.utils.HomeApplianceGroupStatementSetter;
import com.csumut.batches.tasklets.ThirdTasklet;
import com.csumut.batches.util.PromotionListenerKeyConstants;
import com.csumut.homeappliancegroups.model.HomeApplianceGroup;

/**
 * Configuration class for spring batch.
 * 
 * @author UMUT
 *
 */
@Configuration
@EnableBatchProcessing
public class SpringBatchConfiguration {

	private static final String JOB_NAME_KEY = "Sample_Job";
	private static final String FIRST_STEP_NAME_KEY = "First_Step";
	private static final String SECOND_STEP_NAME_KEY = "Second_Step";
	private static final String THIRD_STEP_NAME_KEY = "Third_Step";
	private static final int CHUNK_SIZE = 2;

	private JobBuilderFactory jobBuilderFactory;
	private StepBuilderFactory stepBuilderFactory;
	private Tasklet firstTasklet;	

	public SpringBatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Job sampleJob(Step firstStep, Step secondStep, Step thirdStep) {
		return jobBuilderFactory.get(JOB_NAME_KEY).start(firstStep).next(secondStep).next(thirdStep).build();
	}

	@Bean
	public Step firstStep(ExecutionContextPromotionListener promotionListener) {
		return stepBuilderFactory.get(FIRST_STEP_NAME_KEY).tasklet(firstTasklet).listener(promotionListener).build();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public Step secondStep(ItemReader<HomeApplianceGroup> sampleReader, ItemProcessor sampleProcessor, ItemWriter<HomeApplianceGroup> sampleWriter) {
		return stepBuilderFactory.get(SECOND_STEP_NAME_KEY).chunk(CHUNK_SIZE).reader(sampleReader)
				.processor(sampleProcessor).writer(sampleWriter).build();
	}

	@Bean
	public Step thirdStep(Tasklet thirdTasklet) {
		return stepBuilderFactory.get(THIRD_STEP_NAME_KEY).tasklet(thirdTasklet).build();
	}
	
	@Bean
	public ExecutionContextPromotionListener promotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
		listener.setKeys(new String[] {PromotionListenerKeyConstants.COUNT_OF_HOME_APPLIANCES_KEY});
		return listener;
	}

	/*
	 * Tasklets and chunk based step part beans declared below...
	 */
	
	@Autowired
    public void setFirstTasklet(Tasklet firstTasklet) {        
        this.firstTasklet = firstTasklet;
    }
	
	@Bean
	public ItemProcessor<HomeApplianceGroup, HomeApplianceGroup> sampleProcessor() {
		return new SampleProcessor();
	}

	@Bean
	public Tasklet thirdTasklet() {
		return new ThirdTasklet();
	}

	@Bean
	public ItemReader<HomeApplianceGroup> sampleReader(DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<HomeApplianceGroup>().name("cursorHomeApplianceItemReader")
				.dataSource(dataSource).sql("Select CATEGORY, COUNT(*) AS TOT_COUNT, SUM(PRICE) AS TOT_PRICE from HOME_APPLIANCE group by CATEGORY").rowMapper(new HomeApplianceGroupRowMapper())
				.build();
	}

	@Bean
	public ItemWriter<HomeApplianceGroup> sampleWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
		
        ItemPreparedStatementSetter<HomeApplianceGroup> preparedStatementSetter = new HomeApplianceGroupStatementSetter();
		JdbcBatchItemWriter<HomeApplianceGroup> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setJdbcTemplate(jdbcTemplate);
		itemWriter.setSql("INSERT INTO HOME_APPLIANCE_GROUP(ID, CATEGORY, COUNT, TOTAL_PRICE, PERCENTAGE) VALUES (HOME_APPLIANCE_GROUP_SEQ.nextval, ?, ?, ?, ?)");
		itemWriter.setItemPreparedStatementSetter(preparedStatementSetter);
	    return itemWriter;
	}
}