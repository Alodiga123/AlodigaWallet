package com.alodiga.wallet.responses;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ActivateCardResponXm")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckStatusCardResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    public CheckStatusCredentialCard checkStatusCredentialCard;

    public CheckStatusCardResponses() {
    }

    
    

    public CheckStatusCardResponses(CheckStatusCredentialCard checkStatusCredentialCard, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.checkStatusCredentialCard = checkStatusCredentialCard;
    }
    
    public CheckStatusCardResponses(ResponseCode codigo,
			String mensajeRespuesta) {
	   super(new Date(), codigo.getCodigo(), mensajeRespuesta);	
    }

    public CheckStatusCredentialCard getCheckStatusCredentialCard() {
        return checkStatusCredentialCard;
    }

    public void setCheckStatusCredentialCard(CheckStatusCredentialCard checkStatusCredentialCard) {
        this.checkStatusCredentialCard = checkStatusCredentialCard;
    }

   

        
}
