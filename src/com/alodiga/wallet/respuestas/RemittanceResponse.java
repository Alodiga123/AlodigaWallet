/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.respuestas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Transient;

import com.alodiga.wallet.common.model.Product;

/**
 * @author mGraterol
 * @see Clase desarrollada para simplificar la respuesta del servicio de remesa
 */
public class RemittanceResponse extends Response {

    private String id;
    private String applicationDate;
    private String commentary;
    private String amountOrigin;
    private String totalAmount;
    private String sendingOptionSMS;
    private String amountDestiny;
    private String bank;
    private String paymentServiceId;
    private String secondaryKey;
    private String additionalChanges;
    private String creationDate;
    private String CreationHour;
    private String localSales;
    private String reserveField1;
    private String remittent;
    private String receiver;
    private String Correspondent;
    private String addressReciever;
    private String salesType;
    private String  addressRemittent;
    private String exchangeRate;
    private String ratePaymentNetwork;
    
    private String language;
    private String originCurrent;
    private String destinyCurrent;
    
    private String paymentMethod;
    private String serviceType;
    private String paymentNetwork;
    
    private String paymentNetworkPoint; 
    private String cashBox; 
    private String cashier;
    
    private String status;
    private String remittanceNumber;
    private String paymentKey;
    private String correlative;
    private String deliveryForm;
    
    @Transient
    private String amountTransferTotal;
    

    public RemittanceResponse(String id, String applicationDate, String commentary, String amountOrigin, String totalAmount, String sendingOptionSMS, String amountDestiny, String bank, String paymentServiceId, String secondaryKey, String additionalChanges, String creationDate, String CreationHour, String localSales, String reserveField1, String remittent, String receiver, String Correspondent, String addressReciever, String salesType, String addressRemittent, String exchangeRate, String ratePaymentNetwork, String language, String originCurrent, String destinyCurrent, String paymentMethod, String serviceType, String paymentNetwork, String paymentNetworkPoint, String cashBox, String cashier, String status, String remittanceNumber, String paymentKey, String correlative, String deliveryForm, ResponseCode code, String mensajeRespuesta) {
        super(new Date(), code.getCodigo(), mensajeRespuesta);
        this.id = id;
        this.applicationDate = applicationDate;
        this.commentary = commentary;
        this.amountOrigin = amountOrigin;
        this.totalAmount = totalAmount;
        this.sendingOptionSMS = sendingOptionSMS;
        this.amountDestiny = amountDestiny;
        this.bank = bank;
        this.paymentServiceId = paymentServiceId;
        this.secondaryKey = secondaryKey;
        this.additionalChanges = additionalChanges;
        this.creationDate = creationDate;
        this.CreationHour = CreationHour;
        this.localSales = localSales;
        this.reserveField1 = reserveField1;
        this.remittent = remittent;
        this.receiver = receiver;
        this.Correspondent = Correspondent;
        this.addressReciever = addressReciever;
        this.salesType = salesType;
        this.addressRemittent = addressRemittent;
        this.exchangeRate = exchangeRate;
        this.ratePaymentNetwork = ratePaymentNetwork;
        this.language = language;
        this.originCurrent = originCurrent;
        this.destinyCurrent = destinyCurrent;
        this.paymentMethod = paymentMethod;
        this.serviceType = serviceType;
        this.paymentNetwork = paymentNetwork;
        
        this.paymentNetworkPoint = paymentNetworkPoint;
        this.cashBox = cashBox;
        this.cashier = cashier;
        this.status = status;
        this.remittanceNumber = remittanceNumber;
        this.paymentKey = paymentKey;
        this.correlative = correlative;
        this.deliveryForm = deliveryForm;
        
    }

    public RemittanceResponse() {
    }

