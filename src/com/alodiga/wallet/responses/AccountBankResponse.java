package com.alodiga.wallet.responses;


import com.alodiga.wallet.common.model.AccountBank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "AccountBankResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountBankResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	public AccountBank accountBank;

        
        
	public AccountBankResponse() {
		super();
	}

	public AccountBankResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCode(), codigo.name());
		this.accountBank = null;
	}

	public AccountBankResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCode(), mensajeRespuesta);
		this.accountBank = null;
	}

	public AccountBankResponse(ResponseCode codigo,
			String mensajeRespuesta, AccountBank accountBank) {
		super(new Date(), codigo.getCode(), mensajeRespuesta);
		this.accountBank = accountBank;
	}

}

