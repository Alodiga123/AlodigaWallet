package com.alodiga.wallet.responses;

import com.alodiga.wallet.common.model.CreditcardType;


import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CreditCardListResponse extends Response {

	public List<CreditcardType> creditcardTypes;
	
	public CreditCardListResponse() {
		super();
	}
	
	public CreditCardListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.creditcardTypes = null;
	}
	
	public CreditCardListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.creditcardTypes = null;
	}

	public CreditCardListResponse(ResponseCode code, String mensaje, List<CreditcardType> creditcardTypes) {
		super(new Date(), code.getCodigo(), mensaje);
		this.creditcardTypes = creditcardTypes;
	}
        
}
