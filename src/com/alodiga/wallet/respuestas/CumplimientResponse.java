package com.alodiga.wallet.respuestas;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.alodiga.wallet.common.model.Cumplimient;
import com.alodiga.wallet.common.model.Product;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CumplimientResponse extends Response {

	public Cumplimient cumplimients;
       
	
	public CumplimientResponse() {
		super();
	}
	
	public CumplimientResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.cumplimients = null;
	}
	
	public CumplimientResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.cumplimients = null;
	}

	public CumplimientResponse(ResponseCode code, String mensaje, Cumplimient cumplimients) {
		super(new Date(), code.getCodigo(), mensaje);
		this.cumplimients = cumplimients;
	}
        
}
