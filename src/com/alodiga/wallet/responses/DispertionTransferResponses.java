package com.alodiga.wallet.responses;


import com.alodiga.autorization.credential.response.DispertionResponse;
import com.alodiga.wallet.common.model.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ActivateCardResponXm")
@XmlAccessorType(XmlAccessType.FIELD)
public class DispertionTransferResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    public DispertionTransferCredential dispertionTransferCredential;
    public List<Product> products;
    
    public DispertionTransferResponses() {
    }   
    
    public DispertionTransferResponses(DispertionTransferCredential dispertionTransferCredential, ResponseCode codigo, String mensajeRespuesta,ArrayList<Product> products) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
        this.dispertionTransferCredential = dispertionTransferCredential;
        this.products = products;
    }
    
    public DispertionTransferResponses(DispertionTransferCredential dispertionTransferCredential, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
        this.dispertionTransferCredential = dispertionTransferCredential;
        
    }
    public DispertionTransferResponses(ResponseCode codigo,
			String mensajeRespuesta) {
	   super(new Date(), codigo.getCode(), mensajeRespuesta);	
    }

    public DispertionTransferCredential getTransferCardToCardCredential() {
        return dispertionTransferCredential;
    }

    public void setTransferCardToCardCredential(DispertionTransferCredential dispertionTransferCredential) {
        this.dispertionTransferCredential = dispertionTransferCredential;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    
        
}
