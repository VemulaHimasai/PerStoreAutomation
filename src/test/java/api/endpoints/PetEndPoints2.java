package api.endpoints;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ResourceBundle;

import api.payload.Pet;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PetEndPoints2 {
	
	static ResourceBundle getURL() {
		ResourceBundle routes = ResourceBundle.getBundle("routes"); // load properties file
		return routes;
	}

	public static Response createPet(Pet payload) {
		
		String post_pet_url = getURL().getString("post_pet_url");
		
		Response response=given()
		
		   .contentType(ContentType.JSON)
		   .accept(ContentType.JSON)
		   .body(payload)
		
		.when()
		  .post(post_pet_url);
		
		return response;
		
	}
	
	public static Response readPet(long id) {
		
		String get_pet_url = getURL().getString("get_pet_url");
		
		Response response = given()
		 
		 .pathParam("petId", id)
		
		.when()
		  .get(get_pet_url);
		
		return response;
		
	}
	
	public static Response updatePet(long id,Pet payload) {
		
		String update_pet_url = getURL().getString("update_pet_url");
		
		Response response = given()
		
		.contentType(ContentType.JSON)
		.accept(ContentType.JSON)
		.pathParam("petId", id)
		.body(payload)
		
		.when()
		  .put(update_pet_url);
		
		return response;
		
	}
	public static Response deletePet(long id) {
		
		 //RestAssured.baseURI = "https://petstore.swagger.io/v2";
		
		String delete_pet_url = getURL().getString("delete_pet_url");
		
		Response response = given()
		
		.pathParam("petId", id)
		
		.when()
		 .delete(delete_pet_url);
		
		return response;
	}
}
