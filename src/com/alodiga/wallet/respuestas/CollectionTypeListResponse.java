package com.alodiga.wallet.respuestas;


import com.alodiga.wallet.common.model.CollectionType;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CollectionTypeListResponse extends Response {

	public List<CollectionType> collectionTypes;
	
	public CollectionTypeListResponse() {
		super();
	}
	
	public CollectionTypeListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.collectionTypes = null;
	}
	
	public CollectionTypeListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.collectionTypes = null;
	}

	public CollectionTypeListResponse(ResponseCode code, String mensaje, List<CollectionType> collectionTypes) {
		super(new Date(), code.getCodigo(), mensaje);
		this.collectionTypes = collectionTypes;
	}
        
}
