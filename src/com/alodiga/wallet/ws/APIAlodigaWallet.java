package com.alodiga.wallet.ws;


import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.alodiga.wallet.bean.APICardOperations;
import com.alodiga.wallet.bean.APIOperations;
import com.alodiga.wallet.bean.APIRechargeOperations;
import com.alodiga.wallet.common.model.Address;
import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.common.model.StatusTransactionApproveRequest;
import com.alodiga.wallet.responses.AccountBankResponse;
import com.alodiga.wallet.responses.ActivateCardResponses;
import com.alodiga.wallet.responses.BalanceHistoryResponse;
import com.alodiga.wallet.responses.BalanceInquiryWithMovementsResponses;
import com.alodiga.wallet.responses.BalanceInquiryWithoutMovementsResponses;
import com.alodiga.wallet.responses.BankListResponse;
import com.alodiga.wallet.responses.BusinessHasProductResponse;
import com.alodiga.wallet.responses.BusinessShopResponse;
import com.alodiga.wallet.responses.CardListResponse;
import com.alodiga.wallet.responses.CardResponse;
import com.alodiga.wallet.responses.CheckStatusAccountResponses;
import com.alodiga.wallet.responses.CheckStatusCardResponses;
import com.alodiga.wallet.responses.CollectionListResponse;
import com.alodiga.wallet.responses.CountryListResponse;
import com.alodiga.wallet.responses.CreditCardListResponse;
import com.alodiga.wallet.responses.CumplimientResponse;
import com.alodiga.wallet.responses.DesactivateCardResponses;
import com.alodiga.wallet.responses.DispertionTransferResponses;
import com.alodiga.wallet.responses.ExchangeTokenPlaidResponses;
import com.alodiga.wallet.responses.LanguageListResponse;
import com.alodiga.wallet.responses.LimitAdvanceResponses;
import com.alodiga.wallet.responses.PaymentInfoListResponse;
import com.alodiga.wallet.responses.PaymentInfoResponse;
import com.alodiga.wallet.responses.ProductListResponse;
import com.alodiga.wallet.responses.ProductResponse;
import com.alodiga.wallet.responses.RechargeAfinitasResponses;
import com.alodiga.wallet.responses.RechargeValidationResponse;
import com.alodiga.wallet.responses.RemittanceResponse;
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
import com.alodiga.wallet.responses.TransferCardToCardResponses;
import com.alodiga.wallet.responses.UserHasProductResponse;

@WebService
public class APIAlodigaWallet {

    private static final Logger logger = Logger
            .getLogger(APIAlodigaWallet.class);

    @EJB
    private APIOperations operations;

    @EJB
    private APIRechargeOperations rechargeOperations;

    @EJB
    private APICardOperations cardOperations;


    @WebMethod
    public ProductResponse saveProduct(
            @WebParam(name = "enterprise") String enterpriseId,
            @WebParam(name = "category") String categoryId,
            @WebParam(name = "productIntegrationTypeId") String productIntegrationTypeId,
            @WebParam(name = "name") String name,
            @WebParam(name = "taxInclude") boolean taxInclude,
            @WebParam(name = "status") boolean status,
            @WebParam(name = "referenceCode") String referenceCode,
            @WebParam(name = "rateUrl") String rateUrl,
            @WebParam(name = "accesNumberUrl") String accesNumberUrl,
            @WebParam(name = "isFree") boolean isFree,
            @WebParam(name = "isAlocashproduct") boolean isAlocashproduct,
            @WebParam(name = "symbol") String symbol) {
        return operations.saveProduct(Long.valueOf(enterpriseId), Long.valueOf(categoryId), Long.valueOf(productIntegrationTypeId), name, taxInclude, status, referenceCode, rateUrl, accesNumberUrl, isFree, isAlocashproduct, symbol);
    }
    
    @WebMethod
    public ProductResponse getProductPrepaidCardByUser(
            @WebParam(name = "userId") Long userId) {
        return operations.getProductPrepaidCardByUser(userId);
    }

    @WebMethod
    public UserHasProductResponse saveUserHasProduct(
            @WebParam(name = "userId") String userId,
            @WebParam(name = "productId") String producId) {
        return operations.saveUserHasProduct(Long.valueOf(userId), Long.valueOf(producId));
    }

