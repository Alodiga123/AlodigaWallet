package com.alodiga.wallet.respuestas;

import com.alodiga.wallet.common.model.Address;
import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ProductResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddressResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	public Address response;

	public AddressResponse() {
		super();
	}

	public AddressResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
		this.response = null;
	}

	public AddressResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.response = null;
	}

	public AddressResponse(ResponseCode codigo,
			String mensajeRespuesta, Address addressId) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.response = addressId;
	}

}
