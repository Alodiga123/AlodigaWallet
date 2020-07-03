package com.alodiga.wallet.rest.converter;

import javax.ejb.EJB;

import com.alodiga.wallet.bean.APIPreferences;
import com.alodiga.wallet.exception.GeneralException;
import com.alodiga.wallet.exception.NullParameterException;
import com.alodiga.wallet.exception.RegisterNotFoundException;
import com.alodiga.wallet.model.PreferenceField;
import com.alodiga.wallet.rest.request.PreferenceFieldRequest;



public class ConverterPreferenceField  {
	
	@EJB
    private APIPreferences preferences;

	public PreferenceField converterPreferenceField(PreferenceFieldRequest request) {
		PreferenceField preferenceField = new PreferenceField();
		preferenceField.setId(request.getId());
		preferenceField.setName(request.getName());
		preferenceField.setEnabled(request.getEnabled());
		try {
			preferenceField.setPreferenceTypeId(preferences.loadPreferenceTypeById(request.getPreferenceTypeId()));
			preferenceField.setPreferenceId(preferences.loadPreferenceById(request.getPreferenceId()));
		} catch (NullParameterException | GeneralException | RegisterNotFoundException e) {

		}
		return preferenceField;
		
	}
	
		
}
