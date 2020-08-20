package com.alodiga.wallet.respuestas;

import com.alodiga.wallet.common.model.Cumplimient;


import java.util.Date;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


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
