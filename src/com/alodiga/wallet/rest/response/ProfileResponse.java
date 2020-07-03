package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.model.Profile;
import com.alodiga.wallet.model.ProfileData;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ProfileResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "enabled")
    private boolean enabled;
	@XmlElement(name = "name")
    private String name;
	private List<ProfileDataResponse> dataResponse;

	public ProfileResponse() {
		super();
	}

	public ProfileResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public ProfileResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public ProfileResponse(ResponseCode codigo,
			String mensajeRespuesta, Profile profile) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = profile.getId();
		this.enabled = profile.getEnabled();
		this.name = profile.getName();
		this.dataResponse = new ArrayList<ProfileDataResponse>();
		for (ProfileData data:profile.getProfileData()){
			this.dataResponse.add(new ProfileDataResponse(data));
	    }
		
	}
	
	public ProfileResponse(Profile profile) {
		this.id = profile.getId();
		this.enabled = profile.getEnabled();
		this.name = profile.getName();
		this.dataResponse = new ArrayList<ProfileDataResponse>();
		for (ProfileData data:profile.getProfileData()){
			this.dataResponse.add(new ProfileDataResponse(data));
	    }
		
	}

	 public ProfileDataResponse getProfileDataByLanguageId(Long languageId) {
		 ProfileDataResponse pd = null;
	        for (ProfileDataResponse pData : this.dataResponse) {
	            if (pData.getLanguajeResponse().getLanguage().getId().equals(languageId)) {
	                pd = pData;
	                break;
	            }
	        }
	        return pd;
	    }
	
}
