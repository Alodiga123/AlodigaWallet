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
public class BalanceInquiryWithMovementsResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    public BalanceInquiryWithMovementsCredential balanceInquiryWithMovementsCredential;
    
    
    public BalanceInquiryWithMovementsResponses() {
    }

 
    
    public BalanceInquiryWithMovementsResponses(BalanceInquiryWithMovementsCredential balanceInquiryWithMovementsCredential, ResponseCode codigo, String mensajeRespuesta) {
        super(new Date(), codigo.getCode(), mensajeRespuesta);
        this.balanceInquiryWithMovementsCredential = balanceInquiryWithMovementsCredential;
        
    }
    public BalanceInquiryWithMovementsResponses(ResponseCode codigo,
			String mensajeRespuesta) {
	   super(new Date(), codigo.getCode(), mensajeRespuesta);	
    }

    public BalanceInquiryWithMovementsCredential getBalanceInquiryWithMovementsCredential() {
        return balanceInquiryWithMovementsCredential;
    }

    public void setBalanceInquiryWithMovementsCredential(BalanceInquiryWithMovementsCredential balanceInquiryWithMovementsCredential) {
        this.balanceInquiryWithMovementsCredential = balanceInquiryWithMovementsCredential;
    }

    @Override
    public String toString() {
        return "BalanceInquiryWithMovementsResponses{" + "balanceInquiryWithMovementsCredential=" + balanceInquiryWithMovementsCredential + '}';
    }

   

    
        
}
