package com.alodiga.wallet.responses;


import com.alodiga.wallet.common.model.ValidationCollection;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CollectionListResponse extends Response {

	public List<ValidationCollection> validationCollections;
	
	public CollectionListResponse() {
		super();
	}
	
	public CollectionListResponse(ResponseCode code) {
		super(new Date(), code.getCode(), code.name());
		this.validationCollections = null;
	}
	
	public CollectionListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCode(), mensaje);
		this.validationCollections = null;
	}

	public CollectionListResponse(ResponseCode code, String mensaje, List<ValidationCollection> validationCollections) {
		super(new Date(), code.getCode(), mensaje);
		this.validationCollections = validationCollections;
	}
        
}
