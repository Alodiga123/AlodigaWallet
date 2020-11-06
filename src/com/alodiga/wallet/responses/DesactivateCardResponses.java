package com.alodiga.wallet.responses;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ActivateCardResponXm")
@XmlAccessorType(XmlAccessType.FIELD)
public class DesactivateCardResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    public ChangeStatusCredentialCard credentialResponse;

    public DesactivateCardResponses() {
    }

    public DesactivateCardResponses(ChangeStatusCredentialCard credentialResponse) {
        super(ResponseCode.EXITO);
        this.credentialResponse = credentialResponse;
    }

    public DesactivateCardResponses(ChangeStatusCredentialCard credentialResponse, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.credentialResponse = credentialResponse;
    }

    public DesactivateCardResponses(ResponseCode codigo,
            String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
    }

    public ChangeStatusCredentialCard getCredentialResponse() {
        return credentialResponse;
    }

    public void setCredentialResponse(ChangeStatusCredentialCard credentialResponse) {
        this.credentialResponse = credentialResponse;
    }

}
