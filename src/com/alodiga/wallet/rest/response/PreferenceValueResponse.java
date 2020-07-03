package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.model.PreferenceValue;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PreferenceValueResponse extends Response implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "value")
    private String value;
	@XmlElement(name = "beginningDate")
	private Date beginningDate;
	@XmlElement(name = "endingDate")
	private Date endingDate;
	@XmlElement(name = "preferenceFieldResponse")
	private PreferenceFieldResponse preferenceFieldResponse;
	@XmlElement(name = "enterpriseResponse")
	private EnterpriseResponse enterpriseResponse;

	public PreferenceValueResponse() {
		super();
	}

	public PreferenceValueResponse(ResponseCode codigo) {
		super(new Date(), codigo.getCodigo(), codigo.name());
	}

	public PreferenceValueResponse(ResponseCode codigo,
			String mensajeRespuesta) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
	}

	public PreferenceValueResponse(ResponseCode codigo,
			String mensajeRespuesta, PreferenceValue preferenceValue) {
		super(new Date(), codigo.getCodigo(), mensajeRespuesta);
		this.id = preferenceValue.getId();
		this.value = preferenceValue.getValue();
		this.beginningDate = preferenceValue.getBeginningDate();
		this.beginningDate = preferenceValue.getEndingDate();
		this.preferenceFieldResponse = new PreferenceFieldResponse(preferenceValue.getPreferenceFieldId());
		this.enterpriseResponse = new EnterpriseResponse(preferenceValue.getEnterpriseId());
	}
	
	public PreferenceValueResponse(PreferenceValue preferenceValue) {
		this.id = preferenceValue.getId();
		this.value = preferenceValue.getValue();
		this.beginningDate = preferenceValue.getBeginningDate();
		this.beginningDate = preferenceValue.getEndingDate();
		this.preferenceFieldResponse = new PreferenceFieldResponse(preferenceValue.getPreferenceFieldId());
		this.enterpriseResponse = new EnterpriseResponse(preferenceValue.getEnterpriseId());
	}

}
