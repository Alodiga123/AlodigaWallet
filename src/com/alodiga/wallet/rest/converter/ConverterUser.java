package com.alodiga.wallet.rest.converter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alodiga.wallet.model.User;
import com.alodiga.wallet.rest.request.UserRequest;



public class ConverterUser  {

	public User converterUser(UserRequest userRequest) {
		User user = new User();
		user.setId(userRequest.getId());
		try {
		    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		    Date parsedDate = dateFormat.parse(userRequest.getCreationDate());
		    Timestamp timestamp = new Timestamp(parsedDate.getTime());
		    user.setCreationDate(timestamp);
		} catch(Exception e) { 
		    
		}
		user.setEmail(userRequest.getEmail());
		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setLogin(userRequest.getLogin());
		user.setEnabled(userRequest.isEnabled());
		user.setPassword(userRequest.getPassword());
		user.setPhoneNumber(userRequest.getPhoneNumber());
		user.setReceiveTopUpNotification(userRequest.isReceiveTopUpNotification());
		return user;
	}
	
		
}
