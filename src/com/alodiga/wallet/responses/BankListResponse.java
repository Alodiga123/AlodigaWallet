package com.alodiga.wallet.responses;


import com.alodiga.wallet.response.generic.BankGeneric;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BankListResponse extends Response {

	public List<BankGeneric> banks;
	
	public BankListResponse() {
		super();
	}
	
	public BankListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.banks = null;
	}
	
	public BankListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.banks = null;
	}

	public BankListResponse(ResponseCode code, String mensaje, List<BankGeneric> banks_) {
		super(new Date(), code.getCodigo(), mensaje);
		this.banks = banks_;
	}
        
}