    public RemittanceResponse(ResponseCode code, String mensaje) {
        super(new Date(), code.getCodigo(), mensaje);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getAmountOrigin() {
        return amountOrigin;
    }

    public void setAmountOrigin(String amountOrigin) {
        this.amountOrigin = amountOrigin;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSendingOptionSMS() {
        return sendingOptionSMS;
    }

    public void setSendingOptionSMS(String sendingOptionSMS) {
        this.sendingOptionSMS = sendingOptionSMS;
    }

    public String getAmountDestiny() {
        return amountDestiny;
    }

    public void setAmountDestiny(String amountDestiny) {
        this.amountDestiny = amountDestiny;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getPaymentServiceId() {
        return paymentServiceId;
    }

    public void setPaymentServiceId(String paymentServiceId) {
        this.paymentServiceId = paymentServiceId;
    }

    public String getSecondaryKey() {
        return secondaryKey;
    }

    public void setSecondaryKey(String secondaryKey) {
        this.secondaryKey = secondaryKey;
    }

    public String getAdditionalChanges() {
        return additionalChanges;
    }

    public void setAdditionalChanges(String additionalChanges) {
        this.additionalChanges = additionalChanges;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationHour() {
        return CreationHour;
    }

    public void setCreationHour(String CreationHour) {
        this.CreationHour = CreationHour;
    }

    public String getLocalSales() {
        return localSales;
    }

    public void setLocalSales(String localSales) {
        this.localSales = localSales;
    }

    public String getReserveField1() {
        return reserveField1;
    }

    public void setReserveField1(String reserveField1) {
        this.reserveField1 = reserveField1;
    }

    public String getRemittent() {
        return remittent;
    }

    public void setRemittent(String remittent) {
        this.remittent = remittent;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCorrespondent() {
        return Correspondent;
    }

    public void setCorrespondent(String Correspondent) {
        this.Correspondent = Correspondent;
    }

    public String getAddressReciever() {
        return addressReciever;
    }

    public void setAddressReciever(String addressReciever) {
        this.addressReciever = addressReciever;
    }

    public String getSalesType() {
        return salesType;
    }

    public void setSalesType(String salesType) {
        this.salesType = salesType;
    }

    public String getAddressRemittent() {
        return addressRemittent;
    }

    public void setAddressRemittent(String addressRemittent) {
        this.addressRemittent = addressRemittent;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getRatePaymentNetwork() {
        return ratePaymentNetwork;
    }

    public void setRatePaymentNetwork(String ratePaymentNetwork) {
        this.ratePaymentNetwork = ratePaymentNetwork;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOriginCurrent() {
        return originCurrent;
    }

    public void setOriginCurrent(String originCurrent) {
        this.originCurrent = originCurrent;
    }

    public String getDestinyCurrent() {
        return destinyCurrent;
    }

    public void setDestinyCurrent(String destinyCurrent) {
        this.destinyCurrent = destinyCurrent;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPaymentNetwork() {
        return paymentNetwork;
    }

    public void setPaymentNetwork(String paymentNetwork) {
        this.paymentNetwork = paymentNetwork;
    }


    public String getPaymentNetworkPoint() {
        return paymentNetworkPoint;
    }

    public void setPaymentNetworkPoint(String paymentNetworkPoint) {
        this.paymentNetworkPoint = paymentNetworkPoint;
    }

    public String getCashBox() {
        return cashBox;
    }

    public void setCashBox(String cashBox) {
        this.cashBox = cashBox;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemittanceNumber() {
        return remittanceNumber;
    }

    public void setRemittanceNumber(String remittanceNumber) {
        this.remittanceNumber = remittanceNumber;
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public void setPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public String getCorrelative() {
        return correlative;
    }

    public void setCorrelative(String correlative) {
        this.correlative = correlative;
    }

    public String getDeliveryForm() {
        return deliveryForm;
    }

    public void setDeliveryForm(String deliveryForm) {
        this.deliveryForm = deliveryForm;
    }

    public String getAmountTransferTotal() {
        return amountTransferTotal;
    }

    public void setAmountTransferTotal(String amountTransferTotal) {
        this.amountTransferTotal = amountTransferTotal;
    }



    
    
}
