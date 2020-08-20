package com.alodiga.wallet.respuestas;

import com.alodiga.wallet.common.model.Product;
import com.alodiga.wallet.common.model.Transaction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ProductResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionResponse extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    public Transaction response;
    public List<Product> products;
    public Float amountCommission;
    public Float valueCommission;
    public Float totalDebit;
    public Float amountConversion;
    public Float exchangeRateProductSource;
    public Float exchangeRateProductDestination;
    public Short isPercentCommision;
    

    public TransactionResponse() {

        super();
    }

    public TransactionResponse(ResponseCode codigo) {
        super(new Date(), codigo.getCodigo(), codigo.name());
        this.response = null;
    }

    public TransactionResponse(ResponseCode codigo,
            String mensajeRespuesta) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.response = null;
    }

    public TransactionResponse(ResponseCode codigo,
            String mensajeRespuesta, Transaction transactionId) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.response = transactionId;
    }

    public TransactionResponse(ResponseCode codigo,
            String mensajeRespuesta, ArrayList<Product> products) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.products = products;
    }

    public TransactionResponse(ResponseCode codigo,
            String mensajeRespuesta, Float amountCommission,
            Float valueCommission, Float totalDebit, Float amountConversion,
            Float exchangeRateProductSource, Float exchangeRateProductDestination,
            Short isPercentCommision) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.amountCommission = amountCommission;
        this.valueCommission = valueCommission;
        this.totalDebit = totalDebit;
        this.amountConversion = amountConversion;
        this.exchangeRateProductDestination = exchangeRateProductDestination;
        this.exchangeRateProductSource = exchangeRateProductSource;
        this.isPercentCommision = isPercentCommision;

    }

    

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }



   
   

    
    
}
