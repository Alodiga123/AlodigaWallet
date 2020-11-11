package com.alodiga.wallet.responses;


import com.alodiga.plaid.response.RetriveBalanceResponse;
import com.alodiga.wallet.common.model.Product;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
        super(ResponseCode.SUCCESS);
        this.retriveBalanceResponse = retriveBalanceResponse;
    }


    
    public RetriveBalancePlaidResponses(RetriveBalanceResponse retriveBalanceResponse, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
        this.retriveBalanceResponse = retriveBalanceResponse;
        
    }

    public RetriveBalancePlaidResponses(ResponseCode codigo,
            String mensajeRespuesta) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
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
