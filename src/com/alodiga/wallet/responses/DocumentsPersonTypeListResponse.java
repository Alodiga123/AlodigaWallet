package com.alodiga.wallet.responses;


import com.alodiga.wallet.common.model.DocumentsPersonType;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentsPersonTypeListResponse extends Response {

	public List<DocumentsPersonType> documentsPersonTypes;
	
	public DocumentsPersonTypeListResponse() {
		super();
	}
	
	public DocumentsPersonTypeListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.documentsPersonTypes = null;
	}
	
	public DocumentsPersonTypeListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.documentsPersonTypes = null;
	}

	public DocumentsPersonTypeListResponse(ResponseCode code, String mensaje, List<DocumentsPersonType> documentsPersonTypes) {
		super(new Date(), code.getCodigo(), mensaje);
		this.documentsPersonTypes = documentsPersonTypes;
	}
        
}
