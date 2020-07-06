package com.alodiga.wallet.respuestas;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.alodiga.wallet.common.model.Address;
import com.alodiga.wallet.common.model.Product;


@XmlRootElement(name = "ChangeStatusCredentialCardResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckStatusCredentialAccount extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    
    private String code;
    private String description;
    
    private String accountNumber;
    private String stateCode;
    private String stateDescription;
    private String entityCode;
    private String entityDescription;   
    private String branchOffice;
    private String productCode;
    private String productDescription;
    private String countryCode;
    private String countryDescription;
    private String currencyCode;
    private String currencyDescription;
    private String indicatorsVIP;
    private String indicatorsHCC;
    private String indicatorsULC;
    private String indicatorsMCC;
    private String renewalMoment;
    private String momentLastUpdate;
    private String lastOperationMomentApproved;
    private String lastOperationMomentDenied;
    private String lastBlockMoment;
    private String lastUnlockMoment;
    private String availablePurchases;        
    private String availableFees;
    private String advancementsAvailable;
    private String loansAvailable;
    private String shoppingLimits; 
    private String limitedFees;
    private String advancementsLimit;
    private String loansLimit;
    private String expirationDate;
    private String balance;
    private String minimumPayment;
    private String dollarBalance;

    public CheckStatusCredentialAccount() {
    }

    public CheckStatusCredentialAccount(String code, String description, String accountNumber, String stateCode, String stateDescription, String entityCode, String entityDescription, String branchOffice, String productCode, String productDescription, String countryCode, String countryDescription, String currencyCode, String currencyDescription, String indicatorsVIP, String indicatorsHCC, String indicatorsULC, String indicatorsMCC, String renewalMoment, String momentLastUpdate, String lastOperationMomentApproved, String lastOperationMomentDenied, String lastBlockMoment, String lastUnlockMoment, String availablePurchases, String availableFees, String advancementsAvailable, String loansAvailable, String shoppingLimits, String limitedFees, String advancementsLimit, String loansLimit, String expirationDate, String balance, String minimumPayment, String dollarBalance) {
        this.code = code;
        this.description = description;
        this.accountNumber = accountNumber;
        this.stateCode = stateCode;
        this.stateDescription = stateDescription;
        this.entityCode = entityCode;
        this.entityDescription = entityDescription;
        this.branchOffice = branchOffice;
        this.productCode = productCode;
        this.productDescription = productDescription;
        this.countryCode = countryCode;
        this.countryDescription = countryDescription;
        this.currencyCode = currencyCode;
        this.currencyDescription = currencyDescription;
        this.indicatorsVIP = indicatorsVIP;
        this.indicatorsHCC = indicatorsHCC;
        this.indicatorsULC = indicatorsULC;
        this.indicatorsMCC = indicatorsMCC;
        this.renewalMoment = renewalMoment;
        this.momentLastUpdate = momentLastUpdate;
        this.lastOperationMomentApproved = lastOperationMomentApproved;
        this.lastOperationMomentDenied = lastOperationMomentDenied;
        this.lastBlockMoment = lastBlockMoment;
        this.lastUnlockMoment = lastUnlockMoment;
        this.availablePurchases = availablePurchases;
        this.availableFees = availableFees;
        this.advancementsAvailable = advancementsAvailable;
        this.loansAvailable = loansAvailable;
        this.shoppingLimits = shoppingLimits;
        this.limitedFees = limitedFees;
        this.advancementsLimit = advancementsLimit;
        this.loansLimit = loansLimit;
        this.expirationDate = expirationDate;
        this.balance = balance;
        this.minimumPayment = minimumPayment;
        this.dollarBalance = dollarBalance;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyDescription() {
        return currencyDescription;
    }

    public void setCurrencyDescription(String currencyDescription) {
        this.currencyDescription = currencyDescription;
    }

    public String getIndicatorsVIP() {
        return indicatorsVIP;
    }

    public void setIndicatorsVIP(String indicatorsVIP) {
        this.indicatorsVIP = indicatorsVIP;
    }

    public String getIndicatorsHCC() {
        return indicatorsHCC;
    }

    public void setIndicatorsHCC(String indicatorsHCC) {
        this.indicatorsHCC = indicatorsHCC;
    }

    public String getIndicatorsULC() {
        return indicatorsULC;
    }

    public void setIndicatorsULC(String indicatorsULC) {
        this.indicatorsULC = indicatorsULC;
    }

    public String getIndicatorsMCC() {
        return indicatorsMCC;
    }

    public void setIndicatorsMCC(String indicatorsMCC) {
        this.indicatorsMCC = indicatorsMCC;
    }

    public String getRenewalMoment() {
        return renewalMoment;
    }

    public void setRenewalMoment(String renewalMoment) {
        this.renewalMoment = renewalMoment;
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

    public String getLastBlockMoment() {
        return lastBlockMoment;
    }

    public void setLastBlockMoment(String lastBlockMoment) {
        this.lastBlockMoment = lastBlockMoment;
    }

    public String getLastUnlockMoment() {
        return lastUnlockMoment;
    }

    public void setLastUnlockMoment(String lastUnlockMoment) {
        this.lastUnlockMoment = lastUnlockMoment;
    }

    public String getAvailablePurchases() {
        return availablePurchases;
    }

    public void setAvailablePurchases(String availablePurchases) {
        this.availablePurchases = availablePurchases;
    }

    public String getAvailableFees() {
        return availableFees;
    }

    public void setAvailableFees(String availableFees) {
        this.availableFees = availableFees;
    }

    public String getAdvancementsAvailable() {
        return advancementsAvailable;
    }

    public void setAdvancementsAvailable(String advancementsAvailable) {
        this.advancementsAvailable = advancementsAvailable;
    }

    public String getLoansAvailable() {
        return loansAvailable;
    }

    public void setLoansAvailable(String loansAvailable) {
        this.loansAvailable = loansAvailable;
    }

    public String getShoppingLimits() {
        return shoppingLimits;
    }

    public void setShoppingLimits(String shoppingLimits) {
        this.shoppingLimits = shoppingLimits;
    }

    public String getLimitedFees() {
        return limitedFees;
    }

    public void setLimitedFees(String limitedFees) {
        this.limitedFees = limitedFees;
    }

    public String getAdvancementsLimit() {
        return advancementsLimit;
    }

    public void setAdvancementsLimit(String advancementsLimit) {
        this.advancementsLimit = advancementsLimit;
    }

    public String getLoansLimit() {
        return loansLimit;
    }

    public void setLoansLimit(String loansLimit) {
        this.loansLimit = loansLimit;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getMinimumPayment() {
        return minimumPayment;
    }

    public void setMinimumPayment(String minimumPayment) {
        this.minimumPayment = minimumPayment;
    }

    public String getDollarBalance() {
        return dollarBalance;
    }

    public void setDollarBalance(String dollarBalance) {
        this.dollarBalance = dollarBalance;
    }

    
}
