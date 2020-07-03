package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.model.Currency;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class CurrencyResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "name")
	private String name;
	@XmlElement(name = "symbol")
	private String symbol;

	public CurrencyResponse() {
		super();
	}

	public CurrencyResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public CurrencyResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public CurrencyResponse(ResponseCode codigo,
			String mensajeRespuesta, Currency currency) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = currency.getId();
		this.name = currency.getName();
		this.symbol = currency.getSymbol();

	}
	
	public CurrencyResponse(Currency currency) {
		this.id = currency.getId();
		this.name = currency.getName();
		this.symbol = currency.getSymbol();

	}

	
}
