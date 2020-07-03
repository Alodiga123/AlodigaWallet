package com.alodiga.wallet.rest.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.alodiga.wallet.model.PreferenceField;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PreferenceFieldListResponse extends Response {

	private List<PreferenceFieldResponse> preferenceFieldResponses;
	
	public PreferenceFieldListResponse() {
		super();
	}
	
	public PreferenceFieldListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.preferenceFieldResponses = null;
	}
	
	public PreferenceFieldListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.preferenceFieldResponses = null;
	}

	public PreferenceFieldListResponse(ResponseCode code, String mensaje, List<PreferenceField> preferenceFields) {
		super(new Date(), code.getCodigo(), mensaje);
		this.preferenceFieldResponses = new ArrayList<PreferenceFieldResponse>();
		for (PreferenceField p: preferenceFields){
			PreferenceFieldResponse fieldResponse = new PreferenceFieldResponse(p);
			this.preferenceFieldResponses.add(fieldResponse);
		}
	}
        
}
