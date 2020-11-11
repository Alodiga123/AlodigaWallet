package com.alodiga.wallet.responses;

import com.alodiga.wallet.common.model.Transaction;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ProductResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionListResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	public Transaction response;

        public List<Transaction> transactions;
        
	public TransactionListResponse() {
		super();
	}

	public TransactionListResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCode(), codigo.name());
		this.response = null;
	}

	public TransactionListResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCode(), mensajeRespuesta);
		this.response = null;
	}

	public TransactionListResponse(ResponseCode codigo,
			String mensajeRespuesta, List<Transaction> transactions) {
		super(new Date(), codigo.getCode(), mensajeRespuesta);
		this.transactions = transactions;
	}

}

