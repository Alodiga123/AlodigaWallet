package com.alodiga.wallet.ws;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alodiga.wallet.bean.APIAccessControls;
import com.alodiga.wallet.bean.APIAdministrator;
import com.alodiga.wallet.bean.APIPreferences;
import com.alodiga.wallet.model.PreferenceType;
import com.alodiga.wallet.model.PreferenceValue;
import com.alodiga.wallet.model.User;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.rest.converter.ConverterPreferenceType;
import com.alodiga.wallet.rest.converter.ConverterPreferenceValue;
import com.alodiga.wallet.rest.converter.ConverterUser;
import com.alodiga.wallet.rest.request.EnterpriseRequest;
import com.alodiga.wallet.rest.request.PermissionRequest;
import com.alodiga.wallet.rest.request.PreferenceFieldRequest;
import com.alodiga.wallet.rest.request.PreferenceTypeRequest;
import com.alodiga.wallet.rest.request.PreferenceValueRequest;
import com.alodiga.wallet.rest.request.ProfileRequest;
import com.alodiga.wallet.rest.request.UpdateTopNotificationRequest;
import com.alodiga.wallet.rest.request.UserRequest;
import com.alodiga.wallet.rest.response.EnterpriseResponse;
import com.alodiga.wallet.rest.response.PermissionGroupListResponse;
import com.alodiga.wallet.rest.response.PermissionListResponse;
import com.alodiga.wallet.rest.response.PermissionResponse;
import com.alodiga.wallet.rest.response.PreferenceFieldListResponse;
import com.alodiga.wallet.rest.response.PreferenceFieldResponse;
import com.alodiga.wallet.rest.response.PreferenceTypeListResponse;
import com.alodiga.wallet.rest.response.PreferenceTypeResponse;
import com.alodiga.wallet.rest.response.PreferenceValueListResponse;
import com.alodiga.wallet.rest.response.PreferenceValueResponse;
import com.alodiga.wallet.rest.response.ProfileListResponse;
import com.alodiga.wallet.rest.response.ProfileResponse;
import com.alodiga.wallet.rest.response.UserListResponse;
import com.alodiga.wallet.rest.response.UserResponse;
import com.alodiga.wallet.rest.response.ValidateUserResponse;


