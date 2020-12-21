package com.alodiga.wallet.responses;


import com.alodiga.autorization.credential.response.DispertionResponse;
import com.alodiga.wallet.common.model.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ActivateCardResponXm")
@XmlAccessorType(XmlAccessType.FIELD)
public class BalanceInquiryWithoutMovementsResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    public BalanceInquiryWithoutMovementsCredential balanceInquiryWithoutMovementsCredential;
    
    
    public BalanceInquiryWithoutMovementsResponses() {
    }

 
    
    public BalanceInquiryWithoutMovementsResponses(BalanceInquiryWithoutMovementsCredential balanceInquiryWithoutMovementsCredential, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
        this.balanceInquiryWithoutMovementsCredential = balanceInquiryWithoutMovementsCredential;
        
    }
    public BalanceInquiryWithoutMovementsResponses(ResponseCode codigo,
			String mensajeRespuesta) {
	   super(new Date(), codigo.getCode(), mensajeRespuesta);	
    }

    public BalanceInquiryWithoutMovementsCredential getBalanceInquiryWithoutMovementsCredential() {
        return balanceInquiryWithoutMovementsCredential;
    }

    public void setBalanceInquiryWithoutMovementsCredential(BalanceInquiryWithoutMovementsCredential balanceInquiryWithoutMovementsCredential) {
        this.balanceInquiryWithoutMovementsCredential = balanceInquiryWithoutMovementsCredential;
    }

    @Override
    public String toString() {
        return "BalanceInquiryWithoutMovementsResponses{" + "balanceInquiryWithoutMovementsCredential=" + balanceInquiryWithoutMovementsCredential + '}';
    }

   

   

    
        
}
