package com.alodiga.wallet.responses;
import com.alodiga.wallet.common.model.Product;
import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "BusinessHasProductResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class BusinessHasProductResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	public Product response;

	public BusinessHasProductResponse() {
		super();
	}

	public BusinessHasProductResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
		this.response = null;
	}

	public BusinessHasProductResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.response = null;
	}

	public BusinessHasProductResponse(ResponseCode codigo,
			String mensajeRespuesta, Product productId) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.response = productId;
	}

}
