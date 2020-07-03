package com.alodiga.wallet.rest.request;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;


@XmlRootElement(name = "PermissionDataRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PermissionDataRequest implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "alias")
	private String alias;
	@XmlElement(name = "description")
	private String description;
	@XmlElement(name = "languageId")
	private Long languageId;
	@XmlElement(name = "permissionId")
	private Long permissionId;
	
	public PermissionDataRequest() {
		super();
	}
	
		
}
