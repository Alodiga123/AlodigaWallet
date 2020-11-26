/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.responses;


/**
 *
 * @author ltoro
 */
public class Movement {
    
    private String date;
    private String hour;
    private String comerce;
    private String amount;
    private String descriptionAmount;
    private String authorizationCode;

    public Movement() {
    }

    public Movement(String date, String hour, String comerce, String amount, String descriptionAmount, String authorizationCode) {
        this.date = date;
        this.hour = hour;
        this.comerce = comerce;
        this.amount = amount;
        this.descriptionAmount = descriptionAmount;
        this.authorizationCode = authorizationCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getComerce() {
        return comerce;
    }

    public void setComerce(String comerce) {
        this.comerce = comerce;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescriptionAmount() {
        return descriptionAmount;
    }

    public void setDescriptionAmount(String descriptionAmount) {
        this.descriptionAmount = descriptionAmount;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

   
    
    
    
    
}