    @WebMethod
    public UserHasProductResponse saveUserHasProductDefault(
            @WebParam(name = "userId") String userId) {
        return operations.saveUserHasProductDefault(Long.valueOf(userId));
    }

    @WebMethod
    public ProductListResponse getProductsByUserId(
            @WebParam(name = "userId") String userId) {
        return operations.getProductsByUserId(Long.valueOf(userId));
    }
    
    @WebMethod
    public CountryListResponse getCountries() {
        return operations.getCountries();
    }

    @WebMethod
    public BankListResponse getBankApp() {
        return operations.getBankApp();
    }
    
    @WebMethod
    public BankListResponse getBankByUser(
        @WebParam(name = "userId") Long userId) {
        return operations.getBankByUser(userId);
    }

    @WebMethod
    public BankListResponse getBankByCountryApp(
            @WebParam(name = "countryId") String countryId) {
        return operations.getBankByCountryApp(Long.valueOf(countryId));
    }

    @WebMethod
    public TransactionResponse savePaymentShop(
            @WebParam(name = "cryptogramaShop") String cryptogramShop,
            @WebParam(name = "emailUser") String emailUser,
            @WebParam(name = "productId") Long productId,
            @WebParam(name = "amountPayment") Float amountPayment,
            @WebParam(name = "conceptTransaction") String conceptTransaction) {

        return operations.savePaymentShop(cryptogramShop, emailUser, productId, amountPayment,
                conceptTransaction);
    }

    @WebMethod
    public TransactionResponse saveTransferBetweenAccount(
            @WebParam(name = "cryptogramUserSource") String cryptogramUserSource,
            @WebParam(name = "emailUser") String emailUser,
            @WebParam(name = "productId") Long productId,
            @WebParam(name = "amountTransfer") Float amountTransfer,
            @WebParam(name = "conceptTransaction") String conceptTransaction,
            @WebParam(name = "cryptogramUserDestination") String cryptogramUserDestination,
            @WebParam(name = "idUserDestination") Long idUserDestination) {

        return operations.saveTransferBetweenAccount(cryptogramUserSource, emailUser, productId, amountTransfer,
                conceptTransaction, cryptogramUserSource, idUserDestination);
    }

    @WebMethod
    public TransactionResponse previewExchangeProduct(
            @WebParam(name = "emailUser") String emailUser,
            @WebParam(name = "productSourceId") Long productSourceId,
            @WebParam(name = "productDestinationId") Long productDestinationId,
            @WebParam(name = "amountExchange") Float amountExchange,
            @WebParam(name = "includedAmount") int includedAmount) {
        return operations.previewExchangeProduct(emailUser, productSourceId, productDestinationId, amountExchange, includedAmount);
    }

    @WebMethod
    public TransactionResponse exchangeProduct(
            @WebParam(name = "emailUser") String emailUser,
            @WebParam(name = "productSourceId") Long productSourceId,
            @WebParam(name = "productDestinationId") Long productDestinationId,
            @WebParam(name = "amountExchange") Float amountExchange,
            @WebParam(name = "conceptTransaction") String conceptTransaction,
            @WebParam(name = "includedAmount") int includedAmount) {
        return operations.exchangeProduct(emailUser, productSourceId, productDestinationId, amountExchange, conceptTransaction, includedAmount);

    }

    @WebMethod
    public TopUpInfoListResponse topUpList(
            @WebParam(name = "receiverNumber") String receiverNumber,
            @WebParam(name = "phoneNumber") String phoneNumber) {
        return operations.getTopUpInfo(receiverNumber, phoneNumber);
    }

    @WebMethod
    public TransactionListResponse getTransactionsByUserIdApp(
            @WebParam(name = "userId") String userId,
            @WebParam(name = "maxResult") String maxResult) {
        return operations.getTransactionsByUserIdApp(Long.valueOf(userId), Integer.valueOf(maxResult));
    }

