package com.alodiga.wallet.responses;

import com.alodiga.wallet.common.model.PaymentInfo;

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
		super(new Date(), code.getCode(), code.name());
		this.paymentInfos = null;
	}
	
	public PaymentInfoListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCode(), mensaje);
		this.paymentInfos = null;
	}

	public PaymentInfoListResponse(ResponseCode code, String mensaje, List<PaymentInfo> paymentInfos) {
		super(new Date(), code.getCode(), mensaje);
		this.paymentInfos = paymentInfos;
	}
        
}
