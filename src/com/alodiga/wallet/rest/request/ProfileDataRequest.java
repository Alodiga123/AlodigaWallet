package com.alodiga.wallet.rest.request;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;


@XmlRootElement(name = "ProfileDataRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ProfileDataRequest implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private int id;
	@XmlElement(name = "alias")
	private String alias;
	@XmlElement(name = "description")
	private String description;
	@XmlElement(name = "languageId")
	private Long languageId;
	@XmlElement(name = "profileId")
	private Long profileId;
	
	public ProfileDataRequest() {
		super();
	}
	
		
}
