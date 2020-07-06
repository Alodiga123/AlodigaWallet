package com.alodiga.wallet.respuestas;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.alodiga.wallet.common.model.Country;


@XmlRootElement(name = "CountryResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CountryResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	public Country response;
        

	public CountryResponse() {
		super();
	}

	public CountryResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
		this.response = null;
	}

	public CountryResponse(ResponseCode codigo,String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.response = null;
	}

	public CountryResponse(ResponseCode codigo,String mensajeRespuesta, Country countryId) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.response = countryId;
	}
        


}
