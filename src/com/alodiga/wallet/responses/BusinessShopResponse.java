package com.alodiga.wallet.responses;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BusinessShopResponse extends Response {

    String businessName;

    String businessRif;

    String storeName;

    String posCode;

    public BusinessShopResponse() {
        super();
        this.businessName = null;
        this.businessRif = null;
        this.storeName = null;
    }

    public BusinessShopResponse(ResponseCode code) {
        super(new Date(), code.getCode(), code.name());
        this.businessName = null;
        this.businessRif = null;
        this.storeName = null;
    }

    public BusinessShopResponse(ResponseCode code, String mensaje) {
        super(new Date(), code.getCode(), mensaje);
        this.businessName = null;
        this.businessRif = null;
        this.storeName = null;
    }

    public BusinessShopResponse(ResponseCode code, String mensaje, String businessName, String businessRif, String storeName) {
        super(new Date(), code.getCode(), mensaje);
        this.businessName = businessName;
        this.businessRif = businessRif;
        this.storeName = storeName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessRif() {
        return businessRif;
    }

    public void setBusinessRif(String businessRif) {
        this.businessRif = businessRif;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }

}
