package com.alodiga.wallet.responses;


import com.alodiga.wallet.common.model.AffiliationRequest;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AffiliationRequestResponse extends Response {

	public AffiliationRequest affiliationRequest;
	
	public AffiliationRequestResponse() {
		super();
	}
	
	public AffiliationRequestResponse(ResponseCode code) {
		super(new Date(), code.getCode(), code.name());
		this.affiliationRequest = null;
	}
	
	public AffiliationRequestResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCode(), mensaje);
		this.affiliationRequest = null;
	}

	public AffiliationRequestResponse(ResponseCode code, String mensaje, AffiliationRequest affiliationRequest) {
		super(new Date(), code.getCode(), mensaje);
		this.affiliationRequest = affiliationRequest;
	}
        
}
