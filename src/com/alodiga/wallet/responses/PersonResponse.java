package com.alodiga.wallet.responses;

import com.alodiga.wallet.common.model.Person;
import com.alodiga.wallet.response.generic.PersonGeneric;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonResponse extends Response {

	public PersonGeneric person;
	
	public PersonResponse() {
		super();
	}
	
	public PersonResponse(ResponseCode code) {
		super(new Date(), code.getCode(), code.name());
		this.person = null;
	}
	
	public PersonResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCode(), mensaje);
		this.person = null;
	}

	public PersonResponse(ResponseCode code, String mensaje, PersonGeneric person) {
		super(new Date(), code.getCode(), mensaje);
		this.person = person;
	}
        
}
