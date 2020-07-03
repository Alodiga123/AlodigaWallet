package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import com.alodiga.wallet.model.Language;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class LanguageResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "language")
	public Language language;
	
	
	public LanguageResponse() {
		super();
	}

	public LanguageResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public LanguageResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public LanguageResponse(ResponseCode codigo,
			String mensajeRespuesta, Language language) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.language = language;
	}

	public LanguageResponse(Language language) {
		this.language = language;
	}
	
	
}
