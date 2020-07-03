package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.model.Country;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class CountryResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "name")
	private String name;
	@XmlElement(name = "shortName")
	private String shortName;
	@XmlElement(name = "code")
	private String code;
	@XmlElement(name = "alternativeName1")
	private String alternativeName1;
	@XmlElement(name = "alternativeName2")
	private String alternativeName2;
	@XmlElement(name = "alternativeName3")
	private String alternativeName3;

	public CountryResponse() {
		super();
	}

	public CountryResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public CountryResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public CountryResponse(ResponseCode codigo,
			String mensajeRespuesta, Country country) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = country.getId();
		this.name = country.getName();
		this.shortName = country.getShortName();
		this.code = country.getCode();
		this.alternativeName1 = country.getAlternativeName1();
		this.alternativeName2 = country.getAlternativeName2();
		this.alternativeName3 = country.getAlternativeName3();

	}
	
	public CountryResponse(Country country) {
		this.id = country.getId();
		this.name = country.getName();
		this.shortName = country.getShortName();
		this.code = country.getCode();
		this.alternativeName1 = country.getAlternativeName1();
		this.alternativeName2 = country.getAlternativeName2();
		this.alternativeName3 = country.getAlternativeName3();


	}

	
}
