package com.alodiga.wallet.respuestas;

import com.alodiga.plaid.response.ExchangeTokenResponse;
import com.alodiga.plaid.response.RetriveAuthResponse;
import com.alodiga.plaid.response.RetriveTransactionResponse;
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


@XmlRootElement(name = "RetriveTransactionPlaidResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class RetriveTransactionPlaidResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    
    public List<Product> products;
    public RetriveTransactionResponse retriveTransactionResponse;

 
    public RetriveTransactionPlaidResponses() {
    }

    public RetriveTransactionPlaidResponses(RetriveTransactionResponse retriveTransactionResponse) {
        super(ResponseCode.EXITO);
        this.retriveTransactionResponse = retriveTransactionResponse;
    }


    
    public RetriveTransactionPlaidResponses(RetriveTransactionResponse retriveTransactionResponse, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.retriveTransactionResponse = retriveTransactionResponse;
        
    }

    public RetriveTransactionPlaidResponses(ResponseCode codigo,
            String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
    }

    public RetriveTransactionResponse getRetriveTransactionResponse() {
        return retriveTransactionResponse;
    }

    public void setRetriveTransactionResponse(RetriveTransactionResponse retriveTransactionResponse) {
        this.retriveTransactionResponse = retriveTransactionResponse;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    

}
