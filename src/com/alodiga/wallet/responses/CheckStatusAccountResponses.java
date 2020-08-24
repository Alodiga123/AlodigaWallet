package com.alodiga.wallet.responses;


import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ActivateCardResponXm")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckStatusAccountResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    public CheckStatusCredentialAccount checkStatusCredentialAccount;

    public CheckStatusAccountResponses() {
    }

    
    

    public CheckStatusAccountResponses(CheckStatusCredentialAccount checkStatusCredentialAccount, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.checkStatusCredentialAccount = checkStatusCredentialAccount;
    }
    
    public CheckStatusAccountResponses(ResponseCode codigo,
			String mensajeRespuesta) {
	   super(new Date(), codigo.getCodigo(), mensajeRespuesta);	
    }

    public CheckStatusCredentialAccount getCheckStatusCredentialAccount() {
        return checkStatusCredentialAccount;
    }

    public void setCheckStatusCredentialAccount(CheckStatusCredentialAccount checkStatusCredentialAccount) {
        this.checkStatusCredentialAccount = checkStatusCredentialAccount;
    }

   
   

        
}
