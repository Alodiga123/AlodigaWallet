package com.alodiga.wallet.rest.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.alodiga.wallet.model.Permission;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PermissionListResponse extends Response {

	private List<PermissionResponse> permissionResponses;
	
	public PermissionListResponse() {
		super();
	}
	
	public PermissionListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.permissionResponses = null;
	}
	
	public PermissionListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.permissionResponses = null;
	}

	public PermissionListResponse(ResponseCode code, String mensaje, List<Permission> permissions) {
		super(new Date(), code.getCodigo(), mensaje);
		this.permissionResponses = new ArrayList<PermissionResponse>();
		for (Permission p: permissions){
			PermissionResponse permissionResponse = new PermissionResponse(p);
			this.permissionResponses.add(permissionResponse);
		}
	}
        
}
