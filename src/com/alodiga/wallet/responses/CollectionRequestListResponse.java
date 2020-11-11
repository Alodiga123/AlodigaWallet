package com.alodiga.wallet.responses;


import com.alodiga.wallet.common.model.CollectionsRequest;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CollectionRequestListResponse extends Response {

	public List<CollectionsRequest> collectionsRequests;
	
	public CollectionRequestListResponse() {
		super();
	}
	
	public CollectionRequestListResponse(ResponseCode code) {
		super(new Date(), code.getCode(), code.name());
		this.collectionsRequests = null;
	}
	
	public CollectionRequestListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCode(), mensaje);
		this.collectionsRequests = null;
	}

	public CollectionRequestListResponse(ResponseCode code, String mensaje, List<CollectionsRequest> collectionsRequests) {
		super(new Date(), code.getCode(), mensaje);
		this.collectionsRequests = collectionsRequests;
	}
        
}
