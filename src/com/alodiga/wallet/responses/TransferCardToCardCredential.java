package com.alodiga.wallet.responses;


import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ChangeStatusCredentialCardResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferCardToCardCredential extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    
    private String codeError;
    private String messageError;    
    private String codeAnswer;    
    private String messageResponse;
    private String codeAuthorization;
    private String postBalance;
    private String balance;   
    private String rearBalanceAccountDestination;
    private String destinationAccountBalance;
       
    
    public TransferCardToCardCredential() {
    }

    public TransferCardToCardCredential(String codeError, String messageError, String codeAnswer, String messageResponse, String codeAuthorization, String postBalance, String balance, String rearBalanceAccountDestination, String destinationAccountBalance) {
        this.codeError = codeError;
        this.messageError = messageError;
        this.codeAnswer = codeAnswer;
        this.messageResponse = messageResponse;
        this.codeAuthorization = codeAuthorization;
        this.postBalance = postBalance;
        this.balance = balance;
        this.rearBalanceAccountDestination = rearBalanceAccountDestination;
        this.destinationAccountBalance = destinationAccountBalance;
    }

    public TransferCardToCardCredential(String codeError, String messageError, String codeAnswer, String messageResponse, String codeAuthorization) {
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

    public String getPostBalance() {
        return postBalance;
    }

    public void setPostBalance(String postBalance) {
        this.postBalance = postBalance;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRearBalanceAccountDestination() {
        return rearBalanceAccountDestination;
    }

    public void setRearBalanceAccountDestination(String rearBalanceAccountDestination) {
        this.rearBalanceAccountDestination = rearBalanceAccountDestination;
    }

    public String getDestinationAccountBalance() {
        return destinationAccountBalance;
    }

    public void setDestinationAccountBalance(String destinationAccountBalance) {
        this.destinationAccountBalance = destinationAccountBalance;
    }

    
}
