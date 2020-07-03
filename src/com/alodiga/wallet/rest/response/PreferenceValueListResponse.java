package com.alodiga.wallet.rest.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.alodiga.wallet.model.PreferenceValue;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PreferenceValueListResponse extends Response {

	private List<PreferenceValueResponse> preferenceValueResponses;
	
	public PreferenceValueListResponse() {
		super();
	}
	
	public PreferenceValueListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.preferenceValueResponses = null;
	}
	
	public PreferenceValueListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.preferenceValueResponses = null;
	}

	public PreferenceValueListResponse(ResponseCode code, String mensaje, List<PreferenceValue> preferenceValues) {
		super(new Date(), code.getCodigo(), mensaje);
		this.preferenceValueResponses = new ArrayList<PreferenceValueResponse>();
		for (PreferenceValue p: preferenceValues){
			PreferenceValueResponse fieldResponse = new PreferenceValueResponse(p);
			this.preferenceValueResponses.add(fieldResponse);
		}
	}
        
}
