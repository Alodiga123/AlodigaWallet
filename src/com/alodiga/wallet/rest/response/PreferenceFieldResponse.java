package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.model.PreferenceField;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PreferenceFieldResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "name")
    private String name;
	@XmlElement(name = "enabled")
    private short enabled;
	@XmlElement(name = "preferenceFieldResponse")
	private PreferenceTypeResponse preferenceTypeResponse;
	@XmlElement(name = "preferenceResponse")
	private PreferenceResponse preferenceResponse;

	public PreferenceFieldResponse() {
		super();
	}

	public PreferenceFieldResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public PreferenceFieldResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public PreferenceFieldResponse(ResponseCode codigo,
			String mensajeRespuesta, PreferenceField preferenceField) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = preferenceField.getId();
		this.enabled = preferenceField.getEnabled();
		this.name = preferenceField.getName();
		this.preferenceTypeResponse = new PreferenceTypeResponse(preferenceField.getPreferenceTypeId());
		this.preferenceResponse = new PreferenceResponse(preferenceField.getPreferenceId());
	}
	
	public PreferenceFieldResponse(PreferenceField preferenceField) {
		this.id = preferenceField.getId();
		this.enabled = preferenceField.getEnabled();
		this.name = preferenceField.getName();
		this.preferenceTypeResponse = new PreferenceTypeResponse(preferenceField.getPreferenceTypeId());
		this.preferenceResponse = new PreferenceResponse(preferenceField.getPreferenceId());
		
	}

	
}
