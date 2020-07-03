package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.model.User;
import com.alodiga.wallet.model.UserHasProfileHasEnterprise;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class UserResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	public Long id;
	@XmlElement(name = "creationDate")
	public Timestamp creationDate;
	@XmlElement(name = "email")
	public String email;
	@XmlElement(name = "enabled")
	public boolean enabled;
	@XmlElement(name = "receiveTopUpNotification")
	public boolean receiveTopUpNotification;
	@XmlElement(name = "firstName")
	public String firstName;
	@XmlElement(name = "lastName")
	public String lastName;
	@XmlElement(name = "login")
	public String login;
	@XmlElement(name = "password")
	public String password;
	@XmlElement(name = "phoneNumber")
	public String phoneNumber;
	@XmlElement(name = "userHasProfileHasEnterpriseResponse")
	public List<UserHasProfileHasEnterpriseResponse> userHasProfileHasEnterpriseResponse;

	public UserResponse() {
		super();
	}

	public UserResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public UserResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public UserResponse(ResponseCode codigo,
			String mensajeRespuesta, User user) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = user.getId();
		this.creationDate = user.getCreationDate();
		this.email = user.getEmail();
		this.enabled = user.getEnabled();
		this.receiveTopUpNotification = user.getReceiveTopUpNotification();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.login = user.getLogin();
		this.password = user.getPassword();
		this.phoneNumber = user.getPhoneNumber();
		this.userHasProfileHasEnterpriseResponse = new ArrayList<UserHasProfileHasEnterpriseResponse>();
		for (UserHasProfileHasEnterprise uhp:user.getUserHasProfileHasEnterprises()){
			this.userHasProfileHasEnterpriseResponse.add(new UserHasProfileHasEnterpriseResponse(uhp));
	    }
		
	}
	
	public UserResponse(User user) {
		this.id = user.getId();
		this.creationDate = user.getCreationDate();
		this.email = user.getEmail();
		this.enabled = user.getEnabled();
		this.receiveTopUpNotification = user.getReceiveTopUpNotification();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.login = user.getLogin();
		this.password = user.getPassword();
		this.phoneNumber = user.getPhoneNumber();
		this.userHasProfileHasEnterpriseResponse = new ArrayList<UserHasProfileHasEnterpriseResponse>();
		for (UserHasProfileHasEnterprise uhp:user.getUserHasProfileHasEnterprises()){
			userHasProfileHasEnterpriseResponse.add(new UserHasProfileHasEnterpriseResponse(uhp));
	    }
	}

	public Long getCurrentProfile(Long enterpriseId) {
        Long profileId = null;
        for (UserHasProfileHasEnterpriseResponse uhp : this.userHasProfileHasEnterpriseResponse) {
            if (uhp.getEndingDate() == null && uhp.getEnterpriseId().equals(enterpriseId)) {
                profileId = uhp.getProfileId();
            }
        }
        return profileId;
    }
}
