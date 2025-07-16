package api.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import api.endpoints.UserEndPoints;
import api.payload.User;
import api.utilities.DataProviders;
import io.restassured.response.Response;

public class DDTests {

	@Test(priority=1,dataProvider="Data",dataProviderClass=DataProviders.class)
	public void testPostUser(String userID, String userName, String fname, String lname, String useremail, String pwd, String ph) throws InterruptedException {
		
		User userPayload = new User();
		
		userPayload.setId(Integer.parseInt(userID));
		userPayload.setUsername(userName);
		userPayload.setFirstName(fname);
		userPayload.setLastName(lname);
		userPayload.setEmail(useremail);
		userPayload.setPassword(pwd);
		userPayload.setPhone(ph);
		
       System.out.println("Posting user: " + userPayload.getUsername());
		
		Response response=UserEndPoints.createUser(userPayload);
		Thread.sleep(1000); // after post, before assert

		Assert.assertEquals(response.getStatusCode(), 200);
		
	}
	@Test(priority=2, dataProvider="UserNames",dataProviderClass=DataProviders.class)
	public void testDeleteUserByname(String userName) {
		Response response = null;
	    int maxRetries = 5;
	    int delayMs = 1000;
	    boolean deleted = false;
//		Response response = UserEndPoints.deleteUser(userName);
//		Assert.assertEquals(response.getStatusCode(), 200);
	    
	    for (int i = 1; i <= maxRetries; i++) {
	        response = UserEndPoints.deleteUser(userName);
	        int status = response.getStatusCode();
	        System.out.println("Attempt " + i + ": DELETE /user/" + userName + " => " + status);

	        if (status == 200) {
	            deleted = true;
	            break;
	        }
	        
	        try {
	            Thread.sleep(delayMs);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }

	    Assert.assertTrue(deleted, "Failed to delete user '" + userName + "' after retries. Last status: "
	            + (response != null ? response.getStatusCode() : "no response"));
	}
	
	


}
