package com.alodiga.wallet.responses;


import com.alodiga.wallet.common.model.DocumentsPersonType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "AccountTypeBankResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentPersonTypeListResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	public List<DocumentsPersonType> documentsPersonTypes;

        
        
	public DocumentPersonTypeListResponse() {
		super();
	}

	public DocumentPersonTypeListResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCode(), codigo.name());
		this.documentsPersonTypes = null;
	}

	public DocumentPersonTypeListResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCode(), mensajeRespuesta);
		this.documentsPersonTypes = null;
	}

	public DocumentPersonTypeListResponse(ResponseCode codigo,
			String mensajeRespuesta, List<DocumentsPersonType> documentsPersonTypes) {
		super(new Date(), codigo.getCode(), mensajeRespuesta);
		this.documentsPersonTypes = documentsPersonTypes;
	}

}

