package com.alodiga.wallet.rest.request;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;


@XmlRootElement(name = "ProfileRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ProfileRequest implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "name")
	private String name;
	@XmlElement(name = "enabled")
	private boolean enabled;
	@XmlElement(name = "permissionHasProfileRequests")
	private List<PermissionHasProfileRequest> permissionHasProfileRequests;
	@XmlElement(name = "profileDataRequests")
	private List<ProfileDataRequest> profileDataRequests;
	
	public ProfileRequest() {
		super();
	}
	
		
}
