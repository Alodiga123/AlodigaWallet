package com.alodiga.wallet.respuestas;

import com.alodiga.wallet.model.PaymentInfo;
import com.alodiga.wallet.model.Product;
import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ProductResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentInfoResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	public PaymentInfo paymentInfo;

	public PaymentInfoResponse() {
		super();
	}

	public PaymentInfoResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
		this.paymentInfo = null;
	}

	public PaymentInfoResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.paymentInfo = null;
	}

	public PaymentInfoResponse(ResponseCode codigo,
			String mensajeRespuesta, PaymentInfo paymentInfo) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.paymentInfo = paymentInfo;
	}

}
