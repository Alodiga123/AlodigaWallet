package com.alodiga.wallet.respuestas;

import com.alodiga.wallet.model.Card;
import com.alodiga.wallet.model.Product;
import java.util.Date;
import java.util.List;

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
		super(new Date(), code.getCodigo(), code.name());
		this.card = null;
	}
	
	public CardResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCodigo(), mensaje);
		this.card = null;
	}

    public CardResponse(ResponseCode code, String mensaje, String numberCard) {
        super(new Date(), code.getCodigo(), mensaje);
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
