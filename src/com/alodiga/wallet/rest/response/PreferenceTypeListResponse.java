package com.alodiga.wallet.rest.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.alodiga.wallet.common.model.PreferenceType;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PreferenceTypeListResponse extends Response {

	private List<PreferenceTypeResponse> preferenceTypeResponses;
	
	public PreferenceTypeListResponse() {
		super();
	}
	
	public PreferenceTypeListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.preferenceTypeResponses = null;
	}
	
	public PreferenceTypeListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.preferenceTypeResponses = null;
	}

	public PreferenceTypeListResponse(ResponseCode code, String mensaje, List<PreferenceType> preferenceTypes) {
		super(new Date(), code.getCodigo(), mensaje);
		this.preferenceTypeResponses = new ArrayList<PreferenceTypeResponse>();
		for (PreferenceType p: preferenceTypes){
			PreferenceTypeResponse fieldResponse = new PreferenceTypeResponse(p);
			this.preferenceTypeResponses.add(fieldResponse);
		}
	}
        
}
