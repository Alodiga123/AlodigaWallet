package com.alodiga.wallet.respuestas;


import com.alodiga.wallet.common.model.PersonType;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonTypeListResponse extends Response {

	public List<PersonType> personTypes;
	
	public PersonTypeListResponse() {
		super();
	}
	
	public PersonTypeListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.personTypes = null;
	}
	
	public PersonTypeListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.personTypes = null;
	}

	public PersonTypeListResponse(ResponseCode code, String mensaje, List<PersonType> personTypes) {
		super(new Date(), code.getCodigo(), mensaje);
		this.personTypes = personTypes;
	}
        
}
