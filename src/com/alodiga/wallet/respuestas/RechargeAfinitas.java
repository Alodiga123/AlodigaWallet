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
public class RechargeAfinitas extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    private String status;
    private String requestId;
    private String date;
    private String time;
    private ErrorAfinitas error;

    public RechargeAfinitas() {
    }

    public RechargeAfinitas(String status, String requestId, String date, String time, ErrorAfinitas error) {
        this.status = status;
        this.requestId = requestId;
        this.date = date;
        this.time = time;
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ErrorAfinitas getError() {
        return error;
    }

    public void setError(ErrorAfinitas error) {
        this.error = error;
    }
    
    
    
    

	

}
