package com.alodiga.wallet.respuestas;

import com.alodiga.wallet.model.PaymentInfo;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentInfoListResponse extends Response {

	public List<PaymentInfo> paymentInfos;
	
	public PaymentInfoListResponse() {
		super();
	}
	
	public PaymentInfoListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.paymentInfos = null;
	}
	
	public PaymentInfoListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.paymentInfos = null;
	}

	public PaymentInfoListResponse(ResponseCode code, String mensaje, List<PaymentInfo> paymentInfos) {
		super(new Date(), code.getCodigo(), mensaje);
		this.paymentInfos = paymentInfos;
	}
        
}
