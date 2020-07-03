package com.alodiga.wallet.rest.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.alodiga.wallet.model.PermissionGroup;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;
import com.alodiga.wallet.rest.converter.ConverterPermissionGroup;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PermissionGroupListResponse extends Response {

	private List<PermissionGroupResponse> permissionGroupResponses;
	
	public PermissionGroupListResponse() {
		super();
	}
	
	public PermissionGroupListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.permissionGroupResponses = null;
	}
	
	public PermissionGroupListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.permissionGroupResponses = null;
	}

	public PermissionGroupListResponse(ResponseCode code, String mensaje, List<PermissionGroup> permissionGroups) {
		super(new Date(), code.getCodigo(), mensaje);
		permissionGroupResponses = new ArrayList<PermissionGroupResponse>();
		for (PermissionGroup p: permissionGroups){
			PermissionGroupResponse groupResponse = new PermissionGroupResponse(p);
		    permissionGroupResponses.add(groupResponse);
		}
		this.permissionGroupResponses = permissionGroupResponses;
	}
        
}
