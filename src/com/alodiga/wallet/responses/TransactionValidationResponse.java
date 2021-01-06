package com.alodiga.wallet.responses;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author henry
 */
@XmlRootElement(name = "TransactionValidationResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionValidationResponse extends Response implements Serializable {

    public float amountBeforeFee;

    public float fee;

    public float amountAfterFee;

    public TransactionValidationResponse() {
    }

    public TransactionValidationResponse(ResponseCode code) {
        super(code);
    }

    public TransactionValidationResponse(ResponseCode code, String message) {
        super(new Date(), code.getCode(), message);
    }

    public TransactionValidationResponse(float amountBeforeFee, float fee, float amountAfterFee) {
        super(ResponseCode.SUCCESS);
        this.amountBeforeFee = amountBeforeFee;
        this.fee = fee;
        this.amountAfterFee = amountAfterFee;
    }

    public float getAmountBeforeFee() {
        return amountBeforeFee;
    }

    public void setAmountBeforeFee(float amountBeforeFee) {
        this.amountBeforeFee = amountBeforeFee;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public float getAmountAfterFee() {
        return amountAfterFee;
    }

    public void setAmountAfterFee(float amountAfterFee) {
        this.amountAfterFee = amountAfterFee;
    }

}
