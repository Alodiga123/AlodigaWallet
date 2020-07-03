package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.model.PreferenceType;
import com.alodiga.wallet.model.Profile;
import com.alodiga.wallet.model.ProfileData;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PreferenceTypeResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "type")
    private String type;

	public PreferenceTypeResponse() {
		super();
	}

	public PreferenceTypeResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public PreferenceTypeResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public PreferenceTypeResponse(ResponseCode codigo,
			String mensajeRespuesta, PreferenceType preferenceType) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = preferenceType.getId();
		this.type = preferenceType.getType();
		
	}
	
	public PreferenceTypeResponse(PreferenceType preferenceType) {
		this.id = preferenceType.getId();
		this.type = preferenceType.getType();
		
	}

}
