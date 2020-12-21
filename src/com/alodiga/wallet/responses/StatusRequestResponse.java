package com.alodiga.wallet.responses;


import com.alodiga.wallet.common.model.StatusRequest;
import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ProductResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatusRequestResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	public StatusRequest response;

	public StatusRequestResponse() {
		super();
	}

	public StatusRequestResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCode(), codigo.name());
		this.response = null;
	}

	public StatusRequestResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCode(), mensajeRespuesta);
		this.response = null;
	}

	public StatusRequestResponse(ResponseCode codigo,
			String mensajeRespuesta, StatusRequest statusId) {
		super(new Date(), codigo.getCode(), mensajeRespuesta);
		this.response = statusId;
	}

}
