package com.alodiga.wallet.responses;


import com.alodiga.wallet.common.model.TransactionApproveRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "TransactionApproveRequestResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionApproveRequestResponse extends Response{

	public TransactionApproveRequest transactionApproveRequest;       
        
	public TransactionApproveRequestResponse() {
		super();
	}

	public TransactionApproveRequestResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCode(), codigo.name());
		this.transactionApproveRequest = null;
	}

	public TransactionApproveRequestResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCode(), mensajeRespuesta);
		this.transactionApproveRequest = null;
	}

	public TransactionApproveRequestResponse(ResponseCode codigo,
			String mensajeRespuesta, TransactionApproveRequest transactionApproveRequest) {
		super(new Date(), codigo.getCode(), mensajeRespuesta);
		this.transactionApproveRequest = transactionApproveRequest;
	}

}

