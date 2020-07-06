package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.common.model.Permission;
import com.alodiga.wallet.common.model.PermissionData;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PermissionResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "enabled")
    private boolean enabled;
	@XmlElement(name = "name")
    private String name;
	@XmlElement(name = "action")
    private String action;
	@XmlElement(name = "entity")
    private String entity;
	private PermissionGroupResponse groupResponse;
	private List<PermissionDataResponse> dataResponse;

	public PermissionResponse() {
		super();
	}

	public PermissionResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public PermissionResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public PermissionResponse(ResponseCode codigo,
			String mensajeRespuesta, Permission permission) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = permission.getId();
		this.enabled = permission.getEnabled();
		this.name = permission.getName();
		this.action = permission.getAction();
		this.entity = permission.getEntity();
		this.groupResponse =new PermissionGroupResponse(permission.getPermissionGroup());
		this.dataResponse = new ArrayList<PermissionDataResponse>();
		for (PermissionData data:permission.getPermissionData()){
			this.dataResponse.add(new PermissionDataResponse(data));
	    }
		
	}
	
	public PermissionResponse(Permission permission) {
		this.id = permission.getId();
		this.enabled = permission.getEnabled();
		this.name = permission.getName();
		this.groupResponse =new PermissionGroupResponse(permission.getPermissionGroup());
		this.dataResponse = new ArrayList<PermissionDataResponse>();
		for (PermissionData data:permission.getPermissionData()){
			this.dataResponse.add(new PermissionDataResponse(data));
	    }
		
	}
	
	public PermissionDataResponse getPermissionDataByLanguageId(Long languageId) {
	        PermissionDataResponse pd = null;
	        for (PermissionDataResponse pData : this.dataResponse) {
	            if (pData.getLanguajeResponse().getLanguage().getId().equals(languageId)) {
	                pd = pData;
	                break;
	            }
	        }
	        return pd;
	    }

	
}
