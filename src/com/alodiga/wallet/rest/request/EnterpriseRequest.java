package com.alodiga.wallet.rest.request;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;


@XmlRootElement(name = "EnterpriseRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class EnterpriseRequest implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "name")
	private String name;
	@XmlElement(name = "url")
	private String url;
	@XmlElement(name = "email")
	private String email;
	@XmlElement(name = "atcNumber")
	private String atcNumber;
	@XmlElement(name = "address")
	private String address;
	@XmlElement(name = "invoiceAddress")
	private String invoiceAddress;
	@XmlElement(name = "enabled")
    private boolean enabled;
	@XmlElement(name = "infoEmail")
    private String infoEmail;
	@XmlElement(name = "currencyId")
    private Long currencyId;
	@XmlElement(name = "countryId")
    private Long countryId;

}
