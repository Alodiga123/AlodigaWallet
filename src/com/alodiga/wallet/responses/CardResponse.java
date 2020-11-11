package com.alodiga.wallet.responses;

import com.alodiga.wallet.common.model.Card;

import java.util.Date;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CardResponse extends Response {

	public Card card;
        private String numberCard;
	
	public CardResponse() {
		super();
	}
	
	public CardResponse(ResponseCode code) {
		super(new Date(), code.getCode(), code.name());
		this.card = null;
	}
	
	public CardResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCode(), mensaje);
		this.card = null;
	}

    public CardResponse(ResponseCode code, String mensaje, String numberCard) {
        super(new Date(), code.getCode(), mensaje);
        this.numberCard = numberCard;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getNumberCard() {
        return numberCard;
    }

    public void setNumberCard(String numberCard) {
        this.numberCard = numberCard;
    }
    
    

	
        
}
