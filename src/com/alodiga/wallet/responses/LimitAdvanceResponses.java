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
public class LimitAdvanceResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    public LimitAdvanceCredential limitAdvanceCredential;
    public List<Product> products;
    
    public LimitAdvanceResponses() {
    }   
    
    public LimitAdvanceResponses(LimitAdvanceCredential limitAdvanceCredential, ResponseCode codigo, String mensajeRespuesta,ArrayList<Product> products) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
        this.limitAdvanceCredential = limitAdvanceCredential;
        this.products = products;
    }
    
    public LimitAdvanceResponses(LimitAdvanceCredential limitAdvanceCredential, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
        this.limitAdvanceCredential = limitAdvanceCredential;
        
    }
    public LimitAdvanceResponses(ResponseCode codigo,
			String mensajeRespuesta) {
	   super(new Date(), codigo.getCode(), mensajeRespuesta);	
    }

    public LimitAdvanceCredential getTransferCardToCardCredential() {
        return limitAdvanceCredential;
    }

    public void setTransferCardToCardCredential(LimitAdvanceCredential limitAdvanceCredential) {
        this.limitAdvanceCredential = limitAdvanceCredential;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    
        
}
