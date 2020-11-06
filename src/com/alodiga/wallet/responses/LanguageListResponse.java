package com.alodiga.wallet.responses;


import com.alodiga.wallet.common.model.Language;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LanguageListResponse extends Response {

	public List<Language> languages;
	
	public LanguageListResponse() {
		super();
	}
	
	public LanguageListResponse(ResponseCode code) {
		super(new Date(), code.getCodigo(), code.name());
		this.languages = null;
	}
	
	public LanguageListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.languages = null;
	}

	public LanguageListResponse(ResponseCode code, String mensaje, List<Language> languages) {
		super(new Date(), code.getCodigo(), mensaje);
		this.languages = languages;
	}
        
}
