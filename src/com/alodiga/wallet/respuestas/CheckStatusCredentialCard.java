package com.alodiga.wallet.respuestas;

import com.alodiga.wallet.model.Address;
import com.alodiga.wallet.model.Product;
import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ChangeStatusCredentialCardResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckStatusCredentialCard extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    
    private String code;
    private String description;
    private String ticket;
    private String biginingAtention;
    private String endingAtention;
    private String timerAtention;
    
    private String cardNumber;
    private String accountNumber;
    private String entityCode;
    private String entityDescription;
    private String branchOffice;
    private String productCode;
    private String productDescription;
    private String stateCode;
    private String stateDescription;
    private String currentValidity;
    private String previousValidity;
    private String denominationPerson;
    private String documentType;
    private String documentIden;
    private String phoneNumber;
    private String direction;
    private String postalCode;
    private String location;
    private String countryCode;
    private String countryDescription;
    private String momentLastUpdate;
    private String lastOperationMomentApproved;
    private String lastOperationMomentDenied;
    private String momentLastLowNewsletter;
    private String ERRPinCounter;

    public CheckStatusCredentialCard() {
    }

    public CheckStatusCredentialCard(String code, String description, String ticket, String biginingAtention, String endingAtention, String timerAtention, String cardNumber, String accountNumber, String entityCode, String entityDescription, String branchOffice, String productCode, String productDescription, String stateCode, String stateDescription, String currentValidity, String previousValidity, String denominationPerson, String documentType, String documentIden, String phoneNumber, String direction, String postalCode, String location, String countryCode, String countryDescription, String momentLastUpdate, String lastOperationMomentApproved, String lastOperationMomentDenied, String momentLastLowNewsletter, String ERRPinCounter) {
        this.code = code;
        this.description = description;
        this.ticket = ticket;
        this.biginingAtention = biginingAtention;
        this.endingAtention = endingAtention;
        this.timerAtention = timerAtention;
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
        this.entityCode = entityCode;
        this.entityDescription = entityDescription;
        this.branchOffice = branchOffice;
        this.productCode = productCode;
        this.productDescription = productDescription;
        this.stateCode = stateCode;
        this.stateDescription = stateDescription;
        this.currentValidity = currentValidity;
        this.previousValidity = previousValidity;
        this.denominationPerson = denominationPerson;
        this.documentType = documentType;
        this.documentIden = documentIden;
        this.phoneNumber = phoneNumber;
        this.direction = direction;
        this.postalCode = postalCode;
        this.location = location;
        this.countryCode = countryCode;
        this.countryDescription = countryDescription;
        this.momentLastUpdate = momentLastUpdate;
        this.lastOperationMomentApproved = lastOperationMomentApproved;
        this.lastOperationMomentDenied = lastOperationMomentDenied;
        this.momentLastLowNewsletter = momentLastLowNewsletter;
        this.ERRPinCounter = ERRPinCounter;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getBiginingAtention() {
        return biginingAtention;
    }

    public void setBiginingAtention(String biginingAtention) {
        this.biginingAtention = biginingAtention;
    }

    public String getEndingAtention() {
        return endingAtention;
    }

    public void setEndingAtention(String endingAtention) {
        this.endingAtention = endingAtention;
    }

    public String getTimerAtention() {
        return timerAtention;
    }

    public void setTimerAtention(String timerAtention) {
        this.timerAtention = timerAtention;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getEntityDescription() {
        return entityDescription;
    }

    public void setEntityDescription(String entityDescription) {
        this.entityDescription = entityDescription;
    }

    public String getBranchOffice() {
        return branchOffice;
    }

    public void setBranchOffice(String branchOffice) {
        this.branchOffice = branchOffice;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateDescription() {
        return stateDescription;
    }

    public void setStateDescription(String stateDescription) {
        this.stateDescription = stateDescription;
    }

    public String getCurrentValidity() {
        return currentValidity;
    }

    public void setCurrentValidity(String currentValidity) {
        this.currentValidity = currentValidity;
    }

    public String getPreviousValidity() {
        return previousValidity;
    }

    public void setPreviousValidity(String previousValidity) {
        this.previousValidity = previousValidity;
    }

    public String getDenominationPerson() {
        return denominationPerson;
    }

    public void setDenominationPerson(String denominationPerson) {
        this.denominationPerson = denominationPerson;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentIden() {
        return documentIden;
    }

    public void setDocumentIden(String documentIden) {
        this.documentIden = documentIden;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryDescription() {
        return countryDescription;
    }

    public void setCountryDescription(String countryDescription) {
        this.countryDescription = countryDescription;
    }

    public String getMomentLastUpdate() {
        return momentLastUpdate;
    }

    public void setMomentLastUpdate(String momentLastUpdate) {
        this.momentLastUpdate = momentLastUpdate;
    }

    public String getLastOperationMomentApproved() {
        return lastOperationMomentApproved;
    }

    public void setLastOperationMomentApproved(String lastOperationMomentApproved) {
        this.lastOperationMomentApproved = lastOperationMomentApproved;
    }

    public String getLastOperationMomentDenied() {
        return lastOperationMomentDenied;
    }

    public void setLastOperationMomentDenied(String lastOperationMomentDenied) {
        this.lastOperationMomentDenied = lastOperationMomentDenied;
    }

    public String getMomentLastLowNewsletter() {
        return momentLastLowNewsletter;
    }

    public void setMomentLastLowNewsletter(String momentLastLowNewsletter) {
        this.momentLastLowNewsletter = momentLastLowNewsletter;
    }

    public String getERRPinCounter() {
        return ERRPinCounter;
    }

    public void setERRPinCounter(String ERRPinCounter) {
        this.ERRPinCounter = ERRPinCounter;
    }


}
