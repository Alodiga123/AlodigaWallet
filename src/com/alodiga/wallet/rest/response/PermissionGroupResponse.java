package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.common.model.PermissionGroup;
import com.alodiga.wallet.common.model.PermissionGroupData;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PermissionGroupResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "enabled")
    private boolean enabled;
	@XmlElement(name = "name")
    private String name;
	private List<PermissionGroupDataResponse> groupDataResponse;

	public PermissionGroupResponse() {
		super();
	}

	public PermissionGroupResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public PermissionGroupResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public PermissionGroupResponse(ResponseCode codigo,
			String mensajeRespuesta, PermissionGroup permissionGroup) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = permissionGroup.getId();
		this.enabled = permissionGroup.getEnabled();
		this.name = permissionGroup.getName();
		this.groupDataResponse = new ArrayList<PermissionGroupDataResponse>();
		for (PermissionGroupData groupData:permissionGroup.getPermissionGroupData()){
			this.groupDataResponse.add(new PermissionGroupDataResponse(groupData));
	    }
		
	}
	
	public PermissionGroupResponse(PermissionGroup permissionGroup) {
		this.id = permissionGroup.getId();
		this.enabled = permissionGroup.getEnabled();
		this.name = permissionGroup.getName();
		this.groupDataResponse = new ArrayList<PermissionGroupDataResponse>();
		for (PermissionGroupData groupData:permissionGroup.getPermissionGroupData()){
			this.groupDataResponse.add(new PermissionGroupDataResponse(groupData));
	    }
		
	}
	
	public PermissionGroupDataResponse getPermissionGroupDataByLanguageId(Long languageId) {
        for (PermissionGroupDataResponse pgData : this.groupDataResponse) {
            if (pgData.getLanguajeResponse().getLanguage().getId().equals(languageId)) {
                return pgData;
            }
        }
        return null;
    }

	
}
