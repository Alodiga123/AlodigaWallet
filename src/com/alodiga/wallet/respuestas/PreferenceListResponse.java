package com.alodiga.wallet.respuestas;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.alodiga.wallet.common.model.Preference;


@XmlRootElement(name = "PreferenceListResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class PreferenceListResponse extends Response implements Serializable {

	public List<Preference> preferences;
	
	public PreferenceListResponse() {
		super();
	}
	
	public PreferenceListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.preferences = null;
	}
	
	public PreferenceListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.preferences = null;
	}

	public PreferenceListResponse(ResponseCode code, String mensaje, List<Preference> preferences) {
		super(new Date(), code.getCodigo(), mensaje);
		this.preferences = preferences;
	}
        
}
