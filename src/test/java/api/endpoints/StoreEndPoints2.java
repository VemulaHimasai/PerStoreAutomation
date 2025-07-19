package api.endpoints;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ResourceBundle;

import api.payload.Store;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class StoreEndPoints2 {
	
	static ResourceBundle getURL() {
		ResourceBundle routes = ResourceBundle.getBundle("routes"); // load properties file
		return routes;
	}
	
	public static Response placeOrder(Store payload) {
		
		String post_store_url = getURL().getString("post_store_url");
		
		Response response = given()
		.contentType(ContentType.JSON)
		.accept(ContentType.JSON)
		.body(payload)
		.log().body()
		
		.when()
		  .post(post_store_url)
		  .then()
		  .log().all()
		  .extract().response();
		
		return response;
		
	}
	
	public static Response getOrderById(long id) {
		
		String get_store_url = getURL().getString("get_store_url");
		
		Response response = given()
		
		.pathParam("orderId", id)
		
		.when()
		 .get(get_store_url);
		
		return response;
		
	}
	
	public static Response deleteOrderById(long id) {
		
		String delete_store_url = getURL().getString("delete_store_url");
		
		Response response = given()
		
		.pathParam("orderId", id)
		
		.when()
		  .delete(delete_store_url);
		
		return response;
	}

}
