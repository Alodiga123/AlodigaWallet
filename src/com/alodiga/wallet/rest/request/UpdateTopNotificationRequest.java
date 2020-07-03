package com.alodiga.wallet.rest.request;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;


@XmlRootElement(name = "UpdateTopNotificationRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class UpdateTopNotificationRequest implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "ids")
	private String ids;
	
	public UpdateTopNotificationRequest() {
		super();
	}
	
		
}