    @WebMethod
    public TransactionResponse manualWithdrawals(
            @WebParam(name = "bankId") Long bankId,
            @WebParam(name = "emailUser") String emailUser,
            @WebParam(name = "accountBank") String accountBank,
            @WebParam(name = "amountWithdrawal") Float amountWithdrawal,
            @WebParam(name = "productId") Long productId,
            @WebParam(name = "conceptTransaction") String conceptTransaction,
            @WebParam(name = "documentTypeId") Long documentTypeId,
            @WebParam(name = "originApplicationId") Long originApplicationId) {
        return operations.manualWithdrawals(bankId, emailUser, accountBank, amountWithdrawal, productId, conceptTransaction,documentTypeId,originApplicationId);
    }

    @WebMethod
    public TransactionResponse manualRecharge(
            @WebParam(name = "bankId") Long bankId,
            @WebParam(name = "emailUser") String emailUser,
            @WebParam(name = "referenceNumberOperation") String referenceNumberOperation,
            @WebParam(name = "amountRecharge") Float amountRecharge,
            @WebParam(name = "productId") Long productId,
            @WebParam(name = "conceptTransaction") String conceptTransaction,
            @WebParam(name = "documentTypeId") Long documentTypeId,
            @WebParam(name = "originApplicationId") Long originApplicationId) {
        return operations.manualRecharge(bankId, emailUser, referenceNumberOperation, amountRecharge, productId, conceptTransaction,documentTypeId,originApplicationId);
    }

    @WebMethod
    public CountryListResponse getCountriesHasBank(
            @WebParam(name = "userId") Long userId) {
        return operations.getCountriesHasBank(userId);
    }

    @WebMethod
    public ProductListResponse getProductsByBankId(
            @WebParam(name = "bankId") String bankId,
            @WebParam(name = "userId") String userId) {
        return operations.getProductsByBankId(Long.valueOf(bankId), Long.valueOf(userId));
    }

