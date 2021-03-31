package com.alodiga.wallet.utils;

import cardcredentialserviceclient.CardCredentialServiceClient;
import com.alodiga.account.client.AccountCredentialServiceClient;
import com.alodiga.account.credential.response.StatusAccountResponse;
import com.alodiga.card.credential.response.StatusCardResponse;
import com.alodiga.cms.commons.ejb.CardEJB;
import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.wallet.common.model.BalanceHistory;
import com.alodiga.wallet.common.model.Product;
import com.alodiga.wallet.common.model.UserHasCard;
import com.alodiga.wallet.common.model.UserHasProduct;
import com.alodiga.wallet.common.utils.Constants;
import com.alodiga.wallet.common.utils.EncriptedRsa;
import com.alodiga.wallet.responses.CardResponse;
import com.alodiga.wallet.responses.ResponseCode;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.Card;
import com.cms.commons.models.PhonePerson;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author henry
 */
public abstract class UserProductsHelper {

    public static enum ProductBalanceType {
        payTopUp, exchangeProduct, remettence, paymentInfo, usePrepaidCard
    }

    public static ArrayList<Product> getUserProductBalanceList(Long userId, String userEmail, EntityManager entityManager) {

        ArrayList<Product> products = new ArrayList();
        CardResponse cardResponse = null;
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserHasProduct> cq = cb.createQuery(UserHasProduct.class);
            Root<UserHasProduct> from = cq.from(UserHasProduct.class);
            cq.select(from);
            cq.where(cb.equal(from.get("userSourceId"), userId));

            Query query = entityManager.createQuery(cq);
            List<UserHasProduct> userHasProducts = query.getResultList();
            if (userHasProducts.size() <= 0) {
                throw new NoResultException();
            }

            for (UserHasProduct uhp : userHasProducts) {
                Product product = entityManager.find(Product.class, uhp.getProductId());
                if (product.getId().equals(Product.PREPAID_CARD)) {
                    cardResponse = getCardByEmail(userEmail);
                    if (cardResponse.getCodigoRespuesta().equals(ResponseCode.SUCCESS.getCode()) && !cardResponse.getaliasCard().isEmpty()) {
                        products.add(product);
                    }
                } else {
                    products.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadProductBalance(products, cardResponse, userId, entityManager);

        /*for (Product p : products) {
            Float amount = 0F;
            try {
                if (p.getId().equals(Product.PREPAID_CARD)) {
                    AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                    CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                    String alias = cardResponse.getaliasCard();
                    try {
                        if (alias.length() > 0) {
                            String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(alias, Constants.PUBLIC_KEY));
                            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                            if (statusCardResponse.getCodigo().equals("00")) {
                                StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                amount = Float.valueOf(accountResponse.getComprasDisponibles());
                            } else {
                                amount = Float.valueOf(0);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    amount = loadLastBalanceHistoryByUser(userId, p, entityManager).getCurrentAmount();
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                amount = 0F;
            }
            p.setCurrentBalance(amount);
        }*/
        return products;
    }

    public static ArrayList<Product> getUserProductBalanceListByType(Long userId, ProductBalanceType type, EntityManager entityManager) {
        ArrayList<Product> products = new ArrayList();
        CardResponse cardResponse = null;
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserHasProduct> cq = cb.createQuery(UserHasProduct.class);
            Root<UserHasProduct> from = cq.from(UserHasProduct.class);

            cq.select(from);
            cq.where(cb.equal(from.get("userSourceId"), userId));

            Query query = entityManager.createQuery(cq);
            List<UserHasProduct> userHasProducts = query.getResultList();
            if (userHasProducts.size() <= 0) {
                throw new NoResultException();
            }

            CriteriaQuery<Product> cqp = cb.createQuery(Product.class);
            Root<Product> fromProduct = cqp.from(Product.class);
            cqp.select(fromProduct);
            In<Long> productClause = cb.in(fromProduct.get("id"));
            userHasProducts.forEach((uhp) -> {
                productClause.value(uhp.getProductId());
            });
            Predicate where;
            switch (type) {
                case usePrepaidCard:
                    where = cb.and(productClause, cb.equal(fromProduct.get("isUsePrepaidCard"), true));
                    break;
                case payTopUp:
                    where = cb.and(productClause, cb.equal(fromProduct.get("isPayTopUp"), true));
                    break;
                case exchangeProduct:
                    where = cb.and(productClause, cb.equal(fromProduct.get("isExchangeProduct"), true));
                    break;
                case paymentInfo:
                    where = cb.and(productClause, cb.equal(fromProduct.get("isPaymentInfo"), true));
                    break;
                case remettence:
                    where = cb.and(productClause, cb.equal(fromProduct.get("isRemettence"), true));
                    break;
                default:
                    return null;

            }
            cqp.where(where);

            query = entityManager.createQuery(cqp);
            List<Product> uProducts = query.getResultList();

            for (Product product : uProducts) {
                if (product.getId().equals(Product.PREPAID_CARD)) {
                    APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
                    RespuestaUsuario responseUser = proxy.getUsuarioporId("usuarioWS", "passwordWS", Long.toString(userId));
                    cardResponse = getCardByEmail(responseUser.getDatosRespuesta().getEmail());
                    if (cardResponse.getCodigoRespuesta().equals(ResponseCode.SUCCESS.getCode()) && !cardResponse.getaliasCard().isEmpty()) {
                        products.add(product);
                    }
                } else {
                    products.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }

        loadProductBalance(products, cardResponse, userId, entityManager);
        return products;
    }

    private static void loadProductBalance(List<Product> products, CardResponse cardResponse, Long userId, EntityManager entityManager) {
        products.forEach((p) -> {
            Float amount = 0F;
            try {
                if (p.getId().equals(Product.PREPAID_CARD)) {
                    AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                    CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                    String alias = cardResponse.getaliasCard();
                    try {
                        if (alias.length() > 0) {
                            String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(alias, Constants.PUBLIC_KEY));
                            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                            if (statusCardResponse.getCodigo().equals("00")) {
                                StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                amount = Float.valueOf(accountResponse.getComprasDisponibles());
                            } else {
                                amount = Float.valueOf(0);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    amount = loadLastBalanceHistoryByUser(userId, p, entityManager).getCurrentAmount();
                }
            } catch (NoResultException e) {
                amount = 0F;
            }
            p.setCurrentBalance(amount);
        });
    }

    private static CardResponse getCardByEmail(String email) {
        List<Card> cards;
        CardEJB cardEJB = (CardEJB) EJBServiceLocator.getInstance().get(EjbConstants.CARD_EJB);
        PersonEJB personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
        List<PhonePerson> phonePersonList = null;
        String alias = "";
        String name = "";
        String cardHolder = "";
        String emailPerson = "";
        String numberPhone = "";
        try {
            cards = cardEJB.getCardByEmail(email);
            for (Card card : cards) {
                Long havePhone = personEJB.havePhonesByPerson(card.getPersonCustomerId().getId());
                if (havePhone > 0) {
                    EJBRequest request1 = new EJBRequest();
                    Map params = new HashMap();
                    params.put(com.cms.commons.util.Constants.PERSON_KEY, card.getPersonCustomerId().getId());
                    request1.setParams(params);
                    phonePersonList = personEJB.getPhoneByPerson(request1);
                    for (PhonePerson p : phonePersonList) {
                        if (p.getPhoneTypeId().getId() == com.cms.commons.util.Constants.PHONE_TYPE_MOBILE) {
                            String area = p.getAreaCode();
                            String phoneNumber = p.getNumberPhone();
                            numberPhone = area + phoneNumber;
                        }
                    }
                } else {
                    numberPhone = "cliente sin número de teléfono registrado en BD";
                }
                alias = card.getAlias();
                cardHolder = card.getCardHolder();
                if (card.getPersonCustomerId().getEmail() != null) {
                    emailPerson = card.getPersonCustomerId().getEmail();
                }
                name = card.getPersonCustomerId().getNaturalCustomer().getFirstNames() + " " + card.getPersonCustomerId().getNaturalCustomer().getLastNames();
            }
        } catch (NoResultException e) {
            e.printStackTrace();
            return new CardResponse(ResponseCode.EMPTY_LIST_HAS_CARD, "Error loading cards");
        } catch (Exception e) {
            e.printStackTrace();
            return new CardResponse(ResponseCode.INTERNAL_ERROR, "Error loading cards");
        }
        return new CardResponse(ResponseCode.SUCCESS, "Card registered in BD", alias, name, emailPerson, numberPhone, cardHolder);
    }

    private static BalanceHistory loadLastBalanceHistoryByUser(Long userId, Product product, EntityManager entityManager) throws NoResultException {

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<BalanceHistory> cq = cb.createQuery(BalanceHistory.class);
            Root<BalanceHistory> from = cq.from(BalanceHistory.class);
            cq.select(from);
            cq.where(cb.equal(from.get("userId"), userId),
                    cb.equal(from.get("productId"), product));

            cq.orderBy(cb.desc(from.get("id")));

            Query query = entityManager.createQuery(cq);
            query.setMaxResults(1);
            BalanceHistory result = (BalanceHistory) query.setHint("toplink.refresh", "true").getSingleResult();
            return result;
        } catch (NoResultException e) {
            e.printStackTrace();
            throw new NoResultException();
        }

    }

}
