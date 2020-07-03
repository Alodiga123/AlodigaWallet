package com.alodiga.wallet.rest.converter;

import com.alodiga.wallet.model.PreferenceType;
import com.alodiga.wallet.rest.request.PreferenceTypeRequest;



public class ConverterPreferenceType  {

	public PreferenceType converterPreferenceType(PreferenceTypeRequest typeRequest) {
		PreferenceType preferenceType = new PreferenceType();
		preferenceType.setId(typeRequest.getId());
		preferenceType.setType(typeRequest.getType());
		return preferenceType;
		
	}
	
		
}