@Path("/wallet")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class APIAlodigaWalletRest{

	@EJB
    private APIAdministrator administrator;
	@EJB
    private APIAccessControls accessControls;
	@EJB
    private APIPreferences preferences;
      

    @GET  
    public javax.ws.rs.core.Response sayHello() {     
        return javax.ws.rs.core.Response.ok("Hello World desde el API REST",MediaType.APPLICATION_JSON).build();   
    } 

    
    @GET  
    @Path("/user")
    public UserRequest sayHelloUser() {     
    	UserRequest request = new UserRequest();
    	request.setLogin("yalmea");
        return request;   
    } 
	
	@POST
    @Path("/loadUserByLogin")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public UserResponse loadUserByLogin(UserRequest userRequest) {
		UserResponse userResponse = administrator.loadUserByLogin(userRequest.getLogin());
		return userResponse;

	}
	
	@POST
    @Path("/loadUserByEmail")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public UserResponse loadUserByEmail(UserRequest userRequest) {
		UserResponse userResponse = administrator.loadUserByEmail(userRequest.getEmail());
		return userResponse;

	}
	
	@POST
    @Path("/loadUser")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public UserResponse loadUser(UserRequest userRequest) {
		UserResponse userResponse = administrator.loadUser(userRequest.getId());
		return userResponse;

	}
	
	@POST
    @Path("/saveUser")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public UserResponse saveUser(UserRequest userRequest) {
		ConverterUser converterUser= new ConverterUser();
		User user = converterUser.converterUser(userRequest);
		UserResponse userResponse = administrator.saveUser(user);
		return userResponse;

	}
	
	@POST
    @Path("/updateUserPassword")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public UserResponse updateUserPassword(UserRequest userRequest) {
		UserResponse userResponse = administrator.updateUserPassword(userRequest.getId(),userRequest.getPassword());
		return userResponse;

	}
	
	
	@POST
    @Path("/getUsers")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public UserListResponse getUsers() {
		UserListResponse userListResponse = administrator.getUsers();
		return userListResponse;

	}
	
	@POST
    @Path("/validateExistingUser")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public ValidateUserResponse validateExistingUser(UserRequest userRequest) {
		ValidateUserResponse validateUserResponse = administrator.validateExistingUser(userRequest.getLogin(), userRequest.getEmail());
		return validateUserResponse;

	}
	
	@POST
    @Path("/getUserTopUpNotification")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public UserListResponse getUserTopUpNotification() {
		UserListResponse userListResponse = administrator.getUserTopUpNotification();
		return userListResponse;

	}
	
	@POST
    @Path("/updateUserNotifications")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public Response updateUserNotifications(UpdateTopNotificationRequest request) {
		Response response = administrator.updateUserNotifications(request.getIds());
		return response;

	}
	
	@POST
    @Path("/getPermissionGroups")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PermissionGroupListResponse getPermissionGroups() {
		PermissionGroupListResponse response = administrator.getPermissionGroups( );
		return response;

	}
	
	@POST
    @Path("/getPermissions")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PermissionListResponse getPermissions() {
		PermissionListResponse response = administrator.getPermissions();
		return response;

	}
	
	@POST
    @Path("/getPermissionByGroupId")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PermissionListResponse getPermissionByGroupId(PermissionRequest permissionRequest) {
		PermissionListResponse response = administrator.getPermissionByGroupId(permissionRequest.getGroupId());
		return response;

	}
	
	@POST
    @Path("/getPermissionByProfileId")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PermissionListResponse getPermissionByProfileId(ProfileRequest profileRequest) {
		PermissionListResponse response = administrator.getPermissionByProfileId(profileRequest.getId());
		return response;

	}
	
	@POST
    @Path("/getPermissionById")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PermissionResponse getPermissionById(PermissionRequest permissionRequest) {
		PermissionResponse response = administrator.loadPermissionById(permissionRequest.getId());
		return response;

	}
	
	@POST
    @Path("/getProfiles")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public ProfileListResponse getProfiles() {
		ProfileListResponse response = administrator.getProfiles();
		return response;

	}
	
	@POST
    @Path("/deletePermissionHasProfile")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public Response deletePermissionHasProfile(ProfileRequest request) {
		Response response = accessControls.deletePermissionHasProfile(request.getId());
		return response;
	}
	
	@POST
    @Path("/getParentsByProfile")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public ProfileListResponse getParentsByProfile(ProfileRequest request) {
		ProfileListResponse response = accessControls.getParentsByProfile(request.getId());
		return response;

	}
	
	@POST
    @Path("/savePermission")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PermissionResponse savePermission(PermissionRequest permissionRequest) {
		PermissionResponse permissionResponse = accessControls.savePermission(permissionRequest);
		return permissionResponse;

	}
	
	@POST
    @Path("/validateUser")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public UserResponse validateUser(UserRequest userRequest) {
		UserResponse validateUserResponse = accessControls.validateUser(userRequest.getLogin(), userRequest.getPassword());
		return validateUserResponse;

	}
	
	@POST
    @Path("/saveProfile")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public ProfileResponse saveProfile(ProfileRequest profileRequest) {
		ProfileResponse profileResponse = accessControls.saveProfile(profileRequest);
		return profileResponse;

	}
	
	@POST
    @Path("/getProfileById")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public ProfileResponse getProfileById(ProfileRequest profileRequest) {
		ProfileResponse response = accessControls.loadProfileById(profileRequest.getId());
		return response;

	}
	
	@POST
    @Path("/getEnterpriseById")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public EnterpriseResponse getEnterpriseById(EnterpriseRequest enterpriseRequest) {
		EnterpriseResponse response = accessControls.loadEnterpriseById(enterpriseRequest.getId());
		return response;

	}
	
	@POST
    @Path("/getLastPreferenceValueByPreferenceField")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PreferenceValueResponse getLastPreferenceValueByPreferenceField(PreferenceValueResponse preferenceValueResponse) {
		PreferenceValueResponse response = preferences.getLastPreferenceValueByPreferenceField(preferenceValueResponse.getId());
		return response;

	}
	
	@POST
    @Path("/getPreferenceFields")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PreferenceFieldListResponse getPreferenceFields() {
		PreferenceFieldListResponse response = preferences.getPreferenceFields();
		return response;
	}
	
	@POST
    @Path("/getPreferenceTypes")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PreferenceTypeListResponse getPreferenceTypes() {
		PreferenceTypeListResponse response = preferences.getPreferenceTypes();
		return response;
	}
	
	@POST
    @Path("/getPreferenceValues")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PreferenceValueListResponse getPreferenceValues() {
		PreferenceValueListResponse response = preferences.getPreferenceValues();
		return response;

	}
	
	@POST
    @Path("/loadPreferenceField")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PreferenceFieldResponse loadPreferenceField(PreferenceFieldRequest preferenceFieldResponse) {
		PreferenceFieldResponse response = preferences.loadPreferenceField(preferenceFieldResponse.getId());
		return response;
	}
	
	@POST
    @Path("/loadPreferenceType")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PreferenceTypeResponse loadPreferenceType(PreferenceTypeRequest preferenceType) {
		PreferenceTypeResponse response = preferences.loadPreferenceType(preferenceType.getId());
		return response;
	}
	
	@POST
    @Path("/loadPreferenceValue")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PreferenceValueResponse loadPreferenceValue(PreferenceValueRequest request) {
		PreferenceValueResponse response = preferences.loadPreferenceValue(request.getId());
		return response;
	}
	
	@POST
    @Path("/savePreferenceType")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PreferenceTypeResponse savePreferenceType(PreferenceTypeRequest request) {
		ConverterPreferenceType converter = new ConverterPreferenceType();
		PreferenceType preferenceType  = converter.converterPreferenceType(request);
		PreferenceTypeResponse preferenceTypeResponse = preferences.savePreferenceType(preferenceType);
		return preferenceTypeResponse;

	}
	
	@POST
    @Path("/savePreferenceValue")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	public PreferenceValueResponse savePreferenceValue(PreferenceValueRequest request) {
		ConverterPreferenceValue converter = new ConverterPreferenceValue();
		PreferenceValue preferenceValue  = converter.converterPreferenceValue(request);
		PreferenceValueResponse response = preferences.savePreferenceValue(preferenceValue);
		return response;

	}
	
}
