package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Language;
import com.alodiga.wallet.model.Promotion;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-26T16:00:51")
@StaticMetamodel(PromotionData.class)
public class PromotionData_ { 

    public static volatile SingularAttribute<PromotionData, Long> id;
    public static volatile SingularAttribute<PromotionData, Promotion> promotionId;
    public static volatile SingularAttribute<PromotionData, String> mailText;
    public static volatile SingularAttribute<PromotionData, String> smsText;
    public static volatile SingularAttribute<PromotionData, Language> languageId;

}