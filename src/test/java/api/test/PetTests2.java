package api.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.PetEndPoints;
import api.endpoints.PetEndPoints2;
import api.payload.Category;
import api.payload.Pet;
import api.payload.Tag;
import api.utilities.RetryUtilPet;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class PetTests2 {
	// yet to implement
	
	Faker faker;
	Pet petPayload;
	public Logger logger;
	
	
	@BeforeClass
	public void setUp() {
		faker = new Faker();
		petPayload = new Pet();
		
		Category category = new Category();
		
		category.setId(faker.number().numberBetween(1, 100));
        category.setName(faker.animal().name());
		
		List<String> photoUrls = new ArrayList<>();
		photoUrls.add(faker.internet().url());
		//petPayload.setPhotoUrls(photoUrls);
		
		List<Tag> tags = new ArrayList<>();
		
		Tag tag = new Tag();
		tag.setId(faker.number().numberBetween(1, 1000));
		tag.setName(faker.animal().name());
		
		tags.add(tag);
		
		String[] statuses = {"available", "pending", "sold"};
		String randomStatus = statuses[new Random().nextInt(statuses.length)];

		
		category.setId(faker.number().randomNumber());
		category.setName(faker.animal().name());
		
		petPayload.setId(faker.number().numberBetween(1000, 9999));
		petPayload.setName(faker.funnyName().name());
		petPayload.setCategory(category);
		petPayload.setPhotoUrls(photoUrls);
		petPayload.setTags(tags);
		petPayload.setStatus(randomStatus);
		
		System.out.println("Generated PetId : "+petPayload.getId());
		
		 // logs
		 logger=LogManager.getLogger(this.getClass());
		 
		 logger.debug("debugging -------");
	}
	@Test(priority=1)
	public void testPostPet() {
		
		logger.info("********Creating Pet**********");
		
		System.out.println("Generated PetId : "+petPayload.getId());
		
		Response response = PetEndPoints2.createPet(petPayload);
		
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println("Executing [MethodName] for Pet ID: " + petPayload.getId());
		
		logger.info("********Pet is created**********");
		
	}
	@Test(priority=2)
	public void testGetPetById() throws InterruptedException {
		
		logger.info("*******Reading Pet Info************");
		
		long id = petPayload.getId();
		
		System.out.println("Getting Id : "+id);
		
		Response response = RetryUtilPet.retryGetPet(petPayload.getId(), 5, 2000);
		
		response.then().log().all();
	    Assert.assertEquals(response.getStatusCode(), 200, "Pet not found after retrying.");
	    
	    System.out.println("Executing [MethodName] for Pet ID: " + petPayload.getId());
	    
	    logger.info("*******Pet is displayed************");
		
	}
	@Test
	public void testUpdatePet() {
		
		logger.info("********Updating pet ***********");
		
		System.out.println("Updating pet : "+petPayload.getId());
		
		//update data using Payload
		
		//petPayload.setId(faker.number().randomNumber());
		petPayload.setName(faker.animal().name());
		petPayload.setStatus("available");
		
		
		Category category = new Category();
		
		category.setId(faker.number().numberBetween(1, 100));
		category.setName(faker.commerce().department());
		petPayload.setCategory(category);
		
		 petPayload.setPhotoUrls(Arrays.asList(faker.internet().url()));
		 
		// Set tags
		    Tag tag = new Tag();
		    tag.setId(faker.number().numberBetween(1, 100));
		    tag.setName(faker.color().name());
		    petPayload.setTags(Arrays.asList(tag));
		    
		    RestAssured.baseURI = "https://petstore.swagger.io/v2";
		    
		   Response response =  given()
		    
		    .contentType("application/json")
		    .body(petPayload)
		    
		    .when()
		      .put("/pet");
		   
		   response.then().statusCode(200).log().all();
		   
		   System.out.println("Executing [MethodName] for Pet ID: " + petPayload.getId());
		   
		   logger.info("******Pet is updated********");
		    
	}
	@Test
	public void testDeletePetbyId() throws InterruptedException {
		
		logger.info("********Deleting Pet*************");
		
		long petId = petPayload.getId();
	    System.out.println("Deleting Pet ID: " + petId);

	    // Send DELETE request
	    Response deleteResponse = PetEndPoints2.deletePet(petId);
	    int deleteStatusCode = deleteResponse.getStatusCode();
	    
	    System.out.println("Delete Response Code: " + deleteStatusCode);
	    Assert.assertTrue(deleteStatusCode == 200 || deleteStatusCode == 404,
	            "Expected DELETE status 200 or 404, but got: " + deleteStatusCode);
	    
	    int maxRetries = 5;
	    int waitTimeMs = 1000;
	    Response responseAfterDelete = null;
	    
	    for (int i = 1; i <= maxRetries; i++) {
	        responseAfterDelete = PetEndPoints2.readPet(petId);
	        int statusCode = responseAfterDelete.getStatusCode();

	        System.out.println("Retry #" + i + ", Status: " + statusCode);

	        if (statusCode == 404) {
	            break;
	        }

	        Thread.sleep(waitTimeMs);
	    }
	    
	    logger.info("********Pet Deleted*********");
	    
	    Assert.assertEquals(responseAfterDelete.getStatusCode(), 404,
	            "Pet was not deleted successfully after retries.");

	    System.out.println("Successfully confirmed deletion of Pet ID: " + petId);
	}
	

}
