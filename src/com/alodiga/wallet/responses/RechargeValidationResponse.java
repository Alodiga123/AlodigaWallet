package com.alodiga.wallet.responses;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hvarona
 */
@XmlRootElement(name = "RechargeValidtionResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class RechargeValidationResponse extends Response implements Serializable {

    public Double amountBeforeFee;

    public Double totalFee;

    public Double totalAmount;

    public RechargeValidationResponse() {
    }

    public RechargeValidationResponse(ResponseCode code) {
        super(code);
    }

    public RechargeValidationResponse(ResponseCode code, String message) {
        super(new Date(), code.getCode(), message);
    }

    public RechargeValidationResponse(Double amountBeforeFee, Double totalFee, Double totalAmount) {
        this(ResponseCode.SUCCESS);
        this.amountBeforeFee = amountBeforeFee;
        this.totalFee = totalFee;
        this.totalAmount = totalAmount;
    }

    public Double getAmountBeforeFee() {
        return amountBeforeFee;
    }

    public void setAmountBeforeFee(Double amountBeforeFee) {
        this.amountBeforeFee = amountBeforeFee;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
