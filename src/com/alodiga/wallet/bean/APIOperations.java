package com.alodiga.wallet.bean;

import com.alodiga.wallet.common.utils.EncriptedRsa;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.codec.binary.Base64;

import com.alodiga.account.client.AccountCredentialServiceClient;
import com.alodiga.account.credential.response.StatusAccountResponse;
import com.alodiga.afinitas.json.charge.object.ChargeResponse;
import com.alodiga.autorization.credential.client.AutorizationCredentialServiceClient;
import com.alodiga.autorization.credential.response.CardToCardTransferResponse;
import com.alodiga.businessportal.ws.APIBusinessPortalWSProxy;
import com.alodiga.businessportal.ws.BpBusinessSellResponse;
import com.alodiga.card.credential.response.ChangeStatusCardResponse;
import com.alodiga.card.credential.response.StatusCardResponse;
import com.alodiga.massiva.sms.SendSmsMassiva;
import com.alodiga.plaid.response.ExchangeTokenResponse;
import com.alodiga.plaid.response.RetriveAuthResponse;
import com.alodiga.plaid.response.RetriveBalanceResponse;
import com.alodiga.plaid.response.RetriveIdentityResponse;
import com.alodiga.plaid.response.RetriveIncomeResponse;
import com.alodiga.plaid.response.RetriveTransactionResponse;
import com.alodiga.plaid.response.TokenResponse;
import com.alodiga.transferto.integration.connection.RequestManager;
import com.alodiga.transferto.integration.model.MSIDN_INFOResponse;
import com.alodiga.transferto.integration.model.ReserveResponse;
import com.alodiga.transferto.integration.model.TopUpResponse;
import com.alodiga.twilio.sms.services.TwilioSmsSenderProxy;
import com.alodiga.wallet.common.enumeraciones.StatusAccountBankE;
import com.alodiga.wallet.common.enumeraciones.StatusTransactionApproveRequestE;
import com.alodiga.wallet.common.model.AccountBank;
import com.alodiga.wallet.common.model.AccountTypeBank;
import com.alodiga.wallet.common.model.Address;
import com.alodiga.wallet.common.model.BalanceHistory;
import com.alodiga.wallet.common.model.Bank;
import com.alodiga.wallet.common.model.BankHasProduct;
import com.alodiga.wallet.common.model.BankOperation;
import com.alodiga.wallet.common.model.BankOperationMode;
import com.alodiga.wallet.common.model.BankOperationType;
import com.alodiga.wallet.common.model.BusinessHasProduct;
import com.cms.commons.models.Card;
import com.alodiga.wallet.common.model.Category;
import com.alodiga.wallet.common.model.Commission;
import com.alodiga.wallet.common.model.CommissionItem;
import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.common.model.CreditcardType;
import com.alodiga.wallet.common.model.Cumplimient;
import com.alodiga.wallet.common.model.CumplimientStatus;
import com.alodiga.wallet.common.model.ExchangeDetail;
import com.alodiga.wallet.common.model.ExchangeRate;
import com.alodiga.wallet.common.model.Language;
import com.alodiga.wallet.common.model.PaymentInfo;
import com.alodiga.wallet.common.model.PaymentPatner;
import com.alodiga.wallet.common.model.PaymentType;
import com.alodiga.wallet.common.model.Preference;
import com.alodiga.wallet.common.model.PreferenceField;
import com.alodiga.wallet.common.model.PreferenceValue;
import com.alodiga.wallet.common.model.Product;
import com.alodiga.wallet.common.model.Provider;
import com.alodiga.wallet.common.model.Sequences;
import com.alodiga.wallet.common.model.StatusAccountBank;
import com.alodiga.wallet.common.model.StatusTransactionApproveRequest;
import com.alodiga.wallet.common.model.TopUpCountry;
import com.alodiga.wallet.common.model.TopUpResponseConstants;
import com.alodiga.wallet.common.model.Transaction;
import com.alodiga.wallet.common.model.TransactionApproveRequest;
import com.alodiga.wallet.common.model.TransactionSource;
import com.alodiga.wallet.common.model.TransactionStatus;
import com.alodiga.wallet.common.model.TransactionType;
import com.alodiga.wallet.common.model.UserHasCard;
import com.alodiga.wallet.common.model.UserHasProduct;
import com.alodiga.wallet.common.model.UserHasBank;
import com.alodiga.wallet.common.model.UserWS;
import com.alodiga.wallet.common.model.ValidationCollection;
import com.alodiga.wallet.common.utils.AmazonSESSendMail;
import com.alodiga.wallet.common.utils.Constante;
import com.alodiga.wallet.common.utils.Constants;
import com.alodiga.wallet.common.utils.EjbUtils;
import com.alodiga.wallet.common.utils.Mail;
import com.alodiga.wallet.common.utils.QueryConstants;
import com.alodiga.wallet.common.utils.S3cur1ty3Cryt3r;
import com.alodiga.wallet.common.utils.SendMailTherad;
import com.alodiga.wallet.common.utils.SendSmsThread;
import com.alodiga.wallet.common.utils.Utils;
import com.alodiga.wallet.common.utils.XTrustProvider;
import com.alodiga.wallet.response.generic.BankGeneric;
import com.alodiga.wallet.responses.AccountBankListResponse;
import com.alodiga.wallet.responses.AccountBankResponse;
import com.alodiga.wallet.responses.ActivateCardResponses;
import com.alodiga.wallet.responses.BalanceHistoryResponse;
import com.alodiga.wallet.responses.BankListResponse;
import com.alodiga.wallet.responses.BusinessHasProductResponse;
import com.alodiga.wallet.responses.CardListResponse;
import com.alodiga.wallet.responses.CardResponse;
import com.alodiga.wallet.responses.ChangeStatusCredentialCard;
import com.alodiga.wallet.responses.CheckStatusAccountResponses;
import com.alodiga.wallet.responses.CheckStatusCardResponses;
import com.alodiga.wallet.responses.CheckStatusCredentialAccount;
import com.alodiga.wallet.responses.CheckStatusCredentialCard;
import com.alodiga.wallet.responses.CollectionListResponse;
import com.alodiga.wallet.responses.CountryListResponse;
import com.alodiga.wallet.responses.CreditCardListResponse;
import com.alodiga.wallet.responses.CumplimientResponse;
import com.alodiga.wallet.responses.DesactivateCardResponses;
import com.alodiga.wallet.responses.ExchangeTokenPlaidResponses;
import com.alodiga.wallet.responses.LanguageListResponse;
import com.alodiga.wallet.responses.PaymentInfoListResponse;
import com.alodiga.wallet.responses.PaymentInfoResponse;
import com.alodiga.wallet.responses.ProductListResponse;
import com.alodiga.wallet.responses.ProductResponse;
import com.alodiga.wallet.responses.RechargeAfinitasResponses;
import com.alodiga.wallet.responses.RemittanceResponse;
import com.alodiga.wallet.responses.ResponseCode;
import com.alodiga.wallet.responses.RetriveAuthPlaidResponses;
import com.alodiga.wallet.responses.RetriveBalancePlaidResponses;
import com.alodiga.wallet.responses.RetriveIdentityPlaidResponses;
import com.alodiga.wallet.responses.RetriveIncomePlaidResponses;
import com.alodiga.wallet.responses.RetriveTransactionPlaidResponses;
import com.alodiga.wallet.responses.TopUpCountryListResponse;
import com.alodiga.wallet.responses.TopUpInfoListResponse;
import com.alodiga.wallet.responses.TransactionApproveRequestResponse;
import com.alodiga.wallet.responses.TransactionListResponse;
import com.alodiga.wallet.responses.TransactionResponse;
import com.alodiga.wallet.responses.TransferCardToCardCredential;
import com.alodiga.wallet.responses.TransferCardToCardResponses;
import com.alodiga.wallet.responses.UserHasProductResponse;
import com.alodiga.wallet.topup.TopUpInfo;
import com.alodiga.ws.remittance.services.WSOFACMethodProxy;
import com.alodiga.ws.remittance.services.WsExcludeListResponse;
import com.alodiga.ws.remittance.services.WsLoginResponse;
import com.alodiga.ws.remittance.services.WSRemittenceMobileProxy;
import com.alodiga.ws.remittance.services.WsAddressListResponse;
import com.alodiga.ws.remittance.services.WsRemittenceResponse;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.Cuenta;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import com.ericsson.alodiga.ws.Usuario;

