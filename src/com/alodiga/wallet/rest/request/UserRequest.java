package com.alodiga.wallet.rest.request;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;


@XmlRootElement(name = "UserRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class UserRequest implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "login")
	private String login;
	@XmlElement(name = "email")
	private String email;
	@XmlElement(name = "enabled")
	private boolean enabled;
	@XmlElement(name = "firstName")
    private String firstName;
	@XmlElement(name = "lastName")
    private String lastName;
	@XmlElement(name = "password")
    private String password;
	@XmlElement(name = "phoneNumber")
    private String phoneNumber;
	@XmlElement(name = "creationDate")
    private String creationDate;
	@XmlElement(name = "receiveTopUpNotification")
    private boolean receiveTopUpNotification;

}
