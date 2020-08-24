package com.alodiga.wallet.responses;


import com.alodiga.plaid.response.RetriveIdentityResponse;
import com.alodiga.wallet.common.model.Product;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "RetriveIdentityPlaidResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class RetriveIdentityPlaidResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    
    public List<Product> products;
    public RetriveIdentityResponse retriveIdentityResponse;

 
    public RetriveIdentityPlaidResponses() {
    }

    public RetriveIdentityPlaidResponses(RetriveIdentityResponse retriveIdentityResponse) {
        super(ResponseCode.EXITO);
        this.retriveIdentityResponse = retriveIdentityResponse;
    }


    
    public RetriveIdentityPlaidResponses(RetriveIdentityResponse retriveIdentityResponse, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.retriveIdentityResponse = retriveIdentityResponse;
        
    }

    public RetriveIdentityPlaidResponses(ResponseCode codigo,
            String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
    }

    public RetriveIdentityResponse getRetriveIdentityResponse() {
        return retriveIdentityResponse;
    }

    public void setRetriveIdentityResponse(RetriveIdentityResponse retriveIdentityResponse) {
        this.retriveIdentityResponse = retriveIdentityResponse;
    }

 
    

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    

}
