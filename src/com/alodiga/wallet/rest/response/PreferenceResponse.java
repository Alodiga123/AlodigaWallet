package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.common.model.Preference;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PreferenceResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "name")
    private String name;
	@XmlElement(name = "enabled")
    private boolean enabled;
	@XmlElement(name = "description")
    private String description;


	public PreferenceResponse() {
		super();
	}

	public PreferenceResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public PreferenceResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public PreferenceResponse(ResponseCode codigo,
			String mensajeRespuesta, Preference preference) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = preference.getId();
		this.enabled = preference.getEnabled();
		this.name = preference.getName();
		this.description = preference.getDescription();
	}
	
	public PreferenceResponse(Preference preference) {
		this.id = preference.getId();
		this.enabled = preference.getEnabled();
		this.name = preference.getName();
		this.description = preference.getDescription();
	}
	
}
