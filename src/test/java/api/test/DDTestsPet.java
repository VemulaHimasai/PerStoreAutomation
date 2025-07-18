package api.test;

import java.util.Collections;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import api.endpoints.PetEndPoints;
import api.payload.Category;
import api.payload.Pet;
import api.payload.Tag;
import api.utilities.DataProvidersPet;
import io.restassured.response.Response;

public class DDTestsPet {
	
	@Test(priority=1,dataProvider = "petData",dataProviderClass=DataProvidersPet.class)
	public void testCreatePet(String id, String name, String categoryId, String categoryName,
	                          String status, String tagId, String tagName, String photoUrl,ITestContext context) {
		
		if (id == null || id.trim().isEmpty()) {
	        throw new IllegalArgumentException("Invalid ID value from Excel: [" + id + "]");
	    }

	    Pet pet = new Pet();
	    pet.setId(Long.parseLong(id));
	    pet.setName(name);

	    Category category = new Category();
	    category.setId(Long.parseLong(categoryId));
	    category.setName(categoryName);
	    pet.setCategory(category);

	    Tag tag = new Tag();
	    tag.setId(Long.parseLong(tagId));
	    tag.setName(tagName);
	    pet.setTags(Collections.singletonList(tag));

	    pet.setPhotoUrls(Collections.singletonList(photoUrl));
	    pet.setStatus(status);

	    Response response = PetEndPoints.createPet(pet);
	    response.then().log().all().statusCode(200);
	    
	    context.setAttribute("created_pet_id", pet.getId());
	}
	
	@Test(priority=2)
	public void testDeletePetById(ITestContext context) throws InterruptedException {
		
		 Long id = (Long) context.getAttribute("created_pet_id");
		
		System.out.println("Deleting Pet ID: " + id);
		
		Response response = PetEndPoints.deletePet(id);
		 Assert.assertEquals(response.getStatusCode(), 200, "Failed to delete pet.");
		 
		 int maxRetries = 5;
		 int waitTimeMs = 1000;
		 Response getResponse = null;
		 
		 for (int i = 1; i <= maxRetries; i++) {
		        getResponse = PetEndPoints.readPet(id);
		        int statusCode = getResponse.getStatusCode();

		        System.out.println("Retry #" + i + " - Status: " + statusCode);

		        if (statusCode == 404) {
		            break;
		        }

		        Thread.sleep(waitTimeMs);
		    }

		 Assert.assertEquals(getResponse.getStatusCode(), 404, "Pet still exists after deletion.");
		
	}


}