import afinitaspaymentintegration.AfinitasPaymentIntegration;
import cardcredentialserviceclient.CardCredentialServiceClient;
import com.alodiga.autorization.credential.response.BalanceInquiryWithMovementsResponse;
import com.alodiga.autorization.credential.response.BalanceInquiryWithoutMovementsResponse;
import com.alodiga.autorization.credential.response.DispertionResponse;
import com.alodiga.autorization.credential.response.LimitAdvanceResponse;
import com.alodiga.businessportal.ws.BpBusinessInfoResponse;
import com.alodiga.businessportal.ws.BusinessPortalWSException;
import com.alodiga.cms.commons.ejb.CardEJB;
import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.wallet.common.enumeraciones.DocumentTypeE;
import com.alodiga.wallet.common.enumeraciones.TransactionTypeE;
import com.alodiga.wallet.responses.BalanceInquiryWithMovementsCredential;
import com.alodiga.wallet.responses.BalanceInquiryWithMovementsResponses;
import com.alodiga.wallet.responses.BalanceInquiryWithoutMovementsCredential;
import com.alodiga.wallet.responses.BalanceInquiryWithoutMovementsResponses;
import com.alodiga.wallet.responses.BusinessShopResponse;
import com.alodiga.wallet.responses.DispertionTransferCredential;
import com.alodiga.wallet.responses.DispertionTransferResponses;
import com.alodiga.wallet.responses.LimitAdvanceCredential;
import com.alodiga.wallet.responses.LimitAdvanceResponses;
import com.alodiga.wallet.responses.AccountTypeBankListResponse;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.PhonePerson;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import java.util.HashMap;
import java.util.Map;
import plaidclientintegration.PlaidClientIntegration;
import credentialautorizationclient.CredentialAutorizationClient;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "FsProcessorWallet", mappedName = "ejb/FsProcessorWallet")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class APIOperations {

    @PersistenceContext(unitName = "AlodigaWalletPU")
    private EntityManager entityManager;

    public ProductResponse saveProduct(Long enterpriseId, Long categoryId, Long productIntegrationTypeId, String name, boolean taxInclude, boolean status, String referenceCode, String rateUrl, String accesNumberURL, boolean isFree, boolean isAlocashProduct, String symbol) {
        try {
            //t
            Product product = new Product();
            product.setId(null);
            Category category = entityManager.find(Category.class, categoryId);
            product.setCategoryId(category);
            product.setName(name);
            product.setTaxInclude(taxInclude);
            product.setEnabled(status);
            product.setReferenceCode(referenceCode);
            product.setIsFree(isFree);
            product.setIsAlocashProduct(isAlocashProduct);
            product.setSymbol(symbol);
            entityManager.persist(product);
        } catch (Exception e) {
            e.printStackTrace();
            return new ProductResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving product");
        }
        return new ProductResponse(ResponseCode.SUCCESS);
    }

    public UserHasProductResponse saveUserHasProduct(Long userId, Long productId) {
        try {
            UserHasProduct userHasProduct = new UserHasProduct();
            userHasProduct.setProductId(productId);
            userHasProduct.setUserSourceId(userId);
            userHasProduct.setBeginningDate(new Timestamp(new Date().getTime()));
            entityManager.persist(userHasProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return new UserHasProductResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving product_has_response");
        }
        return new UserHasProductResponse(ResponseCode.SUCCESS);
    }

    public UserHasProductResponse saveUserHasProductDefault(Long userId) {
        List<Product> products = new ArrayList<Product>();
        Boolean isDefaultProduct = true;
        try {
            products = (List<Product>) entityManager.createNamedQuery("Product.findByIsDefaultProduct", Product.class).setParameter("isDefaultProduct", isDefaultProduct).getResultList();

            if (!products.isEmpty()) {
                for (Product pr : products) {
                    if (pr.getId() != null) {
                        UserHasProduct userHasProduct = new UserHasProduct();
                        userHasProduct.setProductId(pr.getId());
                        userHasProduct.setUserSourceId(userId);
                        userHasProduct.setBeginningDate(new Timestamp(new Date().getTime()));
                        entityManager.persist(userHasProduct);
                    }
                }
            } else {
                return new UserHasProductResponse(ResponseCode.INTERNAL_ERROR, "There is no default product active at the moment");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new UserHasProductResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving product_has_response");
        }
        return new UserHasProductResponse(ResponseCode.SUCCESS);
    }

    public ProductListResponse getProductsByUserId(Long userId) {
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        List<Product> products = new ArrayList<Product>();
        try {
            userHasProducts = (List<UserHasProduct>) entityManager.createNamedQuery("UserHasProduct.findByUserSourceIdAllProduct", UserHasProduct.class).setParameter("userSourceId", userId).getResultList();

            if (userHasProducts.size() <= 0) {
                return new ProductListResponse(ResponseCode.USER_NOT_HAS_PRODUCT, "They are not products asociated");
            }

            for (UserHasProduct uhp : userHasProducts) {
                Product product = new Product();
                product = entityManager.find(Product.class, uhp.getProductId());
                products.add(product);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ProductListResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
        }

        return new ProductListResponse(ResponseCode.SUCCESS, "", products);
    }

    public CountryListResponse getCountries() {
        List<Country> countries = null;
        try {
            countries = entityManager.createNamedQuery("Country.findAll", Country.class).getResultList();

        } catch (Exception e) {
            return new CountryListResponse(ResponseCode.INTERNAL_ERROR, "Error loading countries");
        }
        return new CountryListResponse(ResponseCode.SUCCESS, "", countries);
    }

    public BankListResponse getBankApp() {
        List<Bank> banks = null;
        try {
            banks = entityManager.createNamedQuery("Bank.findAll", Bank.class).getResultList();

        } catch (Exception e) {
            return new BankListResponse(ResponseCode.INTERNAL_ERROR, "Error loading bank");
        }

        return new BankListResponse(ResponseCode.SUCCESS, "", banks);

    }

    public BankListResponse getBankByCountryApp(Long countryId) {
        List<Bank> banks = new ArrayList<Bank>();
        List<Country> countrys = new ArrayList<Country>();
        try {
            banks = (List<Bank>) entityManager.createNamedQuery("Bank.findByCountryId", Bank.class).setParameter("countryId", countryId).getResultList();

            if (banks.size() <= 0) {
                return new BankListResponse(ResponseCode.INTERNAL_ERROR, "Lista de banco vacia");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new BankListResponse(ResponseCode.INTERNAL_ERROR, "Error loading bank");
        }

        return new BankListResponse(ResponseCode.SUCCESS, "", banks);
    }

    public TransactionResponse savePaymentShop(String cryptogramShop, String emailUser, Long productId, Float amountPayment,
            String conceptTransaction) {

        Long idTransaction = 0L;
        Long userId = 0L;
        int totalTransactionsByUserDaily = 0;
        int totalTransactionsByUserMonthly = 0;
        int totalTransactionsByUserYearly = 0;
        Double totalAmountByUserDaily = 0.00D;
        Double totalAmountByUserMonthly = 0.00D;
        Double totalAmountByUserYearly = 0.00D;
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        ArrayList<Product> products = new ArrayList<Product>();
        Transaction paymentShop = new Transaction();
        APIBusinessPortalWSProxy aPIBusinessPortalWSProxy = new APIBusinessPortalWSProxy();
        BpBusinessSellResponse addSellTransaction = null;
        try {
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            //se buscar el businessId para identificar la billetera del negocio    
            addSellTransaction = aPIBusinessPortalWSProxy.addSellTransaction(cryptogramShop, paymentShop.getId(), "AloWallet", amountPayment);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountPayment) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
            }

            totalTransactionsByUserDaily = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserDaily = AmountMaxByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByUserMonthly = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserMonthly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByUserYearly = TransactionsByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserYearly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction11:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (pv.getValue().equals("0")) {
                                    return new TransactionResponse(ResponseCode.DISABLED_TRANSACTION, "Transactions disabled");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction4:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (amountPayment >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per transaction");
                                }
                            }
                        }
                        break;

                    case Constante.sValidatePreferenceTransaction5:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserDaily >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction6:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserDaily >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_DIALY, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction7:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserMonthly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction8:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserMonthly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_MONTHLY, "The user exceeded the maximum amount per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction9:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserYearly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_YEARLY, "The user exceeded the maximum number of transactions per year");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction10:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {

                                if (totalAmountByUserYearly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_YEARLY, "The user exceeded the maximum amount per year");
                                }
                            }
                        }
                        break;
                }
            }
            paymentShop.setId(null);
            paymentShop.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            paymentShop.setUserDestinationId(BigInteger.valueOf(addSellTransaction.getIdBusiness()));
            paymentShop.setTransactionBusinessId(BigInteger.valueOf(addSellTransaction.getIdTransaction()));
            Product product = entityManager.find(Product.class, productId);
            paymentShop.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypePS);
            paymentShop.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constants.sTransactionSource);
            paymentShop.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            paymentShop.setCreationDate(creationDate);
            paymentShop.setConcept(Constante.sTransactionConceptPaymentShop);
            paymentShop.setAmount(amountPayment);
            paymentShop.setTransactionStatus(TransactionStatus.CREATED.name());
            paymentShop.setTotalAmount(amountPayment);
            paymentShop.setTotalTax(null);
            paymentShop.setPromotionAmount(null);
            paymentShop.setTotalAlopointsUsed(null);
            paymentShop.setTopUpDescription(null);
            paymentShop.setBillPaymentDescription(null);
            paymentShop.setExternalId(null);
            paymentShop.setTransactionNumber("1");
            entityManager.flush();
            entityManager.persist(paymentShop);

            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypePS).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountPayment * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;

                    CommissionItem commissionItem = new CommissionItem();
                    commissionItem.setCommissionId(c);
                    commissionItem.setAmount(amountCommission);
                    Date commissionDate = new Date();
                    Timestamp processedDate = new Timestamp(commissionDate.getTime());
                    commissionItem.setProcessedDate(processedDate);
                    commissionItem.setTransactionId(paymentShop);
                    entityManager.persist(commissionItem);
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
            }

            paymentShop.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(paymentShop);

            balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(userId);
            balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
            Float currentAmountUserSource = balanceUserSource.getCurrentAmount() - amountPayment;
            balanceHistory.setCurrentAmount(currentAmountUserSource);
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(paymentShop);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceUserSource.getId());
            entityManager.persist(balanceHistory);

            BalanceHistory balanceBusinessDestination = loadLastBalanceHistoryByAccount(addSellTransaction.getIdBusiness(), productId);
            balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setBusinessId(addSellTransaction.getIdBusiness());
            balanceHistory.setTransactionBusinessId(addSellTransaction.getIdTransaction());
            if (balanceBusinessDestination == null) {
                balanceHistory.setOldAmount(Constante.sOldAmountUserDestination);
                balanceHistory.setCurrentAmount(amountPayment - amountCommission);
            } else {
                balanceHistory.setOldAmount(balanceBusinessDestination.getCurrentAmount());
                Float currentAmountUserDestination = (balanceBusinessDestination.getCurrentAmount() + amountPayment) - amountCommission;
                balanceHistory.setCurrentAmount(currentAmountUserDestination);
                balanceHistory.setVersion(balanceBusinessDestination.getId());
            }
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(paymentShop);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);

            entityManager.persist(balanceHistory);

            paymentShop.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(paymentShop);

            products = getProductsListByUserId(userId);
            for (Product p : products) {
                Float amount = 0F;
                try {
                    if (p.getId().equals(Product.PREPAID_CARD)) {
                        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                        CardResponse cardResponse = getCardByUserId(userId);
                        String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                        StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                        if (statusCardResponse.getCodigo().equals("00")) {
                            StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                            amount = Float.valueOf(accountResponse.getComprasDisponibles());
                        } else {
                            amount = Float.valueOf(0);
                        }

                    } else {

                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                    }
                } catch (NoResultException e) {
                    e.printStackTrace();
                    amount = 0F;
                } catch (ConnectException e) {
                    e.printStackTrace();
                    amount = 0F;
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    amount = 0F;
                }
                p.setCurrentBalance(amount);
            }

            Usuario usuario = new Usuario();
            usuario.setEmail(emailUser);

            SendMailTherad sendMailTherad = new SendMailTherad("ES", amountPayment, conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), emailUser, Integer.valueOf("3"));
            sendMailTherad.run();
            //Se elimino el envio de SMS y EMail al destino porque en el portal de negocios ya se realizan estos envios

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
        }
        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.SUCCESS, "EXITO", products);
        transactionResponse.setIdTransaction(paymentShop.getId().toString());
        transactionResponse.setProducts(products);
        transactionResponse.setIdBussines(addSellTransaction.getIdBusiness());
        return transactionResponse;
    }

    public TransactionResponse saveTransferBetweenAccount(String cryptograUserSource, String emailUser, Long productId, Float amountTransfer,
            String conceptTransaction, String cryptograUserDestination, Long idUserDestination) {

        Long idTransaction = 0L;
        Long userId = 0L;
        int totalTransactionsByUserDaily = 0;
        int totalTransactionsByUserMonthly = 0;
        int totalTransactionsByUserYearly = 0;
        Double totalAmountByUserDaily = 0.00D;
        Double totalAmountByUserMonthly = 0.00D;
        Double totalAmountByUserYearly = 0.00D;
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionTransfer = new Commission();
        ArrayList<Product> products = new ArrayList<Product>();
        Transaction transfer = new Transaction();
        try {

            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            RespuestaUsuario userDestination = proxy.getUsuarioporId("usuarioWS", "passwordWS", idUserDestination.toString());
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypeTA).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionTransfer = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountTransfer * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
            }
            Float amountTransferTotal = amountTransfer + amountCommission;
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountTransferTotal) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
            }

            totalTransactionsByUserDaily = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserDaily = AmountMaxByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByUserMonthly = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserMonthly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByUserYearly = TransactionsByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserYearly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction11:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (pv.getValue().equals("0")) {
                                    return new TransactionResponse(ResponseCode.DISABLED_TRANSACTION, "Transactions disabled");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction4:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (amountTransfer >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per transaction");
                                }
                            }
                        }
                        break;

                    case Constante.sValidatePreferenceTransaction5:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserDaily >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction6:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserDaily >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_DIALY, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction7:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserMonthly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction8:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserMonthly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_MONTHLY, "The user exceeded the maximum amount per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction9:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserYearly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_YEARLY, "The user exceeded the maximum number of transactions per year");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction10:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {

                                if (totalAmountByUserYearly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_YEARLY, "The user exceeded the maximum amount per year");
                                }
                            }
                        }
                        break;
                }
            }

            transfer.setId(null);
            transfer.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            transfer.setUserDestinationId(BigInteger.valueOf(idUserDestination));
            Product product = entityManager.find(Product.class, productId);
            transfer.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeTA);
            transfer.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            transfer.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            transfer.setCreationDate(creationDate);
            transfer.setConcept(Constante.sTransactionConceptTranferAccounts);
            transfer.setAmount(amountTransfer);
            transfer.setTransactionStatus(TransactionStatus.CREATED.name());
            transfer.setTotalAmount(amountTransfer);
            transfer.setTotalTax(null);
            transfer.setPromotionAmount(null);
            transfer.setTotalAlopointsUsed(null);
            transfer.setTopUpDescription(null);
            transfer.setBillPaymentDescription(null);
            transfer.setExternalId(null);
            transfer.setTransactionNumber("1");
            entityManager.flush();
            entityManager.persist(transfer);

            CommissionItem commissionItem = new CommissionItem();
            commissionItem.setCommissionId(commissionTransfer);
            commissionItem.setAmount(amountCommission);
            Date commissionDate = new Date();
            Timestamp processedDate = new Timestamp(commissionDate.getTime());
            commissionItem.setProcessedDate(processedDate);
            commissionItem.setTransactionId(transfer);
            entityManager.persist(commissionItem);

            transfer.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(transfer);

            balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(userId);
            balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
            Float currentAmountUserSource = balanceUserSource.getCurrentAmount() - amountTransferTotal;
            balanceHistory.setCurrentAmount(currentAmountUserSource);
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(transfer);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceUserSource.getId());
            entityManager.persist(balanceHistory);

            BalanceHistory balanceUserDestination = loadLastBalanceHistoryByAccount(idUserDestination, productId);
            balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(idUserDestination);
            if (balanceUserDestination == null) {
                balanceHistory.setOldAmount(Constante.sOldAmountUserDestination);
                balanceHistory.setCurrentAmount(amountTransfer);
            } else {
                balanceHistory.setOldAmount(balanceUserDestination.getCurrentAmount());
                Float currentAmountUserDestination = balanceUserDestination.getCurrentAmount() + amountTransfer;
                balanceHistory.setCurrentAmount(currentAmountUserDestination);
                balanceHistory.setVersion(balanceUserDestination.getId());
            }
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(transfer);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            entityManager.persist(balanceHistory);

            transfer.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(transfer);

            products = getProductsListByUserId(userId);
            for (Product p : products) {
                Float amount = 0F;
                try {
                    if (p.getId().equals(Product.PREPAID_CARD)) {
                        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                        CardResponse cardResponse = getCardByUserId(userId);
                        String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                        StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                        if (statusCardResponse.getCodigo().equals("00")) {
                            StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                            amount = Float.valueOf(accountResponse.getComprasDisponibles());
                        } else {
                            amount = Float.valueOf(0);
                        }

                    } else {

                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                    }
                } catch (NoResultException e) {
                    e.printStackTrace();
                    amount = 0F;
                } catch (ConnectException e) {
                    e.printStackTrace();
                    amount = 0F;
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    amount = 0F;
                }
                p.setCurrentBalance(amount);
            }

            SendMailTherad sendMailTherad = new SendMailTherad("ES", amountTransfer, conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), emailUser, Integer.valueOf("8"));
            sendMailTherad.run();

            SendMailTherad sendMailTherad1 = new SendMailTherad("ES", amountTransfer, conceptTransaction, userDestination.getDatosRespuesta().getNombre() + " " + userDestination.getDatosRespuesta().getApellido(), userDestination.getDatosRespuesta().getEmail(), Integer.valueOf("9"));
            sendMailTherad1.run();

            SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), amountTransfer, Integer.valueOf("27"), userId, entityManager);
            sendSmsThread.run();

            SendSmsThread sendSmsThread1 = new SendSmsThread(userDestination.getDatosRespuesta().getMovil(), amountTransfer, Integer.valueOf("28"), Long.valueOf(userDestination.getDatosRespuesta().getUsuarioID()), entityManager);
            sendSmsThread1.run();

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
        }

        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.SUCCESS, "EXITO", products);
        transactionResponse.setIdTransaction(transfer.getId().toString());
        transactionResponse.setProducts(products);
        return transactionResponse;
    }

    public TransactionResponse previewExchangeProduct(String emailUser, Long productSourceId, Long productDestinationId,
            Float amountExchange, int includedAmount) {

        Long userId = 0L;
        Float amountCommission = 0.00F;
        List<Commission> commissions = new ArrayList<Commission>();
        short isPercentCommission = 0;
        Float valueCommission = 0.00F;
        Float totalDebit = 0.00F;
        Float amountConversion = 0.00F;
        Float valueRateByProductSource = 0.00F;
        Float valueRateByProductDestination = 0.00F;

        try {
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, productSourceId);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productSourceId).setParameter("transactionTypeId", Constante.sTransationTypeEP).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productSourceId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    valueCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && valueCommission > 0) {
                        amountCommission = (amountExchange * valueCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
            }
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < totalDebit) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
            }

            ExchangeRate RateByProductSource = (ExchangeRate) entityManager.createNamedQuery("ExchangeRate.findByProduct", ExchangeRate.class).setParameter("productId", productSourceId).getSingleResult();
            valueRateByProductSource = RateByProductSource.getValue();
            ExchangeRate RateByProductDestination = (ExchangeRate) entityManager.createNamedQuery("ExchangeRate.findByProduct", ExchangeRate.class).setParameter("productId", productDestinationId).getSingleResult();
            valueRateByProductDestination = RateByProductDestination.getValue();
            if (includedAmount == 0) {
                totalDebit = amountExchange + amountCommission;
                amountConversion = (amountExchange * RateByProductSource.getValue()) / RateByProductDestination.getValue();
            } else {
                totalDebit = amountExchange;
                amountExchange = amountExchange - amountCommission;
                amountConversion = (amountExchange * RateByProductSource.getValue()) / RateByProductDestination.getValue();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
        }
        return new TransactionResponse(ResponseCode.SUCCESS, "", amountCommission, valueCommission, totalDebit,
                amountConversion, valueRateByProductSource, valueRateByProductDestination, isPercentCommission);
    }

    public TransactionResponse exchangeProduct(String emailUser, Long productSourceId, Long productDestinationId,
            Float amountExchange, String conceptTransaction, int includedAmount) {

        Long idTransaction = 0L;
        Long userId = 0L;
        Float totalDebit = 0.00F;
        int totalTransactionsByUserDaily = 0;
        int totalTransactionsByUserMonthly = 0;
        int totalTransactionsByUserYearly = 0;
        Double totalAmountByUserDaily = 0.00D;
        Double totalAmountByUserMonthly = 0.00D;
        Double totalAmountByUserYearly = 0.00D;
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        ArrayList<Product> products = new ArrayList<Product>();
        Transaction exchange = new Transaction();

        try {
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            totalTransactionsByUserDaily = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserDaily = AmountMaxByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByUserMonthly = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserMonthly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByUserYearly = TransactionsByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserYearly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction11:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (pv.getValue().equals("0")) {
                                    return new TransactionResponse(ResponseCode.DISABLED_TRANSACTION, "Transactions disabled");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction4:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (amountExchange >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per transaction");
                                }
                            }
                        }
                        break;

                    case Constante.sValidatePreferenceTransaction5:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserDaily >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction6:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserDaily >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_DIALY, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction7:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserMonthly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction8:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserMonthly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_MONTHLY, "The user exceeded the maximum amount per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction9:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserYearly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_YEARLY, "The user exceeded the maximum number of transactions per year");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction10:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {

                                if (totalAmountByUserYearly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_YEARLY, "The user exceeded the maximum amount per year");
                                }
                            }
                        }
                        break;
                }
            }

            exchange.setId(null);
            exchange.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            exchange.setUserDestinationId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            Product productSource = entityManager.find(Product.class, productSourceId);
            exchange.setProductId(productSource);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeEP);
            exchange.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            exchange.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            exchange.setCreationDate(creationDate);
            exchange.setConcept(conceptTransaction);
            exchange.setAmount(amountExchange);
            exchange.setTransactionStatus(TransactionStatus.CREATED.name());
            exchange.setTotalAmount(amountExchange);
            exchange.setTotalTax(null);
            exchange.setPromotionAmount(null);
            exchange.setTotalAlopointsUsed(null);
            exchange.setTopUpDescription(null);
            exchange.setBillPaymentDescription(null);
            exchange.setExternalId(null);
            exchange.setTransactionNumber("1");
            entityManager.flush();
            entityManager.persist(exchange);

            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productSourceId).setParameter("transactionTypeId", Constante.sTransationTypeEP).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productSourceId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountExchange * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                    CommissionItem commissionItem = new CommissionItem();
                    commissionItem.setCommissionId(c);
                    commissionItem.setAmount(amountCommission);
                    Date commissionDate = new Date();
                    Timestamp processedDate = new Timestamp(commissionDate.getTime());
                    commissionItem.setProcessedDate(processedDate);
                    commissionItem.setTransactionId(exchange);
                    entityManager.persist(commissionItem);
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
            }

            ExchangeRate RateByProductSource = (ExchangeRate) entityManager.createNamedQuery("ExchangeRate.findByProduct", ExchangeRate.class).setParameter("productId", productSourceId).getSingleResult();
            ExchangeRate RateByProductDestination = (ExchangeRate) entityManager.createNamedQuery("ExchangeRate.findByProduct", ExchangeRate.class).setParameter("productId", productDestinationId).getSingleResult();
            if (includedAmount == 0) {
                totalDebit = amountExchange + amountCommission;
            } else {
                totalDebit = amountExchange;
                amountExchange = amountExchange - amountCommission;
            }
            Float amountConversion = (amountExchange * RateByProductSource.getValue()) / RateByProductDestination.getValue();

            exchange.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(exchange);

            ExchangeDetail detailProductDestination = new ExchangeDetail();
            detailProductDestination.setId(null);
            detailProductDestination.setExchangeRateId(RateByProductDestination);
            Product productDestination = entityManager.find(Product.class, productDestinationId);
            detailProductDestination.setProductId(productDestination);
            detailProductDestination.setTransactionId(exchange);
            entityManager.persist(detailProductDestination);

            BalanceHistory balanceProductSource = loadLastBalanceHistoryByAccount(userId, productSourceId);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(userId);
            balanceHistory.setOldAmount(balanceProductSource.getCurrentAmount());
            Float currentAmountProductSource = balanceProductSource.getCurrentAmount() - totalDebit;
            balanceHistory.setCurrentAmount(currentAmountProductSource);
            balanceHistory.setProductId(productSource);
            balanceHistory.setTransactionId(exchange);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceProductSource.getId());
            entityManager.persist(balanceHistory);

            BalanceHistory balanceProductDestination = loadLastBalanceHistoryByAccount(userId, productDestinationId);
            balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(userId);
            if (balanceProductDestination == null) {
                balanceHistory.setOldAmount(Constante.sOldAmountUserDestination);
                balanceHistory.setCurrentAmount(amountConversion);
            } else {
                balanceHistory.setOldAmount(balanceProductDestination.getCurrentAmount());
                Float currentAmountUserDestination = balanceProductDestination.getCurrentAmount() + amountConversion;
                balanceHistory.setCurrentAmount(currentAmountUserDestination);
                balanceHistory.setVersion(balanceProductDestination.getId());
            }
            balanceHistory.setProductId(productDestination);
            balanceHistory.setTransactionId(exchange);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            entityManager.persist(balanceHistory);

            exchange.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(exchange);

            try {
                products = getProductsListByUserId(userId);
                for (Product p : products) {
                    Float amount_1 = 0F;
                    try {
                        if (p.getId().equals(Product.PREPAID_CARD)) {
                            AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                            CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                            CardResponse cardResponse = getCardByUserId(userId);
                            String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                            if (statusCardResponse.getCodigo().equals("00")) {
                                StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                            } else {
                                amount_1 = Float.valueOf(0);
                            }

                        } else {
                            amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                        }

                    } catch (NoResultException e) {
                        amount_1 = 0F;
                    } catch (ConnectException e) {
                        e.printStackTrace();
                        amount_1 = 0F;
                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                        amount_1 = 0F;
                    }
                    p.setCurrentBalance(amount_1);
                }
            } catch (Exception ex) {

                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
            }
            SendMailTherad sendMailTherad = new SendMailTherad("ES", productSource.getName(), productDestination.getName(), amountExchange, emailUser, conceptTransaction, emailUser, Integer.valueOf("10"));
            sendMailTherad.run();

            SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), productSource.getName(), productDestination.getName(), amountExchange, Integer.valueOf("29"), userId, entityManager);
            sendSmsThread.run();

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
        }
        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.SUCCESS, "EXITO", products);
        transactionResponse.setIdTransaction(exchange.getId().toString());
        transactionResponse.setProducts(products);
        return transactionResponse;
    }

    private List<Preference> getPreferences() {
        return entityManager.createNamedQuery("Preference.findAll", Preference.class).getResultList();
    }

    private List<Transaction> getTransactions() {
        return entityManager.createNamedQuery("Transaction.findAll", Transaction.class).getResultList();
    }

    private List<Transaction> getTransactionsByUser(Long userId) {
        return entityManager.createNamedQuery("Transaction.findByUserSourceId", Transaction.class)
                .setParameter("userSourceId", userId).getResultList();
    }

    private List<PreferenceValue> getPreferenceValuePayment(PreferenceField pf, Long preferenceClassficationId) {
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        Long idPreferenceField = pf.getId();
        return preferencesValue = entityManager.createNamedQuery("PreferenceValue.findByPreferenceFieldId", PreferenceValue.class).setParameter("preferenceFieldId", idPreferenceField).setParameter("preferenceClassficationId", preferenceClassficationId).getResultList();
    }

    public BalanceHistory loadLastBalanceHistoryByAccount(Long userId, Long productId) {
        try {
            Query query = entityManager.createQuery("SELECT b FROM BalanceHistory b WHERE b.userId = " + userId + " AND b.productId.id = " + productId + " ORDER BY b.id desc");
            query.setMaxResults(1);
            BalanceHistory result = (BalanceHistory) query.setHint("toplink.refresh", "true").getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public int TransactionsByUserCurrentDate(Long userId, Date begginingDateTime, Date endingDateTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.size();
    }

    public int TransactionsByUserByTransactionByProductCurrentDate(Long userId, Timestamp begginingDateTime, Timestamp endingDateTime, Long productId, Long transactionTypeId) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3 AND t.productId = ?4 AND t.transactionTypeId = ?5");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        query.setParameter("4", productId);
        query.setParameter("5", transactionTypeId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.size();
    }

    public Double AmountMaxByUserCurrentDate(Long userId, Date begginingDateTime, Date endingDateTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT SUM(t.totalAmount) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (double) result.get(0) : 0f;
    }

    public Double AmountMaxByUserByUserByTransactionByProductCurrentDate(Long userId, Timestamp begginingDateTime, Timestamp endingDateTime, Long productId, Long transactionTypeId) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT SUM(t.totalAmount) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3 AND t.productId = ?4 AND t.transactionTypeId = ?5");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        query.setParameter("4", productId);
        query.setParameter("5", transactionTypeId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (double) result.get(0) : 0f;
    }

    public Long TransactionsByProductByUserCurrentDate(Long productId, Long userId, Timestamp begginingDateTime, Timestamp endingDateTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(t.productId) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3 AND t.productId = ?4");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        query.setParameter("4", productId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (Long) result.get(0) : 0l;
    }

    public Long TransactionsByProductByUserByTransactionCurrentDate(Long productId, Long userId, Timestamp begginingDateTime, Timestamp endingDateTime, Long transactionTypeId) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(t.productId) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.userSourceId = ?3 AND t.productId = ?4 AND t.transactionTypeId = ?5");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", userId);
        query.setParameter("4", productId);
        query.setParameter("5", transactionTypeId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (Long) result.get(0) : 0l;
    }

    private Provider getProviderById(Long providerId) {
        return entityManager.createNamedQuery("Provider.findById", Provider.class).setParameter("id", providerId)
                .getSingleResult();
    }

    public TopUpInfoListResponse getTopUpInfo(String receiverNumber, String phoneNumber) {

        Provider provider = null;
        try {
            provider = getProviderById(1L);
        } catch (Exception ex) {
            return new TopUpInfoListResponse(ResponseCode.INTERNAL_ERROR, "Error al buscar el proveedor");
        }
        Float percentAditional = provider.getAditionalPercent();

        MSIDN_INFOResponse inf = null;

        List<TopUpInfo> topUpInfos = new ArrayList<TopUpInfo>();
        if (receiverNumber == null) {
            return new TopUpInfoListResponse(ResponseCode.INTERNAL_ERROR, "Error parametro receiver null");
        }
        try {
            inf = RequestManager.getMsisdn_ingo(receiverNumber);
            String[] productIds = null;
            String skuid = null;
            String[] skuids = null;
            String[] productRetailsPrices = null;
            String[] denominationsReceiver = null;
            String[] productWholesalePrices = null;

            skuid = inf.getSkuid();
            if (skuid != null) {
                TopUpInfo topUpInfo = new TopUpInfo();
                topUpInfo.setCountry(inf.getCountry());
                topUpInfo.setCoutryId(inf.getCountryId());
                topUpInfo.setIsOpenRange(true);
                topUpInfo.setOpertador(inf.getOperator());
                topUpInfo.setOperatorid(inf.getOperatorId());
                topUpInfo.setDestinationCurrency(inf.getDestinationCurrency());

                topUpInfo.setSkuid(inf.getSkuid());
                topUpInfo.setMinimumAmount(Float.parseFloat(inf.getOpen_range_minimum_amount_requested_currency()));
                topUpInfo.setMaximumAmount(Float.parseFloat(inf.getOpen_range_maximum_amount_requested_currency()));
                topUpInfo.setIncrement(Float.parseFloat(inf.getOpen_range_increment_local_currency()));
                Float amount = Float.parseFloat(inf.getOpen_range_minimum_amount_requested_currency()) + Float.parseFloat(inf.getOpen_range_increment_local_currency());

                TopUpResponse topUpResponse = RequestManager.simulationDoTopUp(phoneNumber, receiverNumber, inf.getOpen_range_minimum_amount_requested_currency(), inf.getSkuid());
                Float wholesalePrice = Float.parseFloat(topUpResponse.getWholesalePrice());
                topUpInfo.setWholesalePrice(wholesalePrice);
                Float realRetailPrice;
                Float commissionPercent;
                //solo se adiciona si es cuba
                if (inf.getCountry().equals("Cuba")) {
                    realRetailPrice = Float.parseFloat(inf.getOpen_range_minimum_amount_local_currency()) + (Float.parseFloat(inf.getOpen_range_minimum_amount_local_currency()) * percentAditional / 100);
                } else {
                    realRetailPrice = Float.parseFloat(inf.getOpen_range_minimum_amount_local_currency());
                }
                commissionPercent = ((realRetailPrice - wholesalePrice) / realRetailPrice) * 100;
                topUpInfo.setCommissionPercent(commissionPercent);
                topUpInfos.add(topUpInfo);
            } else {
                productIds = inf.getProduct_list().split(",");
                skuids = inf.getSkuid_list().split(",");
                productRetailsPrices = inf.getRetail_price_list().split(",");
                productWholesalePrices = inf.getWholesale_price_list().split(",");
                denominationsReceiver = inf.getLocal_info_amount_list().split(",");

                List<Float> denominationsReceivers = new ArrayList<Float>();
                for (int i = 0; i < productIds.length; i++) {
                    TopUpInfo topUpInfo = new TopUpInfo();
                    topUpInfo.setCountry(inf.getCountry());
                    topUpInfo.setCoutryId(inf.getCountryId());
                    topUpInfo.setIsOpenRange(false);
                    topUpInfo.setOpertador(inf.getOperator());
                    topUpInfo.setOperatorid(inf.getOperatorId());
                    topUpInfo.setDestinationCurrency(inf.getLocal_info_currency());

                    topUpInfo.setSkuid(skuids[i]);
                    Float retailPrice = Float.parseFloat(productRetailsPrices[i]);
                    Float wholesalePrice = Float.parseFloat(productWholesalePrices[i]);
                    topUpInfo.setWholesalePrice(wholesalePrice);
                    Float realRetailPrice;
                    Float commissionPercent;
                    //solo se adiciona si es cuba
                    if (inf.getCountry().equals("Cuba")) {
                        realRetailPrice = (retailPrice) + ((retailPrice * percentAditional) / 100);
                    } else {
                        realRetailPrice = retailPrice;
                    }
                    commissionPercent = ((realRetailPrice - wholesalePrice) / realRetailPrice) * 100;
                    topUpInfo.setCommissionPercent(commissionPercent);
                    topUpInfo.setDenominationSale(realRetailPrice);
                    topUpInfo.setDenomination(retailPrice);
                    topUpInfo.setDenominationReceiver(Float.parseFloat(denominationsReceiver[i]));
                    topUpInfos.add(topUpInfo);
                }

            }

        } catch (Exception ex) {
            return new TopUpInfoListResponse(ResponseCode.INTERNAL_ERROR, "Error en el metodo getTopUpInfs");
        }
        return new TopUpInfoListResponse(ResponseCode.SUCCESS, "", topUpInfos);
    }

    private TransactionResponse executeTopUp(String skuidIdRequest, Float amount, String destinationNumber, String senderNumber, String emailUser) {
        TransactionResponse transaction = new TransactionResponse();
        String phoneNumber = destinationNumber;
        try {
            MSIDN_INFOResponse response1 = RequestManager.getMsisdn_ingo(phoneNumber);
            String skuidId = response1.getSkuid();
            if (response1.getSkuid() == null) {
                String[] Skuids = response1.getSkuid_list().split(",");
                String[] products = response1.getProduct_list().split(",");
                for (int o = 0; o < products.length; o++) {
                    if (Float.parseFloat(products[o]) == amount) {
                        skuidId = Skuids[o];
                    }
                }
            }
            if (skuidId.equals(skuidIdRequest)) {
                ReserveResponse response2 = RequestManager.getReserve();
                TopUpResponse topUpResponseExecute = RequestManager.simulationDoTopUp(senderNumber, phoneNumber, amount.toString(), skuidId);
                //TopUpResponse topUpResponseExecute = RequestManager.newDoTopUp(senderNumber, phoneNumber, amount.toString(), skuidId, response2.getReserved_id());
                String code = topUpResponseExecute.getErrorCode();
                if (!code.equals("0")) {//Cuando es 0 esta bien...
                    StringBuilder errorBuilder = new StringBuilder(TopUpResponseConstants.TRANSFER_TO_CODES.get(code));
                    errorBuilder.append("Integrator = ").append("TransferTo").append("ProductId = ").append(response1.getOperatorId()).append("phoneNumber = ").append(destinationNumber);
                    if (code.equals("301") || topUpResponseExecute.getErrorText().equals("Denomination not available")) {
                        transaction.setCodigoRespuesta(ResponseCode.DENOMINATION_NOT_AVAILABLE.getCode());
                        transaction.setMensajeRespuesta("DENOMINATION NOT AVAILABLE");
                    } else if (code.equals("101") || topUpResponseExecute.getErrorText().equals("Destination MSISDN out of range")) {
                        transaction.setCodigoRespuesta(ResponseCode.DESTINATION_MSISDN_OUT_OF_RANGE.getCode());
                        transaction.setMensajeRespuesta("DESTINATION MSISDN OUT OF RANGE");
                    } else if (code.equals("204")) {
                        transaction.setCodigoRespuesta(ResponseCode.DESTINATION_NOT_PREPAID.getCode());
                        transaction.setMensajeRespuesta("DESTINATION NOT PREPAID");
                    } else {
                        transaction.setCodigoRespuesta(ResponseCode.ERROR_TRANSACTION_TOP_UP.getCode());
                        transaction.setMensajeRespuesta("ERROR TRANSACTION TOP UP");
                    }
                } else {

                    transaction.setCodigoRespuesta(ResponseCode.SUCCESS.getCode());
                    transaction.setMensajeRespuesta("TOPUP TRANSACTION SUCCESSFUL");

                }
            } else {
                transaction.setCodigoRespuesta(ResponseCode.DENOMINATION_NOT_AVAILABLE.getCode());
                transaction.setMensajeRespuesta("DENOMINATION NOT AVAILABLE");
            }
        } catch (Exception ex) {
            transaction.setCodigoRespuesta(ResponseCode.ERROR_TRANSACTION_TOP_UP.getCode());
            transaction.setMensajeRespuesta("TOPUP TRANSACTION FAILED");
        }

        return transaction;
    }

    public TransactionListResponse getTransactionsByUserIdApp(Long userId, Integer maxResult) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        try {
            entityManager.flush();

            transactions = (List<Transaction>) entityManager.createNamedQuery("Transaction.findByUserSourceId", Transaction.class).setParameter("userSourceId", userId).setMaxResults(maxResult).setParameter("userDestinationId", userId).getResultList();
            if (transactions.size() < 1) {
                throw new NoResultException(ResponseCode.TRANSACTION_LIST_NOT_FOUND_EXCEPTION.toString());
            }
        } catch (NoResultException e) {
            e.printStackTrace();
            return new TransactionListResponse(ResponseCode.TRANSACTION_LIST_NOT_FOUND_EXCEPTION, "El usuario no tiene transacciones asociadas");
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionListResponse(ResponseCode.INTERNAL_ERROR, "error interno");
        }

        APIRegistroUnificadoProxy api = new APIRegistroUnificadoProxy();

        for (Transaction t : transactions) {
            t.setPaymentInfoId(null);
            t.setProductId(t.getProductId());
            t.setTransactionType(t.getTransactionTypeId().getId().toString());
            t.setId(t.getId());
            RespuestaUsuario usuarioRespuesta = new RespuestaUsuario();
            try {
                usuarioRespuesta = api.getUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId));
                t.setDestinationUser(usuarioRespuesta.getDatosRespuesta().getEmail() + " / " + usuarioRespuesta.getDatosRespuesta().getMovil() + " / " + usuarioRespuesta.getDatosRespuesta().getNombre());
            } catch (RemoteException ex) {
                return new TransactionListResponse(ResponseCode.INTERNAL_ERROR, "No se logro comunicacion entre alodiga wallet y RU");
            }
        }
        return new TransactionListResponse(ResponseCode.SUCCESS, "", transactions);
    }

    public TransactionResponse manualWithdrawals(Long bankId, String emailUser, String accountBank,
            Float amountWithdrawal, Long productId, String conceptTransaction, Long documentTypeId, Long originApplicationId) {

        Long idTransaction = 0L;
        Long userId = 0L;
        int totalTransactionsByUserDaily = 0;
        int totalTransactionsByUserMonthly = 0;
        int totalTransactionsByUserYearly = 0;
        Double totalAmountByUserDaily = 0.00D;
        Double totalAmountByUserMonthly = 0.00D;
        Double totalAmountByUserYearly = 0.00D;
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionWithdrawal = new Commission();
        Transaction withdrawal = new Transaction();
        ArrayList<Product> products = new ArrayList<Product>();
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();

        try {
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            totalTransactionsByUserDaily = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserDaily = AmountMaxByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByUserMonthly = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserMonthly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByUserYearly = TransactionsByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserYearly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction11:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (pv.getValue().equals("0")) {
                                    return new TransactionResponse(ResponseCode.DISABLED_TRANSACTION, "Transactions disabled");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction4:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (amountWithdrawal >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per transaction");
                                }
                            }
                        }
                        break;

                    case Constante.sValidatePreferenceTransaction5:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserDaily >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction6:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserDaily >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_DIALY, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction7:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserMonthly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction8:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserMonthly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_MONTHLY, "The user exceeded the maximum amount per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction9:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserYearly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_YEARLY, "The user exceeded the maximum number of transactions per year");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction10:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {

                                if (totalAmountByUserYearly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_YEARLY, "The user exceeded the maximum amount per year");
                                }
                            }
                        }
                        break;
                }
            }

            withdrawal.setId(null);
            withdrawal.setTransactionNumber("1");
            withdrawal.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            withdrawal.setUserDestinationId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            Product product = entityManager.find(Product.class, productId);
            withdrawal.setProductId(product);
            withdrawal.setPaymentInfoId(null);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeManualWithdrawal);
            withdrawal.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            withdrawal.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            withdrawal.setCreationDate(creationDate);
            withdrawal.setConcept(conceptTransaction);
            withdrawal.setAmount(amountWithdrawal);
            withdrawal.setTransactionStatus(TransactionStatus.CREATED.name());
            withdrawal.setTotalAmount(amountWithdrawal);
            withdrawal.setTotalTax(null);
            withdrawal.setPromotionAmount(null);
            withdrawal.setTotalAlopointsUsed(null);
            withdrawal.setTopUpDescription(null);
            withdrawal.setBillPaymentDescription(null);
            withdrawal.setExternalId(null);
            entityManager.flush();
            entityManager.persist(withdrawal);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypeManualWithdrawal).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionWithdrawal = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountWithdrawal * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }

                CommissionItem commissionItem = new CommissionItem();
                commissionItem.setCommissionId(commissionWithdrawal);
                commissionItem.setAmount(amountCommission);
                Date commissionDate = new Date();
                Timestamp processedDate = new Timestamp(commissionDate.getTime());
                commissionItem.setProcessedDate(processedDate);
                commissionItem.setTransactionId(withdrawal);
                entityManager.persist(commissionItem);
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
            }

            BankOperation manualWithdrawal = new BankOperation();
            manualWithdrawal.setId(null);
            manualWithdrawal.setUserSourceId(BigInteger.valueOf(userId));
            manualWithdrawal.setProductId(product);
            manualWithdrawal.setTransactionId(withdrawal);
            manualWithdrawal.setCommisionId(commissionWithdrawal);
            BankOperationType operationType = entityManager.find(BankOperationType.class, Constante.sBankOperationTypeWithdrawal);
            manualWithdrawal.setBankOperationTypeId(operationType);
            BankOperationMode operationMode = entityManager.find(BankOperationMode.class, Constante.sBankOperationModeManual);
            manualWithdrawal.setBankOperationModeId(operationMode);
            Bank bank = entityManager.find(Bank.class, bankId);
            manualWithdrawal.setBankId(bank);
            manualWithdrawal.setBankOperationNumber(accountBank);
            entityManager.persist(manualWithdrawal);

            withdrawal.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(withdrawal);
            Usuario usuario = new Usuario();
            usuario.setEmail(emailUser);
            try {
                System.out.println("" + withdrawal.getId());
                TransactionApproveRequestResponse transactionApproveRequestResponse = saveTransactionApproveRequest(userId, product.getId(), withdrawal.getId(), bankId, documentTypeId, originApplicationId, 0L);
            } catch (Exception e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error saving transaction Aprrove Request");
            }
            try {
                products = getProductsListByUserId(userId);
                for (Product p : products) {
                    Float amount_1 = 0F;
                    try {
                        if (p.getId().equals(Product.PREPAID_CARD)) {
                            CardResponse cardResponse = getCardByUserId(userId);
                            String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                            if (statusCardResponse.getCodigo().equals("00")) {
                                StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                            } else {
                                amount_1 = Float.valueOf(0);
                            }

                        } else {
                            amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                        }

                    } catch (NoResultException e) {
                        amount_1 = 0F;
                    } catch (ConnectException e) {
                        e.printStackTrace();
                        amount_1 = 0F;
                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                        amount_1 = 0F;
                    }
                    p.setCurrentBalance(amount_1);
                }
            } catch (Exception ex) {

                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
            }
            SendMailTherad sendMailTherad = new SendMailTherad("ES", accountBank, amountWithdrawal, conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), emailUser, Integer.valueOf("4"));
            sendMailTherad.run();

            SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), Integer.valueOf("23"), amountWithdrawal, userId, entityManager);
            sendSmsThread.run();
        } catch (ConnectException e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
        }

        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.SUCCESS, "EXITO", products);
        transactionResponse.setIdTransaction(withdrawal.getId().toString());
        transactionResponse.setProducts(products);
        return transactionResponse;

    }

    public TransactionResponse manualRecharge(Long bankId, String emailUser, String referenceNumberOperation,
            Float amountRecharge, Long productId, String conceptTransaction, Long documentTypeId, Long originApplicationId) {

        Long idTransaction = 0L;
        Long userId = 0L;
        int totalTransactionsByUserDaily = 0;
        int totalTransactionsByUserMonthly = 0;
        int totalTransactionsByUserYearly = 0;
        Double totalAmountByUserDaily = 0.00D;
        Double totalAmountByUserMonthly = 0.00D;
        Double totalAmountByUserYearly = 0.00D;
        List<Transaction> transactionsByUser = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionRecharge = new Commission();
        Transaction recharge = new Transaction();
        ArrayList<Product> products = new ArrayList<Product>();
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();

        try {
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            //Validar preferencias
            totalTransactionsByUserDaily = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));
            totalAmountByUserDaily = AmountMaxByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));
            totalTransactionsByUserMonthly = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));
            totalAmountByUserMonthly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));
            totalTransactionsByUserYearly = TransactionsByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));
            totalAmountByUserYearly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction11:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (pv.getValue().equals("0")) {
                                    return new TransactionResponse(ResponseCode.DISABLED_TRANSACTION, "Transactions disabled");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction4:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (amountRecharge >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per transaction");
                                }
                            }
                        }
                        break;

                    case Constante.sValidatePreferenceTransaction5:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserDaily >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction6:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserDaily >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_DIALY, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction7:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserMonthly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction8:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserMonthly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_MONTHLY, "The user exceeded the maximum amount per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction9:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserYearly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_YEARLY, "The user exceeded the maximum number of transactions per year");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction10:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {

                                if (totalAmountByUserYearly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_YEARLY, "The user exceeded the maximum amount per year");
                                }
                            }
                        }
                        break;
                }
            }

            recharge.setId(null);
            recharge.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            recharge.setUserDestinationId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            Product product = entityManager.find(Product.class, productId);
            recharge.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeManualRecharge);
            recharge.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            recharge.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            recharge.setCreationDate(creationDate);
            recharge.setConcept(conceptTransaction);
            recharge.setAmount(amountRecharge);
            recharge.setTransactionStatus(TransactionStatus.CREATED.name());
            recharge.setTotalAmount(amountRecharge);
            recharge.setTotalTax(null);
            recharge.setPromotionAmount(null);
            recharge.setTotalAlopointsUsed(null);
            recharge.setTopUpDescription(null);
            recharge.setBillPaymentDescription(null);
            recharge.setExternalId(null);
            
            //Se hace el conteo de los id de transacciones para la secuancia
            Query query = entityManager.createQuery("SELECT COUNT(t.Id) FROM transaction t ");
            List count =(List) query.getResultList();
            //Se le suma al valor total para la secuencia
            Integer numberSecuence = ((BigInteger) count.get(0)).intValue() + 1;
            //Se estructura la secuancia
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            String begginingDate = formatter.format(creationDate);
            StringBuilder transactionNumber = new StringBuilder(transactionSource.getCode());
            transactionNumber.append("-");
            transactionNumber.append(transactionType.getCode());
            transactionNumber.append("-");
            transactionNumber.append(begginingDate);
            transactionNumber.append("-");
            transactionNumber.append(numberSecuence);
            recharge.setTransactionNumber(transactionNumber.toString());
            entityManager.flush();
            entityManager.persist(recharge);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypeManualRecharge).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionRecharge = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountRecharge * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }
                CommissionItem commissionItem = new CommissionItem();
                commissionItem.setCommissionId(commissionRecharge);
                commissionItem.setAmount(amountCommission);
                Date commissionDate = new Date();
                Timestamp processedDate = new Timestamp(commissionDate.getTime());
                commissionItem.setProcessedDate(processedDate);
                commissionItem.setTransactionId(recharge);
                entityManager.persist(commissionItem);
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
            }
            BankOperation manualRecharge = new BankOperation();
            manualRecharge.setId(null);
            manualRecharge.setUserSourceId(BigInteger.valueOf(userId));
            manualRecharge.setProductId(product);
            manualRecharge.setTransactionId(recharge);
            manualRecharge.setCommisionId(commissionRecharge);
            BankOperationType operationType = entityManager.find(BankOperationType.class, Constante.sBankOperationTypeRecharge);
            manualRecharge.setBankOperationTypeId(operationType);
            BankOperationMode operationMode = entityManager.find(BankOperationMode.class, Constante.sBankOperationModeManual);
            manualRecharge.setBankOperationModeId(operationMode);
            Bank bank = entityManager.find(Bank.class, bankId);
            manualRecharge.setBankId(bank);
            manualRecharge.setBankOperationNumber(referenceNumberOperation);
            entityManager.persist(manualRecharge);
            recharge.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(recharge);

            try {
                System.out.println("" + recharge.getId());
                TransactionApproveRequestResponse transactionApproveRequestResponse = saveTransactionApproveRequest(userId, product.getId(), recharge.getId(), bankId, documentTypeId, originApplicationId, 0L);
            } catch (Exception e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error saving transaction Aprrove Request");
            }
            try {
                products = getProductsListByUserId(userId);
                for (Product p : products) {
                    Float amount_1 = 0F;
                    try {
                        if (p.getId().equals(Product.PREPAID_CARD)) {
                            CardResponse cardResponse = getCardByUserId(userId);
                            String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                            if (statusCardResponse.getCodigo().equals("00")) {
                                StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                            } else {
                                amount_1 = Float.valueOf(0);
                            }

                        } else {
                            amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                        }

                    } catch (NoResultException e) {
                        amount_1 = 0F;
                    } catch (ConnectException e) {
                        e.printStackTrace();
                        amount_1 = 0F;
                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                        amount_1 = 0F;
                    }
                    p.setCurrentBalance(amount_1);
                }
            } catch (Exception ex) {

                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
            }
            Usuario usuario = new Usuario();
            usuario.setEmail(emailUser);
            SendMailTherad sendMailTherad = new SendMailTherad("ES", referenceNumberOperation, conceptTransaction, amountRecharge, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), emailUser, Integer.valueOf("2"));
            sendMailTherad.run();

            SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), amountRecharge, referenceNumberOperation, Integer.valueOf("21"), userId, entityManager);
            sendSmsThread.run();
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error interno");
        }
        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.SUCCESS, "EXITO", products);
        transactionResponse.setIdTransaction(recharge.getId().toString());
        transactionResponse.setProducts(products);
        return transactionResponse;
    }

    public ProductListResponse getProductsByBankId(Long bankId, Long userId) {
        List<BankHasProduct> bankHasProducts = new ArrayList<BankHasProduct>();
        List<Product> products = new ArrayList<Product>();
        try {
            bankHasProducts = (List<BankHasProduct>) entityManager.createNamedQuery("BankHasProduct.findByBankId", BankHasProduct.class).setParameter("bankId", bankId).getResultList();
            if (bankHasProducts.size() <= 0) {
                return new ProductListResponse(ResponseCode.USER_NOT_HAS_PRODUCT, "They are not products asociated");
            }
            for (BankHasProduct bhp : bankHasProducts) {
                Product product = new Product();
                product = entityManager.find(Product.class, bhp.getProductId().getId());
                BalanceHistory balanceHistory = new BalanceHistory();
                try {
                    balanceHistory = loadLastBalanceHistoryByAccount_(userId, product.getId());
                    if (product.getId().equals(Constants.PREPAY_CARD_CREDENTIAL)) {
                        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                        CardResponse cardResponse = getCardByUserId(userId);
                        String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                        StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                        StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                        balanceHistory.setCurrentAmount(Float.valueOf(accountResponse.getComprasDisponibles()));
                    }
                    product.setCurrentBalance(balanceHistory.getCurrentAmount());
                } catch (NoResultException e) {
                    product.setCurrentBalance(0F);
                } catch (ConnectException e) {
                    product.setCurrentBalance(0F);
                } catch (SocketTimeoutException e) {
                    product.setCurrentBalance(0F);
                }
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ProductListResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
        }

        return new ProductListResponse(ResponseCode.SUCCESS, "", products);
    }

    public CountryListResponse getCountriesHasBank(Long userId) {
        List countries = null;
        ArrayList<Country> countrys = new ArrayList<Country>();
        try {
            StringBuilder sqlBuilder = new StringBuilder("SELECT b.countryId FROM alodigaWallet.bank b WHERE b.id IN (SELECT bhp.bankId FROM alodigaWallet.bank_has_product bhp WHERE bhp.productId IN (SELECT uhp.productId FROM alodigaWallet.user_has_product uhp  WHERE uhp.userSourceId='" + userId + "'))GROUP BY b.countryId;");
            Query query = entityManager.createNativeQuery(sqlBuilder.toString());
            countries = query.setHint("toplink.refresh", "true").getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CountryListResponse(ResponseCode.INTERNAL_ERROR, "Error loading Countries");
        }
        if (countries != null && countries.size() > 0) {
            for (int i = 0; i < countries.size(); i++) {
                Long countryId = (Long) countries.get(i);

                Country country = entityManager.find(Country.class, countryId);
                countrys.add(country);
            }
        } else {

            return new CountryListResponse(ResponseCode.EMPTY_LIST_COUNTRY, "Empty Countries List");
        }
        return new CountryListResponse(ResponseCode.SUCCESS, "", countrys);
    }

    public BalanceHistoryResponse getBalanceHistoryByUserAndProduct(Long userId, Long productId) {
        BalanceHistory balanceHistory = new BalanceHistory();
        try {
            balanceHistory = loadLastBalanceHistoryByAccount_(userId, productId);
            if (productId.equals(Constants.PREPAY_CARD_CREDENTIAL)) {
                AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                CardResponse cardResponse = getCardByUserId(userId);
                String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                if (statusCardResponse.getCodigo().equals("00")) {
                    StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                    balanceHistory.setCurrentAmount(Float.valueOf(accountResponse.getComprasDisponibles()));
                } else {
                    balanceHistory.setCurrentAmount(0);
                }

            }

        } catch (NoResultException e) {
            return new BalanceHistoryResponse(ResponseCode.BALANCE_HISTORY_NOT_FOUND_EXCEPTION, "Error loading BalanceHistory");
        } catch (ConnectException e) {
            return new BalanceHistoryResponse(ResponseCode.CONNECT_TIMEOUT_EXCEPTION, "Conexión excedida");
        } catch (SocketTimeoutException e) {
            return new BalanceHistoryResponse(ResponseCode.SOCKECT_TIMEOUT_EXCEPTION, "SOCKECT TIMEOUT EXCEPTION");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BalanceHistoryResponse(ResponseCode.INTERNAL_ERROR, "Error loading BalanceHistory");
        }
        return new BalanceHistoryResponse(ResponseCode.SUCCESS, "", balanceHistory);
    }

    public BalanceHistory loadLastBalanceHistoryByAccount_(Long userId, Long productId) throws NoResultException {

        try {
            Query query = entityManager.createQuery("SELECT b FROM BalanceHistory b WHERE b.userId = " + userId + " AND b.productId.id = " + productId + " ORDER BY b.id desc");
            query.setMaxResults(1);
            BalanceHistory result = (BalanceHistory) query.setHint("toplink.refresh", "true").getSingleResult();
            return result;
        } catch (NoResultException e) {
            e.printStackTrace();
            throw new NoResultException();
        }

    }

    public void sendmailTest() {

        Usuario usuario = new Usuario();
        usuario.setNombre("Kerwin");
        usuario.setApellido("Gomez");
        usuario.setCredencial("DAnye");
        usuario.setEmail("moisegrat12@hotmail.com");
        usuario.setMovil("584241934005");
        Cuenta cunCuenta = new Cuenta();
        cunCuenta.setNumeroCuenta("01050614154515461528");
        usuario.setCuenta(cunCuenta);
        Transaction transaction = new Transaction();
        transaction.setId(1412L);
        transaction.getId();
        transaction.getTotalAmount();
        transaction.setTotalAmount(Float.valueOf("2"));
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setCurrentAmount(20);
        balanceHistory.setOldAmount(25);

        Mail mail = Utils.SendMailUserChangePassword("ES", usuario);
        System.out.println("body: " + mail.getBody());
        try {
            AmazonSESSendMail.SendMail(mail.getSubject(), mail.getBody(), mail.getTo().get(0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendMail(String subject, String body, String to, String from) {
        Mail mail = new Mail(subject, body);
        mail.setSubject(subject);
        mail.setFrom(from);
        mail.setBody(body);

        try {
            AmazonSESSendMail.SendMail(subject, body, to);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Product> getProductsListByUserId(Long userId) throws NoResultException, Exception {
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            userHasProducts = (List<UserHasProduct>) entityManager.createNamedQuery("UserHasProduct.findByUserSourceIdAllProduct", UserHasProduct.class).setParameter("userSourceId", userId).getResultList();

            if (userHasProducts.size() <= 0) {
                throw new NoResultException();
            }

            for (UserHasProduct uhp : userHasProducts) {
                Product product = new Product();
                product = entityManager.find(Product.class, uhp.getProductId());
                products.add(product);
            }
        } catch (Exception e) {
            throw new Exception();
        }
        return products;
    }

    public Boolean hasPrepayCardAsociated(Long userId) throws Exception {
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            userHasProducts = (List<UserHasProduct>) entityManager.createNamedQuery("UserHasProduct.findByUserSourceId", UserHasProduct.class).setParameter("userSourceId", userId).getResultList();
            if (userHasProducts.size() >= 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public Boolean hasPrepayCard(Long userId) throws Exception {
        List<UserHasCard> userHasCards = new ArrayList<UserHasCard>();
        try {
            userHasCards = (List<UserHasCard>) entityManager.createNamedQuery("UserHasCard.findByUserId", UserHasCard.class).setParameter("userId", userId).getResultList();
            if (userHasCards.size() >= 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public TransactionResponse saveRechargeTopUp(String emailUser, Long productId, String cryptogramUser,
            String skudId, String destinationNumber, String senderNumber, Float amountRecharge, Float amountPayment, String language) {

        Long idTransaction = 0L;
        Long idPreferenceField = 0L;
        Long userId = 0L;
        int totalTransactionsByUser = 0;
        Long totalTransactionsByProduct = 0L;
        Double totalAmountByUser = 0.00D;
        List<Transaction> transactionsByUser = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Timestamp begginingDateTime = new Timestamp(0);
        Timestamp endingDateTime = new Timestamp(0);
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionTopUp = new Commission();

        TransactionResponse response = null;
        try {
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", emailUser);
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountRecharge) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
            }
            begginingDateTime = Utils.DateTransaction()[0];
            endingDateTime = Utils.DateTransaction()[1];
            totalTransactionsByUser = TransactionsByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalAmountByUser = AmountMaxByUserCurrentDate(userId, begginingDateTime, endingDateTime);

            totalTransactionsByProduct = TransactionsByProductByUserCurrentDate(productId, userId, begginingDateTime, endingDateTime);

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constants.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constants.sValidatePreferenceTransaction1:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUser >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constants.sValidatePreferenceTransaction2:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByProduct >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_ACCOUNT, "The user exceeded the maximum number of transactions per product");
                                }
                            }
                        }
                        break;
                    case Constants.sValidatePreferenceTransaction3:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUser >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_MAX_NUMBER_BY_CUSTOMER, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                }
            }

            Transaction recharge = new Transaction();
            recharge.setId(null);
            recharge.setUserSourceId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            recharge.setUserDestinationId(BigInteger.valueOf(responseUser.getDatosRespuesta().getUsuarioID()));
            recharge.setTopUpDescription("Destination:" + destinationNumber + " SkuidID:" + skudId);
            Product product = entityManager.find(Product.class, productId);
            recharge.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeTopUP);
            recharge.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSourceTopUP);
            recharge.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            recharge.setCreationDate(creationDate);
            recharge.setConcept(Constante.sTransactionConceptTopUp);
            recharge.setAmount(amountRecharge);
            recharge.setTransactionStatus(TransactionStatus.CREATED.name());
            recharge.setTotalAmount(amountRecharge);
            recharge.setId(recharge.getId());
            recharge.setTotalTax(null);
            recharge.setPromotionAmount(null);
            recharge.setTotalAlopointsUsed(null);
            recharge.setTopUpDescription(null);
            recharge.setBillPaymentDescription(null);
            recharge.setExternalId(null);
            recharge.setTransactionNumber("1");
            entityManager.flush();
            entityManager.persist(recharge);

            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypeTopUP).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionTopUp = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountRecharge * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }
                //Se crea el objeto commissionItem y se persiste en BD
                CommissionItem commissionItem = new CommissionItem();
                commissionItem.setCommissionId(commissionTopUp);
                commissionItem.setAmount(amountCommission);
                Date commissionDate = new Date();
                Timestamp processedDate = new Timestamp(commissionDate.getTime());
                commissionItem.setProcessedDate(processedDate);
                commissionItem.setTransactionId(recharge);
                entityManager.persist(commissionItem);
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
            }

            amountPayment = amountRecharge + amountCommission;
            recharge.setTotalAmount(amountPayment);
            entityManager.merge(recharge);

            balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(userId);
            balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
            Float currentAmountUserSource = balanceUserSource.getCurrentAmount() - amountPayment;
            balanceHistory.setCurrentAmount(currentAmountUserSource);
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(recharge);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceUserSource.getId());
            entityManager.persist(balanceHistory);

            recharge.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(recharge);

            response = this.executeTopUp(skudId, amountRecharge, destinationNumber, senderNumber, emailUser);
            if (response.getCodigoRespuesta().equals(ResponseCode.SUCCESS.getCode())) {

                recharge.setTransactionStatus(TransactionStatus.COMPLETED.name());
                entityManager.merge(recharge);
                response.setIdTransaction(recharge.getId().toString());

                SendMailTherad sendMailTherad = new SendMailTherad("ES", destinationNumber, senderNumber, amountRecharge, amountPayment, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), emailUser, Integer.valueOf("6"));
                sendMailTherad.run();

                SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), destinationNumber, amountRecharge, Integer.valueOf("25"), userId, entityManager);
                sendSmsThread.run();

            } else {
                balanceUserSource = loadLastBalanceHistoryByAccount(userId, productId);
                balanceHistory = new BalanceHistory();
                balanceHistory.setId(null);
                balanceHistory.setUserId(userId);
                balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
                currentAmountUserSource = balanceUserSource.getCurrentAmount() + amountPayment;
                balanceHistory.setCurrentAmount(currentAmountUserSource);
                balanceHistory.setProductId(product);
                balanceHistory.setTransactionId(recharge);
                balanceDate = new Date();
                balanceHistoryDate = new Timestamp(balanceDate.getTime());
                balanceHistory.setDate(balanceHistoryDate);
                balanceHistory.setVersion(balanceUserSource.getId());
                balanceHistory.setAdjusmentInfo("TopUp Failed. Balance Refund");
                entityManager.persist(balanceHistory);

                //Se actualiza el estatus de la transaccion a FAILED
                recharge.setTransactionStatus(TransactionStatus.FAILED.name());
                entityManager.merge(recharge);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction saveRechargeTopUp");
        }

        ArrayList<Product> productResponses = new ArrayList<Product>();
        try {
            productResponses = getProductsListByUserId(userId);
            for (Product p : productResponses) {
                Float amount_1 = 0F;
                try {
                    if (p.getId().equals(Product.PREPAID_CARD)) {
                        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                        CardResponse cardResponse = getCardByUserId(userId);
                        String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                        StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                        if (statusCardResponse.getCodigo().equals("00")) {
                            StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                            amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                        } else {
                            amount_1 = Float.valueOf(0);
                        }

                    } else {
                        amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                    }
                } catch (NoResultException e) {
                    amount_1 = 0F;
                } catch (ConnectException e) {
                    e.printStackTrace();
                    amount_1 = 0F;
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    amount_1 = 0F;
                }
                p.setCurrentBalance(amount_1);
            }

        } catch (Exception ex) {

            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
        }

        response.setProducts(productResponses);
        return response;
    }

    public TopUpCountryListResponse getTopUpCountries() {
        List<TopUpCountry> topUpCountrys = null;
        try {
            topUpCountrys = entityManager.createNamedQuery("TopUpCountry.findAll", TopUpCountry.class).getResultList();
        } catch (Exception e) {
            return new TopUpCountryListResponse(ResponseCode.INTERNAL_ERROR, "Error loading countries");
        }
        return new TopUpCountryListResponse(ResponseCode.SUCCESS, "", topUpCountrys);
    }

    public LanguageListResponse getLanguage() {
        List<Language> languages = null;
        try {
            languages = entityManager.createNamedQuery("Language.findAll", Language.class).getResultList();

        } catch (Exception e) {
            return new LanguageListResponse(ResponseCode.INTERNAL_ERROR, "Error loading countries");
        }
        return new LanguageListResponse(ResponseCode.SUCCESS, "", languages);
    }

    public ProductListResponse getProductsPayTopUpByUserId(Long userId) {
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        List<Product> products = new ArrayList<Product>();
        List<Product> productFinals = new ArrayList<Product>();
        try {
            products = getProductsListByUserId(userId);
            for (Product p : products) {
                if (p.isIsPayTopUp()) {
                    Float amount = 0F;
                    try {
                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                    } catch (NoResultException e) {
                        amount = 0F;
                    }
                    p.setCurrentBalance(amount);
                    productFinals.add(p);
                }
            }
            if (productFinals.size() <= 0) {
                return new ProductListResponse(ResponseCode.USER_NOT_HAS_PRODUCT, "They are not products asociated");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ProductListResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
        }

        return new ProductListResponse(ResponseCode.SUCCESS, "", productFinals);
    }

    public ProductListResponse getProductsIsExchangeProductUserId(Long userId) {
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        List<Product> products = new ArrayList<Product>();
        List<Product> productFinals = new ArrayList<Product>();
        try {
            products = getProductsListByUserId(userId);
            for (Product p : products) {
                if (p.isIsExchangeProduct()) {
                    Float amount = 0F;
                    try {
                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                    } catch (NoResultException e) {
                        amount = 0F;
                    }
                    p.setCurrentBalance(amount);
                    productFinals.add(p);
                }
            }
            if (productFinals.size() <= 0) {
                return new ProductListResponse(ResponseCode.USER_NOT_HAS_PRODUCT, "They are not products asociated");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ProductListResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
        }

        return new ProductListResponse(ResponseCode.SUCCESS, "", productFinals);
    }

    public String sendSmsSimbox(String text, String phoneNumber, Long userId) {
        try {
            return Utils.sendSmsSimbox(text, phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public Cumplimient LoadCumplimientStatus(Long userId) {
        try {
            Query query = entityManager.createQuery("SELECT c FROM Cumplimient c WHERE c.userSourceId = " + userId + " AND c.endingDate IS NULL ORDER BY c.id desc");
            query.setMaxResults(1);
            Cumplimient result = (Cumplimient) query.setHint("toplink.refresh", "true").getSingleResult();
            return result;
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }

    }

    public CumplimientResponse getCumplimientStatus(Long userId) {
        Cumplimient cumplimients = new Cumplimient();
        try {
            cumplimients = LoadCumplimientStatus(userId);
        } catch (NoResultException e) {
            e.printStackTrace();
            return new CumplimientResponse(ResponseCode.NOT_VALIDATE, "User Not Validate");
        }
        return new CumplimientResponse(ResponseCode.SUCCESS, "", cumplimients);
    }

    public CollectionListResponse getValidateCollection(Long userId, String language) {
        List<ValidationCollection> validationCollections = new ArrayList<ValidationCollection>();
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        try {
            responseUser = proxy.getUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            Country codeCountry = getCountryCode(responseUser.getDatosRespuesta().getMovil());
            validationCollections = entityManager.createNamedQuery("ValidationCollection.findByStatusByLanguage", ValidationCollection.class).setParameter("languageId", language).setParameter("countryId", codeCountry.getId()).getResultList();

        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new CollectionListResponse(ResponseCode.INTERNAL_ERROR, "Error validating collections");
        }

        return new CollectionListResponse(ResponseCode.SUCCESS, "", validationCollections);
    }

    public Country getCountryCode(String strAni) {
        long ani = Long.parseLong(strAni);

        String number = ani + "";

        Country aniCode = null;
        int index = number.length();

        while (aniCode == null && index > 0) {
            try {
                aniCode = (Country) entityManager.createQuery("SELECT c FROM Country c WHERE c.code=" + number.substring(0, index)).getSingleResult();
            } catch (Exception e) {
            }
            index--;
        }

        return aniCode;
    }

    public LanguageListResponse getLanguageByIso(String language) {
        List<Language> languages = null;
        try {
            languages = entityManager.createNamedQuery("Language.findByIso", Language.class).setParameter("iso", language).getResultList();

        } catch (Exception e) {
            return new LanguageListResponse(ResponseCode.INTERNAL_ERROR, "Error loading countries");
        }
        return new LanguageListResponse(ResponseCode.SUCCESS, "", languages);
    }

    public Address saveAddress(Long userId, String estado, String ciudad, String zipCode, String addres1) throws RemoteException, Exception {

        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        try {
            responseUser = proxy.getUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            Country codeCountry = getCountryCode(responseUser.getDatosRespuesta().getMovil());
            Address address = new Address();
            address.setId(null);
            address.setCountryId(codeCountry);
            address.setCityId(null);
            address.setCountyId(null);
            address.setAddressLine1(addres1);
            address.setZipCode(zipCode);
            entityManager.persist(address);
            return address;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            throw new RemoteException(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public CollectionListResponse saveCumplimient(Long userId, byte[] imgDocument, byte[] imgProfile, String estado, String ciudad, String zipCode, String addres1) {

        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        Cumplimient cumplimient = new Cumplimient();
        try {
            responseUser = proxy.getUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            Address address = saveAddress(userId, estado, ciudad, zipCode, addres1);
            WSOFACMethodProxy oFACMethodWSProxy = new WSOFACMethodProxy();

            WsLoginResponse response;
            WsExcludeListResponse response2;
            response = oFACMethodWSProxy.loginWS("alodiga", "d6f80e647631bb4522392aff53370502");
            response2 = oFACMethodWSProxy.queryOFACList(response.getToken(), responseUser.getDatosRespuesta().getApellido(), responseUser.getDatosRespuesta().getNombre(), null, null, null, null, 0.5F);
            cumplimient.setUserSourceId(userId);
            cumplimient.setIsKYC(true);
            cumplimient.setIsAML(true);
            cumplimient.setBeginningDate(new Timestamp(new Date().getTime()));
            cumplimient.setEndingDate(null);
            cumplimient.setAprovedDate(null);
            cumplimient.setAMLPercent(Float.valueOf(response2.getPercentMatch()) * 100);
            cumplimient.setImgDocumentDate(imgDocument);
            cumplimient.setImgProfile(imgProfile);
            CumplimientStatus cumplimientStatus = entityManager.find(CumplimientStatus.class, CumplimientStatus.IN_PROCESS);
            cumplimient.setComplientStatusId(cumplimientStatus);
            cumplimient.setAgentComplientId(null);
            cumplimient.setAddressId(address);
            cumplimient.setAdditional(null);
            entityManager.persist(cumplimient);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new CollectionListResponse(ResponseCode.INTERNAL_ERROR, "Error remote");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CollectionListResponse(ResponseCode.INTERNAL_ERROR, "Error validating");
        }

        return new CollectionListResponse(ResponseCode.SUCCESS);

    }

    public ActivateCardResponses activateCard(Long userId, String card, String timeZone, String status) {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            responseUser = proxy.getUsuarioporId(Constants.ALODIGA_WALLET_USUARIO_API, Constants.ALODIGA_WALLET_PASSWORD_API, String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            String Card = S3cur1ty3Cryt3r.aloEncrpter(card, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            if (isCardUnique(Card)) {
                return new ActivateCardResponses(
                        ResponseCode.CARD_NUMBER_EXISTS, "CARD NUMBER EXISTS");
            }
            ignoreSSL();
            String encryptedString = Base64.encodeBase64String(EncriptedRsa.encrypt(Card, Constants.PUBLIC_KEY));
            ChangeStatusCardResponse response = cardCredentialServiceClient.changeStatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, timeZone, encryptedString, status);
            if (response.getCodigoRespuesta().equals("00") || response.getCodigoRespuesta().equals("-024")) {
                if (!hasPrepayCardAsociated(userId)) {
                    //Si no lo tiene se debe afiliar 
                    UserHasProduct userHasProduct2 = new UserHasProduct();
                    userHasProduct2.setProductId(Product.PREPAID_CARD);
                    userHasProduct2.setUserSourceId(userId);
                    userHasProduct2.setBeginningDate(new Timestamp(new Date().getTime()));
                    entityManager.persist(userHasProduct2);
                }
                try {
                    products = getProductsListByUserId(userId);
                    for (Product p : products) {
                        Float amount_1 = 0F;
                        try {
                            if (p.getId().equals(Product.PREPAID_CARD)) {
                                CardResponse cardResponse = getCardByUserId(userId);
                                String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                                StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                                if (statusCardResponse.getCodigo().equals("00")) {
                                    StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                    amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                                } else {
                                    amount_1 = Float.valueOf(0);
                                }

                            } else {
                                amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                            }

                        } catch (NoResultException e) {
                            amount_1 = 0F;
                        } catch (ConnectException e) {
                            e.printStackTrace();
                            amount_1 = 0F;
                        } catch (SocketTimeoutException e) {
                            e.printStackTrace();
                            amount_1 = 0F;
                        }
                        p.setCurrentBalance(amount_1);
                    }
                } catch (Exception ex) {

                    return new ActivateCardResponses(ResponseCode.INTERNAL_ERROR, "Error loading products");
                }

                ChangeStatusCredentialCard changeStatusCredentialcardResponse = new ChangeStatusCredentialCard(response.getInicio(), response.getFin(), response.getTiempo(), response.getCodigoRespuesta(), response.getDescripcion(), response.getTicketWS());
                ActivateCardResponses activateCardResponses = new ActivateCardResponses(changeStatusCredentialcardResponse, ResponseCode.SUCCESS, "", products);
                activateCardResponses.setProducts(products);

                CardResponse respuestaTarjeta = getCardByUserId(userId);
                activateCardResponses.setNumberCard(respuestaTarjeta.getaliasCard());
                return activateCardResponses;
            } else if (response.getCodigoRespuesta().equals("-024")) {
                return new ActivateCardResponses(ResponseCode.NOT_ALLOWED_TO_CHANGE_STATE, "NOT ALLOWED TO CHANGE STATE");
            } else if (response.getCodigoRespuesta().equals("-011")) {
                return new ActivateCardResponses(ResponseCode.AUTHENTICATE_IMPOSSIBLE, "Authenticate Impossible");
            } else if (response.getCodigoRespuesta().equals("-13")) {
                return new ActivateCardResponses(ResponseCode.SERVICE_NOT_ALLOWED, "Service Not Allowed");
            } else if (response.getCodigoRespuesta().equals("-14")) {
                return new ActivateCardResponses(ResponseCode.OPERATION_NOT_ALLOWED_FOR_THIS_SERVICE, "Operation Not Allowed For This Service");
            } else if (response.getCodigoRespuesta().equals("-060")) {
                return new ActivateCardResponses(ResponseCode.UNABLE_TO_ACCESS_DATA, "Unable to Access Data");
            } else if (response.getCodigoRespuesta().equals("-120")) {
                return new ActivateCardResponses(ResponseCode.THERE_ARE_NO_RECORDS_FOR_THE_REQUESTED_SEARCH, "There are no Records for the Requested Search");
            } else if (response.getCodigoRespuesta().equals("-140")) {
                return new ActivateCardResponses(ResponseCode.THE_REQUESTED_PRODUCT_DOES_NOT_EXIST, "The Requested Product does not Exist");
            } else if (response.getCodigoRespuesta().equals("-160")) {
                return new ActivateCardResponses(ResponseCode.THE_NUMBER_OF_ORDERS_ALLOWED_IS_EXCEEDED, "The Number of Orders Allowed is Exceeded");
            }
            return new ActivateCardResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");

        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new ActivateCardResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ActivateCardResponses(ResponseCode.INTERNAL_ERROR, "");
        }
    }

    public DesactivateCardResponses desactivateCard(Long userId, String card, String timeZone, String status) {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        try {
            responseUser = proxy.getUsuarioporId(Constants.ALODIGA_WALLET_USUARIO_API, Constants.ALODIGA_WALLET_PASSWORD_API, String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            ignoreSSL();
            //card = S3cur1ty3Cryt3r.aloEncrpter(card, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            String encryptedString = Base64.encodeBase64String(EncriptedRsa.encrypt(card, Constants.PUBLIC_KEY));
            ChangeStatusCardResponse response = cardCredentialServiceClient.changeStatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, timeZone, encryptedString, status);
            System.out.println(response.getCodigoRespuesta());
            response.setCodigoRespuesta("00");
            if (response.getCodigoRespuesta().equals("00")) {
                ChangeStatusCredentialCard changeStatusCredentialcardResponse = new ChangeStatusCredentialCard(response.getInicio(), response.getFin(), response.getTiempo(), response.getCodigoRespuesta(), response.getDescripcion(), response.getTicketWS());
                return new DesactivateCardResponses(changeStatusCredentialcardResponse, ResponseCode.SUCCESS, "");
            } else if (response.getCodigoRespuesta().equals("-024")) {
                return new DesactivateCardResponses(ResponseCode.NOT_ALLOWED_TO_CHANGE_STATE, "NOT ALLOWED TO CHANGE STATE");
            } else if (response.getCodigoRespuesta().equals("-011")) {
                return new DesactivateCardResponses(ResponseCode.AUTHENTICATE_IMPOSSIBLE, "Authenticate Impossible");
            } else if (response.getCodigoRespuesta().equals("-13")) {
                return new DesactivateCardResponses(ResponseCode.SERVICE_NOT_ALLOWED, "Service Not Allowed");
            } else if (response.getCodigoRespuesta().equals("-14")) {
                return new DesactivateCardResponses(ResponseCode.OPERATION_NOT_ALLOWED_FOR_THIS_SERVICE, "Operation Not Allowed For This Service");
            } else if (response.getCodigoRespuesta().equals("-060")) {
                return new DesactivateCardResponses(ResponseCode.UNABLE_TO_ACCESS_DATA, "Unable to Access Data");
            } else if (response.getCodigoRespuesta().equals("-120")) {
                return new DesactivateCardResponses(ResponseCode.THERE_ARE_NO_RECORDS_FOR_THE_REQUESTED_SEARCH, "There are no Records for the Requested Search");
            } else if (response.getCodigoRespuesta().equals("-140")) {
                return new DesactivateCardResponses(ResponseCode.THE_REQUESTED_PRODUCT_DOES_NOT_EXIST, "The Requested Product does not Exist");
            } else if (response.getCodigoRespuesta().equals("-160")) {
                return new DesactivateCardResponses(ResponseCode.THE_NUMBER_OF_ORDERS_ALLOWED_IS_EXCEEDED, "The Number of Orders Allowed is Exceeded");
            }
            return new DesactivateCardResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new DesactivateCardResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new DesactivateCardResponses(ResponseCode.INTERNAL_ERROR, "");
        }
    }

    public CheckStatusCardResponses checkStatusCard(Long userId, String card, String timeZone) {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        try {
            responseUser = proxy.getUsuarioporId(Constants.ALODIGA_WALLET_USUARIO_API, Constants.ALODIGA_WALLET_PASSWORD_API, String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            card = S3cur1ty3Cryt3r.aloEncrpter(card, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            String encryptedString = Base64.encodeBase64String(EncriptedRsa.encrypt(card, Constants.PUBLIC_KEY));
            System.out.println("encryptedString " + encryptedString);
            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, timeZone, encryptedString);
            if (statusCardResponse.getCodigo().equals("00")) {
                CheckStatusCredentialCard checkStatusCredentialCard = new CheckStatusCredentialCard(statusCardResponse.getCodigo(), statusCardResponse.getDescripcion(), statusCardResponse.getTicketWS(), statusCardResponse.getInicio(), statusCardResponse.getFin(), statusCardResponse.getTiempo(), statusCardResponse.getNumero(), statusCardResponse.getCuenta(), statusCardResponse.getCodigoEntidad(), statusCardResponse.getDescripcionEntidad(), statusCardResponse.getSucursal(), statusCardResponse.getCodigoProducto(), statusCardResponse.getDescripcionProducto(), statusCardResponse.getCodigoEstado(), statusCardResponse.getDescripcionEstado(), statusCardResponse.getActual(), statusCardResponse.getAnterior(), statusCardResponse.getDenominacion(), statusCardResponse.getTipo(), statusCardResponse.getIden(), statusCardResponse.getTelefono(), statusCardResponse.getDireccion(), statusCardResponse.getCodigoPostal(), statusCardResponse.getLocalidad(), statusCardResponse.getCodigoPais(), statusCardResponse.getDescripcionPais(), statusCardResponse.getMomentoUltimaActualizacion(), statusCardResponse.getMomentoUltimaOperacionAprobada(), statusCardResponse.getMomentoUltimaOperacionDenegada(), statusCardResponse.getMomentoUltimaBajaBoletin(), statusCardResponse.getContadorPinERR());
                return new CheckStatusCardResponses(checkStatusCredentialCard, ResponseCode.SUCCESS, "");
            } else if (statusCardResponse.getCodigo().equals("-024")) {
                return new CheckStatusCardResponses(ResponseCode.NOT_ALLOWED_TO_CHANGE_STATE, "NOT ALLOWED TO CHANGE STATE");
            } else if (statusCardResponse.getCodigo().equals("-011")) {
                return new CheckStatusCardResponses(ResponseCode.AUTHENTICATE_IMPOSSIBLE, "Authenticate Impossible");
            } else if (statusCardResponse.getCodigo().equals("-13")) {
                return new CheckStatusCardResponses(ResponseCode.SERVICE_NOT_ALLOWED, "Service Not Allowed");
            } else if (statusCardResponse.getCodigo().equals("-14")) {
                return new CheckStatusCardResponses(ResponseCode.OPERATION_NOT_ALLOWED_FOR_THIS_SERVICE, "Operation Not Allowed For This Service");
            } else if (statusCardResponse.getCodigo().equals("-060")) {
                return new CheckStatusCardResponses(ResponseCode.UNABLE_TO_ACCESS_DATA, "Unable to Access Data");
            } else if (statusCardResponse.getCodigo().equals("-120")) {
                return new CheckStatusCardResponses(ResponseCode.THERE_ARE_NO_RECORDS_FOR_THE_REQUESTED_SEARCH, "There are no Records for the Requested Search");
            } else if (statusCardResponse.getCodigo().equals("-140")) {
                return new CheckStatusCardResponses(ResponseCode.THE_REQUESTED_PRODUCT_DOES_NOT_EXIST, "The Requested Product does not Exist");
            } else if (statusCardResponse.getCodigo().equals("-160")) {
                return new CheckStatusCardResponses(ResponseCode.THE_NUMBER_OF_ORDERS_ALLOWED_IS_EXCEEDED, "The Number of Orders Allowed is Exceeded");
            } else if (statusCardResponse.getCodigo().equals("-030")) {
                return new CheckStatusCardResponses(ResponseCode.NON_EXISTENT_ACCOUNT, "Non-existent account");
            }
            return new CheckStatusCardResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new CheckStatusCardResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CheckStatusCardResponses(ResponseCode.INTERNAL_ERROR, "");
        }

    }

    public CheckStatusAccountResponses checkStatusAccount(Long userId, String numberAccount, String timeZone) {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;
        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
        try {
            responseUser = proxy.getUsuarioporId(Constants.ALODIGA_WALLET_USUARIO_API, Constants.ALODIGA_WALLET_PASSWORD_API, String.valueOf(userId));
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            numberAccount = S3cur1ty3Cryt3r.aloEncrpter(numberAccount, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, timeZone, numberAccount);
            accountResponse.setCodigo("00");
            if (accountResponse.getCodigo().equals("00")) {
                CheckStatusCredentialAccount checkStatusCredentialAccount = new CheckStatusCredentialAccount(accountResponse.getCodigo(), accountResponse.getDescripcion(), accountResponse.getNumero(), accountResponse.getCodigoEstado(), accountResponse.getDescripcionEstado(), accountResponse.getCodigoEntidad(), accountResponse.getDescripcionEntidad(), accountResponse.getSucursal(), accountResponse.getCodigoProducto(), accountResponse.getDescripcionProducto(), accountResponse.getCodigoPais(), accountResponse.getDescripcionPais(), accountResponse.getCodigoMoneda(), accountResponse.getDescripcionMoneda(), accountResponse.getVIP(), accountResponse.getHCC(), accountResponse.getULC(), accountResponse.getMCC(), accountResponse.getMomentoRenewal(), accountResponse.getMomentoUltimaActualizacion(), accountResponse.getMomentoUltimaOperacionAprobada(), accountResponse.getMomentoUltimaOperacionDenegada(), accountResponse.getMomentoUltimoBloqueo(), accountResponse.getMomentoUltimoDesbloqueo(), accountResponse.getComprasDisponibles(), accountResponse.getCuotasDisponibles(), accountResponse.getAdelantosDisponibles(), accountResponse.getPrestamosDisponibles(), accountResponse.getComprasLimites(), accountResponse.getCuotasLimites(), accountResponse.getAdelantosLimites(), accountResponse.getPrestamosLimites(), accountResponse.getFechaVencimiento(), accountResponse.getSaldo(), accountResponse.getPagoMinimo(), accountResponse.getSaldoDolar());
                return new CheckStatusAccountResponses(checkStatusCredentialAccount, ResponseCode.SUCCESS, "");
            } else if (accountResponse.getCodigo().equals("-030")) {
                return new CheckStatusAccountResponses(ResponseCode.NON_EXISTENT_ACCOUNT, "Non-existent account");
            } else if (accountResponse.getCodigo().equals("-024")) {
                return new CheckStatusAccountResponses(ResponseCode.NOT_ALLOWED_TO_CHANGE_STATE, "NOT ALLOWED TO CHANGE STATE");
            } else if (accountResponse.getCodigo().equals("-011")) {
                return new CheckStatusAccountResponses(ResponseCode.AUTHENTICATE_IMPOSSIBLE, "Authenticate Impossible");
            } else if (accountResponse.getCodigo().equals("-13")) {
                return new CheckStatusAccountResponses(ResponseCode.SERVICE_NOT_ALLOWED, "Service Not Allowed");
            } else if (accountResponse.getCodigo().equals("-14")) {
                return new CheckStatusAccountResponses(ResponseCode.OPERATION_NOT_ALLOWED_FOR_THIS_SERVICE, "Operation Not Allowed For This Service");
            } else if (accountResponse.getCodigo().equals("-060")) {
                return new CheckStatusAccountResponses(ResponseCode.UNABLE_TO_ACCESS_DATA, "Unable to Access Data");
            } else if (accountResponse.getCodigo().equals("-120")) {
                return new CheckStatusAccountResponses(ResponseCode.THERE_ARE_NO_RECORDS_FOR_THE_REQUESTED_SEARCH, "There are no Records for the Requested Search");
            } else if (accountResponse.getCodigo().equals("-140")) {
                return new CheckStatusAccountResponses(ResponseCode.THE_REQUESTED_PRODUCT_DOES_NOT_EXIST, "The Requested Product does not Exist");
            } else if (accountResponse.getCodigo().equals("-160")) {
                return new CheckStatusAccountResponses(ResponseCode.THE_NUMBER_OF_ORDERS_ALLOWED_IS_EXCEEDED, "The Number of Orders Allowed is Exceeded");
            }
            return new CheckStatusAccountResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new CheckStatusAccountResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CheckStatusAccountResponses(ResponseCode.INTERNAL_ERROR, "");
        }

    }

    public TransferCardToCardResponses transferCardToCardAutorization(Long userId, String numberCardOrigin, String numberCardDestinate, String balance, Long idUserDestination, String conceptTransaction) {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        System.out.println("date1" + new Date().getTime());
        AutorizationCredentialServiceClient autorizationCredentialServiceClient = new AutorizationCredentialServiceClient();
        System.out.println("date2" + new Date().getTime());
        ArrayList<Product> products = new ArrayList<Product>();
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        System.out.println("date3" + new Date().getTime());
        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Transaction transfer = new Transaction();
        System.out.println("date4" + new Date().getTime());

        try {
            System.out.println("date5" + new Date().getTime());
            RespuestaUsuario responseUser = proxy.getUsuarioporId(Constants.ALODIGA_WALLET_USUARIO_API, Constants.ALODIGA_WALLET_PASSWORD_API, String.valueOf(userId));
            System.out.println("date6" + new Date().getTime());
            RespuestaUsuario userDestination = proxy.getUsuarioporId("usuarioWS", "passwordWS", idUserDestination.toString());
            userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
            System.out.println("date7" + new Date().getTime());
            ignoreSSLAutorization();
            System.out.println("date8" + new Date().getTime());
            numberCardOrigin = S3cur1ty3Cryt3r.aloEncrpter(numberCardOrigin, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            numberCardDestinate = S3cur1ty3Cryt3r.aloEncrpter(numberCardDestinate, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            SimpleDateFormat sdg = new SimpleDateFormat("yyyyMMdd");
            String hour = sdf.format(timestamp);
            System.out.println(hour);
            String date = sdg.format(timestamp);
            System.out.println(date);
            System.out.println("date9" + new Date().getTime());
            CardToCardTransferResponse cardToCardTransferResponse = new CardToCardTransferResponse();
            //  CardToCardTransferResponse cardToCardTransferResponse = autorizationCredentialServiceClient.cardToCardTransfer(date, hour, numberCardOrigin, numberCardDestinate, balance);
            ////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////
            //CABLE
            /////////////////////////////////
            /////////////////////////////////
            cardToCardTransferResponse.setCodigoError("-1");
            cardToCardTransferResponse.setMensajeError("APROVADO");
            cardToCardTransferResponse.setCodigoRespuesta("-1");
            cardToCardTransferResponse.setMensajeRespuesta("APROVADO");
            cardToCardTransferResponse.setCodigoAutorizacion("-1");
            cardToCardTransferResponse.setSaldoPosterior("2000");
            cardToCardTransferResponse.setSaldo("3000");
            cardToCardTransferResponse.setSaldoPosteriorCuentaDestino("1000");
            cardToCardTransferResponse.setSaldoCuentaDestino("2000");

            ////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////
            //CABLE
            /////////////////////////////////
            /////////////////////////////////
            if (cardToCardTransferResponse.getCodigoError().equals("-1")) {
                TransferCardToCardCredential cardCredential = new TransferCardToCardCredential(cardToCardTransferResponse.getCodigoError(), cardToCardTransferResponse.getMensajeError(), cardToCardTransferResponse.getCodigoRespuesta(), cardToCardTransferResponse.getMensajeRespuesta(), cardToCardTransferResponse.getCodigoAutorizacion(), cardToCardTransferResponse.getSaldoPosterior(), cardToCardTransferResponse.getSaldo(), cardToCardTransferResponse.getSaldoPosteriorCuentaDestino(), cardToCardTransferResponse.getSaldoCuentaDestino());
                /////cable
                //cardCredential.setRearBalanceAccountDestination("2000");
                //cardCredential.setDestinationAccountBalance("3000");
                ////
                transfer.setId(null);
                transfer.setUserSourceId(BigInteger.valueOf(userId));
                transfer.setUserDestinationId(BigInteger.valueOf(idUserDestination));
                Product product = entityManager.find(Product.class, 3L);
                transfer.setProductId(product);
                System.out.println("date10" + new Date().getTime());
                TransactionType transactionType = entityManager.find(TransactionType.class, Constants.TRANSFER_CARD_TO_CARD);
                transfer.setTransactionTypeId(transactionType);
                TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constants.TRANSFER_CARD_TO_CARD_SOURCE);
                transfer.setTransactionSourceId(transactionSource);
                Date date_ = new Date();
                Timestamp creationDate = new Timestamp(date_.getTime());
                transfer.setCreationDate(creationDate);

                System.out.println("date11" + new Date().getTime());

                transfer.setConcept(Constants.TRANSACTION_CONCEPT_TRANSFER_CARD_TO_CARD);
                transfer.setAmount(Float.valueOf(balance));
                transfer.setTransactionStatus(TransactionStatus.COMPLETED.name());
                transfer.setTotalAmount(Float.valueOf(balance));
                entityManager.persist(transfer);
                System.out.println("date12" + new Date().getTime());
                BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, 3L);
                BalanceHistory balanceHistory = new BalanceHistory();
                balanceHistory.setId(null);
                balanceHistory.setUserId(userId);
                if (balanceUserSource == null) {
                    balanceHistory.setOldAmount(Float.valueOf(cardCredential.getRearBalanceAccountDestination()));
                    balanceHistory.setCurrentAmount(Float.valueOf(cardCredential.getDestinationAccountBalance()));
                } else {
                    balanceHistory.setOldAmount(Float.valueOf(cardCredential.getRearBalanceAccountDestination()));
                    balanceHistory.setCurrentAmount(Float.valueOf(cardCredential.getDestinationAccountBalance()));
                    balanceHistory.setVersion(balanceUserSource.getId());
                }
                balanceHistory.setProductId(product);
                balanceHistory.setTransactionId(transfer);
                Date balanceDate = new Date();
                Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
                balanceHistory.setDate(balanceHistoryDate);
                entityManager.persist(balanceHistory);
                System.out.println("date13" + new Date().getTime());
                BalanceHistory balanceUserDestination = loadLastBalanceHistoryByAccount(idUserDestination, 3L);
                balanceHistory = new BalanceHistory();
                balanceHistory.setId(null);
                balanceHistory.setUserId(idUserDestination);
                if (balanceUserDestination == null) {
                    balanceHistory.setOldAmount(Float.valueOf(cardCredential.getRearBalanceAccountDestination()));
                    balanceHistory.setCurrentAmount(Float.valueOf(cardCredential.getDestinationAccountBalance()));
                } else {
                    balanceHistory.setOldAmount(Float.valueOf(cardCredential.getRearBalanceAccountDestination()));
                    balanceHistory.setCurrentAmount(Float.valueOf(cardCredential.getDestinationAccountBalance()));
                    balanceHistory.setVersion(balanceUserDestination.getId());
                }
                balanceHistory.setProductId(product);
                balanceHistory.setTransactionId(transfer);
                balanceDate = new Date();
                balanceHistoryDate = new Timestamp(balanceDate.getTime());
                balanceHistory.setDate(balanceHistoryDate);
                entityManager.persist(balanceHistory);
                System.out.println("date14" + new Date().getTime());
                try {
                    products = getProductsListByUserId(userId);
                    for (Product p : products) {
                        Float amount_1 = 0F;
                        try {
                            if (p.getId().equals(Product.PREPAID_CARD)) {
                                CardResponse cardResponse = getCardByUserId(userId);
                                String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                                StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                                if (statusCardResponse.getCodigo().equals("00")) {
                                    StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                    amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                                } else {
                                    amount_1 = Float.valueOf(0);
                                }
                            } else {
                                amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                            }

                        } catch (NoResultException e) {
                            amount_1 = 0F;
                        } catch (ConnectException e) {
                            e.printStackTrace();
                            amount_1 = 0F;
                        } catch (SocketTimeoutException e) {
                            e.printStackTrace();
                            amount_1 = 0F;
                        }
                        p.setCurrentBalance(amount_1);
                    }
                } catch (Exception ex) {

                    return new TransferCardToCardResponses(ResponseCode.INTERNAL_ERROR, "Error loading products");
                }
                System.out.println("date16" + new Date().getTime());
                SendMailTherad sendMailTherad = new SendMailTherad("ES", Float.valueOf(balance), conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), responseUser.getDatosRespuesta().getEmail(), Integer.valueOf("11"));
                sendMailTherad.run();
                System.out.println("date17" + new Date().getTime());
                SendMailTherad sendMailTherad1 = new SendMailTherad("ES", Float.valueOf(balance), conceptTransaction, userDestination.getDatosRespuesta().getNombre() + " " + userDestination.getDatosRespuesta().getApellido(), userDestination.getDatosRespuesta().getEmail(), Integer.valueOf("12"));
                sendMailTherad1.run();
                System.out.println("date18" + new Date().getTime());
                SendSmsThread sendSmsThread = new SendSmsThread(responseUser.getDatosRespuesta().getMovil(), Float.valueOf(balance), Integer.valueOf("30"), userId, entityManager);
                sendSmsThread.run();
                System.out.println("date19" + new Date().getTime());
                //SendSmsThread sendSmsThread1 = new SendSmsThread(userDestination.getDatosRespuesta().getMovil(), Float.valueOf(balance), Integer.valueOf("31"), Long.valueOf(userDestination.getDatosRespuesta().getUsuarioID()), entityManager);
//                sendSmsThread1.run();

                System.out.println("date15" + new Date().getTime());
                TransferCardToCardResponses cardResponses = new TransferCardToCardResponses(cardCredential, ResponseCode.SUCCESS, "", products);
                cardResponses.setIdTransaction(transfer.getId().toString());
                cardResponses.setProducts(products);
                return cardResponses;
            } else if (cardToCardTransferResponse.getCodigoError().equals("204")) {
                return new TransferCardToCardResponses(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("913")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_AMOUNT, "INVALID AMOUNT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("203")) {
                return new TransferCardToCardResponses(ResponseCode.EXPIRATION_DATE_DIFFERS, "EXPIRATION DATE DIFFERS");
            } else if (cardToCardTransferResponse.getCodigoError().equals("205")) {
                return new TransferCardToCardResponses(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("202")) {
                return new TransferCardToCardResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("201")) {
                return new TransferCardToCardResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("03")) {
                return new TransferCardToCardResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("28")) {
                return new TransferCardToCardResponses(ResponseCode.LOCKED_CARD, "LOCKED_CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("211")) {
                return new TransferCardToCardResponses(ResponseCode.BLOCKED_ACCOUNT, "BLOCKED ACCOUNT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("210")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_ACCOUNT, "INVALID ACCOUNT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("998")) {
                return new TransferCardToCardResponses(ResponseCode.INSUFFICIENT_BALANCE, "INSUFFICIENT BALANCE");
            } else if (cardToCardTransferResponse.getCodigoError().equals("986")) {
                return new TransferCardToCardResponses(ResponseCode.INSUFFICIENT_LIMIT, "INSUFFICIENT LIMIT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("987")) {
                return new TransferCardToCardResponses(ResponseCode.CREDIT_LIMIT_0, "CREDIT LIMIT 0");
            } else if (cardToCardTransferResponse.getCodigoError().equals("988")) {
                return new TransferCardToCardResponses(ResponseCode.CREDIT_LIMIT_0_OF_THE_DESTINATION_ACCOUNT, "CREDIT LIMIT 0 OF THE DESTINATION ACCOUNT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("999")) {
                return new TransferCardToCardResponses(ResponseCode.ERROR_PROCESSING_THE_TRANSACTION, "ERROR PROCESSING THE TRANSACTION");
            } else if (cardToCardTransferResponse.getCodigoError().equals("101")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_TRANSACTION, "INVALID TRANSACTION");
            } else if (cardToCardTransferResponse.getCodigoError().equals("105")) {
                return new TransferCardToCardResponses(ResponseCode.ERROR_VALIDATION_THE_TERMINAL, "ERROR VALIDATION THE TERMINAL");
            } else if (cardToCardTransferResponse.getCodigoError().equals("241")) {
                return new TransferCardToCardResponses(ResponseCode.DESTINATION_ACCOUNT_LOCKED, "DESTINATION ACCOUNT LOCKED");
            } else if (cardToCardTransferResponse.getCodigoError().equals("230")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_DESTINATION_CARD, "INVALID DESTINATION CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("240")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_DESTINATION_ACCOUNT, "INVALID DESTINATION ACCOUNT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("301")) {
                return new TransferCardToCardResponses(ResponseCode.THE_AMOUNT_MUST_BE_POSITIVE_AND_THE_AMOUNT_IS_REPORTED, "THE AMOUNT MUST BE POSITIVE AND THE AMOUNT IS REPORTED");
            } else if (cardToCardTransferResponse.getCodigoError().equals("302")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_TRANSACTION_DATE, "INVALID TRANSACTION DATE");
            } else if (cardToCardTransferResponse.getCodigoError().equals("303")) {
                return new TransferCardToCardResponses(ResponseCode.INVALID_TRANSACTION_TIME, "INVALID TRANSACTION TIME");
            } else if (cardToCardTransferResponse.getCodigoError().equals("994")) {
                return new TransferCardToCardResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NN");
            } else if (cardToCardTransferResponse.getCodigoError().equals("991")) {
                return new TransferCardToCardResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_SN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION SN");
            } else if (cardToCardTransferResponse.getCodigoError().equals("992")) {
                return new TransferCardToCardResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
            } else if (cardToCardTransferResponse.getCodigoError().equals("993")) {
                return new TransferCardToCardResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
            } else if (cardToCardTransferResponse.getCodigoError().equals("990")) {
                return new TransferCardToCardResponses(ResponseCode.TRASACTION_BETWEEN_ACCOUNTS_NOT_ALLOWED, "TRASACTION BETWEEN ACCOUNTS NOT ALLOWED");
            } else if (cardToCardTransferResponse.getCodigoError().equals("120")) {
                return new TransferCardToCardResponses(ResponseCode.TRADE_VALIDATON_ERROR, "TRADE VALIDATON ERROR");
            } else if (cardToCardTransferResponse.getCodigoError().equals("110")) {
                return new TransferCardToCardResponses(ResponseCode.DESTINATION_CARD_DOES_NOT_SUPPORT_TRANSACTION, "DESTINATION CARD DOES NOT SUPPORT TRANSACTION");
            } else if (cardToCardTransferResponse.getCodigoError().equals("111")) {
                return new TransferCardToCardResponses(ResponseCode.OPERATION_NOT_ENABLED_FOR_THE_DESTINATION_CARD, "OPERATION NOT ENABLED FOR THE DESTINATION CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("206")) {
                return new TransferCardToCardResponses(ResponseCode.BIN_NOT_ALLOWED, "BIN NOT ALLOWED");
            } else if (cardToCardTransferResponse.getCodigoError().equals("207")) {
                return new TransferCardToCardResponses(ResponseCode.STOCK_CARD, "STOCK CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("205")) {
                return new TransferCardToCardResponses(ResponseCode.THE_ACCOUNT_EXCEEDS_THE_MONTHLY_LIMIT, "THE ACCOUNT EXCEEDS THE MONTHLY LIMIT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("101")) {
                return new TransferCardToCardResponses(ResponseCode.THE_PAN_FIELD_IS_MANDATORY, "THE PAN FIELD IS MANDATORY");
            } else if (cardToCardTransferResponse.getCodigoError().equals("102")) {
                return new TransferCardToCardResponses(ResponseCode.THE_AMOUNT_TO_BE_RECHARGE_IS_INCORRECT, "THE AMOUNT TO BE RECHARGE IS INCORRECT");
            } else if (cardToCardTransferResponse.getCodigoError().equals("3")) {
                return new TransferCardToCardResponses(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("8")) {
                return new TransferCardToCardResponses(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
            } else if (cardToCardTransferResponse.getCodigoError().equals("33")) {
                return new TransferCardToCardResponses(ResponseCode.THE_AMOUNT_MUST_BE_GREATER_THAN_0, "THE AMOUNT MUST BE GREATER THAN 0");
            } else if (cardToCardTransferResponse.getCodigoError().equals("1")) {
                return new TransferCardToCardResponses(ResponseCode.SUCCESSFUL_RECHARGE, "SUCCESSFUL RECHARGE");
            } else if (cardToCardTransferResponse.getCodigoError().equals("410")) {
                return new TransferCardToCardResponses(ResponseCode.ERROR_VALIDATING_PIN, "THE PAN FIELD IS MANDATORY");
            } else if (cardToCardTransferResponse.getCodigoError().equals("430")) {
                return new TransferCardToCardResponses(ResponseCode.ERROR_VALIDATING_CVC1, "ERROR VALIDATING CVC1");
            } else if (cardToCardTransferResponse.getCodigoError().equals("400")) {
                return new TransferCardToCardResponses(ResponseCode.ERROR_VALIDATING_CVC2, "ERROR VALIDATING CVC2");
            } else if (cardToCardTransferResponse.getCodigoError().equals("420")) {
                return new TransferCardToCardResponses(ResponseCode.PIN_CHANGE_ERROR, "PIN CHANGE ERROR");
            } else if (cardToCardTransferResponse.getCodigoError().equals("250")) {
                return new TransferCardToCardResponses(ResponseCode.ERROR_VALIDATING_THE_ITEM, " ERROR VALIDATING THE ITEM");
            }
            return new TransferCardToCardResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new TransferCardToCardResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new TransferCardToCardResponses(ResponseCode.INTERNAL_ERROR, "");
        }

    }

    private boolean isCardUnique(String card) {
        try {
            entityManager
                    .createNamedQuery("Card.findByNumberCard", Card.class)
                    .setParameter("numberCard", card).getSingleResult();
        } catch (NoResultException e) {
            return true;
        }
        return false;
    }

    private void ignoreSSL() {
        try {
            XTrustProvider.install();
            final String TEST_URL = "https://10.70.10.85:8000/CASA_SRTMX_TarjetaService?wsdl";
            URL url = new URL(TEST_URL);
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
            httpsCon.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            httpsCon.connect();
            InputStream is = httpsCon.getInputStream();
            int nread = 0;
            byte[] buf = new byte[8192];
            while ((nread = is.read(buf)) != -1) {
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    private void ignoreSSLAutorization() {
        try {
            XTrustProvider.install();
            final String TEST_URL = "https://10.70.10.85:8000/Autorizacion?wsdl";
            URL url = new URL(TEST_URL);
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
            httpsCon.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            httpsCon.connect();
            InputStream is = httpsCon.getInputStream();
            int nread = 0;
            byte[] buf = new byte[8192];
            while ((nread = is.read(buf)) != -1) {
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    public CardResponse getCardByUserId(Long userId) {
        UserHasCard userHasCard = new UserHasCard();
        try {
            userHasCard = (UserHasCard) entityManager.createNamedQuery("UserHasCard.findByUserId", UserHasCard.class).setParameter("userId", userId).getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            return new CardResponse(ResponseCode.EMPTY_LIST_HAS_CARD, "Error loading cards");
        } catch (Exception e) {
            e.printStackTrace();
            return new CardResponse(ResponseCode.INTERNAL_ERROR, "Error loading cards");
        }
        return new CardResponse(ResponseCode.SUCCESS, "", userHasCard.getCardId().getNumberCard());
    }

    public CardListResponse getCardsListByUserId(Long userId) throws NoResultException, Exception {
        List<UserHasCard> userHasCards = new ArrayList<UserHasCard>();
        Card card = new Card();
        List<Card> cards = new ArrayList<Card>();
        try {
            userHasCards = (List<UserHasCard>) entityManager.createNamedQuery("UserHasCard.findByUserIdAndParentId", UserHasCard.class).setParameter("userId", userId).getResultList();

            if (userHasCards.size() <= 0) {
                return new CardListResponse(ResponseCode.USER_NOT_HAS_CARD, "They are not cards asociated");
            }

            for (UserHasCard uhc : userHasCards) {

                cards = (List<Card>) entityManager.createNamedQuery("Card.findByParentId", Card.class).setParameter("parentId", uhc.getCardId().getId()).getResultList();

                if (cards.size() <= 0) {
                    return new CardListResponse(ResponseCode.DOES_NOT_HAVE_AN_ASSOCIATED_COMPANION_CARD, "Does not have an associated companion card");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CardListResponse(ResponseCode.INTERNAL_ERROR, "Error loading cards");
        }
        return new CardListResponse(ResponseCode.SUCCESS, "", cards);
    }

    public ProductListResponse getProductsRemettenceByUserId(Long userId) {
        List<UserHasProduct> userHasProducts = new ArrayList<UserHasProduct>();
        List<Product> products = new ArrayList<Product>();
        List<Product> productFinals = new ArrayList<Product>();
        try {
            products = getProductsListByUserId(userId);
            for (Product p : products) {
                if (p.isIsRemettence()) {
                    Float amount = 0F;
                    try {
                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                    } catch (NoResultException e) {
                        amount = 0F;
                    }
                    p.setCurrentBalance(amount);
                    productFinals.add(p);
                }
            }
            if (productFinals.size() <= 0) {
                return new ProductListResponse(ResponseCode.USER_NOT_HAS_PRODUCT, "They are not products asociated");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ProductListResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
        }

        return new ProductListResponse(ResponseCode.SUCCESS, "", productFinals);
    }

    public RemittanceResponse processRemettenceAccount(Long userId,
            Float amountOrigin,
            Float totalAmount,
            Float amountDestiny,
            String exchangeRateId,
            String ratePaymentNetworkId,
            String originCurrentId,
            String destinyCurrentId,
            String paymentNetworkId,
            String deliveryFormId,
            Long addressId,
            String remittentCountryId,
            String remittentStateName,
            String remittentCityName,
            String remittentAddress,
            String remittentZipCode,
            String remittentStateId,
            String remittentCityId,
            String receiverFirstName,
            String receiverMiddleName,
            String receiverLastName,
            String receiverSecondSurname,
            String receiverPhoneNumber,
            String receiverEmail,
            String receiverCountryId,
            String receiverCityId,
            String receiverStateId,
            String receiverStateName,
            String receiverCityName,
            String receiverAddress,
            String receiverZipCode,
            String languageId) {
        try {
            SimpleDateFormat sdg = new SimpleDateFormat("yyyy-MM-dd");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String applicationDate = sdg.format(timestamp);
            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario userSource;
            List<Commission> commissions = new ArrayList<Commission>();
            List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
            List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
            Commission commissionTransfer = new Commission();
            Float amountCommission = 0.00F;
            short isPercentCommission = 0;

            int totalTransactionsByUserDaily = 0;
            int totalTransactionsByUserMonthly = 0;
            int totalTransactionsByUserYearly = 0;
            Double totalAmountByUserDaily = 0.00D;
            Double totalAmountByUserMonthly = 0.00D;
            Double totalAmountByUserYearly = 0.00D;

            Long idTransaction = 0L;
            Transaction transfer = new Transaction();
            String remittentCountryId_ = null;
            String remittentCityId_ = null;
            String remittentStateId_ = null;
            String remittentStateName_ = null;
            String remittentCityName_ = null;
            String remittentAddress_ = null;
            String remittentZipCode_ = null;

            userSource = proxy.getUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId));
            String middleName = userSource.getDatosRespuesta().getNombre().split(" ")[0].trim();
            String secondSurname = userSource.getDatosRespuesta().getApellido().split(" ")[0].trim();

            BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, Constants.PRODUCT_REMITTANCE);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", Constants.PRODUCT_REMITTANCE).setParameter("transactionTypeId", Constante.sTransationTypeTA).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + Constants.PRODUCT_REMITTANCE + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionTransfer = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (totalAmount * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new RemittanceResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
            }
            Float amountTransferTotal = totalAmount + amountCommission;
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountTransferTotal) {
                return new RemittanceResponse(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
            }

            //Validar preferencias
            totalTransactionsByUserDaily = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserDaily = AmountMaxByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByUserMonthly = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserMonthly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByUserYearly = TransactionsByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserYearly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction11:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (pv.getValue().equals("0")) {
                                    return new RemittanceResponse(ResponseCode.DISABLED_TRANSACTION, "Transactions disabled");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction4:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmount >= Double.parseDouble(pv.getValue())) {
                                    return new RemittanceResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per transaction");
                                }
                            }
                        }
                        break;

                    case Constante.sValidatePreferenceTransaction5:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserDaily >= Integer.parseInt(pv.getValue())) {
                                    return new RemittanceResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction6:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserDaily >= Double.parseDouble(pv.getValue())) {
                                    return new RemittanceResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_DIALY, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction7:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserMonthly >= Integer.parseInt(pv.getValue())) {
                                    return new RemittanceResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction8:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserMonthly >= Double.parseDouble(pv.getValue())) {
                                    return new RemittanceResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_MONTHLY, "The user exceeded the maximum amount per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction9:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserYearly >= Integer.parseInt(pv.getValue())) {
                                    return new RemittanceResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_YEARLY, "The user exceeded the maximum number of transactions per year");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction10:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {

                                if (totalAmountByUserYearly >= Double.parseDouble(pv.getValue())) {
                                    return new RemittanceResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_YEARLY, "The user exceeded the maximum amount per year");
                                }
                            }
                        }
                        break;
                }
            }

            //Crear el objeto Transaction para registrar la transferencia del cliente
            transfer.setId(null);
            transfer.setUserSourceId(BigInteger.valueOf(userSource.getDatosRespuesta().getUsuarioID()));
            transfer.setUserDestinationId(null);
            Product product = entityManager.find(Product.class, Constants.PRODUCT_REMITTANCE);
            transfer.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeTR);
            transfer.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            transfer.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            transfer.setCreationDate(creationDate);
            //cambiar por valor de parÃ¡metro
            transfer.setConcept(Constante.sTransactionConceptTranferRemmittance);
            transfer.setAmount(totalAmount);
            transfer.setTransactionStatus(TransactionStatus.CREATED.name());
            transfer.setTotalAmount(totalAmount);
            transfer.setTotalTax(null);
            transfer.setPromotionAmount(null);
            transfer.setTotalAlopointsUsed(null);
            transfer.setTopUpDescription(null);
            transfer.setBillPaymentDescription(null);
            transfer.setExternalId(null);
            transfer.setTransactionNumber("1");
            entityManager.flush();
            entityManager.persist(transfer);

            //Se crea el objeto commissionItem y se persiste en BD
            CommissionItem commissionItem = new CommissionItem();
            commissionItem.setCommissionId(commissionTransfer);
            commissionItem.setAmount(amountCommission);
            Date commissionDate = new Date();
            Timestamp processedDate = new Timestamp(commissionDate.getTime());
            commissionItem.setProcessedDate(processedDate);
            commissionItem.setTransactionId(transfer);

            entityManager.persist(commissionItem);

            //Se actualiza el estatus de la transaccion a IN_PROCESS
            transfer.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(transfer);

            //Se actualizan los saldos de los usuarios involucrados en la transferencia
            //Balance History del usuario que transfiere el saldo
            balanceUserSource = loadLastBalanceHistoryByAccount(userId, Constants.PRODUCT_REMITTANCE);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(userId);
            balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
            Float currentAmountUserSource = balanceUserSource.getCurrentAmount() - amountTransferTotal;
            balanceHistory.setCurrentAmount(currentAmountUserSource);
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(transfer);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceUserSource.getId());
            entityManager.persist(balanceHistory);

            //Se actualiza el estado de la transaccion a COMPLETED
            transfer.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(transfer);

            WSRemittenceMobileProxy wSRemittenceMobileProxy = new WSRemittenceMobileProxy();
            WsAddressListResponse addressListResponse = new WsAddressListResponse();
            WsRemittenceResponse response = new WsRemittenceResponse();
            if (addressId != 0) {
                addressListResponse = wSRemittenceMobileProxy.getAddressById(addressId);
                remittentCountryId_ = String.valueOf(addressListResponse.getAddresses(0).getCountry().getId());
                remittentCityId_ = String.valueOf(addressListResponse.getAddresses(0).getCity().getId());
                remittentStateId_ = String.valueOf(addressListResponse.getAddresses(0).getState().getId());
                remittentStateName_ = addressListResponse.getAddresses(0).getStateName();
                remittentCityName_ = addressListResponse.getAddresses(0).getCityName();
                remittentAddress_ = addressListResponse.getAddresses(0).getAddress();
                remittentZipCode_ = addressListResponse.getAddresses(0).getZipCode();
            } else {
                remittentCountryId_ = remittentCountryId;
                if (remittentCityId != null) {
                    remittentCityId_ = remittentCityId;
                } else {
                    remittentCityId_ = null;
                }

                if (remittentStateId_ != null) {
                    remittentStateId_ = remittentStateId;
                } else {
                    remittentStateId_ = null;
                }

                if (remittentStateName_ != null) {
                    remittentStateName_ = remittentStateName;
                } else {
                    remittentStateName_ = null;
                }

                if (remittentCityName_ != null) {
                    remittentCityName_ = remittentCityName;
                } else {
                    remittentCityName_ = null;
                }

                remittentAddress_ = addressListResponse.getAddresses(0).getAddress();
                remittentZipCode_ = addressListResponse.getAddresses(0).getZipCode();
            }
            response = wSRemittenceMobileProxy.saverRemittence(applicationDate,
                    Constants.COMMENTARY_REMETTENCE,
                    amountOrigin,
                    totalAmount,
                    Constants.SENDING_OPTION_SMS_REMETTENCE,
                    amountDestiny,
                    Constants.BANK_REMETTENCE,
                    Constants.PAYMENT_SERVICE_REMETTENCE,
                    Constants.ADDITIONAL_CHANGES_REMITTANCE,
                    Constants.CORRESPONDENT_REMITTANCE,
                    Constants.SALES_TYPE_REMETTENCE,
                    exchangeRateId,
                    ratePaymentNetworkId,
                    Constants.SALES_PRICE_REMITTANCE,
                    languageId,
                    originCurrentId,
                    destinyCurrentId,
                    Constants.STORE_REMETTENCE,
                    Constants.PAYMENT_METHOD_REMITTANCE,
                    Constants.SERVICE_TYPE_REMITTANCE,
                    paymentNetworkId,
                    Constants.POINT_REMITTANCE,
                    Constants.USER_REMITTANCE,
                    Constants.CASH_BOX_REMITTANCE,
                    deliveryFormId,
                    userSource.getDatosRespuesta().getNombre(),
                    middleName,
                    userSource.getDatosRespuesta().getApellido(),
                    secondSurname,
                    userSource.getDatosRespuesta().getMovil(),
                    userSource.getDatosRespuesta().getEmail(),
                    remittentCountryId_,
                    remittentCityId_,
                    remittentStateId_,
                    remittentStateName_,
                    remittentCityName_,
                    remittentAddress_,
                    remittentZipCode_,
                    receiverFirstName,
                    receiverMiddleName,
                    receiverLastName,
                    receiverSecondSurname,
                    receiverPhoneNumber,
                    receiverEmail,
                    receiverCountryId,
                    receiverCityId,
                    receiverStateId,
                    receiverStateName,
                    receiverCityName,
                    receiverAddress,
                    receiverZipCode);
            if (addressId == 0) {
                //proxy.actualizarUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId), response.getRemittanceSingleResponse().getAddressId());
            }
            RemittanceResponse remittanceResponse = new RemittanceResponse(response.getRemittanceSingleResponse().getId(), response.getRemittanceSingleResponse().getApplicationDate(), response.getRemittanceSingleResponse().getCommentary(), response.getRemittanceSingleResponse().getAmountOrigin(), response.getRemittanceSingleResponse().getTotalAmount(), response.getRemittanceSingleResponse().getSendingOptionSMS(), response.getRemittanceSingleResponse().getAmountDestiny(), response.getRemittanceSingleResponse().getBank(), response.getRemittanceSingleResponse().getPaymentServiceId(), response.getRemittanceSingleResponse().getSecondaryKey(), response.getRemittanceSingleResponse().getAdditionalChanges(), response.getRemittanceSingleResponse().getCreationDate(), response.getRemittanceSingleResponse().getCreationHour(), response.getRemittanceSingleResponse().getLocalSales(), response.getRemittanceSingleResponse().getReserveField1(), response.getRemittanceSingleResponse().getRemittent(), response.getRemittanceSingleResponse().getReceiver(), response.getRemittanceSingleResponse().getCorrespondent(), response.getRemittanceSingleResponse().getAddressReciever(), response.getRemittanceSingleResponse().getSalesType(), response.getRemittanceSingleResponse().getAddressRemittent(), response.getRemittanceSingleResponse().getExchangeRate(), response.getRemittanceSingleResponse().getRatePaymentNetwork(), response.getRemittanceSingleResponse().getLanguage(), response.getRemittanceSingleResponse().getOriginCurrent(), response.getRemittanceSingleResponse().getDestinyCurrent(), response.getRemittanceSingleResponse().getPaymentMethod(), response.getRemittanceSingleResponse().getServiceType(), response.getRemittanceSingleResponse().getPaymentNetwork(), response.getRemittanceSingleResponse().getPaymentNetworkPoint(), response.getRemittanceSingleResponse().getCashBox(), response.getRemittanceSingleResponse().getCashier(), response.getRemittanceSingleResponse().getStatus(), response.getRemittanceSingleResponse().getRemittanceNumber(), response.getRemittanceSingleResponse().getPaymentKey(), response.getRemittanceSingleResponse().getCorrelative(), response.getRemittanceSingleResponse().getDeliveryForm(), ResponseCode.SUCCESS, "");
            remittanceResponse.setAmountTransferTotal(String.valueOf(amountTransferTotal));
            return remittanceResponse;

        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new RemittanceResponse(ResponseCode.INTERNAL_ERROR, "");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return new RemittanceResponse(ResponseCode.INTERNAL_ERROR, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new RemittanceResponse(ResponseCode.INTERNAL_ERROR, "");
        }

    }

    public RechargeAfinitasResponses saveRechargeAfinitas(Long userId, Float amountRecharge, String currency, String cardNumber, String expirationYear, String expirationMonth, String cvv, String cardHolderName, Long paymentInfoId) {

        Long idTransaction = 0L;
        Long idPreferenceField = 0L;
        int totalTransactionsByUserDaily = 0;
        int totalTransactionsByUserMonthly = 0;
        int totalTransactionsByUserYearly = 0;
        Double totalAmountByUserDaily = 0.00D;
        Double totalAmountByUserMonthly = 0.00D;
        Double totalAmountByUserYearly = 0.00D;
        List<Transaction> transactionsByUser = new ArrayList<Transaction>();
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Timestamp begginingDateTime = new Timestamp(0);
        Timestamp endingDateTime = new Timestamp(0);
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionRecharge = new Commission();
        ArrayList<Product> products = new ArrayList<Product>();
        Transaction transaction = new Transaction();
        AfinitasPaymentIntegration afinitasPaymentIntegration = new AfinitasPaymentIntegration();
        ChargeResponse chargeResponse = new ChargeResponse();
        String paymentInfoCVV = null;
        String paymenInfoCardNumber = null;
        String paymenInfoCardName = null;
        String paymentInfoDateYear = null;
        String paymentInfoDateMonth = null;
        try {

            //Se obtiene el usuario de la API de Registro Unificado
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario userSource_ = proxy.getUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId));

            //Validar preferencias
            totalTransactionsByUserDaily = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserDaily = AmountMaxByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByUserMonthly = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserMonthly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByUserYearly = TransactionsByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByUserYearly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction11:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (pv.getValue().equals("0")) {
                                    return new RechargeAfinitasResponses(ResponseCode.DISABLED_TRANSACTION, "Transactions disabled");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction4:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (amountRecharge >= Double.parseDouble(pv.getValue())) {
                                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per transaction");
                                }
                            }
                        }
                        break;

                    case Constante.sValidatePreferenceTransaction5:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserDaily >= Integer.parseInt(pv.getValue())) {
                                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction6:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserDaily >= Double.parseDouble(pv.getValue())) {
                                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT_DIALY, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction7:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserMonthly >= Integer.parseInt(pv.getValue())) {
                                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction8:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByUserMonthly >= Double.parseDouble(pv.getValue())) {
                                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT_MONTHLY, "The user exceeded the maximum amount per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction9:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByUserYearly >= Integer.parseInt(pv.getValue())) {
                                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_QUANTITY_LIMIT_YEARLY, "The user exceeded the maximum number of transactions per year");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction10:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                            for (PreferenceValue pv : preferencesValue) {

                                if (totalAmountByUserYearly >= Double.parseDouble(pv.getValue())) {
                                    return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT_YEARLY, "The user exceeded the maximum amount per year");
                                }
                            }
                        }
                        break;
                }
            }
            //Crear el objeto Transaction para registrar la transferencia del cliente
            transaction.setId(null);
            transaction.setUserSourceId(BigInteger.valueOf(userSource_.getDatosRespuesta().getUsuarioID()));
            transaction.setUserDestinationId(BigInteger.valueOf(userSource_.getDatosRespuesta().getUsuarioID()));
            Product product = entityManager.find(Product.class, Constants.PRODUCT_AFINITAS);
            transaction.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeAF);
            transaction.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            transaction.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            transaction.setCreationDate(creationDate);
            //cambiar por valor de parÃ¡metro
            transaction.setConcept(Constante.sTransactionConceptPurchaseBalance);
            transaction.setAmount(amountRecharge);
            transaction.setTransactionStatus(TransactionStatus.CREATED.name());
            transaction.setTotalAmount(amountRecharge);
            transaction.setTotalTax(null);
            transaction.setPromotionAmount(null);
            transaction.setTotalAlopointsUsed(null);
            transaction.setTopUpDescription(null);
            transaction.setBillPaymentDescription(null);
            transaction.setExternalId(null);
            transaction.setTransactionNumber("1");
            entityManager.flush();
            entityManager.persist(transaction);

            //Revisar si la transaccion esta sujeta a comisiones
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", Constants.PRODUCT_AFINITAS).setParameter("transactionTypeId", Constante.sTransationTypeAF).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + Constants.PRODUCT_AFINITAS + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionRecharge = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountRecharge * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }

                //Se crea el objeto commissionItem y se persiste en BD
                CommissionItem commissionItem = new CommissionItem();
                commissionItem.setCommissionId(commissionRecharge);
                commissionItem.setAmount(amountCommission);
                Date commissionDate = new Date();
                Timestamp processedDate = new Timestamp(commissionDate.getTime());
                commissionItem.setProcessedDate(processedDate);
                commissionItem.setTransactionId(transaction);
                entityManager.persist(commissionItem);
            } catch (NoResultException e) {
                e.printStackTrace();
                return new RechargeAfinitasResponses(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
            }
            //Se actualiza el estatus de la transaccion a IN_PROCESS
            transaction.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(transaction);

            if (paymentInfoId != null) {
                PaymentInfo paymentInfo = entityManager.createNamedQuery("PaymentInfo.findByUserIdById", PaymentInfo.class).setParameter("userId", userId).setParameter("id", paymentInfoId).getSingleResult();
                paymentInfoCVV = paymentInfo.getCreditCardCVV();
                paymenInfoCardNumber = S3cur1ty3Cryt3r.aloEncrpter(paymentInfo.getCreditCardNumber(), "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
                paymenInfoCardName = paymentInfo.getCreditCardName();
                Date paymentInfoDate = paymentInfo.getCreditCardDate();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                String DateToStr = format.format(paymentInfoDate);
                paymentInfoDateYear = DateToStr.split("-")[0];
                paymentInfoDateMonth = DateToStr.split("-")[1];
            } else {
                paymentInfoCVV = cvv;
                paymenInfoCardNumber = cardNumber;
                paymenInfoCardName = cardHolderName;
                paymentInfoDateYear = expirationYear;
                paymentInfoDateMonth = expirationMonth;
            }
            chargeResponse = afinitasPaymentIntegration.afinitasCharge(String.valueOf(amountRecharge), currency, paymenInfoCardNumber, paymentInfoDateYear, paymentInfoDateMonth, paymentInfoCVV, paymenInfoCardName);

            chargeResponse.setStatus("true");
            if (chargeResponse.getStatus().equals("true")) {
                //Se actualizan los saldos de los usuarios involucrados en la transferencia
                //Balance History del usuario que transfiere el saldo
                BalanceHistory balanceUserSource = loadLastBalanceHistoryByAccount(userId, Constants.PRODUCT_AFINITAS);
                BalanceHistory balanceHistory = new BalanceHistory();
                balanceHistory.setId(null);
                balanceHistory.setUserId(userId);
                balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
                Float currentAmountUserSource = balanceUserSource.getCurrentAmount() + amountRecharge;
                balanceHistory.setCurrentAmount(currentAmountUserSource);
                balanceHistory.setProductId(product);
                balanceHistory.setTransactionId(transaction);
                Date balanceDate = new Date();
                Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
                balanceHistory.setDate(balanceHistoryDate);
                balanceHistory.setVersion(balanceUserSource.getId());
                entityManager.persist(balanceHistory);
                System.out.println("BALANCE HISTORY" + balanceHistory);
                //Se actualiza el estado de la transaccion a COMPLETED
                transaction.setTransactionStatus(TransactionStatus.COMPLETED.name());
                entityManager.merge(transaction);
                //Envias notificaciones
                //envias sms
                ////////////////////////////////////////////////////////////
                /// se incorpora para delvolver el saldo actual del cliente///
                /////////////////////////////////////////////////////////////
                products = getProductsListByUserId(userId);
                for (Product p : products) {
                    Float amount = 0F;
                    try {
                        if (p.getId().equals(Product.PREPAID_CARD)) {
                            AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                            CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                            CardResponse cardResponse = getCardByUserId(userId);
                            String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                            statusCardResponse.setCodigo("00");
                            if (statusCardResponse.getCodigo().equals("00")) {
                                StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                amount = Float.valueOf(accountResponse.getComprasDisponibles());
                            } else {
                                amount = Float.valueOf(0);
                            }

                        } else {

                            amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                        }
                    } catch (NoResultException e) {
                        amount = 0F;
                    } catch (ConnectException e) {
                        e.printStackTrace();
                        amount = 0F;
                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                        amount = 0F;
                    }
                    p.setCurrentBalance(amount);
                }

                ////////////////////////////////////////////////////////////
                /// se incorpora para delvolver el saldo actual del cliente///
                /////////////////////////////////////////////////////////////
                //Envias notificaciones
                //envias sms Y coreo
                //correo a quien envia
//            SendMailTherad sendMailTherad = new SendMailTherad("ES", amountTransfer, conceptTransaction, responseUser.getDatosRespuesta().getNombre() + " " + responseUser.getDatosRespuesta().getApellido(), emailUser, Integer.valueOf("8"));
//            sendMailTherad.run();
//            
//
//            //Envia quien envia 
//            SendSmsThread sendSmsThread = new SendSmsThread(userSource_.getDatosRespuesta().getMovil(), amountTransfer, Integer.valueOf("27"), userId, entityManager);
//            sendSmsThread.run();
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("000000")) {
                return new RechargeAfinitasResponses(ResponseCode.NOT_AUTHORIZED, "NOT AUTHORIZED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("1")) {
                return new RechargeAfinitasResponses(ResponseCode.CALL_ISSUER, "CALL ISSUER");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("2")) {
                return new RechargeAfinitasResponses(ResponseCode.CALL_ISSUER, "CALL ISSUER");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("3")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_TRADE, "INVALID TRADE");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("4")) {
                return new RechargeAfinitasResponses(ResponseCode.RETAIN_CARD, "RETAIN CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("5")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_TRANSACTION_AFINITAS, "INVALID TRANSACTION");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("6")) {
                return new RechargeAfinitasResponses(ResponseCode.RETRY_AFINITAS, "RETRY");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("12")) {
                return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("13")) {
                return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("14")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("30")) {
                return new RechargeAfinitasResponses(ResponseCode.FORMAT_ERROR, "FORMAT ERROR");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("31")) {
                return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("36")) {
                return new RechargeAfinitasResponses(ResponseCode.RETAIN_CARD, "RETAIN CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("41")) {
                return new RechargeAfinitasResponses(ResponseCode.RETAIN_CARD, "RETAIN CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("43")) {
                return new RechargeAfinitasResponses(ResponseCode.RETAIN_CARD, "RETAIN CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("51")) {
                return new RechargeAfinitasResponses(ResponseCode.INSUFFICIENT_FUNDS, "INSUFFICIENT FUNDS");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("54")) {
                return new RechargeAfinitasResponses(ResponseCode.EXPIRED_CARD_AFINITAS, "EXPIRED CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("55")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_PIN, "INVALID PIN");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("56")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("57")) {
                return new RechargeAfinitasResponses(ResponseCode.DEFERRED_PAYMENT_NOT_PERMITTED, "DEFERRED PAYMENT NOT PERMITTED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("61")) {
                return new RechargeAfinitasResponses(ResponseCode.LIMIT_EXCEEDED, "LIMIT EXCEEDED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("62")) {
                return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("65")) {
                return new RechargeAfinitasResponses(ResponseCode.LIMIT_EXCEEDED, "LIMIT EXCEEDED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("68")) {
                return new RechargeAfinitasResponses(ResponseCode.RETRY_AFINITAS, "RETRY");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("75")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_PIN, "INVALID PIN");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("82")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("83")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("87")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("89")) {
                return new RechargeAfinitasResponses(ResponseCode.TYPE_OF_PLAN_TERM_INVALID, "TYPE OF PLAN / TERM INVALID");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("94")) {
                return new RechargeAfinitasResponses(ResponseCode.DUPLICATED_TRANSACTION, "DUPLICATED TRANSACTION");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("N0")) {
                return new RechargeAfinitasResponses(ResponseCode.RETRY_AFINITAS, "RETRY");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("N2")) {
                return new RechargeAfinitasResponses(ResponseCode.EXCESSED_AUTHORIZATIONS, "RETRY");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("N5")) {
                return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("N6")) {
                return new RechargeAfinitasResponses(ResponseCode.CP_NOT_PERMITTED_BY_TH, "CP NOT PERMITTED BY TH");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("N7")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("N7")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("O4")) {
                return new RechargeAfinitasResponses(ResponseCode.LIMIT_EXCEEDED, "LIMIT EXCEEDED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("O6")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("O8")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("P1")) {
                return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("P9")) {
                return new RechargeAfinitasResponses(ResponseCode.LIMIT_EXCEEDED, "LIMIT EXCEEDED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("Q1")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("T2")) {
                return new RechargeAfinitasResponses(ResponseCode.TERMINAL_ERROR, "TERMINAL ERROR");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("T3")) {
                return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_NOT_PERMITTED, "TRANSACTION NOT PERMITTED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("T5")) {
                return new RechargeAfinitasResponses(ResponseCode.UNACTIVATED_CARD, "UNACTIVATED CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("T9")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_COIN, "INVALID COIN");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("1001")) {
                return new RechargeAfinitasResponses(ResponseCode.CHIP_READING_ERROR, "CHIP READING ERROR");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("1002")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_CHIP, "INVALID CHIP");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("1003")) {
                return new RechargeAfinitasResponses(ResponseCode.CHIP_NOT_SUPPORTED, "CHIP NOT SUPPORTED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("11")) {
                return new RechargeAfinitasResponses(ResponseCode.UNKNOWN, "UNKNOWN");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("DV_002")) {
                return new RechargeAfinitasResponses(ResponseCode.THE_DEVICE_IS_NOT_ACTIVE, "THE DEVICE IS NOT ACTIVE");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("BR_002")) {
                return new RechargeAfinitasResponses(ResponseCode.THE_BRANCH_IS_NOT_ACTIVE, "THE BRANCH IS NOT ACTIVE");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("BS_002")) {
                return new RechargeAfinitasResponses(ResponseCode.TRADE_IS_NOT_ACTIVE, "TRADE IS NOT ACTIVE");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("RQ_001")) {
                return new RechargeAfinitasResponses(ResponseCode.THE_REQUEST_IS_EMPTY, "THE REQUEST IS EMPTY");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("RQ_002")) {
                return new RechargeAfinitasResponses(ResponseCode.MISSING_PARAMETER_ON_REQUEST, "MISSING PARAMETER ON REQUEST");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("RQ_003")) {
                return new RechargeAfinitasResponses(ResponseCode.RESOURCE_NOT_FOUND, "RESOURCE NOT FOUND");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("RQ_004")) {
                return new RechargeAfinitasResponses(ResponseCode.ANSWER_EMPTY, "ANSWER EMPTY");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_001")) {
                return new RechargeAfinitasResponses(ResponseCode.THE_TRANSACTION_EXCEEDS_THE_PERMITTED_AMOUNT, "THE TRANSACTION EXCEEDS THE PERMITTED AMOUNT");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_002")) {
                return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_EXCEEDS_THE_ALLOWED_DAILY_AMOUNT, "TRANSACTION EXCEEDS THE ALLOWED DAILY AMOUNT");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_003")) {
                return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_EXCEEDS_THE_MONTHLY_AMOUNT_ALLOWED, "TRANSACTION EXCEEDS THE MONTHLY AMOUNT ALLOWED");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_005")) {
                return new RechargeAfinitasResponses(ResponseCode.NON_ACTIVE_PROMOTIONS, "NON ACTIVE PROMOTIONS");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_006")) {
                return new RechargeAfinitasResponses(ResponseCode.PROMOTION_NOT_ACTIVE, "PROMOTION NOT ACTIVE");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_007")) {
                return new RechargeAfinitasResponses(ResponseCode.THE_TRANSACTION_IS_NOT_WITHIN_THE_PERMITTED_SCHEDULE, "THE TRANSACTION IS NOT WITHIN THE PERMITTED SCHEDULE");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_008")) {
                return new RechargeAfinitasResponses(ResponseCode.THE_TRANSACTION_DOES_NOT_EXIST, "THE TRANSACTION DOES NOT EXIST");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_009")) {
                return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_WITH_NOT_APPROVED_SOURCE, "TRANSACTION WITH NOT APPROVED SOURCE");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_010")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_CARD, "INVALID CARD");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_011")) {
                return new RechargeAfinitasResponses(ResponseCode.INVALID_MEMBERSHIP, "INVALID MEMBERSHIP");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_013")) {
                return new RechargeAfinitasResponses(ResponseCode.TRANSACTION_CANCELED_PREVIOUSLY, "TRANSACTION CANCELED PREVIOUSLY");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_014")) {
                return new RechargeAfinitasResponses(ResponseCode.PREVIOUSLY_REVERSED_TRANSACTION, "PREVIOUSLY REVERSED TRANSACTION");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_015")) {
                return new RechargeAfinitasResponses(ResponseCode.EXCEED_PERMITTED_DAILY_TRANSACTIONS, "EXCEED PERMITTED DAILY TRANSACTIONS");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("CRP_002")) {
                return new RechargeAfinitasResponses(ResponseCode.THE_CORPORATE_IS_NOT_ACTIVE, "THE CORPORATE IS NOT ACTIVE");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("NF_001")) {
                return new RechargeAfinitasResponses(ResponseCode.ANSWER_NOT_FOUND, "ANSWER NOT FOUND");
            } else if (chargeResponse.getStatus().equals("false") && chargeResponse.getError().getCode().equals("TX_024")) {
                return new RechargeAfinitasResponses(ResponseCode.TIME_EXCEEDED_TO_PERFORM_CANCELLATION, "TIME EXCEEDED TO PERFORM CANCELLATION");
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return new RechargeAfinitasResponses(ResponseCode.THE_SERVICE_NOT_AVAILABLE, "THE SERVICE NOT AVAILABLE");
        } catch (java.net.ConnectException ex) {
            ex.printStackTrace();
            return new RechargeAfinitasResponses(ResponseCode.NOT_AUTHORIZED, "NOT AUTHORIZED");
        } catch (Exception e) {
            e.printStackTrace();
            return new RechargeAfinitasResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
        }
        RechargeAfinitasResponses rechargeAfinitasResponses = new RechargeAfinitasResponses(chargeResponse, ResponseCode.SUCCESS, "EXITO", products);
        rechargeAfinitasResponses.setProducts(products);
        rechargeAfinitasResponses.setIdTransaction(transaction.getId().toString());
        return rechargeAfinitasResponses;
    }

    public PaymentInfoListResponse getPaymentInfo(String userApi, String passwordApi, Long userId) {
        List<PaymentInfo> paymentInfos = null;
        try {
            if (validateUser(userApi, passwordApi)) {
                paymentInfos = entityManager.createNamedQuery("PaymentInfo.findByUserId", PaymentInfo.class).setParameter("userId", userId).getResultList();
                if (paymentInfos.size() <= 0) {
                    return new PaymentInfoListResponse(ResponseCode.NOT_ASSOCIATED_PAYMENT_INFO, "Not associated payment info");
                }
            } else {
                return new PaymentInfoListResponse(ResponseCode.INTERNAL_ERROR, "Error loading Payment Info");
            }

        } catch (Exception e) {
            return new PaymentInfoListResponse(ResponseCode.INTERNAL_ERROR, "Error loading Payment Info");
        }
        return new PaymentInfoListResponse(ResponseCode.SUCCESS, "", paymentInfos);
    }

    public CreditCardListResponse getCreditCardType(String userApi, String passwordApi) {
        List<CreditcardType> creditcardTypes = null;
        try {
            if (validateUser(userApi, passwordApi)) {
                creditcardTypes = entityManager.createNamedQuery("CreditcardType.findByEnabledTrue", CreditcardType.class).getResultList();
            } else {
                return new CreditCardListResponse(ResponseCode.INTERNAL_ERROR, "Error loading Credit Card");
            }

        } catch (Exception e) {
            return new CreditCardListResponse(ResponseCode.INTERNAL_ERROR, "Error loading Payment Info");
        }
        return new CreditCardListResponse(ResponseCode.SUCCESS, "", creditcardTypes);
    }

    public PaymentInfoResponse savePaymentInfo(String userApi, String passwordApi, Long userId, String estado, String ciudad, String zipCode, String addres1, Long paymentPatnerId, Long paymentTypeId, Long creditCardTypeId, String creditCardName, String creditCardNumber, String creditCardCVV, String creditCardDate) throws RemoteException, Exception {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario responseUser = null;

        try {
            if (validateUser(userApi, passwordApi)) {
                responseUser = proxy.getUsuarioporId("usuarioWS", "passwordWS", String.valueOf(userId));
                userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());
                //Address address = saveAddress(userId, estado, ciudad, zipCode, addres1);
                PaymentInfo paymentInfo = new PaymentInfo();
                paymentInfo.setBillingAddressId(null);
                PaymentPatner paymentPatner = entityManager.find(PaymentPatner.class, paymentPatnerId);
                paymentInfo.setPaymentPatnerId(paymentPatner);
                PaymentType paymentType = entityManager.find(PaymentType.class, paymentTypeId);
                paymentInfo.setPaymentTypeId(paymentType);
                paymentInfo.setUserId(BigInteger.valueOf(userId));
                CreditcardType creditcardType = entityManager.find(CreditcardType.class, creditCardTypeId);
                paymentInfo.setCreditCardTypeId(creditcardType);
                paymentInfo.setCreditCardName(creditCardName);
                paymentInfo.setCreditCardNumber(creditCardNumber);
                paymentInfo.setCreditCardCVV(creditCardCVV);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                Date ccdate = format.parse(creditCardDate);
                paymentInfo.setCreditCardDate(ccdate);
                paymentInfo.setBeginningDate(new Timestamp(new Date().getTime()));
                paymentInfo.setEnabled(true);
                entityManager.persist(paymentInfo);

            } else {
                return new PaymentInfoResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving payment info");
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
            throw new RemoteException(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return new PaymentInfoResponse(ResponseCode.SUCCESS);

    }

    private boolean validateUser(String user, String password) {
        UserWS usuarioWS = entityManager
                .createNamedQuery("UserWS.findByUserANDPassword",
                        UserWS.class).setMaxResults(1).getSingleResult();
        return (usuarioWS.getUsuario().equals(user) && usuarioWS
                .getPassword().equals(password));
    }

    public PaymentInfoResponse ChangeStatusPaymentInfo(String userApi, String passwordApi, Long userId, Long paymentInfoId, boolean status) {
        PaymentInfo paymentInfo = null;

        try {
            if (validateUser(userApi, passwordApi)) {
                paymentInfo = entityManager.createNamedQuery("PaymentInfo.findByUserIdById", PaymentInfo.class).setParameter("userId", userId).setParameter("id", paymentInfoId).getSingleResult();
                paymentInfo.setEnabled(status);
                entityManager.merge(paymentInfo);
            } else {
                return new PaymentInfoResponse(ResponseCode.INTERNAL_ERROR, "Error loading Payment Info");
            }

        } catch (Exception e) {
            return new PaymentInfoResponse(ResponseCode.INTERNAL_ERROR, "Error loading Payment Info");
        }
        return new PaymentInfoResponse(ResponseCode.SUCCESS, "", paymentInfo);
    }

    public ProductListResponse getProductsRechargePaymentByUserId(Long userId) {
        List<Product> products = new ArrayList<Product>();
        List<Product> productFinals = new ArrayList<Product>();
        try {
            products = getProductsListByUserId(userId);
            for (Product p : products) {
                if (p.isIsPaymentInfo()) {
                    Float amount = 0F;
                    try {
                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                    } catch (NoResultException e) {
                        amount = 0F;
                    }
                    p.setCurrentBalance(amount);
                    productFinals.add(p);
                }
            }
            if (productFinals.size() <= 0) {
                return new ProductListResponse(ResponseCode.USER_NOT_HAS_PRODUCT, "They are not products asociated");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ProductListResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
        }

        return new ProductListResponse(ResponseCode.SUCCESS, "", productFinals);
    }

    public ProductListResponse generarCodigoMovilSMS(String movil, String codigo) {

        try {
            SendSmsThread sendSmsThread = new SendSmsThread(movil, codigo, Integer.valueOf("32"), entityManager);
            sendSmsThread.start();

        } catch (Exception e) {
            e.printStackTrace();
            return new ProductListResponse(ResponseCode.INTERNAL_ERROR, "ERROR SEND SMS");
        }

        return new ProductListResponse(ResponseCode.SUCCESS, "ENVIO DE SMS EXITOSO");
    }

    public void sendSMS(String movil, String message) {

        try {
            //Solo aplica para dos o tres pasises si se desea hacer dinamicamente se debe agregar un plan de numeraciòn
            String countryCode = movil.substring(0, 2);
            if (movil.substring(0, 1).equals("1")) {
                //lo envia por USA
                TwilioSmsSenderProxy proxy = new TwilioSmsSenderProxy();
                try {
                    proxy.sendTwilioSMS(movil, message);
                } catch (ConnectException e) {
                    e.printStackTrace();
                }

            } else if (movil.substring(0, 2).equals("58")) {
                //Venezuela  integras con Massiva
                SendSmsMassiva sendSmsMassiva = new SendSmsMassiva();
                try {
                    String response = sendSmsMassiva.sendSmsMassiva(message, movil);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (movil.substring(0, 2).equals("52")) {
                //lo envia por TWILIO A MEXICO
                TwilioSmsSenderProxy proxy = new TwilioSmsSenderProxy();
                proxy.sendTwilioSMS(movil, message);
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

    }

    public ExchangeTokenPlaidResponses publicTokenPlaid(String methods) {

        TokenResponse tokenResponse = new TokenResponse();
        ExchangeTokenResponse exchangeTokenResponse = new ExchangeTokenResponse();

        try {
            PlaidClientIntegration plaidClientIntegration = new PlaidClientIntegration();

            tokenResponse = plaidClientIntegration.plaidCreateItem(methods);
            exchangeTokenResponse = plaidClientIntegration.plaidExchangeToken(tokenResponse.getPublic_token());
            ExchangeTokenPlaidResponses exchangeTokenPlaidResponses = new ExchangeTokenPlaidResponses(exchangeTokenResponse, ResponseCode.SUCCESS, "");
            exchangeTokenPlaidResponses.setTokenResponse(tokenResponse.getPublic_token());
            return exchangeTokenPlaidResponses;

        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new ExchangeTokenPlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR BALANCE");
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ExchangeTokenPlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR token");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ExchangeTokenPlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR token");
        }

    }

    public RetriveAuthPlaidResponses retriveAuthPlaid() {

        RetriveAuthResponse retriveAuthResponse = new RetriveAuthResponse();
        String clientId = Constants.CLIENTID;
        String secret = Constants.SECRET;
        try {
            PlaidClientIntegration plaidClientIntegration = new PlaidClientIntegration();

            retriveAuthResponse = plaidClientIntegration.plaidRetrieveAuth(clientId, secret);
            RetriveAuthPlaidResponses retriveAuthPlaidResponses = new RetriveAuthPlaidResponses(retriveAuthResponse, ResponseCode.SUCCESS, "EXITO");
            return retriveAuthPlaidResponses;

        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new RetriveAuthPlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR Auth");
        } catch (IOException ex) {
            ex.printStackTrace();
            return new RetriveAuthPlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR Auth");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new RetriveAuthPlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR Auth");
        }

    }

    public RetriveTransactionPlaidResponses retriveTransactionPlaid() {

        RetriveTransactionResponse retriveTransactionResponse = new RetriveTransactionResponse();
        String clientId = Constants.CLIENTID;
        String secret = Constants.SECRET;
        try {
            PlaidClientIntegration plaidClientIntegration = new PlaidClientIntegration();

            retriveTransactionResponse = plaidClientIntegration.plaidRetrieveTransaction(clientId, secret);
            RetriveTransactionPlaidResponses retriveTransactionPlaidResponses = new RetriveTransactionPlaidResponses(retriveTransactionResponse, ResponseCode.SUCCESS, "EXITO");
            return retriveTransactionPlaidResponses;

        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new RetriveTransactionPlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR TRANSACTION");
        } catch (IOException ex) {
            ex.printStackTrace();
            return new RetriveTransactionPlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR TRANSACTION");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new RetriveTransactionPlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR TRANSACTION");
        }

    }

    public RetriveBalancePlaidResponses retriveBalancePlaid() {

        RetriveBalanceResponse retriveBalanceResponse = new RetriveBalanceResponse();
        String clientId = Constants.CLIENTID;
        String secret = Constants.SECRET;
        try {
            PlaidClientIntegration plaidClientIntegration = new PlaidClientIntegration();

            retriveBalanceResponse = plaidClientIntegration.plaidRetrieveBalance(clientId, secret);
            RetriveBalancePlaidResponses retriveBalancePlaidResponses = new RetriveBalancePlaidResponses(retriveBalanceResponse, ResponseCode.SUCCESS, "EXITO");
            return retriveBalancePlaidResponses;

        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new RetriveBalancePlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR BALANCE");
        } catch (IOException ex) {
            ex.printStackTrace();
            return new RetriveBalancePlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR BALANCE");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new RetriveBalancePlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR BALANCE");
        }

    }

    public RetriveIdentityPlaidResponses retriveIdentityPlaid() {

        RetriveIdentityResponse retriveIdentityResponse = new RetriveIdentityResponse();
        String clientId = Constants.CLIENTID;
        String secret = Constants.SECRET;
        try {
            PlaidClientIntegration plaidClientIntegration = new PlaidClientIntegration();

            retriveIdentityResponse = plaidClientIntegration.plaidRetrieveIdentity(clientId, secret);
            RetriveIdentityPlaidResponses retriveIdentityPlaidResponses = new RetriveIdentityPlaidResponses(retriveIdentityResponse, ResponseCode.SUCCESS, "EXITO");
            return retriveIdentityPlaidResponses;

        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new RetriveIdentityPlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR IDENTITY");
        } catch (IOException ex) {
            ex.printStackTrace();
            return new RetriveIdentityPlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR IDENTITY");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new RetriveIdentityPlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR IDENTITY");
        }

    }

    public RetriveIncomePlaidResponses retriveIncomePlaid() {

        RetriveIncomeResponse retriveIncomeResponse = new RetriveIncomeResponse();
        String clientId = Constants.CLIENTID;
        String secret = Constants.SECRET;
        try {
            PlaidClientIntegration plaidClientIntegration = new PlaidClientIntegration();

            retriveIncomeResponse = plaidClientIntegration.plaidRetrieveIncome(clientId, secret);
            RetriveIncomePlaidResponses retriveIncomePlaidResponses = new RetriveIncomePlaidResponses(retriveIncomeResponse, ResponseCode.SUCCESS, "EXITO");
            return retriveIncomePlaidResponses;

        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new RetriveIncomePlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR INCOME");
        } catch (IOException ex) {
            ex.printStackTrace();
            return new RetriveIncomePlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR INCOME");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new RetriveIncomePlaidResponses(ResponseCode.INTERNAL_ERROR, "ERROR INCOME");
        }

    }

    public Sequences getSequencesByDocumentTypeByOriginApplication(Long documentTypeId, Long originApplicationId) {

        try {
            Sequences sequences = (Sequences) entityManager.createNamedQuery("Sequences.findBydocumentType_idByoriginApplicationId", Sequences.class).setParameter("documentTypeId", documentTypeId).setParameter("originApplicationId", originApplicationId).getSingleResult();
            return sequences;
        } catch (NoResultException e) {
            return null;
        }

    }

    private String generateNumberSequence(Sequences s) {
        String secuence = "";
        try {
            Integer numberSequence = s.getCurrentValue() > 1 ? s.getCurrentValue() : s.getInitialValue();
            s.setCurrentValue(s.getCurrentValue() + 1);
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            secuence = ((s.getOriginApplicationId().getId().equals(Constants.ORIGIN_APPLICATION_APP_ALODIGA_WALLET_ID)) ? "APP-" : (s.getOriginApplicationId().getId().equals(Constants.ORIGIN_APPLICATION_ADMIN_WALLET_ID)) ? "ADM-" : "PBW-")
                    .concat(s.getDocumentTypeId().getAcronym()).concat("-")
                    .concat(String.valueOf(year)).concat("-")
                    .concat(numberSequence.toString());
            entityManager.persist(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secuence;
    }

    public AccountBankResponse saveAccountBank(Long unifiedRegistryId, String accountNumber, Long bankId, Integer accountTypeBankId) {

        String statusAccountBankCode = StatusAccountBankE.ACTIVA.getStatusAccountCode();
        try {
            AccountBank accountBank = new AccountBank();
            accountBank.setUnifiedRegistryId(unifiedRegistryId);
            accountBank.setAccountNumber(accountNumber);
            Bank bank = entityManager.find(Bank.class, bankId);
            accountBank.setBankId(bank);
            StatusAccountBank statusAccountBank = (StatusAccountBank) entityManager.createNamedQuery(QueryConstants.STATUS_ACCOUNT_BANK_BY_CODE, StatusAccountBank.class).setParameter("code", statusAccountBankCode).getSingleResult();
            accountBank.setStatusAccountBankId(statusAccountBank);
            AccountTypeBank accountTypeBank = entityManager.find(AccountTypeBank.class, accountTypeBankId);
            accountBank.setAccountTypeBankId(accountTypeBank);
            accountBank.setCreateDate(new Timestamp(new Date().getTime()));
            entityManager.persist(accountBank);
            return new AccountBankResponse(ResponseCode.SUCCESS, "", accountBank);
        } catch (Exception e) {
            e.printStackTrace();
            return new AccountBankResponse(ResponseCode.INTERNAL_ERROR, "Error");
        }

    }

    public TransactionApproveRequestResponse saveTransactionApproveRequest(Long unifiedRegistryUserId, Long productId, Long transactionId, Long bankOperationId, Long documentTypeId, Long originApplicationId, Long businessId) {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            String statusTransactionApproveRequestE = StatusTransactionApproveRequestE.PENDIEN.getStatusTransactionApproveRequestCode();
            TransactionApproveRequest transactionApproveRequest = new TransactionApproveRequest();
            transactionApproveRequest.setId(null);
            transactionApproveRequest.setUnifiedRegistryUserId(BigInteger.valueOf(unifiedRegistryUserId));
            //se incluyo el bussinessId cuando la trasacion de retiro es de un negocio
            transactionApproveRequest.setBusinessId(BigInteger.valueOf(businessId));
            transactionApproveRequest.setCreateDate(new Timestamp(new Date().getTime()));
            transactionApproveRequest.setUpdateDate(null);
            Sequences sequences = getSequencesByDocumentTypeByOriginApplication(documentTypeId, originApplicationId);
            String generateNumberSequence = generateNumberSequence(sequences);
            transactionApproveRequest.setRequestNumber(generateNumberSequence);
            String DateToStr = format.format(curDate);
            Date fechaDate = null;
            fechaDate = format.parse(DateToStr);
            transactionApproveRequest.setRequestDate(fechaDate);
            Product product = entityManager.find(Product.class, productId);
            transactionApproveRequest.setProductId(product);
            Transaction transaction = entityManager.find(Transaction.class, transactionId);
            transactionApproveRequest.setTransactionId(transaction);
            BankOperation bankOperation = entityManager.find(BankOperation.class, bankOperationId);
            transactionApproveRequest.setBankOperationId(bankOperation);
            StatusTransactionApproveRequest statusTransactionApproveRequest = (StatusTransactionApproveRequest) entityManager.createNamedQuery(QueryConstants.STATUS_TRANSACTION_APPROVE_REQUEST_BY_CODE, StatusTransactionApproveRequest.class).setParameter("code", statusTransactionApproveRequestE).getSingleResult();

            transactionApproveRequest.setStatusTransactionApproveRequestId(statusTransactionApproveRequest);
            entityManager.flush();
            entityManager.persist(transactionApproveRequest);
            return new TransactionApproveRequestResponse(ResponseCode.SUCCESS, "", transactionApproveRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionApproveRequestResponse(ResponseCode.INTERNAL_ERROR, "Error");
        }
    }

    public AccountBankListResponse getAccountBankByUser(Long unifiedRegistryId) {
        List<AccountBank> accountBanks = new ArrayList<AccountBank>();

        try {
            accountBanks = (List<AccountBank>) entityManager.createNamedQuery("AccountBank.findByUnifiedRegistryId", AccountBank.class).setParameter("unifiedRegistryId", unifiedRegistryId).getResultList();
            if (accountBanks.size() <= 0) {
                return new AccountBankListResponse(ResponseCode.INTERNAL_ERROR, "Error loading account bank");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new AccountBankListResponse(ResponseCode.INTERNAL_ERROR, "Error loading account bank");
        }

        return new AccountBankListResponse(ResponseCode.SUCCESS, "", accountBanks);
    }

    public AccountBankResponse updateAccountBankByAccountNumber(Long unifiedRegistryId, String accountNumberOld, String accountNumberCurrent, Long bankId) {

        try {
            AccountBank accountBanks = (AccountBank) entityManager.createNamedQuery("AccountBank.findByUnifiedRegistryIdByAccountNumberByBankIdByStatusAccountId", AccountBank.class).setParameter("unifiedRegistryId", unifiedRegistryId).setParameter("accountNumber", accountNumberOld).setParameter("bankId", bankId).getSingleResult();
            accountBanks.setAccountNumber(accountNumberCurrent);
            accountBanks.setUpdateDate(new Timestamp(new Date().getTime()));
            entityManager.merge(accountBanks);
            return new AccountBankResponse(ResponseCode.SUCCESS, "", accountBanks);
        } catch (Exception e) {
            e.printStackTrace();
            return new AccountBankResponse(ResponseCode.INTERNAL_ERROR, "Error");
        }

    }

    public StatusTransactionApproveRequest getStatusTransactionAprove(String status) {

        try {
            StatusTransactionApproveRequest statusTransactionApproveRequests = entityManager.createNamedQuery(QueryConstants.CODE_BY_STATUS, StatusTransactionApproveRequest.class).setParameter("code", status).getSingleResult();
            return statusTransactionApproveRequests;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public BusinessHasProductResponse saveBusinessHasProductDefault(Long businessId) {
        List<BusinessHasProduct> businessHasProducts = new ArrayList<BusinessHasProduct>();
        try {

            businessHasProducts = (List<BusinessHasProduct>) entityManager.createNamedQuery("BusinessHasProduct.findByBusinessIdAllProduct", BusinessHasProduct.class).setParameter("businessId", businessId).getResultList();
            if (!businessHasProducts.isEmpty()) {
                return new BusinessHasProductResponse(ResponseCode.EXISTING_WALLET_BUSINESS, "Error creating wallet business. Existing");
            }

            BusinessHasProduct businessHasProduct = new BusinessHasProduct();
            businessHasProduct.setProductId(Product.ALOCOIN_PRODUCT);
            businessHasProduct.setBusinessId(businessId);
            businessHasProduct.setBeginningDate(new Timestamp(new Date().getTime()));
            entityManager.persist(businessHasProduct);

            //Crear balance history al crear el producto de negocio
            Product product1 = entityManager.find(Product.class, Product.ALOCOIN_PRODUCT);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setAdjusmentInfo("Creacion de billetera");
            balanceHistory.setBusinessId(businessId);
            balanceHistory.setOldAmount(0F);
            balanceHistory.setCurrentAmount(0F);
            balanceHistory.setProductId(product1);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            entityManager.persist(balanceHistory);

            BusinessHasProduct businessHasProduct1 = new BusinessHasProduct();
            businessHasProduct1.setProductId(Product.ALODIGA_BALANCE);
            businessHasProduct1.setBusinessId(businessId);
            businessHasProduct1.setBeginningDate(new Timestamp(new Date().getTime()));
            entityManager.persist(businessHasProduct1);

            Product product2 = entityManager.find(Product.class, Product.ALODIGA_BALANCE);

            balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setAdjusmentInfo("Creacion de billetera");
            balanceHistory.setBusinessId(businessId);
            balanceHistory.setOldAmount(0F);
            balanceHistory.setCurrentAmount(0F);
            balanceHistory.setProductId(product2);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            entityManager.persist(balanceHistory);

        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessHasProductResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving product_has_business");
        }
        return new BusinessHasProductResponse(ResponseCode.SUCCESS);
    }

    public ProductListResponse getProductsByBusinessId(Long businessId) {
        List<BusinessHasProduct> businessHasProducts = new ArrayList<BusinessHasProduct>();
        List<Product> products = new ArrayList<Product>();
        try {
            businessHasProducts = (List<BusinessHasProduct>) entityManager.createNamedQuery("BusinessHasProduct.findByBusinessIdAllProduct", BusinessHasProduct.class).setParameter("businessId", businessId).getResultList();

            if (businessHasProducts.size() <= 0) {
                return new ProductListResponse(ResponseCode.BUSINESS_NOT_HAS_PRODUCT, "They are not products asociated");
            }

            for (BusinessHasProduct uhp : businessHasProducts) {
                Product product = new Product();
                product = entityManager.find(Product.class, uhp.getProductId());
                products.add(product);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ProductListResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
        }

        return new ProductListResponse(ResponseCode.SUCCESS, "", products);
    }

    public BalanceHistoryResponse getBalanceHistoryByBusinessAndProduct(Long businessId, Long productId) {
        BalanceHistory balanceHistory = new BalanceHistory();
        try {
            balanceHistory = loadLastBalanceHistoryByBusiness_(businessId, productId);

        } catch (NoResultException e) {
            return new BalanceHistoryResponse(ResponseCode.BALANCE_HISTORY_NOT_FOUND_EXCEPTION, "Error loading BalanceHistory");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BalanceHistoryResponse(ResponseCode.INTERNAL_ERROR, "Error loading BalanceHistory");
        }
        return new BalanceHistoryResponse(ResponseCode.SUCCESS, "", balanceHistory);
    }

    public BalanceHistory loadLastBalanceHistoryByBusiness_(Long businessId, Long productId) throws NoResultException {

        try {
            Query query = entityManager.createQuery("SELECT b FROM BalanceHistory b WHERE b.businessId = " + businessId + " AND b.productId.id = " + productId + " ORDER BY b.id desc");
            query.setMaxResults(1);
            BalanceHistory result = (BalanceHistory) query.setHint("toplink.refresh", "true").getSingleResult();
            return result;
        } catch (NoResultException e) {
            e.printStackTrace();
            throw new NoResultException();
        }

    }

    public TransactionResponse manualWithdrawalsBusiness(Long bankId, Long accountBankBusinessId, String accountBank,
            Float amountWithdrawal, Long productId, String conceptTransaction, Long businessId, Long transactionBusinessId) {

        Long idTransaction = 0L;
        int totalTransactionsByBusinessDaily = 0;
        int totalTransactionsByBusinessMonthly = 0;
        int totalTransactionsByBusinessYearly = 0;
        Double totalAmountByBusinessDaily = 0.00D;
        Double totalAmountByBusinessMonthly = 0.00D;
        Double totalAmountByBusinessYearly = 0.00D;
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionWithdrawal = new Commission();
        Transaction withdrawal = new Transaction();
        ArrayList<Product> products = new ArrayList<Product>();

        try {
            totalTransactionsByBusinessDaily = TransactionsByBusinessCurrentDate(businessId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByBusinessDaily = AmountMaxByBusinessCurrentDate(businessId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByBusinessMonthly = TransactionsByBusinessCurrentDate(businessId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByBusinessMonthly = AmountMaxByBusinessCurrentDate(businessId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByBusinessYearly = TransactionsByBusinessCurrentDate(businessId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByBusinessYearly = AmountMaxByBusinessCurrentDate(businessId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction11:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (pv.getValue().equals("0")) {
                                    return new TransactionResponse(ResponseCode.DISABLED_TRANSACTION, "Transactions disabled");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction4:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (amountWithdrawal >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per transaction");
                                }
                            }
                        }
                        break;

                    case Constante.sValidatePreferenceTransaction5:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByBusinessDaily >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction6:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByBusinessDaily >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_DIALY, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction7:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByBusinessMonthly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction8:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByBusinessMonthly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_MONTHLY, "The user exceeded the maximum amount per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction9:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByBusinessYearly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_YEARLY, "The user exceeded the maximum number of transactions per year");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction10:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {

                                if (totalAmountByBusinessYearly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_YEARLY, "The user exceeded the maximum amount per year");
                                }
                            }
                        }
                        break;
                }
            }
            withdrawal.setId(null);
            Sequences sequences = getSequencesByDocumentTypeByOriginApplication(Constante.sDocumentTypeBusinessManualWithdrawal, Constante.sOriginApplicationPortalBusiness);
            String generateNumberSequence = generateNumberSequence(sequences);
            withdrawal.setTransactionNumber(generateNumberSequence);
            withdrawal.setTransactionBusinessId(BigInteger.valueOf(transactionBusinessId));
            withdrawal.setBusinessId(BigInteger.valueOf(businessId));
            Product product = entityManager.find(Product.class, productId);
            withdrawal.setProductId(product);
            withdrawal.setPaymentInfoId(null);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeBusinessManualWithdrawal);
            withdrawal.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            withdrawal.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            withdrawal.setCreationDate(creationDate);
            withdrawal.setConcept(conceptTransaction);
            withdrawal.setAmount(amountWithdrawal);
            withdrawal.setTransactionStatus(TransactionStatus.CREATED.name());
            withdrawal.setTotalAmount(amountWithdrawal);
            withdrawal.setTotalTax(null);
            withdrawal.setPromotionAmount(null);
            withdrawal.setTotalAlopointsUsed(null);
            withdrawal.setTopUpDescription(null);
            withdrawal.setBillPaymentDescription(null);
            withdrawal.setExternalId(null);
            entityManager.flush();
            entityManager.persist(withdrawal);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypeManualWithdrawal).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and businessId: " + businessId);
                }
                for (Commission c : commissions) {
                    commissionWithdrawal = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountWithdrawal * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }

                CommissionItem commissionItem = new CommissionItem();
                commissionItem.setCommissionId(commissionWithdrawal);
                commissionItem.setAmount(amountCommission);
                Date commissionDate = new Date();
                Timestamp processedDate = new Timestamp(commissionDate.getTime());
                commissionItem.setProcessedDate(processedDate);
                commissionItem.setTransactionId(withdrawal);
                entityManager.persist(commissionItem);
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
            }

            BankOperation manualWithdrawal = new BankOperation();
            manualWithdrawal.setId(null);
            manualWithdrawal.setBusinessId(BigInteger.valueOf(businessId));
            manualWithdrawal.setProductId(product);
            manualWithdrawal.setTransactionId(withdrawal);
            manualWithdrawal.setCommisionId(commissionWithdrawal);
            BankOperationType operationType = entityManager.find(BankOperationType.class, Constante.sBankOperationTypeWithdrawal);
            manualWithdrawal.setBankOperationTypeId(operationType);
            BankOperationMode operationMode = entityManager.find(BankOperationMode.class, Constante.sBankOperationModeManual);
            manualWithdrawal.setBankOperationModeId(operationMode);
            Bank bank = entityManager.find(Bank.class, bankId);
            manualWithdrawal.setBankId(bank);
            manualWithdrawal.setBankOperationNumber(accountBank);
            manualWithdrawal.setAccountBankBusinessId(BigInteger.valueOf(accountBankBusinessId));
            entityManager.flush();
            entityManager.persist(manualWithdrawal);
            commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypeManualWithdrawal).getResultList();
            if (commissions.size() < 1) {
                throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and businessId: " + businessId);
            }

            withdrawal.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(withdrawal);

            try {
                System.out.println("" + withdrawal.getId());
                saveTransactionApproveRequest(0L, product.getId(), withdrawal.getId(), manualWithdrawal.getId(), Constante.sDocumentTypeBusinessManualWithdrawal, Constante.sOriginApplicationPortalBusiness, businessId);
            } catch (Exception e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error saving transaction Aprrove Request");
            }
            try {
                products = getProductsListByBusinessId(businessId);
                for (Product p : products) {
                    Float amount_1 = 0F;
                    try {
                        amount_1 = loadLastBalanceHistoryByBusiness_(businessId, p.getId()).getCurrentAmount();
                    } catch (NoResultException e) {
                        amount_1 = 0F;
                    }
                    p.setCurrentBalance(amount_1);
                }
            } catch (Exception ex) {

                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
        }

        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.SUCCESS, "EXITO", products);
        transactionResponse.setIdTransaction(withdrawal.getId().toString());
        transactionResponse.setProducts(products);
        return transactionResponse;

    }

    public ArrayList<Product> getProductsListByBusinessId(Long businessId) throws NoResultException, Exception {
        List<BusinessHasProduct> businessHasProducts = new ArrayList<BusinessHasProduct>();
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            businessHasProducts = (List<BusinessHasProduct>) entityManager.createNamedQuery("BusinessHasProduct.findByBusinessIdAllProduct", BusinessHasProduct.class).setParameter("businessId", businessId).getResultList();

            if (businessHasProducts.size() <= 0) {
                throw new NoResultException();
            }

            for (BusinessHasProduct uhp : businessHasProducts) {
                Product product = new Product();
                product = entityManager.find(Product.class, uhp.getProductId());
                products.add(product);
            }
        } catch (Exception e) {
            throw new Exception();
        }
        return products;
    }

    public TransactionResponse saveTransferBetweenBusinessWithUser(Long productId, Float amountTransfer,
            String conceptTransaction, Long idUserDestination, Long businessId) {

        Long idTransaction = 0L;
        int totalTransactionsByBusinessDaily = 0;
        int totalTransactionsByBusinessMonthly = 0;
        int totalTransactionsByBusinessYearly = 0;
        Double totalAmountByBusinessDaily = 0.00D;
        Double totalAmountByBusinessMonthly = 0.00D;
        Double totalAmountByBusinessYearly = 0.00D;
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionTransfer = new Commission();
        ArrayList<Product> products = new ArrayList<Product>();
        Transaction transfer = new Transaction();
        try {
            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario userDestination = proxy.getUsuarioporId("usuarioWS", "passwordWS", idUserDestination.toString());

            BalanceHistory balanceUserSource = loadLastBalanceHistoryByBusiness_(businessId, productId);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypeTA).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and businessId: " + businessId);
                }
                for (Commission c : commissions) {
                    commissionTransfer = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountTransfer * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
            }
            Float amountTransferTotal = amountTransfer + amountCommission;
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountTransferTotal) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
            }

            totalTransactionsByBusinessDaily = TransactionsByBusinessCurrentDate(businessId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByBusinessDaily = AmountMaxByBusinessCurrentDate(businessId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByBusinessMonthly = TransactionsByBusinessCurrentDate(businessId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByBusinessMonthly = AmountMaxByBusinessCurrentDate(businessId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByBusinessYearly = TransactionsByBusinessCurrentDate(businessId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByBusinessYearly = AmountMaxByBusinessCurrentDate(businessId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction11:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (pv.getValue().equals("0")) {
                                    return new TransactionResponse(ResponseCode.DISABLED_TRANSACTION, "Transactions disabled");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction4:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (amountTransfer >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per transaction");
                                }
                            }
                        }
                        break;

                    case Constante.sValidatePreferenceTransaction5:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByBusinessDaily >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction6:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByBusinessDaily >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_DIALY, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction7:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByBusinessMonthly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction8:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByBusinessMonthly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_MONTHLY, "The user exceeded the maximum amount per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction9:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByBusinessYearly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_YEARLY, "The user exceeded the maximum number of transactions per year");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction10:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {

                                if (totalAmountByBusinessYearly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_YEARLY, "The user exceeded the maximum amount per year");
                                }
                            }
                        }
                        break;
                }
            }

            transfer.setId(null);
            transfer.setBusinessId(BigInteger.valueOf(businessId));
            transfer.setUserDestinationId(BigInteger.valueOf(idUserDestination));
            Product product = entityManager.find(Product.class, productId);
            transfer.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeTA);
            transfer.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            transfer.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            transfer.setCreationDate(creationDate);
            transfer.setConcept(Constante.sTransactionConceptTranferAccounts);
            transfer.setAmount(amountTransfer);
            transfer.setTransactionStatus(TransactionStatus.CREATED.name());
            transfer.setTotalAmount(amountTransfer);
            transfer.setTotalTax(null);
            transfer.setPromotionAmount(null);
            transfer.setTotalAlopointsUsed(null);
            transfer.setTopUpDescription(null);
            transfer.setBillPaymentDescription(null);
            transfer.setExternalId(null);
            transfer.setTransactionNumber("1");
            entityManager.flush();
            entityManager.persist(transfer);

            CommissionItem commissionItem = new CommissionItem();
            commissionItem.setCommissionId(commissionTransfer);
            commissionItem.setAmount(amountCommission);
            Date commissionDate = new Date();
            Timestamp processedDate = new Timestamp(commissionDate.getTime());
            commissionItem.setProcessedDate(processedDate);
            commissionItem.setTransactionId(transfer);
            entityManager.persist(commissionItem);

            transfer.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(transfer);

            balanceUserSource = loadLastBalanceHistoryByBusiness_(businessId, productId);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setBusinessId(businessId);
            balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
            Float currentAmountUserSource = balanceUserSource.getCurrentAmount() - amountTransferTotal;
            balanceHistory.setCurrentAmount(currentAmountUserSource);
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(transfer);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceUserSource.getId());
            entityManager.persist(balanceHistory);

            BalanceHistory balanceUserDestination = loadLastBalanceHistoryByAccount(idUserDestination, productId);
            balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setUserId(idUserDestination);
            if (balanceUserDestination == null) {
                balanceHistory.setOldAmount(Constante.sOldAmountUserDestination);
                balanceHistory.setCurrentAmount(amountTransfer);
            } else {
                balanceHistory.setOldAmount(balanceUserDestination.getCurrentAmount());
                Float currentAmountUserDestination = balanceUserDestination.getCurrentAmount() + amountTransfer;
                balanceHistory.setCurrentAmount(currentAmountUserDestination);
                balanceHistory.setVersion(balanceUserDestination.getId());
            }
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(transfer);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            entityManager.persist(balanceHistory);

            transfer.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(transfer);

            products = getProductsListByBusinessId(businessId);
            for (Product p : products) {
                Float amount = 0F;
                try {
                    amount = loadLastBalanceHistoryByBusiness_(businessId, p.getId()).getCurrentAmount();

                } catch (NoResultException e) {
                    e.printStackTrace();
                    amount = 0F;
                }
                p.setCurrentBalance(amount);
            }

            SendMailTherad sendMailTherad1 = new SendMailTherad("ES", amountTransfer, conceptTransaction, userDestination.getDatosRespuesta().getNombre() + " " + userDestination.getDatosRespuesta().getApellido(), userDestination.getDatosRespuesta().getEmail(), Integer.valueOf("9"));
            sendMailTherad1.run();

            SendSmsThread sendSmsThread1 = new SendSmsThread(userDestination.getDatosRespuesta().getMovil(), amountTransfer, Integer.valueOf("28"), Long.valueOf(userDestination.getDatosRespuesta().getUsuarioID()), entityManager);
            sendSmsThread1.run();

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
        }

        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.SUCCESS, "EXITO", products);
        transactionResponse.setIdTransaction(transfer.getId().toString());
        transactionResponse.setProducts(products);
        return transactionResponse;
    }

    public int TransactionsByBusinessCurrentDate(Long businessId, Date begginingDateTime, Date endingDateTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.businessId = ?3");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", businessId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.size();
    }

    public int TransactionsByBusinessByTransactionByProductCurrentDate(Long businessId, Timestamp begginingDateTime, Timestamp endingDateTime, Long productId, Long transactionTypeId) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.businessId = ?3 AND t.productId = ?4 AND t.transactionTypeId = ?5");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", businessId);
        query.setParameter("4", productId);
        query.setParameter("5", transactionTypeId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.size();
    }

    public Double AmountMaxByBusinessCurrentDate(Long businessId, Date begginingDateTime, Date endingDateTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT SUM(t.totalAmount) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.businessId = ?3");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", businessId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (double) result.get(0) : 0f;
    }

    public Double AmountMaxByBusinessByTransactionByProductCurrentDate(Long businessId, Timestamp begginingDateTime, Timestamp endingDateTime, Long productId, Long transactionTypeId) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT SUM(t.totalAmount) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.businessId = ?3 AND t.productId = ?4 AND t.transactionTypeId = ?5");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", businessId);
        query.setParameter("4", productId);
        query.setParameter("5", transactionTypeId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (double) result.get(0) : 0f;
    }

    public Long TransactionsByProductByBusinessCurrentDate(Long productId, Long businessId, Timestamp begginingDateTime, Timestamp endingDateTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(t.productId) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.businessId = ?3 AND t.productId = ?4");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", businessId);
        query.setParameter("4", productId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (Long) result.get(0) : 0l;
    }

    public Long TransactionsByProductByBusinessByTransactionCurrentDate(Long productId, Long businessId, Timestamp begginingDateTime, Timestamp endingDateTime, Long transactionTypeId) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(t.productId) FROM transaction t WHERE t.creationDate between ?1 AND ?2 AND t.businessId = ?3 AND t.productId = ?4 AND t.transactionTypeId = ?5");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("1", begginingDateTime);
        query.setParameter("2", endingDateTime);
        query.setParameter("3", businessId);
        query.setParameter("4", productId);
        query.setParameter("5", transactionTypeId);
        List result = (List) query.setHint("toplink.refresh", "true").getResultList();
        return result.get(0) != null ? (Long) result.get(0) : 0l;
    }

    public TransactionResponse saveTransferBetweenBusinessAccount(Long productId, Float amountTransfer,
            String conceptTransaction, Long businessId, Long businessDestinationId) {

        Long idTransaction = 0L;
        int totalTransactionsByBusinessDaily = 0;
        int totalTransactionsByBusinessMonthly = 0;
        int totalTransactionsByBusinessYearly = 0;
        Double totalAmountByBusinessDaily = 0.00D;
        Double totalAmountByBusinessMonthly = 0.00D;
        Double totalAmountByBusinessYearly = 0.00D;
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        Float amountCommission = 0.00F;
        short isPercentCommission = 0;
        Commission commissionTransfer = new Commission();
        ArrayList<Product> products = new ArrayList<Product>();
        Transaction transfer = new Transaction();
        try {

            BalanceHistory balanceUserSource = loadLastBalanceHistoryByBusiness_(businessId, productId);
            try {
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransationTypeTA).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and businessId: " + businessId);
                }
                for (Commission c : commissions) {
                    commissionTransfer = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountTransfer * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process saving transaction");
            }
            Float amountTransferTotal = amountTransfer + amountCommission;
            if (balanceUserSource == null || balanceUserSource.getCurrentAmount() < amountTransferTotal) {
                return new TransactionResponse(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
            }

            totalTransactionsByBusinessDaily = TransactionsByBusinessCurrentDate(businessId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByBusinessDaily = AmountMaxByBusinessCurrentDate(businessId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByBusinessMonthly = TransactionsByBusinessCurrentDate(businessId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByBusinessMonthly = AmountMaxByBusinessCurrentDate(businessId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));

            totalTransactionsByBusinessYearly = TransactionsByBusinessCurrentDate(businessId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            totalAmountByBusinessYearly = AmountMaxByBusinessCurrentDate(businessId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

            List<Preference> preferences = getPreferences();
            for (Preference p : preferences) {
                if (p.getName().equals(Constante.sPreferenceTransaction)) {
                    idTransaction = p.getId();
                }
            }
            preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
            for (PreferenceField pf : preferencesField) {
                switch (pf.getName()) {
                    case Constante.sValidatePreferenceTransaction11:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (pv.getValue().equals("0")) {
                                    return new TransactionResponse(ResponseCode.DISABLED_TRANSACTION, "Transactions disabled");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction4:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (amountTransfer >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per transaction");
                                }
                            }
                        }
                        break;

                    case Constante.sValidatePreferenceTransaction5:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByBusinessDaily >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction6:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByBusinessDaily >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_DIALY, "The user exceeded the maximum amount per day");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction7:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByBusinessMonthly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction8:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalAmountByBusinessMonthly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_MONTHLY, "The user exceeded the maximum amount per month");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction9:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {
                                if (totalTransactionsByBusinessYearly >= Integer.parseInt(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_QUANTITY_LIMIT_YEARLY, "The user exceeded the maximum number of transactions per year");
                                }
                            }
                        }
                        break;
                    case Constante.sValidatePreferenceTransaction10:
                        if (pf.getEnabled() == 1) {
                            preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationBusiness);
                            for (PreferenceValue pv : preferencesValue) {

                                if (totalAmountByBusinessYearly >= Double.parseDouble(pv.getValue())) {
                                    return new TransactionResponse(ResponseCode.TRANSACTION_AMOUNT_LIMIT_YEARLY, "The user exceeded the maximum amount per year");
                                }
                            }
                        }
                        break;
                }
            }
            transfer.setId(null);
            transfer.setBusinessId(BigInteger.valueOf(businessId));
            transfer.setBusinessDestinationId(BigInteger.valueOf(businessDestinationId));
            Product product = entityManager.find(Product.class, productId);
            transfer.setProductId(product);
            TransactionType transactionType = entityManager.find(TransactionType.class, Constante.sTransationTypeTA);
            transfer.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, Constante.sTransactionSource);
            transfer.setTransactionSourceId(transactionSource);
            Date date = new Date();
            Timestamp creationDate = new Timestamp(date.getTime());
            transfer.setCreationDate(creationDate);
            transfer.setConcept(Constante.sTransactionConceptTranferAccounts);
            transfer.setAmount(amountTransfer);
            transfer.setTransactionStatus(TransactionStatus.CREATED.name());
            transfer.setTotalAmount(amountTransfer);
            transfer.setTotalTax(null);
            transfer.setPromotionAmount(null);
            transfer.setTotalAlopointsUsed(null);
            transfer.setTopUpDescription(null);
            transfer.setBillPaymentDescription(null);
            transfer.setExternalId(null);
            transfer.setTransactionNumber("1");
            entityManager.flush();
            entityManager.persist(transfer);

            CommissionItem commissionItem = new CommissionItem();
            commissionItem.setCommissionId(commissionTransfer);
            commissionItem.setAmount(amountCommission);
            Date commissionDate = new Date();
            Timestamp processedDate = new Timestamp(commissionDate.getTime());
            commissionItem.setProcessedDate(processedDate);
            commissionItem.setTransactionId(transfer);
            entityManager.persist(commissionItem);

            transfer.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(transfer);

            balanceUserSource = loadLastBalanceHistoryByBusiness_(businessId, productId);
            BalanceHistory balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setBusinessId(businessId);
            balanceHistory.setOldAmount(balanceUserSource.getCurrentAmount());
            Float currentAmountUserSource = balanceUserSource.getCurrentAmount() - amountTransferTotal;
            balanceHistory.setCurrentAmount(currentAmountUserSource);
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(transfer);
            Date balanceDate = new Date();
            Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            balanceHistory.setVersion(balanceUserSource.getId());
            entityManager.persist(balanceHistory);

            BalanceHistory balanceUserDestination = loadLastBalanceHistoryByBusiness_(businessDestinationId, productId);
            balanceHistory = new BalanceHistory();
            balanceHistory.setId(null);
            balanceHistory.setBusinessId(businessDestinationId);
            if (balanceUserDestination == null) {
                balanceHistory.setOldAmount(Constante.sOldAmountUserDestination);
                balanceHistory.setCurrentAmount(amountTransfer);
            } else {
                balanceHistory.setOldAmount(balanceUserDestination.getCurrentAmount());
                Float currentAmountUserDestination = balanceUserDestination.getCurrentAmount() + amountTransfer;
                balanceHistory.setCurrentAmount(currentAmountUserDestination);
                balanceHistory.setVersion(balanceUserDestination.getId());
            }
            balanceHistory.setProductId(product);
            balanceHistory.setTransactionId(transfer);
            balanceDate = new Date();
            balanceHistoryDate = new Timestamp(balanceDate.getTime());
            balanceHistory.setDate(balanceHistoryDate);
            entityManager.persist(balanceHistory);

            transfer.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(transfer);

            products = getProductsListByBusinessId(businessId);
            for (Product p : products) {
                Float amount = 0F;
                try {
                    amount = loadLastBalanceHistoryByBusiness_(businessId, p.getId()).getCurrentAmount();

                } catch (NoResultException e) {
                    e.printStackTrace();
                    amount = 0F;
                }
                p.setCurrentBalance(amount);
            }

//            SendMailTherad sendMailTherad1 = new SendMailTherad("ES", amountTransfer, conceptTransaction, userDestination.getDatosRespuesta().getNombre() + " " + userDestination.getDatosRespuesta().getApellido(), userDestination.getDatosRespuesta().getEmail(), Integer.valueOf("9"));
//            sendMailTherad1.run();
//
//            SendSmsThread sendSmsThread1 = new SendSmsThread(userDestination.getDatosRespuesta().getMovil(), amountTransfer, Integer.valueOf("28"), Long.valueOf(userDestination.getDatosRespuesta().getUsuarioID()), entityManager);
//            sendSmsThread1.run();
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
        }

        TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.SUCCESS, "EXITO", products);
        transactionResponse.setIdTransaction(transfer.getId().toString());
        transactionResponse.setProducts(products);
        return transactionResponse;
    }

    public TransactionListResponse getTransactionsByBusinessId(Long businessId, Integer maxResult) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        try {
            entityManager.flush();

            transactions = (List<Transaction>) entityManager.createNamedQuery("Transaction.findByBusinessId", Transaction.class).setParameter("businessId", businessId).setMaxResults(maxResult).setParameter("businessDestinationId", businessId).getResultList();
            if (transactions.size() < 1) {
                throw new NoResultException(ResponseCode.TRANSACTION_LIST_NOT_FOUND_EXCEPTION.toString());
            }
        } catch (NoResultException e) {
            e.printStackTrace();
            return new TransactionListResponse(ResponseCode.TRANSACTION_LIST_NOT_FOUND_EXCEPTION, "El negocio no tiene transacciones asociadas");
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionListResponse(ResponseCode.INTERNAL_ERROR, "error interno");
        }

        for (Transaction t : transactions) {
            t.setPaymentInfoId(null);
            t.setProductId(t.getProductId());
            t.setTransactionType(t.getTransactionTypeId().getId().toString());
            t.setId(t.getId());
        }
        return new TransactionListResponse(ResponseCode.SUCCESS, "", transactions);
    }

    public TransactionListResponse getTransactionsByBusinessIdBetweenDate(Long businessId, String from, String to) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        try {
            entityManager.flush();
            transactions = (List<Transaction>) entityManager.createNamedQuery("Transaction.findByBusinessIdBetweenDate", Transaction.class).setParameter("businessId", businessId).setParameter("from", EjbUtils.convertStringToTimestampBeginningDate(from), TemporalType.TIMESTAMP).setParameter("to", EjbUtils.convertStringToTimestampEndingDate(to), TemporalType.TIMESTAMP).setParameter("businessDestinationId", businessId).getResultList();
            if (transactions.size() < 1) {
                throw new NoResultException(ResponseCode.TRANSACTION_LIST_NOT_FOUND_EXCEPTION.toString());
            }
        } catch (NoResultException e) {
            e.printStackTrace();
            return new TransactionListResponse(ResponseCode.TRANSACTION_LIST_NOT_FOUND_EXCEPTION, "El negocio no tiene transacciones asociadas");
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionListResponse(ResponseCode.INTERNAL_ERROR, "error interno");
        }

        for (Transaction t : transactions) {
            t.setPaymentInfoId(null);
            t.setProductId(t.getProductId());
            t.setTransactionType(t.getTransactionTypeId().getId().toString());
            t.setId(t.getId());
        }
        return new TransactionListResponse(ResponseCode.SUCCESS, "", transactions);
    }

    public BusinessShopResponse getBusinessInfoByCryptogram(String cryptogram) {
        APIBusinessPortalWSProxy aPIBusinessPortalWSProxy = new APIBusinessPortalWSProxy();

        try {
            BpBusinessInfoResponse response = aPIBusinessPortalWSProxy.getBusinessInfoByCryptogram(cryptogram);
            BusinessShopResponse answer = new BusinessShopResponse(ResponseCode.SUCCESS, "", response.getCommercialDenomination(), response.getBusinessRif(), response.getStoreName());
            answer.setPosCode(response.getPosCode());
            return answer;
        } catch (BusinessPortalWSException ex) {
            return new BusinessShopResponse(ResponseCode.INTERNAL_ERROR, ex.getErrorMessage().getErrorMessageValue());
        } catch (RemoteException ex) {
            return new BusinessShopResponse(ResponseCode.INTERNAL_ERROR, "Error processing businessInfo");
        }
    }

    public CardResponse getCardByIdentificationNumber(String numberIdentification) {

        List<Card> cards = new ArrayList<Card>();
        CardEJB cardEJB = (CardEJB) EJBServiceLocator.getInstance().get(EjbConstants.CARD_EJB);
        PersonEJB personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
        List<PhonePerson> phonePersonList = null;
        String alias = "";
        String name = "";
        String emailPerson = "";
        String numberPhone = "";
        try {
            cards = cardEJB.getCardByIdentificationNumber(numberIdentification);
            for (Card card : cards) {
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

                alias = card.getAlias();
                emailPerson = card.getPersonCustomerId().getEmail();
                name = card.getPersonCustomerId().getNaturalCustomer().getFirstNames() + " " + card.getPersonCustomerId().getNaturalCustomer().getLastNames();
            }
        } catch (NoResultException e) {
            e.printStackTrace();
            return new CardResponse(ResponseCode.EMPTY_LIST_HAS_CARD, "Error loading cards");
        } catch (Exception e) {
            e.printStackTrace();
            return new CardResponse(ResponseCode.INTERNAL_ERROR, "Error loading cards");
        }
        return new CardResponse(ResponseCode.SUCCESS, "", alias, name, emailPerson, numberPhone);
    }

    public CardResponse getCardByEmail(String email) {
        List<Card> cards = new ArrayList<Card>();
        CardEJB cardEJB = (CardEJB) EJBServiceLocator.getInstance().get(EjbConstants.CARD_EJB);
        PersonEJB personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
        List<PhonePerson> phonePersonList = null;
        String alias = "";
        String name = "";
        String emailPerson = "";
        String numberPhone = "";
        try {
            cards = cardEJB.getCardByEmail(email);
            System.out.println("cards " + cards.toString());
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
        return new CardResponse(ResponseCode.SUCCESS, "Card registered in BD", alias, name, emailPerson, numberPhone);
    }

    public CardResponse getCardByPhone(String phone) {

        List<Card> cards = new ArrayList<Card>();
        CardEJB cardEJB = (CardEJB) EJBServiceLocator.getInstance().get(EjbConstants.CARD_EJB);
        PersonEJB personEJB = (PersonEJB) EJBServiceLocator.getInstance().get(EjbConstants.PERSON_EJB);
        List<PhonePerson> phonePersonList = null;
        String alias = "";
        String name = "";
        String emailPerson = "";
        String numberPhone = "";
        try {
            cards = cardEJB.getCardByPhone(phone);
            for (Card card : cards) {

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

                alias = card.getAlias();
                emailPerson = card.getPersonCustomerId().getEmail();
                name = card.getPersonCustomerId().getNaturalCustomer().getFirstNames() + " " + card.getPersonCustomerId().getNaturalCustomer().getLastNames();
            }
        } catch (NoResultException e) {
            e.printStackTrace();
            return new CardResponse(ResponseCode.EMPTY_LIST_HAS_CARD, "Error loading cards");
        } catch (Exception e) {
            e.printStackTrace();
            return new CardResponse(ResponseCode.INTERNAL_ERROR, "Error loading cards");
        }
        return new CardResponse(ResponseCode.SUCCESS, "", alias, name, emailPerson, numberPhone);
    }

    public BankListResponse getBankByUser(Long userId) {
        List<UserHasBank> userHasBank = new ArrayList<UserHasBank>();
        List<Bank> banks = new ArrayList<Bank>();
        try {
            userHasBank = (List<UserHasBank>) entityManager.createNamedQuery("UserHasBank.findByUserSourceIdAllBank", UserHasBank.class).setParameter("userSourceId", userId).getResultList();   
            if (userHasBank.size() <= 0) {
                return new BankListResponse(ResponseCode.USER_NOT_HAS_BANK, "They are not banks asociated");
            }
            
            for (UserHasBank uhb : userHasBank) {
                Bank bank = new Bank();
                bank = entityManager.find(Bank.class, uhb.getBankId().getId());
                banks.add(bank);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return new BankListResponse(ResponseCode.INTERNAL_ERROR, "Error loading banks");
        }

        return new BankListResponse(ResponseCode.SUCCESS, "", banks);
    }

    public DispertionTransferResponses dispertionTransfer(String email, Float amountRecharge, Long productId) {

        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        CredentialAutorizationClient credentialAutorizationClient = new CredentialAutorizationClient();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Timestamp begginingDateTime = new Timestamp(0);
        Timestamp endingDateTime = new Timestamp(0);
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        SimpleDateFormat sdg = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        String hour = sdf.format(timestamp);
        String date = sdg.format(timestamp);
        String yearSequence = year.format(timestamp);
        Integer recharge = DocumentTypeE.PROREC.getId();
        Integer transactionTypeE = TransactionTypeE.PROREC.getId();
        int totalTransactionsByUserDaily = 0;
        int totalTransactionsByUserMonthly = 0;
        int totalTransactionsByUserYearly = 0;
        short isPercentCommission = 0;
        Double totalAmountByUserDaily = 0.00D;
        Double totalAmountByUserMonthly = 0.00D;
        Double totalAmountByUserYearly = 0.00D;
        Float amountCommission = 0.00F;
        Float amountTransferTotal = 0.00F;
        Long idTransaction = 0L;
        Long idPreferenceField = 0L;
        ArrayList<Product> products = new ArrayList<Product>();
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        BalanceHistory balanceProductSource = null;
        BalanceHistory balanceProductDestination = null;
        Commission commissionTransfer = new Commission();

        try {
            ignoreSSLAutorization();

            //Se obtiene el usuario de registro unificado
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", email);
            Long userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            //Se obtiene el saldo disponible del usuario
            balanceProductSource = loadLastBalanceHistoryByAccount(userId, productId);

            try {
                //Se calcula la comisión de la operación 
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransactionTypePR).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionTransfer = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountRecharge * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }

                amountTransferTotal = amountRecharge + amountCommission;
                //Se valida si tiene saldo disponible
                if (balanceProductSource == null || balanceProductSource.getCurrentAmount() < amountTransferTotal) {
                    return new DispertionTransferResponses(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
                }

                //Validar preferencias
                begginingDateTime = Utils.DateTransaction()[0];
                endingDateTime = Utils.DateTransaction()[1];
                totalTransactionsByUserDaily = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));
                totalAmountByUserDaily = AmountMaxByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));
                totalTransactionsByUserMonthly = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));
                totalAmountByUserMonthly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));
                totalTransactionsByUserYearly = TransactionsByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));
                totalAmountByUserYearly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

                //Validar las preferencias
                List<Preference> preferences = getPreferences();
                for (Preference p : preferences) {
                    if (p.getName().equals(Constante.sPreferenceTransaction)) {
                        idTransaction = p.getId();
                    }
                }
                preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
                for (PreferenceField pf : preferencesField) {
                    switch (pf.getName()) {
                        case Constante.sValidatePreferenceTransaction11:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (pv.getValue().equals("0")) {
                                        return new DispertionTransferResponses(ResponseCode.DISABLED_TRANSACTION, "Transactions disabled");
                                    }
                                }
                            }
                            break;
                        case Constante.sValidatePreferenceTransaction4:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (amountRecharge >= Double.parseDouble(pv.getValue())) {
                                        return new DispertionTransferResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per transaction");
                                    }
                                }
                            }
                            break;

                        case Constante.sValidatePreferenceTransaction5:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (totalTransactionsByUserDaily >= Integer.parseInt(pv.getValue())) {
                                        return new DispertionTransferResponses(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per day");
                                    }
                                }
                            }
                            break;
                        case Constante.sValidatePreferenceTransaction6:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (totalAmountByUserDaily >= Double.parseDouble(pv.getValue())) {
                                        return new DispertionTransferResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT_DIALY, "The user exceeded the maximum amount per day");
                                    }
                                }
                            }
                            break;
                        case Constante.sValidatePreferenceTransaction7:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (totalTransactionsByUserMonthly >= Integer.parseInt(pv.getValue())) {
                                        return new DispertionTransferResponses(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per month");
                                    }
                                }
                            }
                            break;
                        case Constante.sValidatePreferenceTransaction8:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (totalAmountByUserMonthly >= Double.parseDouble(pv.getValue())) {
                                        return new DispertionTransferResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT_MONTHLY, "The user exceeded the maximum amount per month");
                                    }
                                }
                            }
                            break;
                        case Constante.sValidatePreferenceTransaction9:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (totalTransactionsByUserYearly >= Integer.parseInt(pv.getValue())) {
                                        return new DispertionTransferResponses(ResponseCode.TRANSACTION_QUANTITY_LIMIT_YEARLY, "The user exceeded the maximum number of transactions per year");
                                    }
                                }
                            }
                            break;
                        case Constante.sValidatePreferenceTransaction10:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {

                                    if (totalAmountByUserYearly >= Double.parseDouble(pv.getValue())) {
                                        return new DispertionTransferResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT_YEARLY, "The user exceeded the maximum amount per year");
                                    }
                                }
                            }
                            break;
                    }
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new DispertionTransferResponses(ResponseCode.INTERNAL_ERROR, "Error in validation process");
            }
            amountTransferTotal = amountRecharge + amountCommission;
            //Se busca por el email el alias que devuelve credencial
            CardResponse cardResponse = getCardByEmail(email);
            String alias = cardResponse.getaliasCard();

            //Se genera la secuencia de la transacción
            Sequences sequences = getSequencesByDocumentTypeByOriginApplication(Long.valueOf(recharge), Long.valueOf(Constants.ORIGIN_APPLICATION_APP_ALODIGA_WALLET_ID));
            String Numbersequence = generateNumberSequence(sequences);
            String sequence = transactionTypeE + yearSequence + Numbersequence;

            //Se efectúa la recarga de la tarjeta
            DispertionResponse dispertionResponse = credentialAutorizationClient.dispertionTransfer(date, hour, alias, String.valueOf(amountRecharge), sequence);

            if (dispertionResponse.getCodigoError().equals("-1")) {
                DispertionTransferCredential dispertionTransferCredential = new DispertionTransferCredential(dispertionResponse.getCodigoError(), dispertionResponse.getMensajeError(), dispertionResponse.getCodigoRespuesta(), dispertionResponse.getMensajeRespuesta(), dispertionResponse.getCodigoAutorizacion());

                //Se guarda el objeto Transaction
                Transaction transaction = new Transaction();
                transaction.setId(null);
                transaction.setTransactionNumber(Numbersequence);
                transaction.setTransactionSequence(sequence);
                transaction.setUserSourceId(BigInteger.valueOf(userId));
                transaction.setUserDestinationId(BigInteger.valueOf(userId));
                Product product = entityManager.find(Product.class, productId);
                transaction.setProductId(product);
                TransactionType transactionType = entityManager.find(TransactionType.class, transactionTypeE);
                transaction.setTransactionTypeId(transactionType);
                TransactionSource transactionSource = entityManager.find(TransactionSource.class, transactionTypeE);
                transaction.setTransactionSourceId(transactionSource);
                Date date_ = new Date();
                Timestamp creationDate = new Timestamp(date_.getTime());
                transaction.setCreationDate(creationDate);
                transaction.setAmount(Float.valueOf(amountRecharge));
                transaction.setTransactionStatus(TransactionStatus.COMPLETED.name());
                transaction.setConcept(Constants.DISPERTION_CONCEPT_TRANSFER);
                transaction.setTotalAmount(Float.valueOf(amountRecharge));
                entityManager.persist(transaction);

                //BalanceHistory del producto de origen                
                balanceProductSource = loadLastBalanceHistoryByAccount(userId, productId);
                BalanceHistory balanceHistory = new BalanceHistory();
                balanceHistory.setId(null);
                balanceHistory.setUserId(userId);
                balanceHistory.setOldAmount(balanceProductSource.getCurrentAmount());
                Float currentAmountProductSource = balanceProductSource.getCurrentAmount() - amountTransferTotal;
                balanceHistory.setCurrentAmount(currentAmountProductSource);
                balanceHistory.setProductId(product);
                balanceHistory.setTransactionId(transaction);
                Date balanceDate = new Date();
                Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
                balanceHistory.setDate(balanceHistoryDate);
                balanceHistory.setVersion(balanceProductSource.getId());
                entityManager.persist(balanceHistory);

                //BalanceHistory del producto de destino
                product = entityManager.find(Product.class, 3L);
                balanceProductDestination = loadLastBalanceHistoryByAccount(userId, product.getId());
                balanceHistory = new BalanceHistory();
                balanceHistory.setId(null);
                balanceHistory.setUserId(userId);
                if (balanceProductDestination == null) {
                    balanceHistory.setOldAmount(Constante.sOldAmountUserDestination);
                    balanceHistory.setCurrentAmount(amountRecharge);
                } else {
                    balanceHistory.setOldAmount(balanceProductDestination.getCurrentAmount());
                    Float currentAmountUserDestination = balanceProductDestination.getCurrentAmount() + amountRecharge;
                    balanceHistory.setCurrentAmount(currentAmountUserDestination);
                    balanceHistory.setVersion(balanceProductDestination.getId());
                }
                balanceHistory.setProductId(product);
                balanceHistory.setTransactionId(transaction);
                balanceDate = new Date();
                balanceHistoryDate = new Timestamp(balanceDate.getTime());
                balanceHistory.setDate(balanceHistoryDate);
                entityManager.persist(balanceHistory);

                //Se obtiene la lista de productos del usuario
                try {
                    products = getProductsListByUserId(userId);
                    for (Product p : products) {
                        Float amount_1 = 0F;
                        try {
                            if (p.getId().equals(Product.PREPAID_CARD)) {
                                CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                                AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                                String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                                StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                                if (statusCardResponse.getCodigo().equals("00")) {
                                    StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                    amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                                } else {
                                    amount_1 = Float.valueOf(0);
                                }
                            } else {
                                amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                            }

                        } catch (NoResultException e) {
                            amount_1 = 0F;
                        } catch (ConnectException e) {
                            e.printStackTrace();
                            amount_1 = 0F;
                        } catch (SocketTimeoutException e) {
                            e.printStackTrace();
                            amount_1 = 0F;
                        }
                        p.setCurrentBalance(amount_1);
                    }
                } catch (Exception ex) {

                    return new DispertionTransferResponses(ResponseCode.INTERNAL_ERROR, "Error loading products");
                }

                DispertionTransferResponses dispertionTransferResponses = new DispertionTransferResponses(dispertionTransferCredential, ResponseCode.SUCCESS, "SUCCESS", products);
                dispertionTransferResponses.setIdTransaction(transaction.getId().toString());
                dispertionTransferResponses.setProducts(products);
                return dispertionTransferResponses;

            } else if (dispertionResponse.getCodigoError().equals("204")) {
                return new DispertionTransferResponses(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
            } else if (dispertionResponse.getCodigoError().equals("913")) {
                return new DispertionTransferResponses(ResponseCode.INVALID_AMOUNT, "INVALID AMOUNT");
            } else if (dispertionResponse.getCodigoError().equals("203")) {
                return new DispertionTransferResponses(ResponseCode.EXPIRATION_DATE_DIFFERS, "EXPIRATION DATE DIFFERS");
            } else if (dispertionResponse.getCodigoError().equals("205")) {
                return new DispertionTransferResponses(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
            } else if (dispertionResponse.getCodigoError().equals("202")) {
                return new DispertionTransferResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (dispertionResponse.getCodigoError().equals("201")) {
                return new DispertionTransferResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (dispertionResponse.getCodigoError().equals("03")) {
                return new DispertionTransferResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (dispertionResponse.getCodigoError().equals("28")) {
                return new DispertionTransferResponses(ResponseCode.LOCKED_CARD, "LOCKED_CARD");
            } else if (dispertionResponse.getCodigoError().equals("211")) {
                return new DispertionTransferResponses(ResponseCode.BLOCKED_ACCOUNT, "BLOCKED ACCOUNT");
            } else if (dispertionResponse.getCodigoError().equals("210")) {
                return new DispertionTransferResponses(ResponseCode.INVALID_ACCOUNT, "INVALID ACCOUNT");
            } else if (dispertionResponse.getCodigoError().equals("998")) {
                return new DispertionTransferResponses(ResponseCode.INSUFFICIENT_BALANCE, "INSUFFICIENT BALANCE");
            } else if (dispertionResponse.getCodigoError().equals("986")) {
                return new DispertionTransferResponses(ResponseCode.INSUFFICIENT_LIMIT, "INSUFFICIENT LIMIT");
            } else if (dispertionResponse.getCodigoError().equals("987")) {
                return new DispertionTransferResponses(ResponseCode.CREDIT_LIMIT_0, "CREDIT LIMIT 0");
            } else if (dispertionResponse.getCodigoError().equals("988")) {
                return new DispertionTransferResponses(ResponseCode.CREDIT_LIMIT_0_OF_THE_DESTINATION_ACCOUNT, "CREDIT LIMIT 0 OF THE DESTINATION ACCOUNT");
            } else if (dispertionResponse.getCodigoError().equals("999")) {
                return new DispertionTransferResponses(ResponseCode.ERROR_PROCESSING_THE_TRANSACTION, "ERROR PROCESSING THE TRANSACTION");
            } else if (dispertionResponse.getCodigoError().equals("101")) {
                return new DispertionTransferResponses(ResponseCode.INVALID_TRANSACTION, "INVALID TRANSACTION");
            } else if (dispertionResponse.getCodigoError().equals("105")) {
                return new DispertionTransferResponses(ResponseCode.ERROR_VALIDATION_THE_TERMINAL, "ERROR VALIDATION THE TERMINAL");
            } else if (dispertionResponse.getCodigoError().equals("241")) {
                return new DispertionTransferResponses(ResponseCode.DESTINATION_ACCOUNT_LOCKED, "DESTINATION ACCOUNT LOCKED");
            } else if (dispertionResponse.getCodigoError().equals("230")) {
                return new DispertionTransferResponses(ResponseCode.INVALID_DESTINATION_CARD, "INVALID DESTINATION CARD");
            } else if (dispertionResponse.getCodigoError().equals("240")) {
                return new DispertionTransferResponses(ResponseCode.INVALID_DESTINATION_ACCOUNT, "INVALID DESTINATION ACCOUNT");
            } else if (dispertionResponse.getCodigoError().equals("301")) {
                return new DispertionTransferResponses(ResponseCode.THE_AMOUNT_MUST_BE_POSITIVE_AND_THE_AMOUNT_IS_REPORTED, "THE AMOUNT MUST BE POSITIVE AND THE AMOUNT IS REPORTED");
            } else if (dispertionResponse.getCodigoError().equals("302")) {
                return new DispertionTransferResponses(ResponseCode.INVALID_TRANSACTION_DATE, "INVALID TRANSACTION DATE");
            } else if (dispertionResponse.getCodigoError().equals("303")) {
                return new DispertionTransferResponses(ResponseCode.INVALID_TRANSACTION_TIME, "INVALID TRANSACTION TIME");
            } else if (dispertionResponse.getCodigoError().equals("994")) {
                return new DispertionTransferResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NN");
            } else if (dispertionResponse.getCodigoError().equals("991")) {
                return new DispertionTransferResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_SN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION SN");
            } else if (dispertionResponse.getCodigoError().equals("992")) {
                return new DispertionTransferResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
            } else if (dispertionResponse.getCodigoError().equals("993")) {
                return new DispertionTransferResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
            } else if (dispertionResponse.getCodigoError().equals("990")) {
                return new DispertionTransferResponses(ResponseCode.TRASACTION_BETWEEN_ACCOUNTS_NOT_ALLOWED, "TRASACTION BETWEEN ACCOUNTS NOT ALLOWED");
            } else if (dispertionResponse.getCodigoError().equals("120")) {
                return new DispertionTransferResponses(ResponseCode.TRADE_VALIDATON_ERROR, "TRADE VALIDATON ERROR");
            } else if (dispertionResponse.getCodigoError().equals("110")) {
                return new DispertionTransferResponses(ResponseCode.DESTINATION_CARD_DOES_NOT_SUPPORT_TRANSACTION, "DESTINATION CARD DOES NOT SUPPORT TRANSACTION");
            } else if (dispertionResponse.getCodigoError().equals("111")) {
                return new DispertionTransferResponses(ResponseCode.OPERATION_NOT_ENABLED_FOR_THE_DESTINATION_CARD, "OPERATION NOT ENABLED FOR THE DESTINATION CARD");
            } else if (dispertionResponse.getCodigoError().equals("206")) {
                return new DispertionTransferResponses(ResponseCode.BIN_NOT_ALLOWED, "BIN NOT ALLOWED");
            } else if (dispertionResponse.getCodigoError().equals("207")) {
                return new DispertionTransferResponses(ResponseCode.STOCK_CARD, "STOCK CARD");
            } else if (dispertionResponse.getCodigoError().equals("205")) {
                return new DispertionTransferResponses(ResponseCode.THE_ACCOUNT_EXCEEDS_THE_MONTHLY_LIMIT, "THE ACCOUNT EXCEEDS THE MONTHLY LIMIT");
            } else if (dispertionResponse.getCodigoError().equals("101")) {
                return new DispertionTransferResponses(ResponseCode.THE_PAN_FIELD_IS_MANDATORY, "THE PAN FIELD IS MANDATORY");
            } else if (dispertionResponse.getCodigoError().equals("102")) {
                return new DispertionTransferResponses(ResponseCode.THE_AMOUNT_TO_BE_RECHARGE_IS_INCORRECT, "THE AMOUNT TO BE RECHARGE IS INCORRECT");
            } else if (dispertionResponse.getCodigoError().equals("3")) {
                return new DispertionTransferResponses(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
            } else if (dispertionResponse.getCodigoError().equals("8")) {
                return new DispertionTransferResponses(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
            } else if (dispertionResponse.getCodigoError().equals("33")) {
                return new DispertionTransferResponses(ResponseCode.THE_AMOUNT_MUST_BE_GREATER_THAN_0, "THE AMOUNT MUST BE GREATER THAN 0");
            } else if (dispertionResponse.getCodigoError().equals("1")) {
                return new DispertionTransferResponses(ResponseCode.SUCCESSFUL_RECHARGE, "SUCCESSFUL RECHARGE");
            } else if (dispertionResponse.getCodigoError().equals("410")) {
                return new DispertionTransferResponses(ResponseCode.ERROR_VALIDATING_PIN, "THE PAN FIELD IS MANDATORY");
            } else if (dispertionResponse.getCodigoError().equals("430")) {
                return new DispertionTransferResponses(ResponseCode.ERROR_VALIDATING_CVC1, "ERROR VALIDATING CVC1");
            } else if (dispertionResponse.getCodigoError().equals("400")) {
                return new DispertionTransferResponses(ResponseCode.ERROR_VALIDATING_CVC2, "ERROR VALIDATING CVC2");
            } else if (dispertionResponse.getCodigoError().equals("420")) {
                return new DispertionTransferResponses(ResponseCode.PIN_CHANGE_ERROR, "PIN CHANGE ERROR");
            } else if (dispertionResponse.getCodigoError().equals("250")) {
                return new DispertionTransferResponses(ResponseCode.ERROR_VALIDATING_THE_ITEM, " ERROR VALIDATING THE ITEM");
            }
            return new DispertionTransferResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new DispertionTransferResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new DispertionTransferResponses(ResponseCode.INTERNAL_ERROR, "");
        }
    }

    public ProductListResponse getProductsUsePrepaidCardByUserId(Long userId) {
        List<Product> products = new ArrayList<Product>();
        List<Product> productFinals = new ArrayList<Product>();
        try {
            products = getProductsListByUserId(userId);
            for (Product p : products) {
                if (p.getIsUsePrepaidCard()) {
                    Float amount = 0F;
                    try {
                        amount = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                    } catch (NoResultException e) {
                        amount = 0F;
                    }
                    p.setCurrentBalance(amount);
                    productFinals.add(p);
                }
            }
            if (productFinals.size() <= 0) {
                return new ProductListResponse(ResponseCode.USER_NOT_HAS_PRODUCT, "They are not products asociated");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ProductListResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
        }

        return new ProductListResponse(ResponseCode.SUCCESS, "", productFinals);
    }

    public AccountBankResponse saveAccountBankUser(Long bankId, Long unifiedRegistryId, String accountNumber, Integer accountTypeBankId) {
        String statusAccountBankCode = StatusAccountBankE.ACTIVA.getStatusAccountCode();
        try {

            //Se consulta si el bank existe
            Bank bank = entityManager.find(Bank.class, bankId);
            if (bank == null) {
                return new AccountBankResponse(ResponseCode.INTERNAL_ERROR, "The Bank is not registered in the BD");
            }

            //Se consulta si el AccountTypeBank existe
            AccountTypeBank accountTypeBank = entityManager.find(AccountTypeBank.class, accountTypeBankId);
            if (accountTypeBank == null) {
                return new AccountBankResponse(ResponseCode.INTERNAL_ERROR, "The Account Type Bank is not registered in the BD");
            }

            //Se busca el status Activo para la cuente bancaria
            StatusAccountBank statusAccountBank = (StatusAccountBank) entityManager.createNamedQuery(QueryConstants.STATUS_ACCOUNT_BANK_BY_CODE, StatusAccountBank.class).setParameter("code", statusAccountBankCode).getSingleResult();

            //Se guarda la cuenta bancaria del usuario en la BD
            AccountBank accountBank = new AccountBank();
            accountBank.setUnifiedRegistryId(unifiedRegistryId);
            accountBank.setAccountNumber(accountNumber);
            accountBank.setBankId(bank);
            accountBank.setStatusAccountBankId(statusAccountBank);
            accountBank.setAccountTypeBankId(accountTypeBank);
            accountBank.setCreateDate(new Timestamp(new Date().getTime()));
            entityManager.persist(accountBank);
            return new AccountBankResponse(ResponseCode.SUCCESS, "", accountBank);

        } catch (Exception e) {
            e.printStackTrace();
            return new AccountBankResponse(ResponseCode.INTERNAL_ERROR, "Error");
        }
    }

    public ProductResponse getProductPrepaidCardByUser(Long userId) {
        Product product = new Product();
        try {
            //Se buscan los productos asociados al usuario
            ProductListResponse productsResponse = getProductsByUserId(userId);

            if (productsResponse == null) {
                return new ProductResponse(ResponseCode.USER_NOT_HAS_PRODUCT, "They are not products asociated");
            }

            //Se verificar que el producto del usuario tiene activado el indicador isUsePrepaidCard 
            List<Product> productsList = productsResponse.products;
            for (Product pr : productsList) {
                if (pr.getIsUsePrepaidCard() == true) {
                    product = entityManager.find(Product.class, pr.getId());
                }
            }

            //Si el usuario no tiene ningun producto con el indicador isUsePrepaidCard se envia un mensaje
            if (product.getId() == null) {
                return new ProductResponse(ResponseCode.INTERNAL_ERROR, "The user does not have a product for the prepaid card");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ProductResponse(ResponseCode.INTERNAL_ERROR, "Error loading products");
        }
        return new ProductResponse(ResponseCode.SUCCESS, "", product);
    }

    public BalanceInquiryWithMovementsResponses balanceInquiryWithMovements(String email) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        SimpleDateFormat sdg = new SimpleDateFormat("yyyyMMdd");
        String hour = sdf.format(timestamp);
        String date = sdg.format(timestamp);
        CredentialAutorizationClient credentialAutorizationClient = new CredentialAutorizationClient();
        try {
            //Se busca por el email el alias que devuelve credencial
            CardResponse cardResponse = getCardByEmail(email);
            String alias = cardResponse.getaliasCard();
            //llamado al servicio de consulta de saldo con movimientos
            BalanceInquiryWithMovementsResponse balanceInquiryWithMovementsResponse = credentialAutorizationClient.balanceInquiryWithMovements(date, hour, alias);
            if (balanceInquiryWithMovementsResponse.getCodigoError().equals("-1")) {
                BalanceInquiryWithMovementsCredential balanceInquiryWithMovementsCredential = new BalanceInquiryWithMovementsCredential(balanceInquiryWithMovementsResponse.getCodigoError(), balanceInquiryWithMovementsResponse.getMensajeError(), balanceInquiryWithMovementsResponse.getCodigoRespuesta(), balanceInquiryWithMovementsResponse.getMensajeRespuesta(), balanceInquiryWithMovementsResponse.getCodigoAutorizacion(), balanceInquiryWithMovementsResponse.getDisponibleConsumos(), balanceInquiryWithMovementsResponse.getDisponibleCuotas(), balanceInquiryWithMovementsResponse.getDisponibleAdelantos(), balanceInquiryWithMovementsResponse.getDisponiblePrestamos(), balanceInquiryWithMovementsResponse.getSaldo(), balanceInquiryWithMovementsResponse.getSaldoEnDolares(), balanceInquiryWithMovementsResponse.getPagoMinimo(), balanceInquiryWithMovementsResponse.getFechaVencimientoUltimaLiquidacion(), balanceInquiryWithMovementsResponse.getMovimientos());
                return new BalanceInquiryWithMovementsResponses(balanceInquiryWithMovementsCredential, ResponseCode.SUCCESS, "SUCCESS");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("204")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("913")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.INVALID_AMOUNT, "INVALID AMOUNT");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("203")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.EXPIRATION_DATE_DIFFERS, "EXPIRATION DATE DIFFERS");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("205")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("202")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("201")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("03")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("28")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.LOCKED_CARD, "LOCKED_CARD");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("211")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.BLOCKED_ACCOUNT, "BLOCKED ACCOUNT");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("210")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.INVALID_ACCOUNT, "INVALID ACCOUNT");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("998")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.INSUFFICIENT_BALANCE, "INSUFFICIENT BALANCE");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("986")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.INSUFFICIENT_LIMIT, "INSUFFICIENT LIMIT");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("987")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.CREDIT_LIMIT_0, "CREDIT LIMIT 0");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("988")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.CREDIT_LIMIT_0_OF_THE_DESTINATION_ACCOUNT, "CREDIT LIMIT 0 OF THE DESTINATION ACCOUNT");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("999")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.ERROR_PROCESSING_THE_TRANSACTION, "ERROR PROCESSING THE TRANSACTION");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("101")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.INVALID_TRANSACTION, "INVALID TRANSACTION");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("105")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.ERROR_VALIDATION_THE_TERMINAL, "ERROR VALIDATION THE TERMINAL");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("241")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.DESTINATION_ACCOUNT_LOCKED, "DESTINATION ACCOUNT LOCKED");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("230")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.INVALID_DESTINATION_CARD, "INVALID DESTINATION CARD");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("240")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.INVALID_DESTINATION_ACCOUNT, "INVALID DESTINATION ACCOUNT");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("301")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.THE_AMOUNT_MUST_BE_POSITIVE_AND_THE_AMOUNT_IS_REPORTED, "THE AMOUNT MUST BE POSITIVE AND THE AMOUNT IS REPORTED");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("302")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.INVALID_TRANSACTION_DATE, "INVALID TRANSACTION DATE");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("303")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.INVALID_TRANSACTION_TIME, "INVALID TRANSACTION TIME");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("994")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NN");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("991")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_SN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION SN");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("992")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("993")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("990")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.TRASACTION_BETWEEN_ACCOUNTS_NOT_ALLOWED, "TRASACTION BETWEEN ACCOUNTS NOT ALLOWED");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("120")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.TRADE_VALIDATON_ERROR, "TRADE VALIDATON ERROR");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("110")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.DESTINATION_CARD_DOES_NOT_SUPPORT_TRANSACTION, "DESTINATION CARD DOES NOT SUPPORT TRANSACTION");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("111")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.OPERATION_NOT_ENABLED_FOR_THE_DESTINATION_CARD, "OPERATION NOT ENABLED FOR THE DESTINATION CARD");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("206")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.BIN_NOT_ALLOWED, "BIN NOT ALLOWED");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("207")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.STOCK_CARD, "STOCK CARD");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("205")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.THE_ACCOUNT_EXCEEDS_THE_MONTHLY_LIMIT, "THE ACCOUNT EXCEEDS THE MONTHLY LIMIT");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("101")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.THE_PAN_FIELD_IS_MANDATORY, "THE PAN FIELD IS MANDATORY");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("102")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.THE_AMOUNT_TO_BE_RECHARGE_IS_INCORRECT, "THE AMOUNT TO BE RECHARGE IS INCORRECT");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("3")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("8")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("33")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.THE_AMOUNT_MUST_BE_GREATER_THAN_0, "THE AMOUNT MUST BE GREATER THAN 0");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("1")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.SUCCESSFUL_RECHARGE, "SUCCESSFUL RECHARGE");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("410")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.ERROR_VALIDATING_PIN, "THE PAN FIELD IS MANDATORY");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("430")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.ERROR_VALIDATING_CVC1, "ERROR VALIDATING CVC1");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("400")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.ERROR_VALIDATING_CVC2, "ERROR VALIDATING CVC2");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("420")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.PIN_CHANGE_ERROR, "PIN CHANGE ERROR");
            } else if (balanceInquiryWithMovementsResponse.getCodigoError().equals("250")) {
                return new BalanceInquiryWithMovementsResponses(ResponseCode.ERROR_VALIDATING_THE_ITEM, " ERROR VALIDATING THE ITEM");
            }
            return new BalanceInquiryWithMovementsResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return new BalanceInquiryWithMovementsResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (IOException ex) {
            ex.printStackTrace();
            return new BalanceInquiryWithMovementsResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BalanceInquiryWithMovementsResponses(ResponseCode.INTERNAL_ERROR, "");
        }
    }

    public BalanceInquiryWithoutMovementsResponses balanceInquiryWithoutMovements(String email) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        SimpleDateFormat sdg = new SimpleDateFormat("yyyyMMdd");
        String hour = sdf.format(timestamp);
        String date = sdg.format(timestamp);
        CredentialAutorizationClient credentialAutorizationClient = new CredentialAutorizationClient();
        try {
            //Se busca por el email el alias que devuelve credencial
            CardResponse cardResponse = getCardByEmail(email);
            String alias = cardResponse.getaliasCard();
            //llamado al servicio de consulta de saldo con movimientos
            BalanceInquiryWithoutMovementsResponse balanceInquiryWithoutMovementsResponse = credentialAutorizationClient.balanceInquiryWithoutMovements(date, hour, alias);
            if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("-1")) {
                BalanceInquiryWithoutMovementsCredential balanceInquiryWithoutMovementsCredential = new BalanceInquiryWithoutMovementsCredential(balanceInquiryWithoutMovementsResponse.getCodigoError(), balanceInquiryWithoutMovementsResponse.getMensajeError(), balanceInquiryWithoutMovementsResponse.getCodigoRespuesta(), balanceInquiryWithoutMovementsResponse.getMensajeRespuesta(), balanceInquiryWithoutMovementsResponse.getCodigoAutorizacion(), balanceInquiryWithoutMovementsResponse.getDisponibleConsumos(), balanceInquiryWithoutMovementsResponse.getDisponibleCuotas(), balanceInquiryWithoutMovementsResponse.getDisponibleAdelantos(), balanceInquiryWithoutMovementsResponse.getDisponiblePrestamos(), balanceInquiryWithoutMovementsResponse.getSaldo(), balanceInquiryWithoutMovementsResponse.getSaldoEnDolares(), balanceInquiryWithoutMovementsResponse.getPagoMinimo(), balanceInquiryWithoutMovementsResponse.getFechaVencimientoUltimaLiquidacion());
                return new BalanceInquiryWithoutMovementsResponses(balanceInquiryWithoutMovementsCredential, ResponseCode.SUCCESS, "SUCCESS");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("204")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("913")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INVALID_AMOUNT, "INVALID AMOUNT");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("203")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.EXPIRATION_DATE_DIFFERS, "EXPIRATION DATE DIFFERS");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("205")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("202")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("201")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("03")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("28")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.LOCKED_CARD, "LOCKED_CARD");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("211")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.BLOCKED_ACCOUNT, "BLOCKED ACCOUNT");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("210")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INVALID_ACCOUNT, "INVALID ACCOUNT");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("998")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INSUFFICIENT_BALANCE, "INSUFFICIENT BALANCE");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("986")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INSUFFICIENT_LIMIT, "INSUFFICIENT LIMIT");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("987")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.CREDIT_LIMIT_0, "CREDIT LIMIT 0");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("988")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.CREDIT_LIMIT_0_OF_THE_DESTINATION_ACCOUNT, "CREDIT LIMIT 0 OF THE DESTINATION ACCOUNT");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("999")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.ERROR_PROCESSING_THE_TRANSACTION, "ERROR PROCESSING THE TRANSACTION");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("101")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INVALID_TRANSACTION, "INVALID TRANSACTION");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("105")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.ERROR_VALIDATION_THE_TERMINAL, "ERROR VALIDATION THE TERMINAL");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("241")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.DESTINATION_ACCOUNT_LOCKED, "DESTINATION ACCOUNT LOCKED");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("230")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INVALID_DESTINATION_CARD, "INVALID DESTINATION CARD");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("240")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INVALID_DESTINATION_ACCOUNT, "INVALID DESTINATION ACCOUNT");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("301")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.THE_AMOUNT_MUST_BE_POSITIVE_AND_THE_AMOUNT_IS_REPORTED, "THE AMOUNT MUST BE POSITIVE AND THE AMOUNT IS REPORTED");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("302")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INVALID_TRANSACTION_DATE, "INVALID TRANSACTION DATE");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("303")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INVALID_TRANSACTION_TIME, "INVALID TRANSACTION TIME");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("994")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NN");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("991")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_SN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION SN");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("992")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("993")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("990")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.TRASACTION_BETWEEN_ACCOUNTS_NOT_ALLOWED, "TRASACTION BETWEEN ACCOUNTS NOT ALLOWED");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("120")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.TRADE_VALIDATON_ERROR, "TRADE VALIDATON ERROR");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("110")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.DESTINATION_CARD_DOES_NOT_SUPPORT_TRANSACTION, "DESTINATION CARD DOES NOT SUPPORT TRANSACTION");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("111")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.OPERATION_NOT_ENABLED_FOR_THE_DESTINATION_CARD, "OPERATION NOT ENABLED FOR THE DESTINATION CARD");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("206")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.BIN_NOT_ALLOWED, "BIN NOT ALLOWED");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("207")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.STOCK_CARD, "STOCK CARD");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("205")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.THE_ACCOUNT_EXCEEDS_THE_MONTHLY_LIMIT, "THE ACCOUNT EXCEEDS THE MONTHLY LIMIT");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("101")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.THE_PAN_FIELD_IS_MANDATORY, "THE PAN FIELD IS MANDATORY");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("102")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.THE_AMOUNT_TO_BE_RECHARGE_IS_INCORRECT, "THE AMOUNT TO BE RECHARGE IS INCORRECT");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("3")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("8")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("33")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.THE_AMOUNT_MUST_BE_GREATER_THAN_0, "THE AMOUNT MUST BE GREATER THAN 0");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("1")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.SUCCESSFUL_RECHARGE, "SUCCESSFUL RECHARGE");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("410")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.ERROR_VALIDATING_PIN, "THE PAN FIELD IS MANDATORY");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("430")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.ERROR_VALIDATING_CVC1, "ERROR VALIDATING CVC1");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("400")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.ERROR_VALIDATING_CVC2, "ERROR VALIDATING CVC2");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("420")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.PIN_CHANGE_ERROR, "PIN CHANGE ERROR");
            } else if (balanceInquiryWithoutMovementsResponse.getCodigoError().equals("250")) {
                return new BalanceInquiryWithoutMovementsResponses(ResponseCode.ERROR_VALIDATING_THE_ITEM, " ERROR VALIDATING THE ITEM");
            }
            return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (IOException ex) {
            ex.printStackTrace();
            return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BalanceInquiryWithoutMovementsResponses(ResponseCode.INTERNAL_ERROR, "");
        }
    }

    public LimitAdvanceResponses limitAdvance(String email, Float amountWithdrawal, Long productId) {

        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        CredentialAutorizationClient credentialAutorizationClient = new CredentialAutorizationClient();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Timestamp begginingDateTime = new Timestamp(0);
        Timestamp endingDateTime = new Timestamp(0);
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        SimpleDateFormat sdg = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        String hour = sdf.format(timestamp);
        String date = sdg.format(timestamp);
        String yearSequence = year.format(timestamp);
        Integer recharge = DocumentTypeE.PROREC.getId();
        Integer transactionTypeE = TransactionTypeE.PROREC.getId();
        int totalTransactionsByUserDaily = 0;
        int totalTransactionsByUserMonthly = 0;
        int totalTransactionsByUserYearly = 0;
        short isPercentCommission = 0;
        Double totalAmountByUserDaily = 0.00D;
        Double totalAmountByUserMonthly = 0.00D;
        Double totalAmountByUserYearly = 0.00D;
        Float amountCommission = 0.00F;
        Float amountTransferTotal = 0.00F;
        Long idTransaction = 0L;
        Long idPreferenceField = 0L;
        ArrayList<Product> products = new ArrayList<Product>();
        List<PreferenceField> preferencesField = new ArrayList<PreferenceField>();
        List<PreferenceValue> preferencesValue = new ArrayList<PreferenceValue>();
        List<Commission> commissions = new ArrayList<Commission>();
        BalanceHistory balanceProductSource = null;
        BalanceHistory balanceProductDestination = null;
        Commission commissionTransfer = new Commission();

        try {
            ignoreSSLAutorization();

            //Se obtiene el usuario de registro unificado
            RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", email);
            Long userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

            //Se obtiene el saldo disponible de la tarjeta
            BalanceInquiryWithoutMovementsResponses balanceInquiryWithoutMovements = new BalanceInquiryWithoutMovementsResponses();
            balanceInquiryWithoutMovements = balanceInquiryWithoutMovements(email);
            Float availableBalance = Float.valueOf(balanceInquiryWithoutMovements.getBalanceInquiryWithoutMovementsCredential().getAvailableConsumption());

            try {
                //Se calcula la comisión de la operación 
                commissions = (List<Commission>) entityManager.createNamedQuery("Commission.findByProductTransactionType", Commission.class).setParameter("productId", productId).setParameter("transactionTypeId", Constante.sTransactionTypePR).getResultList();
                if (commissions.size() < 1) {
                    throw new NoResultException(Constante.sProductNotCommission + " in productId:" + productId + " and userId: " + userId);
                }
                for (Commission c : commissions) {
                    commissionTransfer = (Commission) c;
                    amountCommission = c.getValue();
                    isPercentCommission = c.getIsPercentCommision();
                    if (isPercentCommission == 1 && amountCommission > 0) {
                        amountCommission = (amountWithdrawal * amountCommission) / 100;
                    }
                    amountCommission = (amountCommission <= 0) ? 0.00F : amountCommission;
                }

                amountTransferTotal = amountWithdrawal + amountCommission;
                //Se valida si tiene saldo disponible
                if (availableBalance == null || availableBalance < amountTransferTotal) {
                    return new LimitAdvanceResponses(ResponseCode.USER_HAS_NOT_BALANCE, "The user has no balance available to complete the transaction");
                }

                //Validar preferencias
                begginingDateTime = Utils.DateTransaction()[0];
                endingDateTime = Utils.DateTransaction()[1];
                totalTransactionsByUserDaily = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));
                totalAmountByUserDaily = AmountMaxByUserCurrentDate(userId, EjbUtils.getBeginningDate(new Date()), EjbUtils.getEndingDate(new Date()));
                totalTransactionsByUserMonthly = TransactionsByUserCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));
                totalAmountByUserMonthly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateMonth(new Date()), EjbUtils.getEndingDate(new Date()));
                totalTransactionsByUserYearly = TransactionsByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));
                totalAmountByUserYearly = AmountMaxByBusinessCurrentDate(userId, EjbUtils.getBeginningDateAnnual(new Date()), EjbUtils.getEndingDate(new Date()));

                //Validar las preferencias
                List<Preference> preferences = getPreferences();
                for (Preference p : preferences) {
                    if (p.getName().equals(Constante.sPreferenceTransaction)) {
                        idTransaction = p.getId();
                    }
                }
                preferencesField = (List<PreferenceField>) entityManager.createNamedQuery("PreferenceField.findByPreference", PreferenceField.class).setParameter("preferenceId", idTransaction).getResultList();
                for (PreferenceField pf : preferencesField) {
                    switch (pf.getName()) {
                        case Constante.sValidatePreferenceTransaction11:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (pv.getValue().equals("0")) {
                                        return new LimitAdvanceResponses(ResponseCode.DISABLED_TRANSACTION, "Transactions disabled");
                                    }
                                }
                            }
                            break;
                        case Constante.sValidatePreferenceTransaction4:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (amountWithdrawal >= Double.parseDouble(pv.getValue())) {
                                        return new LimitAdvanceResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT, "The user exceeded the maximum amount per transaction");
                                    }
                                }
                            }
                            break;

                        case Constante.sValidatePreferenceTransaction5:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (totalTransactionsByUserDaily >= Integer.parseInt(pv.getValue())) {
                                        return new LimitAdvanceResponses(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per day");
                                    }
                                }
                            }
                            break;
                        case Constante.sValidatePreferenceTransaction6:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (totalAmountByUserDaily >= Double.parseDouble(pv.getValue())) {
                                        return new LimitAdvanceResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT_DIALY, "The user exceeded the maximum amount per day");
                                    }
                                }
                            }
                            break;
                        case Constante.sValidatePreferenceTransaction7:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (totalTransactionsByUserMonthly >= Integer.parseInt(pv.getValue())) {
                                        return new LimitAdvanceResponses(ResponseCode.TRANSACTION_QUANTITY_LIMIT_DIALY, "The user exceeded the maximum number of transactions per month");
                                    }
                                }
                            }
                            break;
                        case Constante.sValidatePreferenceTransaction8:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (totalAmountByUserMonthly >= Double.parseDouble(pv.getValue())) {
                                        return new LimitAdvanceResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT_MONTHLY, "The user exceeded the maximum amount per month");
                                    }
                                }
                            }
                            break;
                        case Constante.sValidatePreferenceTransaction9:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {
                                    if (totalTransactionsByUserYearly >= Integer.parseInt(pv.getValue())) {
                                        return new LimitAdvanceResponses(ResponseCode.TRANSACTION_QUANTITY_LIMIT_YEARLY, "The user exceeded the maximum number of transactions per year");
                                    }
                                }
                            }
                            break;
                        case Constante.sValidatePreferenceTransaction10:
                            if (pf.getEnabled() == 1) {
                                preferencesValue = getPreferenceValuePayment(pf, Constants.preferenceClassficationUser);
                                for (PreferenceValue pv : preferencesValue) {

                                    if (totalAmountByUserYearly >= Double.parseDouble(pv.getValue())) {
                                        return new LimitAdvanceResponses(ResponseCode.TRANSACTION_AMOUNT_LIMIT_YEARLY, "The user exceeded the maximum amount per year");
                                    }
                                }
                            }
                            break;
                    }
                }
            } catch (NoResultException e) {
                e.printStackTrace();
                return new LimitAdvanceResponses(ResponseCode.INTERNAL_ERROR, "Error in validation process");
            }

            //Se obtiene la tarjeta asociada al usuario
            CardResponse cardResponse = getCardByEmail(email);
            String alias = cardResponse.getaliasCard();

            //Se genera la secuencia de la transacción
            Sequences sequences = getSequencesByDocumentTypeByOriginApplication(Long.valueOf(recharge), Long.valueOf(Constants.ORIGIN_APPLICATION_APP_ALODIGA_WALLET_ID));
            String Numbersequence = generateNumberSequence(sequences);
            String sequence = transactionTypeE + yearSequence + Numbersequence;

            //Se efectua el retiro
            LimitAdvanceResponse limitAdvanceResponse = credentialAutorizationClient.limitAdvance(date, hour, alias, String.valueOf(amountWithdrawal), sequence);

            if (limitAdvanceResponse.getCodigoError().equals("-1")) {

                LimitAdvanceCredential limitAdvanceCredential = new LimitAdvanceCredential(limitAdvanceResponse.getCodigoError(), limitAdvanceResponse.getMensajeError(), limitAdvanceResponse.getCodigoRespuesta(), limitAdvanceResponse.getMensajeRespuesta(), limitAdvanceResponse.getCodigoAutorizacion());

                //Se guarda el objeto Transaction
                Transaction transaction = new Transaction();
                transaction.setId(null);
                transaction.setTransactionNumber(Numbersequence);
                transaction.setTransactionSequence(sequence);
                transaction.setUserSourceId(BigInteger.valueOf(userId));
                transaction.setUserDestinationId(BigInteger.valueOf(userId));
                Product product = entityManager.find(Product.class, productId);
                transaction.setProductId(product);
                TransactionType transactionType = entityManager.find(TransactionType.class, transactionTypeE);
                transaction.setTransactionTypeId(transactionType);
                TransactionSource transactionSource = entityManager.find(TransactionSource.class, transactionTypeE);
                transaction.setTransactionSourceId(transactionSource);
                Date date_ = new Date();
                Timestamp creationDate = new Timestamp(date_.getTime());
                transaction.setCreationDate(creationDate);
                transaction.setAmount(Float.valueOf(amountWithdrawal));
                transaction.setTransactionStatus(TransactionStatus.COMPLETED.name());
                transaction.setConcept(Constants.LIMIT_ADVANCE_CONCEPT_TRANSFER);
                transaction.setTotalAmount(Float.valueOf(amountWithdrawal));
                entityManager.persist(transaction);
                
                //BalanceHistory del producto de origen
                product = entityManager.find(Product.class, Product.PREPAID_CARD);
                balanceProductSource = loadLastBalanceHistoryByAccount(userId, product.getId());
                BalanceHistory balanceHistory = new BalanceHistory();
                balanceHistory.setId(null);
                balanceHistory.setUserId(userId);
                balanceHistory.setOldAmount(balanceProductSource.getCurrentAmount());
                Float currentAmountProductSource = balanceProductSource.getCurrentAmount() - amountTransferTotal;
                balanceHistory.setCurrentAmount(currentAmountProductSource);
                balanceHistory.setProductId(product);
                balanceHistory.setTransactionId(transaction);
                Date balanceDate = new Date();
                Timestamp balanceHistoryDate = new Timestamp(balanceDate.getTime());
                balanceHistory.setDate(balanceHistoryDate);
                balanceHistory.setVersion(balanceProductSource.getId());
                entityManager.persist(balanceHistory);

                //BalanceHistory del producto de destino
                balanceProductDestination = loadLastBalanceHistoryByAccount(userId, productId);
                balanceHistory = new BalanceHistory();
                balanceHistory.setId(null);
                balanceHistory.setUserId(userId);
                if (balanceProductDestination == null) {
                    balanceHistory.setOldAmount(Constante.sOldAmountUserDestination);
                    balanceHistory.setCurrentAmount(amountWithdrawal);
                } else {
                    balanceHistory.setOldAmount(balanceProductDestination.getCurrentAmount());
                    Float currentAmountUserDestination = balanceProductDestination.getCurrentAmount() + amountWithdrawal;
                    balanceHistory.setCurrentAmount(currentAmountUserDestination);
                    balanceHistory.setVersion(balanceProductDestination.getId());
                }
                balanceHistory.setProductId(product);
                balanceHistory.setTransactionId(transaction);
                balanceDate = new Date();
                balanceHistoryDate = new Timestamp(balanceDate.getTime());
                balanceHistory.setDate(balanceHistoryDate);
                entityManager.persist(balanceHistory);

                //Se obtiene la lista de productos del usuario
                try {
                    products = getProductsListByUserId(userId);
                    for (Product p : products) {
                        Float amount_1 = 0F;
                        try {
                            if (p.getId().equals(Product.PREPAID_CARD)) {
                                CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
                                AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
                                String cardEncripter = Base64.encodeBase64String(EncriptedRsa.encrypt(cardResponse.getaliasCard(), Constants.PUBLIC_KEY));
                                StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, cardEncripter);
                                if (statusCardResponse.getCodigo().equals("00")) {
                                    StatusAccountResponse accountResponse = accountCredentialServiceClient.statusAccount(Constants.CREDENTIAL_WEB_SERVICES_USER, Constants.CREDENTIAL_TIME_ZONE, statusCardResponse.getCuenta().toLowerCase().trim());
                                    amount_1 = Float.valueOf(accountResponse.getComprasDisponibles());
                                } else {
                                    amount_1 = Float.valueOf(0);
                                }
                            } else {
                                amount_1 = loadLastBalanceHistoryByAccount_(userId, p.getId()).getCurrentAmount();
                            }

                        } catch (NoResultException e) {
                            amount_1 = 0F;
                        } catch (ConnectException e) {
                            e.printStackTrace();
                            amount_1 = 0F;
                        } catch (SocketTimeoutException e) {
                            e.printStackTrace();
                            amount_1 = 0F;
                        }
                        p.setCurrentBalance(amount_1);
                    }
                } catch (Exception ex) {

                    return new LimitAdvanceResponses(ResponseCode.INTERNAL_ERROR, "Error loading products");
                }
                
                LimitAdvanceResponses limitAdvanceResponses = new LimitAdvanceResponses(limitAdvanceCredential, ResponseCode.SUCCESS, "SUCCESS", products);
                limitAdvanceResponses.setIdTransaction(transaction.getId().toString());
                limitAdvanceResponses.setProducts(products);
                
                return limitAdvanceResponses;

            } else if (limitAdvanceResponse.getCodigoError().equals("204")) {
                return new LimitAdvanceResponses(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
            } else if (limitAdvanceResponse.getCodigoError().equals("913")) {
                return new LimitAdvanceResponses(ResponseCode.INVALID_AMOUNT, "INVALID AMOUNT");
            } else if (limitAdvanceResponse.getCodigoError().equals("203")) {
                return new LimitAdvanceResponses(ResponseCode.EXPIRATION_DATE_DIFFERS, "EXPIRATION DATE DIFFERS");
            } else if (limitAdvanceResponse.getCodigoError().equals("205")) {
                return new LimitAdvanceResponses(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
            } else if (limitAdvanceResponse.getCodigoError().equals("202")) {
                return new LimitAdvanceResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (limitAdvanceResponse.getCodigoError().equals("201")) {
                return new LimitAdvanceResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (limitAdvanceResponse.getCodigoError().equals("03")) {
                return new LimitAdvanceResponses(ResponseCode.LOCKED_CARD, "LOCKED CARD");
            } else if (limitAdvanceResponse.getCodigoError().equals("28")) {
                return new LimitAdvanceResponses(ResponseCode.LOCKED_CARD, "LOCKED_CARD");
            } else if (limitAdvanceResponse.getCodigoError().equals("211")) {
                return new LimitAdvanceResponses(ResponseCode.BLOCKED_ACCOUNT, "BLOCKED ACCOUNT");
            } else if (limitAdvanceResponse.getCodigoError().equals("210")) {
                return new LimitAdvanceResponses(ResponseCode.INVALID_ACCOUNT, "INVALID ACCOUNT");
            } else if (limitAdvanceResponse.getCodigoError().equals("998")) {
                return new LimitAdvanceResponses(ResponseCode.INSUFFICIENT_BALANCE, "INSUFFICIENT BALANCE");
            } else if (limitAdvanceResponse.getCodigoError().equals("986")) {
                return new LimitAdvanceResponses(ResponseCode.INSUFFICIENT_LIMIT, "INSUFFICIENT LIMIT");
            } else if (limitAdvanceResponse.getCodigoError().equals("987")) {
                return new LimitAdvanceResponses(ResponseCode.CREDIT_LIMIT_0, "CREDIT LIMIT 0");
            } else if (limitAdvanceResponse.getCodigoError().equals("988")) {
                return new LimitAdvanceResponses(ResponseCode.CREDIT_LIMIT_0_OF_THE_DESTINATION_ACCOUNT, "CREDIT LIMIT 0 OF THE DESTINATION ACCOUNT");
            } else if (limitAdvanceResponse.getCodigoError().equals("999")) {
                return new LimitAdvanceResponses(ResponseCode.ERROR_PROCESSING_THE_TRANSACTION, "ERROR PROCESSING THE TRANSACTION");
            } else if (limitAdvanceResponse.getCodigoError().equals("101")) {
                return new LimitAdvanceResponses(ResponseCode.INVALID_TRANSACTION, "INVALID TRANSACTION");
            } else if (limitAdvanceResponse.getCodigoError().equals("105")) {
                return new LimitAdvanceResponses(ResponseCode.ERROR_VALIDATION_THE_TERMINAL, "ERROR VALIDATION THE TERMINAL");
            } else if (limitAdvanceResponse.getCodigoError().equals("241")) {
                return new LimitAdvanceResponses(ResponseCode.DESTINATION_ACCOUNT_LOCKED, "DESTINATION ACCOUNT LOCKED");
            } else if (limitAdvanceResponse.getCodigoError().equals("230")) {
                return new LimitAdvanceResponses(ResponseCode.INVALID_DESTINATION_CARD, "INVALID DESTINATION CARD");
            } else if (limitAdvanceResponse.getCodigoError().equals("240")) {
                return new LimitAdvanceResponses(ResponseCode.INVALID_DESTINATION_ACCOUNT, "INVALID DESTINATION ACCOUNT");
            } else if (limitAdvanceResponse.getCodigoError().equals("301")) {
                return new LimitAdvanceResponses(ResponseCode.THE_AMOUNT_MUST_BE_POSITIVE_AND_THE_AMOUNT_IS_REPORTED, "THE AMOUNT MUST BE POSITIVE AND THE AMOUNT IS REPORTED");
            } else if (limitAdvanceResponse.getCodigoError().equals("302")) {
                return new LimitAdvanceResponses(ResponseCode.INVALID_TRANSACTION_DATE, "INVALID TRANSACTION DATE");
            } else if (limitAdvanceResponse.getCodigoError().equals("303")) {
                return new LimitAdvanceResponses(ResponseCode.INVALID_TRANSACTION_TIME, "INVALID TRANSACTION TIME");
            } else if (limitAdvanceResponse.getCodigoError().equals("994")) {
                return new LimitAdvanceResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NN");
            } else if (limitAdvanceResponse.getCodigoError().equals("991")) {
                return new LimitAdvanceResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_SN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION SN");
            } else if (limitAdvanceResponse.getCodigoError().equals("992")) {
                return new LimitAdvanceResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
            } else if (limitAdvanceResponse.getCodigoError().equals("993")) {
                return new LimitAdvanceResponses(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
            } else if (limitAdvanceResponse.getCodigoError().equals("990")) {
                return new LimitAdvanceResponses(ResponseCode.TRASACTION_BETWEEN_ACCOUNTS_NOT_ALLOWED, "TRASACTION BETWEEN ACCOUNTS NOT ALLOWED");
            } else if (limitAdvanceResponse.getCodigoError().equals("120")) {
                return new LimitAdvanceResponses(ResponseCode.TRADE_VALIDATON_ERROR, "TRADE VALIDATON ERROR");
            } else if (limitAdvanceResponse.getCodigoError().equals("110")) {
                return new LimitAdvanceResponses(ResponseCode.DESTINATION_CARD_DOES_NOT_SUPPORT_TRANSACTION, "DESTINATION CARD DOES NOT SUPPORT TRANSACTION");
            } else if (limitAdvanceResponse.getCodigoError().equals("111")) {
                return new LimitAdvanceResponses(ResponseCode.OPERATION_NOT_ENABLED_FOR_THE_DESTINATION_CARD, "OPERATION NOT ENABLED FOR THE DESTINATION CARD");
            } else if (limitAdvanceResponse.getCodigoError().equals("206")) {
                return new LimitAdvanceResponses(ResponseCode.BIN_NOT_ALLOWED, "BIN NOT ALLOWED");
            } else if (limitAdvanceResponse.getCodigoError().equals("207")) {
                return new LimitAdvanceResponses(ResponseCode.STOCK_CARD, "STOCK CARD");
            } else if (limitAdvanceResponse.getCodigoError().equals("205")) {
                return new LimitAdvanceResponses(ResponseCode.THE_ACCOUNT_EXCEEDS_THE_MONTHLY_LIMIT, "THE ACCOUNT EXCEEDS THE MONTHLY LIMIT");
            } else if (limitAdvanceResponse.getCodigoError().equals("101")) {
                return new LimitAdvanceResponses(ResponseCode.THE_PAN_FIELD_IS_MANDATORY, "THE PAN FIELD IS MANDATORY");
            } else if (limitAdvanceResponse.getCodigoError().equals("102")) {
                return new LimitAdvanceResponses(ResponseCode.THE_AMOUNT_TO_BE_RECHARGE_IS_INCORRECT, "THE AMOUNT TO BE RECHARGE IS INCORRECT");
            } else if (limitAdvanceResponse.getCodigoError().equals("3")) {
                return new LimitAdvanceResponses(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
            } else if (limitAdvanceResponse.getCodigoError().equals("8")) {
                return new LimitAdvanceResponses(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
            } else if (limitAdvanceResponse.getCodigoError().equals("33")) {
                return new LimitAdvanceResponses(ResponseCode.THE_AMOUNT_MUST_BE_GREATER_THAN_0, "THE AMOUNT MUST BE GREATER THAN 0");
            } else if (limitAdvanceResponse.getCodigoError().equals("1")) {
                return new LimitAdvanceResponses(ResponseCode.SUCCESSFUL_RECHARGE, "SUCCESSFUL RECHARGE");
            } else if (limitAdvanceResponse.getCodigoError().equals("410")) {
                return new LimitAdvanceResponses(ResponseCode.ERROR_VALIDATING_PIN, "THE PAN FIELD IS MANDATORY");
            } else if (limitAdvanceResponse.getCodigoError().equals("430")) {
                return new LimitAdvanceResponses(ResponseCode.ERROR_VALIDATING_CVC1, "ERROR VALIDATING CVC1");
            } else if (limitAdvanceResponse.getCodigoError().equals("400")) {
                return new LimitAdvanceResponses(ResponseCode.ERROR_VALIDATING_CVC2, "ERROR VALIDATING CVC2");
            } else if (limitAdvanceResponse.getCodigoError().equals("420")) {
                return new LimitAdvanceResponses(ResponseCode.PIN_CHANGE_ERROR, "PIN CHANGE ERROR");
            } else if (limitAdvanceResponse.getCodigoError().equals("250")) {
                return new LimitAdvanceResponses(ResponseCode.ERROR_VALIDATING_THE_ITEM, " ERROR VALIDATING THE ITEM");
            }
            return new LimitAdvanceResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new LimitAdvanceResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new LimitAdvanceResponses(ResponseCode.INTERNAL_ERROR, "");
        }
    }
    
    public AccountTypeBankListResponse getAccountTypeBank() {
        List<AccountTypeBank> accounTypes = null;
        try {
            accounTypes = entityManager.createNamedQuery("AccountTypeBank.findAll", AccountTypeBank.class).getResultList();

        } catch (Exception e) {
            return new AccountTypeBankListResponse(ResponseCode.INTERNAL_ERROR, "Error loading countries");
        }
        return new ProductResponse(ResponseCode.SUCCESS, "", product);
    }
    
    
}
