package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.common.model.ProfileData;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ProfileDataResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private int id;
	@XmlElement(name = "alias")
    private String alias;
	@XmlElement(name = "description")
    private String description;
    private LanguageResponse languajeResponse;  

	public ProfileDataResponse() {
		super();
	}

	public ProfileDataResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public ProfileDataResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public ProfileDataResponse(ResponseCode codigo,
			String mensajeRespuesta, ProfileData profileData) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = profileData.getId();
		this.alias = profileData.getAlias();
		this.description = profileData.getDescription();
		this.languajeResponse = new LanguageResponse(profileData.getLanguage());
		
	}

	public ProfileDataResponse(ProfileData profileData) {
		this.id = profileData.getId();
		this.alias = profileData.getAlias();
		this.description = profileData.getDescription();
		this.languajeResponse = new LanguageResponse(profileData.getLanguage());
		
	}
	
}
