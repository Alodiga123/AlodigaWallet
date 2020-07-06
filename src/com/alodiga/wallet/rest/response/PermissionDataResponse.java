package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.common.model.PermissionData;
import com.alodiga.wallet.common.model.PermissionGroupData;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PermissionDataResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "alias")
    private String alias;
	@XmlElement(name = "description")
    private String description;
    private LanguageResponse languajeResponse;  

	public PermissionDataResponse() {
		super();
	}

	public PermissionDataResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public PermissionDataResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public PermissionDataResponse(ResponseCode codigo,
			String mensajeRespuesta, PermissionData permissionData) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = permissionData.getId();
		this.alias = permissionData.getAlias();
		this.description = permissionData.getDescription();
		this.languajeResponse = new LanguageResponse(permissionData.getLanguage());
		
	}

	public PermissionDataResponse(PermissionData permissionData) {
		this.id = permissionData.getId();
		this.alias = permissionData.getAlias();
		this.description = permissionData.getDescription();
		this.languajeResponse = new LanguageResponse(permissionData.getLanguage());
		
	}
	
}
