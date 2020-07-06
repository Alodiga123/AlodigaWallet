//package com.alodiga.wallet.rest.converter;
//
//import javax.ejb.EJB;
//
//import com.alodiga.wallet.bean.APIPreferences;
//import com.alodiga.wallet.common.model.Preference;
//import com.alodiga.wallet.rest.request.PreferenceRequest;
//
//
//
//public class ConverterPreference  {
//	
//	@EJB
//    private APIPreferences preferences;
//
//	public Preference converterPreferenceField(PreferenceRequest request) {
//		Preference preference = new Preference();
//		preference.setId(request.getId());
//		preference.setName(request.getName());
//		preference.setEnabled(request.isEnabled());
//		preference.setDescription(request.getDescription());
//		return preference;
//		
//	}
//	
//		
//}
