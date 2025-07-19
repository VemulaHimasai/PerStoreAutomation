package api.test;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import api.endpoints.StoreEndPoints;
import api.payload.Store;
import api.utilities.DataProvidersStore;
import io.restassured.response.Response;

public class DDTestsStore {
	
	
	@Test(priority=1,dataProvider = "storeData",dataProviderClass=DataProvidersStore.class)
	public void testCreateOrder(String id, String petId, String quantity, String shipDate, String status, String complete,ITestContext context) {
		
		if (id == null || id.trim().isEmpty()) {
	        throw new IllegalArgumentException("Invalid ID value from Excel: [" + id + "]");
	    }
		
		Store payload = new Store();
		
		payload.setId(Long.parseLong(id));
		payload.setPetId(Long.parseLong(petId));
		payload.setQuantity(Integer.parseInt(quantity));
		payload.setShipDate(shipDate);
		payload.setStatus(status);
		
		 if (complete != null && !complete.trim().isEmpty()) {
		        payload.setComplete(Boolean.parseBoolean(complete));
		    } else {
		        payload.setComplete(false); // Default
		    }
		
		Response response = StoreEndPoints.placeOrder(payload);
	    response.then().log().all();

	    Assert.assertEquals(response.getStatusCode(), 200,"Order creation failed!");
	    
	    context.setAttribute("created_order_id", payload.getId());
		
	}
	@Test(priority=2)
	public void testDeleteOrderById(ITestContext context) throws InterruptedException {
		
		Long id = (Long) context.getAttribute("created_order_id");
		 Assert.assertNotNull(id, "Order ID is null - order might not have been created properly.");
		
		System.out.println("Deleting Order Id : "+ id);
		
		Response response = StoreEndPoints.deleteOrderById(id);
		Assert.assertEquals(response.getStatusCode(), 200, "Failed to delete order");
		
		int maxRetries = 5;
		 int waitTimeMs = 1000;
		 Response getResponse = null;
		 
		 for(int i = 1; i <= maxRetries; i++) {
			 
			 getResponse = StoreEndPoints.getOrderById(id);
			 int statusCode = getResponse.getStatusCode();
			 
			 System.out.println("Retry #" + i + " - Status: " + statusCode);
			 
			 if (statusCode == 404) {
		            break;
		        }
			 Thread.sleep(waitTimeMs);
		 }
		 
		 System.out.println("Final status after deletion retries: " + getResponse.getStatusCode());
		 Assert.assertEquals(getResponse.getStatusCode(), 404 , "Order still exists after deletion");
		
	}

}
