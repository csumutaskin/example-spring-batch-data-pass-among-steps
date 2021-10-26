# Introduction

Spring Batch is one of the most preferred frameworks to develop a robust batch application. Since its origins reaches back to the last 2 decades, it is still considered to be a valid, robust and lightweight solution when implementing batch applications. Data transferring from one source to another can be done in various ways: a popular solution, nowadays, might be an event driven data transferring, or implementing your own solution w.r.t. the given problem, but all these approaches need to pay extra attention, like sometimes more coding, more maintenance and/or controlling on third party APIs and libraries. Among these approaches, if you need a *more lightweight* solution, I think Spring Batch is still the way to go. For more information on Spring Batch and Spring framework, please read the official documentation, and follow the code repository.

* [Spring Framework Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html)
* [Spring Batch Framework Code Repository](https://github.com/spring-projects/spring-batch)

### What this project is about?

This project is just an example implementation that shows different ways of passing data inside a Spring Batch Job among steps. One of the key problems that people meet in Spring Batch Applications is information passing. Since steps seem to be decoupled from each other, an extra implementation or way might be needed to pass data from predecessor steps to the future steps. Spring Official Documentation also provides information [here](https://docs.spring.io/spring-batch/docs/current/reference/html/common-patterns.html#passingDataToFutureSteps) about how to pass data between steps, so please consider this example as one of the implementations that is instructed in the official guide, along with some other alternates that are discussed in popular software forums, and of course with more detailed explanation.

### What is needed to run this application?

To compile and run this application you need to have:

* [JDK 15](https://jdk.java.net/java-se-ri/15) (You can of course change the compiler version to the installed version on your local environment, but slight modifications might be needed if you do so.)
* [Maven](https://maven.apache.org/download.cgi)
* A Java IDE (Preferably but not mandatory)

installed on your system.

### How to run the project?

* Clone the project to your local
* Run:
	`mvn clean install spring-boot:run`
	
### Links you might need throughout the project execution:

This application is implemented using spring boot with batch and web support. 
There is an in memory h2 database which is added to the runtime scope of the project. H2 is running in PostgreSQL mode (Check application.yml for details).
There is an end point set that you can use to re-trigger the sample job that is implemented, and clean the destination table in h2 database.
Assuming you did not change the application port (still running on 8080), you can reach:

* [The H2 console @ http://localhost:8080/h2](localhost:8080/h2)
* [Trigger Sample Job URL @ http://localhost:8080/job/triggerSampleJob](http://localhost:8080/job/triggerSampleJob)
* [Clean the tuples in destination table @ http://localhost:8080/job/cleanTable](http://localhost:8080/job/cleanTable)

after you run the application.

# About the Project

## The story

A home appliance store reporting department is asked to prepare report of different types of devices they have and calculate the percentage of each category (like electronic, furniture, mechanic etc...) among all. The data might be prepared at night, using batch programming, after the daily sale is over. So we need to create a batch application that will query the HOME_APPLIANCE table of the database, 
calculate some aggregation data (total count and price w.r.t the category) on the current catalog information, calculate the category item percentage among all sale item population and load the transformed data to the destination HOME_APPLIANCE_GROUP table for the report. Consider this scenario to be our basic "batch problem".

What we do here is to prepare a batch job with 3 steps:
1. This tasklet based step gets the total count of items in the store and passes this information to the future steps.
2. A chunk based 2nd step reads - processes and writes the data to the destination table in chunks.
3. Assume a 3rd tasklet based step is just implemented to log some information, nothing more.

Here is the main flow of our sample batch application step by step:

![step flows](https://github.com/csumutaskin/project-docs/blob/main/example-spring-batch-data-pass-among-steps/Design/UML/FlowCharts/Flow_chart_steps.jpg?raw=true)

# How to Pass Information Among Steps, Deep Dive!

As stated before, one of the problems that developers confront is "passing data from one step to other steps". There are of course different ways to pass data. But before explaining these solutions, it will be better to advise that: "without exceptional cases, passing information from one step to another step might be regarded as a poor batch architecture design" so you might first consider to revise your solution. 

Solutions that are not shown here in this project, but can still be considered as a proper implementation to pass information among steps might be:
 
*Using a "custom structure" in a persistent store, like file or database, to store the data that will be needed among steps. After storing it in a predecessor step, it can be retrieved by reaching the same store and reading it back using a query. However it is totally the developer's concern of how the data is stored, and the maintenance cost of the storage might be an additional problem
*Using an in memory / cache solution to store the data and read it back whenever it is necessary. Although this solution is a faster way to reach the stored data, maintenance of the cache solution might still be a problem. Design of the cache map, setting, resetting data on that map, concurrency issues can also cause additional problems.

Along with these 2 conceptual solutions, spring batch provides more convenient ways to pass data to future steps. **Using execution context** during the job execution is the preferred way to go when someone needs to pass information between steps. Another solution might be using a data holder bean controlled by the spring context. Using a data holder bean is not a suggested spring batch solution. A stateful (kind of) bean is harder to control and not convenient as expected. Thus this implementation still exists, just to show that it can store data, however it is not a recommended way to use.

Spring batch provides execution contexts to store any kind of *serializable* data. An execution context can be considered a data map that any batch component might interact during job executions. There are 2 execution contexts that the spring batch framework provides:

* Step's ExecutionContext: This context is available during the step execution. It is unreachable afterwards.
* Job's ExecutionContext: This context is available during the job execution. It is unreachable afterwards.

Spring Batch framework creates a set of database tables to control job and step executions. Execution Contexts are serialized and stored in 2 of these tables. 
 
 * Job's Execution Context is stored in BATCH_JOB_EXECUTION_TABLE
 * Step's Execution Context is stored in BATCH_STEP_EXECUTION_TABLE
 
 [Here](https://docs.spring.io/spring-batch/docs/current/reference/html/schema-appendix.html#metaDataBatchJobExecutionContext) you can see the data schema of the tables created on the official guide.
 
 ![ER from official guide](https://github.com/csumutaskin/project-docs/blob/main/example-spring-batch-data-pass-among-steps/Design/UML/FlowCharts/er-diagram.jpg?raw=true)
 
 As it is clearly seen, there are 2 different columns that store context data in each of these 2 table. They are:
 
* SHORT_CONTEXT 
* SERIALIZED_CONTEXT

Execution context is tried to be stored in short_context first. But this column is limited to varchar(2500) and this might lead to a data loss or exception if this was the only column to store the data.
If the serialized execution context exceeds the size of varchar(2500), then serialized_context comes on the scene. serialized_context is a much larger area to store the context. Serialized_context column is the place to store the execution context if the execution context becomes bigger.

[Here](https://github.com/spring-projects/spring-batch/blob/main/spring-batch-core/src/main/java/org/springframework/batch/core/repository/dao/JdbcExecutionContextDao.java) on this link if you pay attention to the method: "persistSerializedContext()" on the actual implementation of spring batch framework you can see:


	private void persistSerializedContext(final Long executionId, String serializedContext, String sql) {
		final String shortContext;
		final String longContext;
		if (serializedContext.length() > shortContextLength) {
			// Overestimate length of ellipsis to be on the safe side with
			// 2-byte chars
			shortContext = serializedContext.substring(0, shortContextLength - 8) + " ...";
			longContext = serializedContext;
		}
		else {
			shortContext = serializedContext;
			longContext = null;
		}
		getJdbcTemplate().update(getQuery(sql), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, shortContext);
				if (longContext != null) {
					lobHandler.getLobCreator().setClobAsString(ps, 2, longContext);
				}
				else {
					ps.setNull(2, getClobTypeToUse());
				}
				ps.setLong(3, executionId);
			}
		});
	}


If the size of the serialized content is more than the shortContextLength, then the beginning part of the content that fits the short context is stored in **SHORT_CONTEXT** area, whereas the whole context is stored in **SERIALIZED_CONTEXT** without any data loss. Since serialized context is a **CLOB** column, quite amount of context can be stored here without any problem.

After this brief information on execution contexts, we can continue to talk about the solution:

##Approach 1: Storing and reading the data from Job's Execution Context.

Store any key value pair in Job's Execution Context during the job execution, and directly read it from any other component of the job using the same context. On the very first tasklet based step running, you can [see](https://github.com/csumutaskin/example-spring-batch-data-pass-among-steps/blob/main/src/main/java/com/csumut/batches/tasklets/FirstTasklet.java) a job's ExecutionContext can be reached from the chunkContext given to the execute method of the tasklet using the code snippet:

 `chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();`
 
 Using the job's ExecutionContext data is stored in key value pairs as in the following code running in execute() method:
 
 `jobExecutionContext.put(AnykeyHere!, "Any Object or Value Here")`
 
 Any data stored in job execution context can be reached in successor step's components [like in here](https://github.com/csumutaskin/example-spring-batch-data-pass-among-steps/blob/main/src/main/java/com/csumut/batches/tasklets/ThirdTasklet.java):
 
 `jobExecutionContext.get(TheSameKeyWhenPuttingTheData);`
 
 This solution is a practical way to store and pass information. But this solution is a more **"data coupled"** way of step implementation. When reusing this step implementation in different modules of our batch project, we might not need to put the same data since it might not be necessary to reach the stored data in future steps. So what we need might be an alternative a less coupled way of implementation.
 
##Approach 2: Using an ExecutionContextPromotionListener to listen to and promote the data when necessary

What is we had a way to store any data in running step's ExecutionContext and any necessary data is promoted to Job's Execution Context automatically after that Step's execution? This is the right decoupled way of the step implementation. This is possible if we define a ExecutionContextPromotionListener instance and register it as a listener to the Step that we are storing the data. A more declarative explanation is [here](https://docs.spring.io/spring-batch/docs/current/reference/html/common-patterns.html#passingDataToFutureSteps) on the official guide.

What we simply do is:

###Declare a promotion listener bean and put the list of the key values that will be promoted at the end of the step execution to the listener: [Here](https://github.com/csumutaskin/example-spring-batch-data-pass-among-steps/blob/main/src/main/java/com/csumut/configuration/SpringBatchConfiguration.java)

	@Bean
	public ExecutionContextPromotionListener promotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
		listener.setKeys(new String[] { PromotionListenerKeyConstants.COUNT_OF_HOME_APPLIANCES_KEY });
		return listener;
	}

###Register it to a particular step: [Here](https://github.com/csumutaskin/example-spring-batch-data-pass-among-steps/blob/main/src/main/java/com/csumut/configuration/SpringBatchConfiguration.java)

	@Bean
	public Step firstStep(ExecutionContextPromotionListener promotionListener) {
		return stepBuilderFactory.get(FIRST_STEP_NAME_KEY).allowStartIfComplete(false)
										.tasklet(firstTasklet)
										.listener(promotionListener).build();
	}

###Store any key value data in **Step's** ExecutionContext: [Here](https://github.com/csumutaskin/example-spring-batch-data-pass-among-steps/blob/main/src/main/java/com/csumut/batches/tasklets/FirstTasklet.java)

	stepExecutionContext.put(PromotionListenerKeyConstants.COUNT_OF_HOME_APPLIANCES_KEY, homeAppliancesCount);

###Read the same data in **Job's** ExecutionContext in future steps: [Here](https://github.com/csumutaskin/example-spring-batch-data-pass-among-steps/blob/main/src/main/java/com/csumut/batches/tasklets/ThirdTasklet.java)

	jobExecutionContext.getLong(PromotionListenerKeyConstants.COUNT_OF_HOME_APPLIANCES_KEY)

> **_ATTENTION:_**	Note that no further steps can read the data from their **STEP EXECUTION CONTEXT**. They have to access the data from **JOB'S EXECUTION CONTEXT**. In addition, if the key is not listened by the promotion listener, it is not promoted to the Job's Execution Context. Thus it is already lost after the step's execution.

##Approach3: Using a Data Holder Bean

This solution is not a good way to choose, however if you somehow need to reach the stored data after the job's execution, you might still use this solution. See [DataHolder bean](https://github.com/csumutaskin/example-spring-batch-data-pass-among-steps/blob/main/src/main/java/com/csumut/batches/util/DataHolder.java) and its usage for details. This is just a simple implementation.
It can be modified and shaped according to the necessities.

> **_ATTENTION:_**	The idea in using the data holder bean is allowing spring context to create a stateful component that holds data for further flow. So it is the developer's responsibility to handle the state.

## Final Remarks:

As stated earlier, passing information to future steps, without exceptional cases, is considered to be a flaw in current design. So first you might need to consider revising your solution. However, if this seems to be the most appropriate solution, you need to access the step and job execution contexts using the following utilities provided by the spring batch utilities:

If you can reach the chunkContext (as in Tasklet implementations), you can reach the Step Execution Context:

	chunkContext.getStepContext().getStepExecution().getExecutionContext()

and Job Execution Context:

	chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();

If you need to access the execution contexts from Chunk Based Components, then you need to implement the [StepExecutionListener](https://github.com/spring-projects/spring-batch/blob/main/spring-batch-core/src/main/java/org/springframework/batch/core/StepExecutionListener.java) interface as [here](https://github.com/csumutaskin/example-spring-batch-data-pass-among-steps/blob/main/src/main/java/com/csumut/batches/chunkprocessing/processors/SampleProcessor.java):

	public class SampleProcessor implements ItemProcessor<HomeApplianceGroup, HomeApplianceGroup>, StepExecutionListener{
		...
		@Override
		public void beforeStep(StepExecution stepExecution) {
			this.stepExecution = stepExecution;
		
		}
		@Override
		public ExitStatus afterStep(StepExecution stepExecution) {
			return stepExecution.getExitStatus();
		}
	}	
