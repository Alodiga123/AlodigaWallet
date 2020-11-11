package com.alodiga.wallet.responses;


import com.alodiga.wallet.common.model.TopUpCountry;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TopUpCountryListResponse extends Response {

	public List<TopUpCountry> topUpCountrys;
	
	public TopUpCountryListResponse() {
		super();
	}
	
	public TopUpCountryListResponse(ResponseCode code) {
		super(new Date(), code.getCode(), code.name());
		this.topUpCountrys = null;
	}
	
	public TopUpCountryListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCode(), mensaje);
		this.topUpCountrys = null;
	}

	public TopUpCountryListResponse(ResponseCode code, String mensaje, List<TopUpCountry> topUpCountrys) {
		super(new Date(), code.getCode(), mensaje);
		this.topUpCountrys = topUpCountrys;
	}
        
}
