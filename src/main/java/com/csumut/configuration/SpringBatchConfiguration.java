package com.csumut.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
	private static final int CHUNK_SIZE = 2; // This is not a good chunk size, however the main purpose of this example
											 // is not finding an optimum ETL solution, it is about data passing to
											 // future steps

	private static final String ITEM_READER_SQL = "Select CATEGORY, COUNT(*) AS TOT_COUNT, SUM(PRICE) AS TOT_PRICE from HOME_APPLIANCE group by CATEGORY";
	private static final String ITEM_WRITER_SQL = "INSERT INTO HOME_APPLIANCE_GROUP(ID, CATEGORY, COUNT, TOTAL_PRICE, PERCENTAGE) VALUES (HOME_APPLIANCE_GROUP_SEQ.nextval, ?, ?, ?, ?)";

	private JobBuilderFactory jobBuilderFactory;
	private StepBuilderFactory stepBuilderFactory;
	private Tasklet firstTasklet;
	private Tasklet thirdTasklet;

	public SpringBatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Job sampleJob(Step firstStep, Step secondStep, Step thirdStep) {
		return jobBuilderFactory.get(JOB_NAME_KEY).incrementer(new RunIdIncrementer()).start(firstStep).next(secondStep)
				.next(thirdStep).build();
	}

	@Bean
	public Step firstStep(ExecutionContextPromotionListener promotionListener) {
		/*
		 * This step's allowStartIfComplete property is set to false to show that: if
		 * you restart the job using the rest end point, this step will not run if it is
		 * already completed, but the job execution context of this JOB still contains
		 * the promoted value given from this step. Thus, for future restarts of the
		 * same job instance, you will still reach that key-value pair
		 * (COUNT_OF_HOME_APPLIANCES key) that is already stored from the very first run
		 * of this step in previous executions.
		 */
		return stepBuilderFactory.get(FIRST_STEP_NAME_KEY).allowStartIfComplete(false).tasklet(firstTasklet)
				.listener(promotionListener).build();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public Step secondStep(ItemReader<HomeApplianceGroup> sampleReader, ItemProcessor sampleProcessor,
			ItemWriter<HomeApplianceGroup> sampleWriter) {
		return stepBuilderFactory.get(SECOND_STEP_NAME_KEY).allowStartIfComplete(true).chunk(CHUNK_SIZE)
				.reader(sampleReader).processor(sampleProcessor).writer(sampleWriter).build();
	}

	@Bean
	public Step thirdStep() {
		return stepBuilderFactory.get(THIRD_STEP_NAME_KEY).allowStartIfComplete(true).tasklet(thirdTasklet).build();
	}

	@Bean
	public ExecutionContextPromotionListener promotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
		//Setting keys is the essential part: the promotion listener listens to the keys that are given below, and promotes them to the job's execution context after the step execution ends.
		listener.setKeys(new String[] { PromotionListenerKeyConstants.COUNT_OF_HOME_APPLIANCES_KEY });
		return listener;
	}

	/*
	 * TASKLETS and CHUNK BASED STEP's are are declared below.
	 */

	@Autowired
	public void setFirstTasklet(Tasklet firstTasklet) {
		this.firstTasklet = firstTasklet;
	}

	@Bean
	public ItemProcessor<HomeApplianceGroup, HomeApplianceGroup> sampleProcessor() {
		return new SampleProcessor();
	}

	@Autowired
	public void setThirdTasklet(Tasklet thirdTasklet) {
		this.thirdTasklet = thirdTasklet;
	}

	@Bean
	public ItemReader<HomeApplianceGroup> sampleReader(DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<HomeApplianceGroup>().name("cursorHomeApplianceItemReader")
				.dataSource(dataSource).sql(ITEM_READER_SQL).rowMapper(new HomeApplianceGroupRowMapper()).build();
	}

	@Bean
	public ItemWriter<HomeApplianceGroup> sampleWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
		ItemPreparedStatementSetter<HomeApplianceGroup> preparedStatementSetter = new HomeApplianceGroupStatementSetter();
		JdbcBatchItemWriter<HomeApplianceGroup> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setJdbcTemplate(jdbcTemplate);
		itemWriter.setSql(ITEM_WRITER_SQL);
		itemWriter.setItemPreparedStatementSetter(preparedStatementSetter);
		return itemWriter;
	}
}