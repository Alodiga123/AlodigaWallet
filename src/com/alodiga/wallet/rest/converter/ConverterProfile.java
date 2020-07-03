package com.alodiga.wallet.rest.converter;

import com.alodiga.wallet.model.Language;
import com.alodiga.wallet.model.Profile;
import com.alodiga.wallet.rest.request.PermissionHasProfileRequest;
import com.alodiga.wallet.rest.request.ProfileDataRequest;
import com.alodiga.wallet.rest.request.ProfileRequest;


public class ConverterProfile  {

	public Profile converterProfile(ProfileRequest profileRequest) {
		Profile profile = new Profile();
		profile.setId(profileRequest.getId());
		profile.setEnabled(profileRequest.isEnabled());
		profile.setName(profileRequest.getName());
		for (ProfileDataRequest data:profileRequest.getProfileDataRequests()) {
			data.setId(data.getId());
			data.setAlias(data.getAlias());
			data.setDescription(data.getDescription());
			Language language = new Language();
			language.setId(data.getLanguageId());	
			data.setProfileId(data.getProfileId());
		}
		for (PermissionHasProfileRequest request :profileRequest.getPermissionHasProfileRequests()) {
			request.setId(request.getId());
			request.setProfileId(request.getProfileId());
			request.setProfileId(request.getProfileId());
		}
		return profile;
	}
	
		
}
