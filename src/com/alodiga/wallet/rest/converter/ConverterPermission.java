package com.alodiga.wallet.rest.converter;

import com.alodiga.wallet.model.Language;
import com.alodiga.wallet.model.Permission;
import com.alodiga.wallet.model.PermissionGroup;
import com.alodiga.wallet.rest.request.PermissionDataRequest;
import com.alodiga.wallet.rest.request.PermissionRequest;


public class ConverterPermission  {

	public Permission converterPermission(PermissionRequest permissionRequest) {
		Permission permission = new Permission();
		permission.setId(permissionRequest.getId());
		permission.setAction(permissionRequest.getAction());
		permission.setEnabled(permissionRequest.isEnabled());
		permission.setName(permissionRequest.getName());
		permission.setEntity(permissionRequest.getEntity());
		PermissionGroup permissionGroup = new PermissionGroup();
		permissionGroup.setId(permissionRequest.getId());
		permission.setPermissionGroup(permissionGroup);
		for (PermissionDataRequest data:permissionRequest.getPermissionDataRequest()) {
			data.setId(data.getId());
			data.setAlias(data.getAlias());
			data.setDescription(data.getDescription());
			Language language = new Language();
			language.setId(data.getLanguageId());	
		}
		return permission;
	}
	
		
}
