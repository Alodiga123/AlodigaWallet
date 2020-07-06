package com.alodiga.wallet.respuestas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.alodiga.wallet.common.model.Card;
import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.common.model.Product;


@XmlRootElement(name = "ActivateCardResponXm")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferCardToCardResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    public TransferCardToCardCredential transferCardToCardCredential;
    public List<Product> products;
    
    public TransferCardToCardResponses() {
    }

    
    

    public TransferCardToCardResponses(TransferCardToCardCredential transferCardToCardCredential, ResponseCode codigo, String mensajeRespuesta,ArrayList<Product> products) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.transferCardToCardCredential = transferCardToCardCredential;
        this.products = products;
    }
    
    public TransferCardToCardResponses(TransferCardToCardCredential transferCardToCardCredential, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.transferCardToCardCredential = transferCardToCardCredential;
        
    }
    public TransferCardToCardResponses(ResponseCode codigo,
			String mensajeRespuesta) {
	   super(new Date(), codigo.getCodigo(), mensajeRespuesta);	
    }

    public TransferCardToCardCredential getTransferCardToCardCredential() {
        return transferCardToCardCredential;
    }

    public void setTransferCardToCardCredential(TransferCardToCardCredential transferCardToCardCredential) {
        this.transferCardToCardCredential = transferCardToCardCredential;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    
        
}
