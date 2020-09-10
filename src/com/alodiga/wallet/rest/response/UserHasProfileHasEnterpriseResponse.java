package com.alodiga.wallet.rest.response;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.alodiga.wallet.common.model.UserHasProfile;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class UserHasProfileHasEnterpriseResponse implements Serializable {

	private static final long serialVersionUID = -5826822375335798732L;

	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "beginningDate")
    private Timestamp beginningDate;
	@XmlElement(name = "endingDate")
    private Timestamp endingDate;
	@XmlElement(name = "profileId")
	private Long profileId;
	@XmlElement(name = "enterpriseId")
	private Long enterpriseId;


	public UserHasProfileHasEnterpriseResponse() {
		super();
	}

	public UserHasProfileHasEnterpriseResponse(UserHasProfile userHasProfileHasEnterprise) {
		this.id = userHasProfileHasEnterprise.getId();
		this.beginningDate = userHasProfileHasEnterprise.getBeginningDate();
		this.endingDate = userHasProfileHasEnterprise.getEndingDate();
		this.profileId = userHasProfileHasEnterprise.getProfile().getId();
		this.enterpriseId = userHasProfileHasEnterprise.getEnterprise().getId();
		
	}
	
}
