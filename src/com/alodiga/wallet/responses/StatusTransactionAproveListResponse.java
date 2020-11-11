package com.alodiga.wallet.responses;

import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.common.model.StatusTransactionApproveRequest;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StatusTransactionAproveListResponse extends Response {

	public List<StatusTransactionApproveRequest> statusTransactionApproveRequests;
	
	public StatusTransactionAproveListResponse() {
		super();
	}
	
	public StatusTransactionAproveListResponse(ResponseCode code) {
		super(new Date(), code.getCode(), code.name());
		this.statusTransactionApproveRequests = null;
	}
	
	public StatusTransactionAproveListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCode(), mensaje);
		this.statusTransactionApproveRequests = null;
	}

	public StatusTransactionAproveListResponse(ResponseCode code, String mensaje, List<StatusTransactionApproveRequest> statusTransactionApproveRequests) {
		super(new Date(), code.getCode(), mensaje);
		this.statusTransactionApproveRequests = statusTransactionApproveRequests;
	}
        
}
