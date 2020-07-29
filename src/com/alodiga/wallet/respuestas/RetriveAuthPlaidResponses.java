package com.alodiga.wallet.respuestas;

import com.alodiga.plaid.response.ExchangeTokenResponse;
import com.alodiga.plaid.response.RetriveAuthResponse;
import com.alodiga.plaid.response.TokenResponse;
import com.alodiga.wallet.model.Card;
import com.alodiga.wallet.model.Country;
import com.alodiga.wallet.model.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Transient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "RetriveAuthPlaidResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class RetriveAuthPlaidResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    
    public List<Product> products;
    public RetriveAuthResponse retriveAuthResponse;

 
    public RetriveAuthPlaidResponses() {
    }

    public RetriveAuthPlaidResponses(RetriveAuthResponse retriveAuthResponse) {
        super(ResponseCode.EXITO);
        this.retriveAuthResponse = retriveAuthResponse;
    }


    
    public RetriveAuthPlaidResponses(RetriveAuthResponse retriveAuthResponse, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.retriveAuthResponse = retriveAuthResponse;
        
    }

    public RetriveAuthPlaidResponses(ResponseCode codigo,
            String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
    }

    public RetriveAuthResponse getRetriveAuthResponse() {
        return retriveAuthResponse;
    }

    public void setRetriveAuthResponse(RetriveAuthResponse retriveAuthResponse) {
        this.retriveAuthResponse = retriveAuthResponse;
    }

   

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    

}
