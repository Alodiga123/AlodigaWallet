package com.alodiga.wallet.rest.request;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;


@XmlRootElement(name = "PermissionHasProfileRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PermissionHasProfileRequest implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "permissionId")
	private Long permissionId;
	@XmlElement(name = "profileId")
	private Long profileId;
	
	public PermissionHasProfileRequest() {
		super();
	}
	
		
}
