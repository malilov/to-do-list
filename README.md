# Service to-do-list

**Description**
This service provides the functionalities to manage a checklist. 
In this service we handle basically two entities: 
* task 
* person (*Assumption:* because we provide RESTs Endpoints to manage the tasks we need the person entity to relate the task to a user.)

For the management of the two entities we have 2 controllers.
* Person controller: The only goal with this controller is to provide the endpoint to create a Person.
* Task controller: Provides the operations to handle the tasks

This service was implemented with Java 20, using JPA and Spring Data, Mapstruct to map the DTOs into Entities 
and vice versa. Testing with Mockito and Rest Assure for the integration tests.

**How to run the service in local**
* With IntelliJ the 'to-do-list/to-do-list [spring-boot_run].run.xml' file contains the spring configs to start the service
* With docker:
  *  Create the image from the DOCKER file:
        ```docker build --tag=to-do-list:latest . ```
  * Run the container
      ```docker run -p8887:8081 to-do-list:latest ```
  
**How to test the service in local**
The folder 'to-do-list/postman-tests' contains the postman collection and the environment variables
to call the service attending in the port ```8887``` in ```localhost```.
***Note*** Please create a person before creating the tasks
  
**How to run the service tests**
Unit tests: ```mvn test```
Integration Tests: ```mvn verify -Pfailsafe```
  