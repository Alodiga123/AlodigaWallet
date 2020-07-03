package com.alodiga.wallet.rest.converter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;

import com.alodiga.wallet.bean.APIPreferences;
import com.alodiga.wallet.exception.GeneralException;
import com.alodiga.wallet.exception.NullParameterException;
import com.alodiga.wallet.exception.RegisterNotFoundException;
import com.alodiga.wallet.model.PreferenceField;
import com.alodiga.wallet.model.PreferenceValue;
import com.alodiga.wallet.rest.request.PreferenceFieldRequest;
import com.alodiga.wallet.rest.request.PreferenceValueRequest;



public class ConverterPreferenceValue  {
	
	@EJB
    private APIPreferences preferences;

	public PreferenceValue converterPreferenceValue(PreferenceValueRequest request) {
		PreferenceValue preferenceValue = new PreferenceValue();
		preferenceValue.setId(request.getId());
		preferenceValue.setValue(request.getValue());
		preferenceValue.setBeginningDate(request.getBeginningDate());
		preferenceValue.setEndingDate(request.getEndingDate());
		try {
			preferenceValue.setPreferenceFieldId(preferences.loadPreferenceFieldById(request.getPreferenceFieldId()));
			preferenceValue.setEnterpriseId(preferences.loadEnterprisedById(request.getEnterpriseId()));
		} catch (NullParameterException | GeneralException | RegisterNotFoundException e) {

		}
		return preferenceValue;
		
	}
	
		
}
