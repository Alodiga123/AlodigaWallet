package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Enterprise;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-26T16:00:51")
@StaticMetamodel(Currency.class)
public class Currency_ { 

    public static volatile SingularAttribute<Currency, Long> id;
    public static volatile CollectionAttribute<Currency, Enterprise> enterpriseCollection;
    public static volatile SingularAttribute<Currency, String> symbol;
    public static volatile SingularAttribute<Currency, String> name;

}