    @WebMethod
    public BalanceHistoryResponse getBalanceHistoryByProductAndUser(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "productId") Long productId) {
        return operations.getBalanceHistoryByUserAndProduct(userId, productId);
    }

    @WebMethod
    public TransactionResponse saveRechargeTopUp(
            @WebParam(name = "emailUser") String emailUser,
            @WebParam(name = "productId") Long productId,
            @WebParam(name = "cryptogramUser") String cryptogramUser,
            @WebParam(name = "skudId") String skudId,
            @WebParam(name = "destinationNumber") String destinationNumber,
            @WebParam(name = "senderNumber") String senderNumber,
            @WebParam(name = "amountRecharge") Float amountRecharge,
            @WebParam(name = "amountPayment") Float amountPayment,
            @WebParam(name = "language") String language) {

        return operations.saveRechargeTopUp(emailUser, productId, cryptogramUser, skudId, destinationNumber, senderNumber,
                amountRecharge, amountPayment, language);
    }

    @WebMethod
    public TopUpCountryListResponse getTopUpCountries() {
        return operations.getTopUpCountries();
    }

    @WebMethod
    public LanguageListResponse getLanguage() {
        return operations.getLanguage();
    }

    @WebMethod
    public ProductListResponse getProductsPayTopUpByUserId(
            @WebParam(name = "userId") String userId) {
        return operations.getProductsPayTopUpByUserId(Long.valueOf(userId));
    }

    @WebMethod
    public ProductListResponse getProductsIsExchangeProductUserId(
            @WebParam(name = "userId") String userId) {
        return operations.getProductsIsExchangeProductUserId(Long.valueOf(userId));
    }

    @WebMethod
    public void sendMailTest() {
        operations.sendmailTest();
    }

    @WebMethod
    public String sendSmsSimbox(
            @WebParam(name = "phoneNumber") String phoneNumber,
            @WebParam(name = "text") String text,
            @WebParam(name = "userId") Long userId) {
        return operations.sendSmsSimbox(phoneNumber, text, userId);
    }

    @WebMethod
    public CumplimientResponse getCumplimientStatus(
            @WebParam(name = "userId") String userId) {
        return operations.getCumplimientStatus(Long.valueOf(userId));
    }

    @WebMethod
    public CollectionListResponse getValidateCollection(
            @WebParam(name = "userId") String userId,
            @WebParam(name = "language") String language) {
        return operations.getValidateCollection(Long.valueOf(userId), language);
    }

    @WebMethod
    public Country getCountryCode(
            @WebParam(name = "strAni") String strAni) {
        return operations.getCountryCode(strAni);
    }

    @WebMethod
    public Address saveAddress(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "estado") String estado,
            @WebParam(name = "ciudad") String ciudad,
            @WebParam(name = "zipCode") String zipCode,
            @WebParam(name = "addres1") String addres1) throws Exception {
        return operations.saveAddress(Long.valueOf(userId), estado, ciudad, zipCode, addres1);
    }

    @WebMethod
    public CollectionListResponse saveCumplimient(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "imgDocument") byte[] imgDocument,
            @WebParam(name = "imgProfile") byte[] imgProfile,
            @WebParam(name = "estado") String estado,
            @WebParam(name = "ciudad") String ciudad,
            @WebParam(name = "zipCode") String zipCode,
            @WebParam(name = "addres1") String addres1) {
        return operations.saveCumplimient(Long.valueOf(userId), imgDocument, imgProfile, estado, ciudad, zipCode, addres1);
    }

    @WebMethod
    public ActivateCardResponses activateCard(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "card") String card,
            @WebParam(name = "timeZone") String timeZone,
            @WebParam(name = "status") String status) {
        return operations.activateCard(userId, card, timeZone, status);
    }

    @WebMethod
    public DesactivateCardResponses desactivateCard(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "card") String card,
            @WebParam(name = "timeZone") String timeZone,
            @WebParam(name = "status") String status) {
        return operations.desactivateCard(userId, card, timeZone, status);
    }

    @WebMethod
    public CheckStatusCardResponses checkStatusCard(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "card") String card,
            @WebParam(name = "timeZone") String timeZone) {
        return operations.checkStatusCard(userId, card, timeZone);
    }

    @WebMethod
    public CheckStatusAccountResponses checkStatusAccount(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "card") String card,
            @WebParam(name = "timeZone") String timeZone) {
        return operations.checkStatusAccount(userId, card, timeZone);
    }

    @WebMethod
    public Boolean hasPrepayCardAsociated(
            @WebParam(name = "userId") Long userId) throws Exception {
        return operations.hasPrepayCardAsociated(userId);
    }

    @WebMethod
    public Boolean hasPrepayCard(
            @WebParam(name = "userId") Long userId) throws Exception {
        return operations.hasPrepayCard(userId);
    }

    @WebMethod
    public CardResponse getCardByUserId(
            @WebParam(name = "userId") String userId) {
        return operations.getCardByUserId(Long.valueOf(userId));
    }

    @WebMethod
    public TransferCardToCardResponses transferCardToCardAutorization(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "numberCardOrigin") String numberCardOrigin,
            @WebParam(name = "numberCardDestinate") String numberCardDestinate,
            @WebParam(name = "balance") String balance,
            @WebParam(name = "idUserDestination") Long idUserDestination,
            @WebParam(name = "conceptTransaction") String conceptTransaction) {
        return operations.transferCardToCardAutorization(userId, numberCardOrigin, numberCardDestinate, balance, idUserDestination, conceptTransaction);
    }

    @WebMethod
    public CardListResponse getCardsListByUserId(
            @WebParam(name = "userId") String userId) throws Exception {
        return operations.getCardsListByUserId(Long.valueOf(userId));
    }

    @WebMethod
    public ProductListResponse getProductsRemettenceByUserId(
            @WebParam(name = "userId") String userId) {
        return operations.getProductsRemettenceByUserId(Long.valueOf(userId));
    }

    @WebMethod
    public RemittanceResponse processRemettenceAccount(
            @WebParam(name = "userId") String userId,
            @WebParam(name = "amountOrigin") Float amountOrigin,
            @WebParam(name = "totalAmount") Float totalAmount,
            @WebParam(name = "amountDestiny") Float amountDestiny,
            @WebParam(name = "exchangeRateId") String exchangeRateId,
            @WebParam(name = "ratePaymentNetworkId") String ratePaymentNetworkId,
            @WebParam(name = "originCurrentId") String originCurrentId,
            @WebParam(name = "destinyCurrentId") String destinyCurrentId,
            @WebParam(name = "paymentNetworkId") String paymentNetworkId,
            @WebParam(name = "deliveryFormId") String deliveryFormId,
            @WebParam(name = "addressId") String addressId,
            @WebParam(name = "remittentCountryId") String remittentCountryId,/**/
            @WebParam(name = "remittentStateName") String remittentStateName,/**/
            @WebParam(name = "remittentCityName") String remittentCityName,/**/
            @WebParam(name = "remittentAddress") String remittentAddress,/**/
            @WebParam(name = "remittentZipCode") String remittentZipCode,/**/
            @WebParam(name = "remittentStateId") String remittentStateId,/**/
            @WebParam(name = "remittentCityId") String remittentCityId,/**/
            @WebParam(name = "receiverFirstName") String receiverFirstName,
            @WebParam(name = "receiverMiddleName") String receiverMiddleName,
            @WebParam(name = "receiverLastName") String receiverLastName,
            @WebParam(name = "receiverSecondSurname") String receiverSecondSurname,
            @WebParam(name = "receiverPhoneNumber") String receiverPhoneNumber,
            @WebParam(name = "receiverEmail") String receiverEmail,
            @WebParam(name = "receiverCountryId") String receiverCountryId,/**/
            @WebParam(name = "receiverCityId") String receiverCityId,/**/
            @WebParam(name = "receiverStateId") String receiverStateId,/**/
            @WebParam(name = "receiverStateName") String receiverStateName,
            @WebParam(name = "receiverCityName") String receiverCityName,
            @WebParam(name = "receiverAddress") String receiverAddress,
            @WebParam(name = "receiverZipCode") String receiverZipCode,
            @WebParam(name = "languageId") String languageId) {

        return operations.processRemettenceAccount(Long.valueOf(userId), amountOrigin, totalAmount, amountDestiny, exchangeRateId, ratePaymentNetworkId, originCurrentId, destinyCurrentId, paymentNetworkId, deliveryFormId, Long.valueOf(addressId), remittentCountryId, remittentStateName, remittentCityName, remittentAddress, remittentZipCode, remittentStateId, remittentCityId, receiverFirstName, receiverMiddleName, receiverLastName, receiverSecondSurname, receiverPhoneNumber, receiverEmail, receiverCountryId, receiverCityId, receiverStateId, receiverStateName, receiverCityName, receiverAddress, receiverZipCode, languageId);

    }

    @WebMethod
    public RechargeAfinitasResponses saveRechargeAfinitas(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "amountRecharge") Float amountRecharge,
            @WebParam(name = "currency") String currency,
            @WebParam(name = "cardNumber") String cardNumber,
            @WebParam(name = "expirationYear") String expirationYear,
            @WebParam(name = "expirationMonth") String expirationMonth,
            @WebParam(name = "cvv") String cvv,
            @WebParam(name = "cardHolderName") String cardHolderName,
            @WebParam(name = "paymentInfoId") Long paymentInfoId) {
        return operations.saveRechargeAfinitas(userId, amountRecharge, currency, cardNumber, expirationYear, expirationMonth, cvv, cardHolderName, paymentInfoId);
    }

    @WebMethod(operationName = "validateRechargeProduct")
    public RechargeValidationResponse validateRechargeProduct(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "productID") Long productID,
            @WebParam(name = "amountToRecharge") Double amountToRecharge,
            @WebParam(name = "includeFee") boolean includeFee) {
        return rechargeOperations.getRechargeProductValidation(userId, productID,
                amountToRecharge, includeFee);
    }

    @WebMethod(operationName = "rechargeWalletProduct")
    public TransactionResponse rechargeWalletProduct(
            @WebParam(name = "businessId") final Long businessId,
            @WebParam(name = "userId") final Long userId,
            @WebParam(name = "productId") Long productId,
            @WebParam(name = "amountToRecharge") Double amountToRecharge,
            @WebParam(name = "includeFee") boolean includeFee) {
        return rechargeOperations.rechargeWallet(businessId, userId, productId,
                amountToRecharge, includeFee);
    }

    @WebMethod(operationName = "validateRechargeCard")
    public RechargeValidationResponse validateRechargeCard(
            @WebParam(name = "rechargeAmount") Double rechargeAmount,
            @WebParam(name = "includeFee") boolean includeFee) {
        return rechargeOperations.getRechargeCardValidation(rechargeAmount, includeFee);
    }

    @WebMethod(operationName = "rechargeCard")
    public TransactionResponse rechargeCard(
            @WebParam(name = "businessId") Long businessId,
            @WebParam(name = "eCardNumber") String eCardNumber,
            @WebParam(name = "rechargeAmount") Double rechargeAmount,
            @WebParam(name = "includeFee") boolean includeFee) {
        return rechargeOperations.rechargeCard(businessId, eCardNumber, rechargeAmount, includeFee);
    }

    @WebMethod(operationName = "activateCardbyBusiness")
    public ActivateCardResponses activateCardbyBusiness(
            @WebParam(name = "businessId") Long businessId,
            @WebParam(name = "card") String card,
            @WebParam(name = "timeZone") String timeZone) {
        return cardOperations.activateCardByBusiness(businessId, card, timeZone);
    }

    @WebMethod(operationName = "desactivateCardByBusiness")
    public DesactivateCardResponses desactivateCardByBusiness(
            @WebParam(name = "businessId") Long businessId,
            @WebParam(name = "card") String card,
            @WebParam(name = "timeZone") String timeZone) {
        return cardOperations.deactivateCardByBusiness(businessId, card, timeZone);
    }

    @WebMethod(operationName = "checkStatusCardByBusiness")
    public CheckStatusCardResponses checkStatusCardbyBusiness(
            @WebParam(name = "card") String card,
            @WebParam(name = "timeZone") String timeZone) {
        return cardOperations.checkStatusCard(card, timeZone);
    }

    @WebMethod
    public PaymentInfoListResponse getPaymentInfo(
            @WebParam(name = "userApi") String userApi,
            @WebParam(name = "passwordApi") String passwordApi,
            @WebParam(name = "userId") String userId) {
        return operations.getPaymentInfo(userApi, passwordApi, Long.valueOf(userId));
    }

    @WebMethod
    public CreditCardListResponse getCreditCardType(
            @WebParam(name = "userApi") String userApi,
            @WebParam(name = "passwordApi") String passwordApi) {
        return operations.getCreditCardType(userApi, passwordApi);
    }

    @WebMethod
    public PaymentInfoResponse savePaymentInfo(
            @WebParam(name = "userApi") String userApi,
            @WebParam(name = "passwordApi") String passwordApi,
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "estado") String estado,
            @WebParam(name = "ciudad") String ciudad,
            @WebParam(name = "zipCode") String zipCode,
            @WebParam(name = "addres1") String addres1,
            @WebParam(name = "paymentPatnerId") Long paymentPatnerId,
            @WebParam(name = "paymentTypeId") Long paymentTypeId,
            @WebParam(name = "creditCardTypeId") Long creditCardTypeId,
            @WebParam(name = "creditCardName") String creditCardName,
            @WebParam(name = "creditCardNumber") String creditCardNumber,
            @WebParam(name = "creditCardCVV") String creditCardCVV,
            @WebParam(name = "creditCardDate") String creditCardDate) throws Exception {
        return operations.savePaymentInfo(userApi, passwordApi, Long.valueOf(userId), estado, ciudad, zipCode, addres1, paymentPatnerId, paymentTypeId, creditCardTypeId, creditCardName, creditCardNumber, creditCardCVV, creditCardDate);
    }

    @WebMethod
    public PaymentInfoResponse ChangeStatusPaymentInfo(
            @WebParam(name = "userApi") String userApi,
            @WebParam(name = "passwordApi") String passwordApi,
            @WebParam(name = "userId") String userId,
            @WebParam(name = "paymentInfoId") Long paymentInfoId,
            @WebParam(name = "status") boolean status) {
        return operations.ChangeStatusPaymentInfo(userApi, passwordApi, Long.valueOf(userId), paymentInfoId, status);
    }

    @WebMethod
    public ProductListResponse getProductsRechargePaymentByUserId(
            @WebParam(name = "userId") String userId) {
        return operations.getProductsRechargePaymentByUserId(Long.valueOf(userId));
    }

    @WebMethod
    public ProductListResponse generarCodigoMovilSMS(
            @WebParam(name = "movil") String movil,
            @WebParam(name = "codigo") String codigo) {
        return operations.generarCodigoMovilSMS(movil, codigo);
    }

    @WebMethod
    public void sendMail(
            @WebParam(name = "subject") String subject,
            @WebParam(name = "body") String body,
            @WebParam(name = "to") String to,
            @WebParam(name = "from") String from) {
        operations.sendMail(subject, body, to, from);
    }

    @WebMethod
    public void sendSMS(
            @WebParam(name = "movil") String movil,
            @WebParam(name = "message") String message) {
        operations.sendSMS(movil, message);
    }

    @WebMethod
    public ExchangeTokenPlaidResponses publicTokenPlaid(
            @WebParam(name = "methods") String methods) {
        return operations.publicTokenPlaid(methods);

    }

    @WebMethod
    public RetriveAuthPlaidResponses retriveAuthPlaid() {
        return operations.retriveAuthPlaid();

    }

    @WebMethod
    public RetriveTransactionPlaidResponses retriveTransactionPlaid() {
        return operations.retriveTransactionPlaid();

    }

    @WebMethod
    public RetriveBalancePlaidResponses retriveBalancePlaid() {
        return operations.retriveBalancePlaid();

    }

    @WebMethod
    public RetriveIdentityPlaidResponses retriveIdentityPlaid() {
        return operations.retriveIdentityPlaid();

    }

    @WebMethod
    public RetriveIncomePlaidResponses retriveIncomePlaid() {
        return operations.retriveIncomePlaid();

    }  
    
    @WebMethod
    public StatusTransactionApproveRequest getStatusTransactionAprove(
    @WebParam(name = "status") String status) {
        return operations.getStatusTransactionAprove(status);

    }
 
    
    @WebMethod
    public AccountBankResponse saveAccountBank(
    @WebParam(name = "unifiedRegistryId") Long unifiedRegistryId,
            @WebParam(name = "accountNumber") String accountNumber,
            @WebParam(name = "bankId") Long bankId,
            @WebParam(name = "accountTypeBankId") Integer accountTypeBankId) {
        return operations.saveAccountBank(unifiedRegistryId,accountNumber,bankId,accountTypeBankId);

    }
    
    @WebMethod
    public AccountBankResponse saveAccountBankUser(
    @WebParam(name = "bankId") Long bankId,
            @WebParam(name = "unifiedRegistryId") Long unifiedRegistryId,
            @WebParam(name = "accountNumber") String accountNumber,
            @WebParam(name = "accountTypeBankId") Integer accountTypeBankId) {
        return operations.saveAccountBankUser(bankId,unifiedRegistryId,accountNumber,accountTypeBankId);

    }
    
    @WebMethod
    public TransactionApproveRequestResponse saveTransactionApproveRequest(
    @WebParam(name = "unifiedRegistryId") Long unifiedRegistryId,
            @WebParam(name = "productId") Long productId,
            @WebParam(name = "transactionId") Long transactionId,
            @WebParam(name = "bankOperationId") Long bankOperationId,
            @WebParam(name = "documentTypeId") Long documentTypeId,
            @WebParam(name = "originApplicationId") Long originApplicationId){
        return operations.saveTransactionApproveRequest(unifiedRegistryId,productId,transactionId,bankOperationId,documentTypeId,originApplicationId,0L);

    }
    
    @WebMethod
    public BusinessHasProductResponse saveBusinessHasProductDefault(
            @WebParam(name = "businessId") String businessId) {
        return operations.saveBusinessHasProductDefault(Long.valueOf(businessId));
    }

    @WebMethod
    public ProductListResponse getProductsByBusinessId(
            @WebParam(name = "businessId") String businessId) {
        return operations.getProductsByBusinessId(Long.valueOf(businessId));
    }
    
    @WebMethod
    public BalanceHistoryResponse getBalanceHistoryByProductAndBusinessId(
            @WebParam(name = "businessId") Long businessId,
            @WebParam(name = "productId") Long productId) {
        return operations.getBalanceHistoryByBusinessAndProduct(businessId, productId);
    }
    
    @WebMethod
    public TransactionResponse manualWithdrawalsBusiness(
            @WebParam(name = "bankId") Long bankId,
            @WebParam(name = "accountBankBusinessId") Long accountBankBusinessId,
            @WebParam(name = "accountBank") String accountBank,
            @WebParam(name = "amountWithdrawal") Float amountWithdrawal,
            @WebParam(name = "productId") Long productId,
            @WebParam(name = "conceptTransaction") String conceptTransaction,
            @WebParam(name = "businessId") Long businessId,
            @WebParam(name = "businessTransactionId") Long businessTransactionId) {
        return operations.manualWithdrawalsBusiness(bankId,accountBankBusinessId, accountBank, amountWithdrawal, productId, conceptTransaction, businessId,businessTransactionId);
    }
    
    @WebMethod
    public TransactionResponse saveTransferBetweenBusinessWithUser(
            @WebParam(name = "productId") Long productId,
            @WebParam(name = "amountTransfer") Float amountTransfer,
            @WebParam(name = "conceptTransaction") String conceptTransaction,
            @WebParam(name = "idUserDestination") Long idUserDestination,
            @WebParam(name = "businessId") Long businessId) {

        return operations.saveTransferBetweenBusinessWithUser(productId, amountTransfer,
                conceptTransaction, idUserDestination, businessId);
    }
    
    @WebMethod
    public TransactionResponse saveTransferBetweenBusinessAccount(
            @WebParam(name = "productId") Long productId,
            @WebParam(name = "amountTransfer") Float amountTransfer,
            @WebParam(name = "conceptTransaction") String conceptTransaction,
            @WebParam(name = "businessId") Long businessId,
            @WebParam(name = "businessDestinationId") Long businessDestinationId) {
        return operations.saveTransferBetweenBusinessAccount(productId, amountTransfer,
                conceptTransaction,businessId, businessDestinationId);
    }
    
    @WebMethod
    public TransactionListResponse getTransactionsByBusinessId(
            @WebParam(name = "businessId") String businessId,
            @WebParam(name = "maxResult") String maxResult) {
        return operations.getTransactionsByBusinessId(Long.valueOf(businessId), Integer.valueOf(maxResult));
    }
    
    
    @WebMethod
    public TransactionListResponse getTransactionsByBusinessIdBetweenDate(
            @WebParam(name = "businessId") Long businessId,
            @WebParam(name = "from") String from,
            @WebParam(name = "to") String to) {
        return operations.getTransactionsByBusinessIdBetweenDate(Long.valueOf(businessId), from, to);
    }

    
    @WebMethod
    public BusinessShopResponse getBusinessInfoByCryptogram(
            @WebParam(name = "cryptogram") String cryptogram){
        return operations.getBusinessInfoByCryptogram(cryptogram);
    }
    
    @WebMethod
    public CardResponse getCardByPhone(
            @WebParam(name = "phone") String phone){
        return operations.getCardByPhone(phone);
    }
    
    @WebMethod
    public CardResponse getCardByEmail(
            @WebParam(name = "email") String email){
        return operations.getCardByEmail(email);
    }
    
    @WebMethod
    public CardResponse getCardByIdentificationNumber(
            @WebParam(name = "numberIdentification") String numberIdentification){
        return operations.getCardByIdentificationNumber(numberIdentification);
    }
    
    @WebMethod
    public DispertionTransferResponses dispertionTransfer(
            @WebParam(name = "email") String email,
            @WebParam(name = "balance") Float balance,
            @WebParam(name = "productId") Long productId){
        return operations.dispertionTransfer(email,balance,productId);
    }
    
    @WebMethod
    public ProductListResponse getProductsUsePrepaidCardByUserId(
            @WebParam(name = "userId") Long userId){
        return operations.getProductsUsePrepaidCardByUserId(userId);
    }
    
    
    @WebMethod
    public BalanceInquiryWithoutMovementsResponses balanceInquiryWithoutMovements(
            @WebParam(name = "email") String email){
        return operations.balanceInquiryWithoutMovements(email);
    }
    
    @WebMethod
    public BalanceInquiryWithMovementsResponses balanceInquiryWithMovements(
            @WebParam(name = "email") String email){
        return operations.balanceInquiryWithMovements(email);
    }
    
    @WebMethod
    public LimitAdvanceResponses limitAdvance(
            @WebParam(name = "email") String email,
            @WebParam(name = "balance") Float balance,
            @WebParam(name = "productId") Long productId){
        return operations.limitAdvance(email,balance,productId);
    }
    
}
