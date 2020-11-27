package com.alodiga.wallet.responses;


import com.alodiga.wallet.common.model.AccountTypeBank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "AccountTypeBankResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountTypeBankListResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	public List<AccountTypeBank> accountTypeBank;

        
        
	public AccountTypeBankListResponse() {
		super();
	}

	public AccountTypeBankListResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCode(), codigo.name());
		this.accountTypeBank = null;
	}

	public AccountTypeBankListResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCode(), mensajeRespuesta);
		this.accountTypeBank = null;
	}

	public AccountTypeBankListResponse(ResponseCode codigo,
			String mensajeRespuesta, List<AccountTypeBank> accountTypeBank) {
		super(new Date(), codigo.getCode(), mensajeRespuesta);
		this.accountTypeBank = accountTypeBank;
	}

}

