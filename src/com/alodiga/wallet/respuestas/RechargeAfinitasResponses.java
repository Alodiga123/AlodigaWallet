package com.alodiga.wallet.respuestas;

import com.alodiga.afinitas.json.charge.object.ChargeResponse;

import com.alodiga.wallet.common.model.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ActivateCardResponXm")
@XmlAccessorType(XmlAccessType.FIELD)
public class RechargeAfinitasResponses extends Response implements Serializable {

    private static final long serialVersionUID = -5826822375335798732L;
    public RechargeAfinitas rechargeAfinitas;
    public List<Product> products;
    public ChargeResponse chargeResponse;

    public RechargeAfinitasResponses() {
    }

    

    public RechargeAfinitasResponses(ChargeResponse chargeResponse, ResponseCode codigo, String mensajeRespuesta, ArrayList<Product> products) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.chargeResponse = chargeResponse;
        this.products = products;
    }
    
    public RechargeAfinitasResponses(ResponseCode codigo,
			String mensajeRespuesta) {
	   super(new Date(), codigo.getCodigo(), mensajeRespuesta);
           
    }
    
    public RechargeAfinitasResponses(ResponseCode codigo,
            String mensajeRespuesta, ArrayList<Product> products, RechargeAfinitas rechargeAfinitas) {
        super(new Date(), codigo.getCodigo(), mensajeRespuesta);
        this.rechargeAfinitas = rechargeAfinitas;
        this.products = products;
    }

    public RechargeAfinitas getRechargeAfinitas() {
        return rechargeAfinitas;
    }

    public void setRechargeAfinitas(RechargeAfinitas rechargeAfinitas) {
        this.rechargeAfinitas = rechargeAfinitas;
    }

    
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

        
}
