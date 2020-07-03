package com.alodiga.wallet.rest.request;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PreferenceValueRequest implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "value")
    private String value;
	@XmlElement(name = "beginningDate")
	private Date beginningDate;
	@XmlElement(name = "endingDate")
	private Date endingDate;
	@XmlElement(name = "preferenceFieldId")
	private Long preferenceFieldId;
	@XmlElement(name = "enterpriseId")
	private Long enterpriseId;

	public PreferenceValueRequest() {
		super();
	}

	
}
