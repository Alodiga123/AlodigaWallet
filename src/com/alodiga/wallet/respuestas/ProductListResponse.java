package com.alodiga.wallet.respuestas;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.alodiga.wallet.common.model.Product;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductListResponse extends Response {

	public List<Product> products;
	
	public ProductListResponse() {
		super();
	}
	
	public ProductListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.products = null;
	}
	
	public ProductListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.products = null;
	}

	public ProductListResponse(ResponseCode code, String mensaje, List<Product> products) {
		super(new Date(), code.getCodigo(), mensaje);
		this.products = products;
	}
        
}
