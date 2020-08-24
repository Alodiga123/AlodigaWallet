package com.alodiga.wallet.responses;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ChangeStatusCredentialCardResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChangeStatusCredentialCard extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    private String biginingAtention;
    private String endingAtention;
    private String timerAtention;
    private String code;
    private String description;
    private String ticket;

    public ChangeStatusCredentialCard() {
    }

    public ChangeStatusCredentialCard(String biginingAtention, String endingAtention, String timerAtention, String code, String description, String ticket) {
        this.biginingAtention = biginingAtention;
        this.endingAtention = endingAtention;
        this.timerAtention = timerAtention;
        this.code = code;
        this.description = description;
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
    
    
    
    
    
	

}
