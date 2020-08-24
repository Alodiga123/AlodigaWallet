package com.alodiga.wallet.responses;

import com.alodiga.wallet.common.model.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Transient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ActivateCardResponXm")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivateCardResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    public ChangeStatusCredentialCard credentialResponse;
    public List<Product> products;

    @Transient
    private String numberCard;

    public ActivateCardResponses() {
    }

    public ActivateCardResponses(ChangeStatusCredentialCard credentialResponse) {
        super(ResponseCode.EXITO);
        this.credentialResponse = credentialResponse;
    }

    public ActivateCardResponses(ChangeStatusCredentialCard credentialResponse, ResponseCode codigo, String mensajeRespuesta, ArrayList<Product> products) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.credentialResponse = credentialResponse;
        this.products = products;
    }

    public ActivateCardResponses(ResponseCode codigo,
            String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
    }

    public ChangeStatusCredentialCard getCredentialResponse() {
        return credentialResponse;
    }

    public void setCredentialResponse(ChangeStatusCredentialCard credentialResponse) {
        this.credentialResponse = credentialResponse;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getNumberCard() {
        return numberCard;
    }

    public void setNumberCard(String numberCard) {
        this.numberCard = numberCard;
    }

}
