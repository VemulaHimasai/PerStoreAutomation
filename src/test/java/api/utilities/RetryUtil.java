package api.utilities;

import api.endpoints.UserEndPoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class RetryUtil {
	
	public static Response retryGetUser(String username,int maxAttempts, int delayMillis) throws InterruptedException {
		
		 RestAssured.baseURI = "https://petstore.swagger.io/v2";
		 
		Response response = null;
		
		for (int i = 1; i <= maxAttempts; i++) {
            try {
            	response = given()
                        .pathParam("username", username)
                    .when()
                        .get("/user/{username}");

                System.out.println("Attempt " + i + ": GET /user/" + username + " => " + response.getStatusCode());

                if (response.getStatusCode() == 200) {
                    return response;
                }
            }catch (Exception e) {
                System.err.println("Attempt " + i + " failed due to: " + e.getMessage());
            }
            if (i < maxAttempts) {
                Thread.sleep(delayMillis);
            }
	}
		 return response;

}
}
