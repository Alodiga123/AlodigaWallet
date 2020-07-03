package com.alodiga.wallet.rest.response;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ValidateUserResponse extends Response{

	@XmlElement(name = "valid")
	private boolean valid;

	public ValidateUserResponse() {
		super();
	}

	public ValidateUserResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public ValidateUserResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}
	
	public ValidateUserResponse(ResponseCode codigo,
			String mensajeRespuesta, boolean valid) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.valid = valid;
	}
	
	
}
