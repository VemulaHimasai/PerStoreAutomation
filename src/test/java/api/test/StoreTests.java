package api.test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.PetEndPoints;
import api.endpoints.StoreEndPoints;
import api.payload.Pet;
import api.payload.Store;
import api.utilities.RetryUtilStore;
import io.restassured.response.Response;

public class StoreTests {
	
	Faker faker;
	Store storePayload;
	public Logger logger;
	
	
	@BeforeClass
	public void setUp() {
		faker = new Faker();
		storePayload = new Store();
		
		Pet petPayload = new Pet();
		petPayload.setId(faker.number().numberBetween(1000, 9999));
		petPayload.setName("doggie");
		petPayload.setStatus("available");

		// Call createPet endpoint
		Response petResponse = PetEndPoints.createPet(petPayload);
		Assert.assertEquals(petResponse.getStatusCode(), 200);

		// Use the created petId in order
		//storePayload.setPetId(petPayload.getId());

		
		storePayload.setId(faker.number().numberBetween(1000, 9999));
		//storePayload.setPetId(faker.number().numberBetween(1, 100));
		storePayload.setPetId(petPayload.getId());
		storePayload.setQuantity(faker.number().numberBetween(1, 10));
		storePayload.setShipDate(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
		storePayload.setStatus(faker.options().option("placed","approved","delivered"));
		storePayload.setComplete(true);
		
		System.out.println("Generated OrderId : "+storePayload.getId());
		
		// logs
				 logger=LogManager.getLogger(this.getClass());
				 
				 logger.debug("debugging -------");
	}
	
	@Test(priority=1)
	public void testPostStore() {
		
		logger.info("******Create Order ***********");
		
		System.out.println("Generated OrderId : "+storePayload.getId());
		
		Response response = StoreEndPoints.placeOrder(storePayload);
		
         response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		System.out.println("Executing testPostStore for Order Id : "+storePayload.getId());
		
		logger.info("************Order is created*************");
		
	}
	@Test(priority=2)
	public void testGetStoreById() throws InterruptedException {
		
		logger.info("**********Reading Order Info***************");
		
		long id = storePayload.getId();
		
		System.out.println("Getting Id : "+id);
		
		Response response = RetryUtilStore.retryGetStore(storePayload.getId(), 5, 2000);
		
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200, "Order not found after retrying.");
		
		System.out.println("Executing testGetStoreById for Order Id : "+storePayload.getId());
		
		logger.info("**********Order is displayed ************");
	}
	
	@Test(priority=3)
	public void testDeleteStoreById() throws InterruptedException {
		
		logger.info("*********Deleting Order **************");
		
		long orderId = storePayload.getId();
		
		System.out.println("Deleting Order Id : "+orderId);
		
		// send delete request
		Response deleteResponse = StoreEndPoints.deleteOrderById(orderId);
		int deleteStatusCode = deleteResponse.getStatusCode();
		
		System.out.println("Delete Response code : "+deleteStatusCode);
		Assert.assertTrue(deleteStatusCode == 200 || deleteStatusCode == 404,
				"Expected DELETE status 200 or 404, but got: "+deleteStatusCode);
		
		    int maxRetries = 5;
		    int waitTimeMs = 1000;
		    Response responseAfterDelete = null;
		
		    for(int i = 1; i <= maxRetries; i++) {
		    	responseAfterDelete = StoreEndPoints.deleteOrderById(orderId);
		    	int statusCode = responseAfterDelete.getStatusCode();

		        System.out.println("Retry #" + i + ", Status: " + statusCode);
		        
		        if(statusCode == 404) {
		        	break;
		        }
		        Thread.sleep(waitTimeMs);
		    }
		    
		    logger.info("***********Order is deleted *************");
		    
		    Assert.assertEquals(responseAfterDelete.getStatusCode(), 404, 
		    		"Store was not deleted successfully after retries");
		    
		    
		    System.out.println("Successfully confirmed deletion of Order Id : "+orderId);
	}

}
