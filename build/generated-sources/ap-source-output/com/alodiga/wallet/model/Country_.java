package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Address;
import com.alodiga.wallet.model.Bank;
import com.alodiga.wallet.model.Enterprise;
import com.alodiga.wallet.model.State;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T15:02:10")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-30T13:29:06")
>>>>>>> JesusMerge
@StaticMetamodel(Country.class)
public class Country_ { 

    public static volatile SingularAttribute<Country, String> alternativeName2;
    public static volatile SingularAttribute<Country, String> alternativeName3;
    public static volatile CollectionAttribute<Country, Address> addressCollection;
    public static volatile SingularAttribute<Country, String> code;
    public static volatile SingularAttribute<Country, String> alternativeName1;
    public static volatile CollectionAttribute<Country, Bank> bankCollection;
    public static volatile SingularAttribute<Country, String> name;
    public static volatile CollectionAttribute<Country, State> stateCollection;
    public static volatile SingularAttribute<Country, Long> id;
    public static volatile SingularAttribute<Country, String> shortName;
    public static volatile CollectionAttribute<Country, Enterprise> enterpriseCollection;

}