package com.alodiga.wallet.responses;


import com.alodiga.plaid.response.RetriveTransactionResponse;
import com.alodiga.wallet.common.model.Product;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
        super(ResponseCode.SUCCESS);
        this.retriveTransactionResponse = retriveTransactionResponse;
    }


    
    public RetriveTransactionPlaidResponses(RetriveTransactionResponse retriveTransactionResponse, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
        this.retriveTransactionResponse = retriveTransactionResponse;
        
    }

    public RetriveTransactionPlaidResponses(ResponseCode codigo,
            String mensajeRespuesta) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
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
