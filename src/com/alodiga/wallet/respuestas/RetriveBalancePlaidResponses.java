package com.alodiga.wallet.respuestas;

import com.alodiga.plaid.response.ExchangeTokenResponse;
import com.alodiga.plaid.response.RetriveAuthResponse;
import com.alodiga.plaid.response.RetriveBalanceResponse;
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


@XmlRootElement(name = "RetriveBalancePlaidResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class RetriveBalancePlaidResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    
    public List<Product> products;
    public RetriveBalanceResponse retriveBalanceResponse;

 
    public RetriveBalancePlaidResponses() {
    }

    public RetriveBalancePlaidResponses(RetriveBalanceResponse retriveBalanceResponse) {
        super(ResponseCode.EXITO);
        this.retriveBalanceResponse = retriveBalanceResponse;
    }


    
    public RetriveBalancePlaidResponses(RetriveBalanceResponse retriveBalanceResponse, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.retriveBalanceResponse = retriveBalanceResponse;
        
    }

    public RetriveBalancePlaidResponses(ResponseCode codigo,
            String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
    }

    public RetriveBalanceResponse getRetriveBalanceResponse() {
        return retriveBalanceResponse;
    }

    public void setRetriveBalanceResponse(RetriveBalanceResponse retriveBalanceResponse) {
        this.retriveBalanceResponse = retriveBalanceResponse;
    }

    

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    

}
