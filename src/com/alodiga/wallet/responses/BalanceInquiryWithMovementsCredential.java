package com.alodiga.wallet.responses;



import com.alodiga.autorization.credential.response.Movimiento;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class BalanceInquiryWithMovementsCredential extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    
    private String codeError;
    private String messageError;    
    private String codeAnswer;    
    private String messageResponse;
    private String codeAuthorization;
    private String availableConsumption;
    private String availableQuotas;    
    private String availableAdvances;    
    private String availableLoans;
    private String balance;
    private String balanceInDollars;
    private String minimumPayment;
    private String expirationDateLastSettlement;
    private List<Movimiento> movements;
    
    
    
    public BalanceInquiryWithMovementsCredential() {
    }

    public BalanceInquiryWithMovementsCredential(String codeError, String messageError, String codeAnswer, String messageResponse, String codeAuthorization, String availableConsumption, String availableQuotas, String availableAdvances, String availableLoans, String balance, String balanceInDollars, String minimumPayment, String expirationDateLastSettlement, List<Movimiento> movements) {
        this.codeError = codeError;
        this.messageError = messageError;
        this.codeAnswer = codeAnswer;
        this.messageResponse = messageResponse;
        this.codeAuthorization = codeAuthorization;
        this.availableConsumption = availableConsumption;
        this.availableQuotas = availableQuotas;
        this.availableAdvances = availableAdvances;
        this.availableLoans = availableLoans;
        this.balance = balance;
        this.balanceInDollars = balanceInDollars;
        this.minimumPayment = minimumPayment;
        this.expirationDateLastSettlement = expirationDateLastSettlement;
        this.movements = movements;
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

    public String getAvailableConsumption() {
        return availableConsumption;
    }

    public void setAvailableConsumption(String availableConsumption) {
        this.availableConsumption = availableConsumption;
    }

    public String getAvailableQuotas() {
        return availableQuotas;
    }

    public void setAvailableQuotas(String availableQuotas) {
        this.availableQuotas = availableQuotas;
    }

    public String getAvailableAdvances() {
        return availableAdvances;
    }

    public void setAvailableAdvances(String availableAdvances) {
        this.availableAdvances = availableAdvances;
    }

    public String getAvailableLoans() {
        return availableLoans;
    }

    public void setAvailableLoans(String availableLoans) {
        this.availableLoans = availableLoans;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceInDollars() {
        return balanceInDollars;
    }

    public void setBalanceInDollars(String balanceInDollars) {
        this.balanceInDollars = balanceInDollars;
    }

    public String getMinimumPayment() {
        return minimumPayment;
    }

    public void setMinimumPayment(String minimumPayment) {
        this.minimumPayment = minimumPayment;
    }

    public String getExpirationDateLastSettlement() {
        return expirationDateLastSettlement;
    }

    public void setExpirationDateLastSettlement(String expirationDateLastSettlement) {
        this.expirationDateLastSettlement = expirationDateLastSettlement;
    }

    public List<Movimiento> getMovements() {
        return movements;
    }

    public void setMovements(List<Movimiento> movements) {
        this.movements = movements;
    }

    @Override
    public String toString() {
        return "BalanceInquiryWithMovementsCredential{" + "codeError=" + codeError + ", messageError=" + messageError + ", codeAnswer=" + codeAnswer + ", messageResponse=" + messageResponse + ", codeAuthorization=" + codeAuthorization + ", availableConsumption=" + availableConsumption + ", availableQuotas=" + availableQuotas + ", availableAdvances=" + availableAdvances + ", availableLoans=" + availableLoans + ", balance=" + balance + ", balanceInDollars=" + balanceInDollars + ", minimumPayment=" + minimumPayment + ", expirationDateLastSettlement=" + expirationDateLastSettlement + ", movements=" + movements + '}';
    }  
   

    
}
