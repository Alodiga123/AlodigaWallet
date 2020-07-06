//package com.alodiga.wallet.rest.converter;
//
//import javax.ejb.EJB;
//
//import com.alodiga.wallet.bean.APIPreferences;
//import com.alodiga.wallet.common.exception.GeneralException;
//import com.alodiga.wallet.common.exception.NullParameterException;
//import com.alodiga.wallet.common.exception.RegisterNotFoundException;
//import com.alodiga.wallet.common.model.PreferenceValue;
//import com.alodiga.wallet.rest.request.PreferenceValueRequest;
//
//
//
//public class ConverterPreferenceValue  {
//	
//	@EJB
//    private APIPreferences preferences;
//
//	public PreferenceValue converterPreferenceValue(PreferenceValueRequest request) {
//		PreferenceValue preferenceValue = new PreferenceValue();
//		preferenceValue.setId(request.getId());
//		preferenceValue.setValue(request.getValue());
//		preferenceValue.setBeginningDate(request.getBeginningDate());
//		preferenceValue.setEndingDate(request.getEndingDate());
//		try {
//			preferenceValue.setPreferenceFieldId(preferences.loadPreferenceFieldById(request.getPreferenceFieldId()));
//			preferenceValue.setEnterpriseId(preferences.loadEnterprisedById(request.getEnterpriseId()));
//		} catch (NullParameterException | GeneralException | RegisterNotFoundException e) {
//
//		}
//		return preferenceValue;
//		
//	}
//	
//		
//}
