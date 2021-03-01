package com.alodiga.wallet.responses;

import com.cms.commons.models.Card;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CardListResponse extends Response {

	public List<Card> cards;
	
	public CardListResponse() {
		super();
	}
	
	public CardListResponse(ResponseCode code) {
		super(new Date(), code.getCode(), code.name());
		this.cards = null;
	}
	
	public CardListResponse(ResponseCode code, String mensaje) {
		super(new Date(), code.getCode(), mensaje);
		this.cards = null;
	}

	public CardListResponse(ResponseCode code, String mensaje, List<Card> cards) {
		super(new Date(), code.getCode(), mensaje);
		this.cards = cards;
	}
        
}
