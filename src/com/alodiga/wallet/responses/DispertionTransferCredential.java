package com.alodiga.wallet.responses;


import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DispertionTransferCredential extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    
    private String codeError;
    private String messageError;    
    private String codeAnswer;    
    private String messageResponse;
    private String codeAuthorization;
    
    
    public DispertionTransferCredential() {
    }

    public DispertionTransferCredential(String codeError, String messageError, String codeAnswer, String messageResponse, String codeAuthorization) {
        this.codeError = codeError;
        this.messageError = messageError;
        this.codeAnswer = codeAnswer;
        this.messageResponse = messageResponse;
        this.codeAuthorization = codeAuthorization;
   
    }

    public String getCodeError() {
        return codeError;
    }

    public void setCodeError(String codeError) {
        this.codeError = codeError;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getCodeAnswer() {
        return codeAnswer;
    }

    public void setCodeAnswer(String codeAnswer) {
        this.codeAnswer = codeAnswer;
    }

    public String getMessageResponse() {
        return messageResponse;
    }

    public void setMessageResponse(String messageResponse) {
        this.messageResponse = messageResponse;
    }

    public String getCodeAuthorization() {
        return codeAuthorization;
    }

    public void setCodeAuthorization(String codeAuthorization) {
        this.codeAuthorization = codeAuthorization;
    }

    @Override
    public String toString() {
        return "DispertionTransferCredential{" + "codeError=" + codeError + ", messageError=" + messageError + ", codeAnswer=" + codeAnswer + ", messageResponse=" + messageResponse + ", codeAuthorization=" + codeAuthorization + '}';
    }

   

    
}
