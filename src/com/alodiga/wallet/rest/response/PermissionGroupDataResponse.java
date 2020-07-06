package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.common.model.PermissionGroupData;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PermissionGroupDataResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "alias")
    private String alias;
	@XmlElement(name = "description")
    private String description;
    private LanguageResponse languajeResponse;  

	public PermissionGroupDataResponse() {
		super();
	}

	public PermissionGroupDataResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public PermissionGroupDataResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public PermissionGroupDataResponse(ResponseCode codigo,
			String mensajeRespuesta, PermissionGroupData permissionGroupData) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = permissionGroupData.getId();
		this.alias = permissionGroupData.getAlias();
		this.description = permissionGroupData.getDescription();
		this.languajeResponse = new LanguageResponse(permissionGroupData.getLanguage());
		
	}

	public PermissionGroupDataResponse( PermissionGroupData permissionGroupData) {
		this.id = permissionGroupData.getId();
		this.alias = permissionGroupData.getAlias();
		this.description = permissionGroupData.getDescription();
		this.languajeResponse = new LanguageResponse(permissionGroupData.getLanguage());
		
	}
	
}
