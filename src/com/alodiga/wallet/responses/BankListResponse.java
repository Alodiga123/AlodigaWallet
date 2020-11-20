package com.alodiga.wallet.responses;


import com.alodiga.wallet.common.model.Bank;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BankListResponse extends Response {

	public List<Bank> banks;
	
	public BankListResponse() {
		super();
	}
	
	public BankListResponse(ResponseCode code) {
		super(new Date(), code.getCode(), code.name());
		this.banks = null;
	}
	
	public BankListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCode(), mensaje);
		this.banks = null;
	}

	public BankListResponse(ResponseCode code, String mensaje, List<Bank> banks_) {
		super(new Date(), code.getCode(), mensaje);
		this.banks = banks_;
	}
        
}
