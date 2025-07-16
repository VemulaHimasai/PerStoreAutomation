package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import api.utilities.RetryUtil;
import io.restassured.response.Response;

public class UserTests {
	
	Faker faker;
	User userPayload;
	public Logger logger;
	
	@BeforeClass
	public void setup() {
		
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		userPayload.setUserStatus(1);
		
		 System.out.println("Generated username: " + userPayload.getUsername());
		 
		 // logs
		 logger=LogManager.getLogger(this.getClass());
		 
		 logger.debug("debugging....");
		 
		
	}
	
	@Test(priority=1)
	public void testPostUser() {
		
		logger.info("*********Creating User*********");
		
		System.out.println("Posting user: " + userPayload.getUsername());
		
		Response response=UserEndPoints.createUser(userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("*********User is created **********");
		
	}
	@Test(priority=2)
	public void testGetUserByName() throws InterruptedException {
		
		logger.info("*********Reading User Info***************");

		String username = userPayload.getUsername();
		 System.out.println("Getting user: " + username);
		 
		 Response response = RetryUtil.retryGetUser(userPayload.getUsername(), 5, 2000);

		    response.then().log().all();
		    Assert.assertEquals(response.getStatusCode(), 200, "User not found after retrying.");
		    
		    logger.info("*******User is displayed*******");
		
//		Response response=UserEndPoints.readUser(userPayload.getUsername());
//		response.then().log().all();
//		Assert.assertEquals(response.getStatusCode(), 200);
		 
//		    int maxRetries = 10;
//		    int waitMillis = 2000;
//		    Response response = null;
//		    boolean isFound = false;
//		    
//		    for (int i = 1; i <= maxRetries; i++) {
//		        response = UserEndPoints.readUser(username);
//		        int statusCode = response.getStatusCode();
//
//		        if (statusCode == 200) {
//		            break;
//		        } 
//		            
//		        System.out.println("Attempt " + i + ": GET /user/" + username + 
//		                               " returned status " + statusCode);
//		            Thread.sleep(waitMillis);
//		    }
		    
//		    if (response != null) {
//		        response.then().log().all();
//		    }

//		    Assert.assertTrue(isFound, "User was not found after " + maxRetries + " retries.");
//		    Assert.assertEquals(response.getStatusCode(), 200, "User not found after retries.");
//		    response.then().log().all();
		    
		    
		
	}
	@Test(priority=3)
	public void testUpdateUserByName() {
		
		logger.info("*******Updating user ***********");
		 System.out.println("Updating user: " + userPayload.getUsername());
		 
		//update data using Payload
		 
	    userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		 
		 
		 Response response=UserEndPoints.updateUser(userPayload.getUsername(),userPayload);
			response.then().log().body();
			//response.then().body().statusCode(200); - chai assertion 
			
			Assert.assertEquals(response.getStatusCode(), 200);
			
			logger.info("**********User Updated***********");
			
			//checking data after update
			
			Response responseAfterupdate=UserEndPoints.readUser(userPayload.getUsername());
			//response.then().log().all();
			Assert.assertEquals(responseAfterupdate.getStatusCode(), 200);
			
			
		 
	}
	@Test(priority=4)
//	public void testDeleteUserByName() throws InterruptedException {
//		
//		Response response = UserEndPoints.deleteUser(userPayload.getUsername());
//		Assert.assertEquals(response.getStatusCode(), 200);
//		
//		int maxRetries = 5;
//	    int waitTimeMs = 1000;
//	    Response responseAfterDelete = null;
//	    
//	    for (int i = 1; i <= maxRetries; i++) {
//	        responseAfterDelete = UserEndPoints.readUser(userPayload.getUsername());
//	        int statusCode = responseAfterDelete.getStatusCode();
//
//	        if (statusCode == 404) {
//	            break;
//	        }
//	        
//	        System.out.println("Attempt " + i + ": User still exists, status: " + statusCode);
//	        Thread.sleep(waitTimeMs);
//		
////		Response responseAfterDelete = UserEndPoints.readUser(userPayload.getUsername());
////		Assert.assertEquals(responseAfterDelete.getStatusCode(), 404);
//
//		
//	}
//	    Assert.assertEquals(responseAfterDelete.getStatusCode(), 404, "User was not deleted successfully after retries.");
//
//}
	public void testDeleteUserByName() throws InterruptedException {
		
		logger.info("**********Deleting User***********");
	    
	    // Step 1: Delete the user
	    Response response = UserEndPoints.deleteUser(userPayload.getUsername());
	    Assert.assertEquals(response.getStatusCode(), 200);

	    // Step 2: Retry reading the user until 404 is received or retry limit is reached
	    int maxRetries = 5;
	    int waitTimeMs = 1000;
	    Response responseAfterDelete = null;

	    for (int i = 1; i <= maxRetries; i++) {
	        responseAfterDelete = UserEndPoints.readUser(userPayload.getUsername());
	        int statusCode = responseAfterDelete.getStatusCode();

	        if (statusCode == 404) {
	            break;
	        }

	        System.out.println("Attempt " + i + ": User still exists, status: " + statusCode);
	        Thread.sleep(waitTimeMs);
	    }
	    
	    logger.info("************User Deleted***********");

	    // Step 3: Final assertion
	    Assert.assertEquals(responseAfterDelete.getStatusCode(), 404, "User was not deleted successfully after retries.");
	}

}