package api.endpoints;

/*
 Swagger URI ---> https://petstore.swagger.io/
 
 Create user (post) - https://petstore.swagger.io/v2/user
 Get user (get) - https://petstore.swagger.io/v2/user/{username}
 Update user (put) - https://petstore.swagger.io/v2/user/{username}
 Delete user (delete) - https://petstore.swagger.io/v2/user/{username}
 */

public class Routes {
	
	public static String base_url = "https://petstore.swagger.io/v2";
	
	//user module
	
	public static String post_url = base_url+"/user";
	
	public static String get_url = base_url+"/user/{username}";
	
	public static String update_url = base_url+"/user/{username}";
	
	public static String delete_url = base_url+"/user/{username}";
	
	//store module
	
	//we will create Store module URLs
	
	
	//Pet module
	
	//we will create Pet module URLs
	
	public static String post_pet_url = base_url+"/pet";
	
	public static String get_pet_url = base_url+"/pet/{petId}";
	
	public static String update_pet_url = base_url+"/pet";
	
	public static String delete_pet_url = base_url+"/pet/{petId}";
	

}
