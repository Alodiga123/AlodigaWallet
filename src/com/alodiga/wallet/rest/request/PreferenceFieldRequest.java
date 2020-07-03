package com.alodiga.wallet.rest.request;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PreferenceFieldRequest implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "name")
    private String name;
	@XmlElement(name = "enabled")
    private short enabled;
	@XmlElement(name = "preferenceFieldId")
	private Long preferenceTypeId;
	@XmlElement(name = "preferenceId")
	private Long preferenceId;

	public PreferenceFieldRequest() {
		super();
	}
	
}
