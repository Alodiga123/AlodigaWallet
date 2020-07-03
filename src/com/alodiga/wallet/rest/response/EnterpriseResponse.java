package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.model.Enterprise;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class EnterpriseResponse extends Response implements Serializable {

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
	@XmlElement(name = "currency")
    private CurrencyResponse currencyResponse;
	@XmlElement(name = "country")
    private CountryResponse countryResponse;

	public EnterpriseResponse() {
		super();
	}

	public EnterpriseResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public EnterpriseResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public EnterpriseResponse(ResponseCode codigo,
			String mensajeRespuesta, Enterprise enterprise) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = enterprise.getId();
		this.name = enterprise.getName();
		this.url = enterprise.getUrl();
		this.email = enterprise.getEmail();
		this.atcNumber = enterprise.getAtcNumber();
		this.address = enterprise.getAddress();
		this.invoiceAddress = enterprise.getInvoiceAddress();
		this.enabled = enterprise.getEnabled();
		this.infoEmail = enterprise.getInfoEmail();
		this.currencyResponse = new CurrencyResponse(enterprise.getCurrencyId());
		this.countryResponse = new CountryResponse(enterprise.getCountryId());
		
	}
	
	public EnterpriseResponse(Enterprise enterprise) {
		this.id = enterprise.getId();
		this.name = enterprise.getName();
		this.url = enterprise.getUrl();
		this.email = enterprise.getEmail();
		this.atcNumber = enterprise.getAtcNumber();
		this.address = enterprise.getAddress();
		this.invoiceAddress = enterprise.getInvoiceAddress();
		this.enabled = enterprise.getEnabled();
		this.infoEmail = enterprise.getInfoEmail();
		this.currencyResponse = new CurrencyResponse(enterprise.getCurrencyId());
		this.countryResponse = new CountryResponse(enterprise.getCountryId());
	}

	
}
