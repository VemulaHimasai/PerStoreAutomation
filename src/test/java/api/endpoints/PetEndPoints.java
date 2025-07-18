package api.endpoints;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import api.payload.Pet;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PetEndPoints {

	public static Response createPet(Pet payload) {
		
		Response response=given()
		
		   .contentType(ContentType.JSON)
		   .accept(ContentType.JSON)
		   .body(payload)
		
		.when()
		  .post(Routes.post_pet_url);
		
		return response;
		
	}
	
	public static Response readPet(long id) {
		
		Response response = given()
		 
		 .pathParam("petId", id)
		
		.when()
		  .get(Routes.get_pet_url);
		
		return response;
		
	}
	
	public static Response updatePet(long id,Pet payload) {
		
		Response response = given()
		
		.contentType(ContentType.JSON)
		.accept(ContentType.JSON)
		.pathParam("petId", id)
		.body(payload)
		
		.when()
		  .put(Routes.update_pet_url);
		
		return response;
		
	}
	public static Response deletePet(long id) {
		
		 RestAssured.baseURI = "https://petstore.swagger.io/v2";
		
		Response response = given()
		
		.pathParam("petId", id)
		
		.when()
		 .delete(Routes.delete_pet_url);
		
		return response;
	}
}
