package com.alodiga.wallet.model;

import com.alodiga.wallet.model.Address;
import com.alodiga.wallet.model.City;
import com.alodiga.wallet.model.Country;
import com.alodiga.wallet.model.County;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-27T13:34:56")
@StaticMetamodel(State.class)
public class State_ { 

    public static volatile CollectionAttribute<State, Address> addressCollection;
    public static volatile SingularAttribute<State, String> name;
    public static volatile CollectionAttribute<State, City> cityCollection;
    public static volatile SingularAttribute<State, Long> id;
    public static volatile CollectionAttribute<State, County> countyCollection;
    public static volatile SingularAttribute<State, Country> countryId;

}