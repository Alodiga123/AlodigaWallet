package com.alodiga.wallet.responses;

import com.alodiga.plaid.response.TokenResponse;
import com.alodiga.wallet.common.model.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Transient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "PublicTokenPlaidResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class PublicTokenPlaidResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    
    public List<Product> products;
    public TokenResponse tokenResponse;

  
    public PublicTokenPlaidResponses() {
    }

    public PublicTokenPlaidResponses(TokenResponse tokenResponse) {
        super(ResponseCode.SUCCESS);
        this.tokenResponse = tokenResponse;
    }


    
    public PublicTokenPlaidResponses(TokenResponse tokenResponse, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
        this.tokenResponse = tokenResponse;
        
    }

    public PublicTokenPlaidResponses(ResponseCode codigo,
            String mensajeRespuesta) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
    }

    public TokenResponse getTokenResponse() {
        return tokenResponse;
    }

    public void setTokenResponse(TokenResponse tokenResponse) {
        this.tokenResponse = tokenResponse;
    }

   

   

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

   

}
