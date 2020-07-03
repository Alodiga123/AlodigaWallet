package com.alodiga.wallet.rest.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import lombok.Data;

import com.alodiga.wallet.model.User;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;
import com.alodiga.wallet.rest.converter.ConverterUser;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class UserListResponse extends Response {

	private List<UserResponse> userResponses;
	
	public UserListResponse() {
		super();
	}
	
	public UserListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.userResponses = null;
	}
	
	public UserListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.userResponses = null;
	}

	public UserListResponse(ResponseCode code, String mensaje, List<User> users) {
		super(new Date(), code.getCodigo(), mensaje);
		this.userResponses = new ArrayList<UserResponse>();
		for (User user: users){
		    UserResponse userResponse = new UserResponse(user);
		    this.userResponses.add(userResponse);
		}
	}
        
}
