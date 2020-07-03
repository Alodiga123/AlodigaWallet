package com.alodiga.wallet.ws.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alodiga.wallet.model.PermissionData;
import com.alodiga.wallet.model.PreferenceType;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.rest.request.EnterpriseRequest;
import com.alodiga.wallet.rest.request.PermissionDataRequest;
import com.alodiga.wallet.rest.request.PermissionRequest;
import com.alodiga.wallet.rest.request.PreferenceFieldRequest;
import com.alodiga.wallet.rest.request.PreferenceTypeRequest;
import com.alodiga.wallet.rest.request.ProfileRequest;
import com.alodiga.wallet.rest.request.UpdateTopNotificationRequest;
import com.alodiga.wallet.rest.request.UserRequest;
import com.alodiga.wallet.rest.response.EnterpriseResponse;
import com.alodiga.wallet.rest.response.PermissionResponse;
import com.alodiga.wallet.rest.response.PreferenceFieldResponse;
import com.alodiga.wallet.rest.response.PreferenceTypeResponse;
import com.alodiga.wallet.rest.response.ProfileListResponse;
import com.alodiga.wallet.rest.response.ProfileResponse;
import com.alodiga.wallet.rest.response.UserListResponse;
import com.alodiga.wallet.rest.response.UserResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
 

public class TestServiceRest {
	
	public String consumeRest(String urlRest,Object object) {
		String respose = "";
		try {
			 
			URL url = new URL(urlRest);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
 
			Gson gson = new Gson();
			String input = gson.toJson(object);
 
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
 
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) 
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			
 
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
 
			String output = null;
			while ((output = br.readLine()) != null) {
				respose = respose.concat(output);
				System.out.println(output);
			}
 
			conn.disconnect();
 
		} catch (MalformedURLException e) {
 
			e.printStackTrace();
		} catch (IOException e) {
 
			e.printStackTrace();
 
		}
		return respose;
	}
	
	public static Object getResponse(String metod, Object request, Class clazz) {
		TestServiceRest rest = new TestServiceRest();
		String jsonComplejo = rest.consumeRest("http://localhost:8080/AlodigaWallet/wallet/"+metod, request);
		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateFormatter()).create();
		Object response = gson.fromJson(jsonComplejo, clazz);
		return response;
	}
	
	public static void main(String[] args) {
		UserRequest userRequest = new UserRequest();
		userRequest.setLogin("yalmea");
		userRequest.setEmail("mgraterol@alodiga.com");
		UserResponse userResponse = (UserResponse) getResponse("loadUserByLogin",userRequest,UserResponse.class);
		System.out.println("Usuario:"+userResponse.getFirstName() +" "+userResponse.getLastName());
		
		userResponse = (UserResponse) getResponse("loadUserByEmail",userRequest,UserResponse.class);
		System.out.println("Usuario:"+userResponse.getFirstName() +" "+userResponse.getLastName());
		
		userRequest = new UserRequest();
		userRequest.setId(1L);
		userRequest.setPassword("123456");
		
		EnterpriseRequest enterpriseRequest = new EnterpriseRequest();
		enterpriseRequest.setId(1L);
		EnterpriseResponse e = (EnterpriseResponse)getResponse("getEnterpriseById", enterpriseRequest, EnterpriseResponse.class);
		System.out.println("Empresa:"+e.getName());
		
		userResponse = (UserResponse) getResponse("updateUserPassword", userRequest, UserResponse.class);
		System.out.println("Usuario:"+userResponse.getFirstName() +" "+userResponse.getPassword());
	
		UserListResponse userListResponse = (UserListResponse) getResponse("getUsers",userRequest,UserListResponse.class);
		for(UserResponse response:userListResponse.getUserResponses()) {			
			System.out.println("Usuarios:"+response.getFirstName() +" "+response.getLastName());
		}
		
		
		UpdateTopNotificationRequest notificationRequest = new UpdateTopNotificationRequest();
		notificationRequest.setIds("1,3");
		Response response =(Response) getResponse("updateUserNotifications",notificationRequest,Response.class);
		System.out.println("Response:"+response.toString());
		
		ProfileListResponse profileListResponse = (ProfileListResponse) getResponse("getProfiles",null,ProfileListResponse.class);
		for (ProfileResponse profile:profileListResponse.getProfileResponses()) {
			System.out.println("Profile:"+profile.getId()+" "+profile.getName());
		}
		
		ProfileRequest profileRequest = new ProfileRequest();
		profileRequest.setId(1L);
		ProfileResponse p= (ProfileResponse) getResponse("getProfileById",profileRequest,ProfileResponse.class);
		System.out.println("Profile:"+p.getName());
	
		PreferenceTypeRequest preferenceType = new PreferenceTypeRequest();
		preferenceType.setType("prueba");
		PreferenceTypeResponse  typeResponse =   (PreferenceTypeResponse) getResponse("savePreferenceType",preferenceType,PreferenceTypeResponse.class);
		System.out.println("PreferenceType:"+typeResponse.getId() +" "+typeResponse.getType());
		
	
		PreferenceFieldRequest preferenceFieldRequest = new PreferenceFieldRequest();
		preferenceFieldRequest.setId(1L);
		PreferenceFieldResponse preferenceFieldResponse = (PreferenceFieldResponse) getResponse("loadPreferenceField",preferenceFieldRequest,PreferenceFieldResponse.class);
		System.out.println("PreferenceField:"+preferenceFieldResponse.getName());
		
//		PermissionRequest permissionRequest = new PermissionRequest();
//		permissionRequest.setAction("action");
//		permissionRequest.setEntity("entity");
//		permissionRequest.setEnabled(true);
//		permissionRequest.setName("ViewAction");
//		permissionRequest.setGroupId(2L);
//		List<PermissionDataRequest> list = new ArrayList<PermissionDataRequest>();
//		PermissionDataRequest permissionDataRequest = new PermissionDataRequest();
//		permissionDataRequest.setAlias("alias");
//		permissionDataRequest.setDescription("description");
//		permissionDataRequest.setLanguageId(1L);
//		list.add(permissionDataRequest);
//		permissionDataRequest = new PermissionDataRequest();
//		permissionDataRequest.setAlias("alias2");
//		permissionDataRequest.setDescription("description2");
//		permissionDataRequest.setLanguageId(2L);
//		list.add(permissionDataRequest);
//		permissionRequest.setPermissionDataRequest(list);
//		PermissionResponse permissionResponse = (PermissionResponse) getResponse("savePermission",permissionRequest,PermissionResponse.class);
//		System.out.println("Permision:"+permissionResponse.getId()+" "+permissionResponse.getName());
	
	}

}
