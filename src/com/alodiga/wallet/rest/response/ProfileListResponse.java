package com.alodiga.wallet.rest.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.alodiga.wallet.common.model.Profile;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ProfileListResponse extends Response {

	private List<ProfileResponse> profileResponses;
	
	public ProfileListResponse() {
		super();
	}
	
	public ProfileListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.profileResponses = null;
	}
	
	public ProfileListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.profileResponses = null;
	}

	public ProfileListResponse(ResponseCode code, String mensaje, List<Profile> profiles) {
		super(new Date(), code.getCodigo(), mensaje);
		this.profileResponses = new ArrayList<ProfileResponse>();
		for (Profile p: profiles){
			ProfileResponse profileResponse = new ProfileResponse(p);
			this.profileResponses.add(profileResponse);
		}
	}
        
}
