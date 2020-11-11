package com.alodiga.wallet.responses;


import com.alodiga.plaid.response.RetriveAuthResponse;
import com.alodiga.wallet.common.model.Product;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


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
        super(ResponseCode.SUCCESS);
        this.retriveAuthResponse = retriveAuthResponse;
    }


    
    public RetriveAuthPlaidResponses(RetriveAuthResponse retriveAuthResponse, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
        this.retriveAuthResponse = retriveAuthResponse;
        
    }

    public RetriveAuthPlaidResponses(ResponseCode codigo,
            String mensajeRespuesta) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
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
