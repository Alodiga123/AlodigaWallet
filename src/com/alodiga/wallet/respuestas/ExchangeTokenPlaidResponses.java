package com.alodiga.wallet.respuestas;

import com.alodiga.plaid.response.ExchangeTokenResponse;

import com.alodiga.wallet.common.model.Product;
import java.io.Serializable;

import java.util.Date;
import java.util.List;
import javax.persistence.Transient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ExchangeTokenPlaidResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExchangeTokenPlaidResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    
    public List<Product> products;
    public ExchangeTokenResponse exchangeTokenResponse;

   @Transient
    private String tokenResponse;
  
    public ExchangeTokenPlaidResponses() {
    }

    public ExchangeTokenPlaidResponses(ExchangeTokenResponse exchangeTokenResponse) {
        super(ResponseCode.EXITO);
        this.exchangeTokenResponse = exchangeTokenResponse;
    }


    
    public ExchangeTokenPlaidResponses(ExchangeTokenResponse exchangeTokenResponse, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.exchangeTokenResponse = exchangeTokenResponse;
        
    }

    public ExchangeTokenPlaidResponses(ResponseCode codigo,
            String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
    }

    public ExchangeTokenResponse getExchangeTokenResponse() {
        return exchangeTokenResponse;
    }

    public void setExchangeTokenResponse(ExchangeTokenResponse exchangeTokenResponse) {
        this.exchangeTokenResponse = exchangeTokenResponse;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getTokenResponse() {
        return tokenResponse;
    }

    public void setTokenResponse(String tokenResponse) {
        this.tokenResponse = tokenResponse;
    }

   

}
