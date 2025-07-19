package api.utilities;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
public class RetryUtilStore {
	
	public static Response retryGetStore(long id, int maxAttempts, int delayMillis) throws InterruptedException {
		
		RestAssured.baseURI = "https://petstore.swagger.io/v2";
		
		Response response = null;
		
		for(int i = 1; i <= maxAttempts; i++) {
			try {
				response = given()
						.pathParam("orderId", id)
						
						.when()
						.get("/store/order/{orderId}");
				
				System.out.println("Attempt "+i + ": GET /store/order" +  id  + "=> " + response.getStatusCode());
				
				if(response.getStatusCode()==200) {
					return response;
				}
			}catch(Exception e) {
				System.err.println("Attempt " + i + " failed due to: "+e.getMessage());
			}
			if(i < maxAttempts) {
				Thread.sleep(delayMillis);
			}
		}
		return response;
	}
		
}		
