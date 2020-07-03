package com.alodiga.wallet.rest.request;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;


@XmlRootElement(name = "PermissionRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PermissionRequest implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "groupId")
	private Long groupId;
	@XmlElement(name = "action")
	private String action;
	@XmlElement(name = "entity")
	private String entity;
	@XmlElement(name = "name")
	private String name;
	@XmlElement(name = "enabled")
	private boolean enabled;
	@XmlElement(name = "permissionDataRequest")
	private List<PermissionDataRequest> permissionDataRequest;
	
	public PermissionRequest() {
		super();
	}
	
		
}
