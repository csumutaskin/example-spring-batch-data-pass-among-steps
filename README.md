# Introduction

Spring Batch is one of the most preferred frameworks to develop a robust batch application. Since its origins reaches back to the last 2 decades, it is still considered to be a valid, robust and lightweight solution when implementing batch applications. Data transferring from one source to another can be done in various ways: a popular solution, nowadays, might be an event driven data transferring, or implementing your own solution w.r.t. the given problem, but all these approaches need to pay extra attention, like sometimes more coding, more maintenance and/or controlling on third party APIs and libraries. Among these approaches, if you need a <i>more lightweight</i> solution, I think Spring Batch is still the way to go. For more information on Spring Batch and Spring framework, please read the official documentation, and follow the code repository.

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

A home appliance store reporting department is asked to prepare report of different types of devices they have and calculate the percentage of each category (like electonic, furniture, mechanic etc...) among all. The data might be prepared at night, using batch programming, after the daily sale is over. So we need to create a batch application that will query the HOME_APPLIANCE table of the database, 
calculate some aggregation data (total count and price w.r.t the category) on the current catalog information, calculate the category item percentage among all sale item population and load the transformed data to the destination HOME_APPLIANCE_GROUP table for the report. Consider this scenario to be our basic "batch problem".

What we do here is to prepare a batch job with 3 steps:
1. This tasklet based step gets the total count of items in the store and passes this information to the future steps.
2. A chunk based 2nd step reads - processes and writes the data to the destination table in chunks.
3. Assume a 3rd tasklet based step is just implemented to log some information, nothing more.

Here is the main flow of our sample batch application step by step:

![step flows](https://github.com/csumutaskin/project-docs/blob/main/example-spring-batch-data-pass-among-steps/Design/UML/FlowCharts/Flow_chart_steps.jpg?raw=true)

# How to Pass Information Among Steps

will be added...