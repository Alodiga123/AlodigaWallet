package com.alodiga.wallet.respuestas;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.alodiga.wallet.common.model.Country;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CountryListResponse extends Response {

	public List<Country> countries;
	
	public CountryListResponse() {
		super();
	}
	
	public CountryListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.countries = null;
	}
	
	public CountryListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.countries = null;
	}

	public CountryListResponse(ResponseCode code, String mensaje, List<Country> countries) {
		super(new Date(), code.getCodigo(), mensaje);
		this.countries = countries;
	}
        
}
