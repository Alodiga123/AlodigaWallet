package com.alodiga.wallet.genericEJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class EntityManagerWallet {

    @PersistenceContext(unitName = "AlodigaWalletPU")
    protected EntityManager entityManager;

   
}
