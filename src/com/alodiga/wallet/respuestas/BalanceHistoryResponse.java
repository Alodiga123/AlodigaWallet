package com.alodiga.wallet.respuestas;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.alodiga.wallet.common.model.BalanceHistory;


@XmlRootElement(name = "BalanceHistoryResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class BalanceHistoryResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	public BalanceHistory response;

	public BalanceHistoryResponse() {
		super();
	}

	public BalanceHistoryResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
		this.response = null;
	}

	public BalanceHistoryResponse(ResponseCode codigo,String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.response = null;
	}

	public BalanceHistoryResponse(ResponseCode codigo,String mensajeRespuesta, BalanceHistory balanceHistory) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.response = balanceHistory;
	}

}