package api.endpoints;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import api.payload.Store;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class StoreEndPoints {
	
	public static Response placeOrder(Store payload) {
		
		Response response = given()
		.contentType(ContentType.JSON)
		.accept(ContentType.JSON)
		.body(payload)
		.log().body()
		
		.when()
		  .post(Routes.post_store_url)
		  .then()
		  .log().all()
		  .extract().response();
		
		return response;
		
	}
	
	public static Response getOrderById(long id) {
		
		Response response = given()
		
		.pathParam("orderId", id)
		
		.when()
		 .get(Routes.get_store_url);
		
		return response;
		
	}
	
	public static Response deleteOrderById(long id) {
		
		Response response = given()
		
		.pathParam("orderId", id)
		
		.when()
		  .delete(Routes.delete_store_url);
		
		return response;
	}

}
