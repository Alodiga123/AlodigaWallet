package com.alodiga.wallet.bean;

import cardcredentialserviceclient.CardCredentialServiceClient;
import com.alodiga.account.client.AccountCredentialServiceClient;
import com.alodiga.card.credential.response.ChangeStatusCardResponse;
import com.alodiga.card.credential.response.StatusCardResponse;
import com.alodiga.wallet.dao.TransactionDAO;
import com.alodiga.wallet.common.model.Commission;
import com.alodiga.wallet.common.model.CommissionItem;
import com.alodiga.wallet.common.model.Product;
import com.alodiga.wallet.common.model.Transaction;
import com.alodiga.wallet.common.model.TransactionSource;
import com.alodiga.wallet.common.model.TransactionStatus;
import com.alodiga.wallet.common.model.TransactionType;
import com.alodiga.wallet.responses.ActivateCardResponses;
import com.alodiga.wallet.responses.ChangeStatusCredentialCard;
import com.alodiga.wallet.responses.CheckStatusCardResponses;
import com.alodiga.wallet.responses.CheckStatusCredentialCard;
import com.alodiga.wallet.responses.DesactivateCardResponses;
import com.alodiga.wallet.responses.ResponseCode;
import com.alodiga.wallet.common.utils.Constants;
import static com.alodiga.wallet.common.utils.EncriptedRsa.encrypt;
import com.alodiga.wallet.common.utils.S3cur1ty3Cryt3r;
import com.alodiga.wallet.utils.TransactionHelper;
import java.io.IOException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.codec.binary.Base64;

@Stateless(name = "FsProcessorCardWallet",
        mappedName = "ejb/FsProcessorCardWallet")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class APICardOperations {

    private static final String CARD_ACTIVE_STATUS = "01";
    private static final String CARD_DEACTIVE_STATUS = "24";

    
    private EntityManager entityManager;

    private int credentialsRetries = 0;

    private void saveCardFromBusinessTransaction(Long businessId, TransactionType type) {

        Product product = entityManager.find(Product.class, Product.PREPAID_CARD);

        Commission commission = TransactionDAO.getCommision(product, type, entityManager);
        if (commission == null) {
            return;
        }

        Transaction transaction = new Transaction();
        transaction.setUserSourceId(BigInteger.valueOf(businessId));
        transaction.setUserDestinationId(null);
        transaction.setProductId(product);
        transaction.setTransactionTypeId(type);
        TransactionSource transactionSource = entityManager.find(TransactionSource.class, 2L);
        transaction.setTransactionSourceId(transactionSource);
        transaction.setCreationDate(new Timestamp(System.currentTimeMillis()));
        transaction.setConcept(type.getValue());
        transaction.setAmount(0);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED.name());
        transaction.setTotalAmount(0F);
        transaction.setTransactionNumber(TransactionHelper.generateNextRechargeSequence(TransactionHelper.OriginApplicationType.BUSINESS_PORTAL));
                entityManager.persist(transaction);

        CommissionItem commissionItem = new CommissionItem(commission.getValue(),
                new Timestamp(System.currentTimeMillis()), transaction, commission);
        entityManager.persist(commissionItem);
    }

    public ActivateCardResponses activateCardByBusiness(Long businessId, String eCardNumber, String timeZone) {

        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
        try {
            String Card = S3cur1ty3Cryt3r.aloEncrpter(eCardNumber, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            String encryptedString = Base64.encodeBase64String(encrypt(Card, Constants.PUBLIC_KEY));
            ChangeStatusCardResponse response = cardCredentialServiceClient.changeStatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, timeZone, encryptedString, CARD_ACTIVE_STATUS);

            switch (response.getCodigoRespuesta()) {
                case "00":
                    ChangeStatusCredentialCard changeStatusCredentialcardResponse = new ChangeStatusCredentialCard(response.getInicio(), response.getFin(), response.getTiempo(), response.getCodigoRespuesta(), response.getDescripcion(), response.getTicketWS());
                    ActivateCardResponses activateCardResponses = new ActivateCardResponses(changeStatusCredentialcardResponse);
                    activateCardResponses.setNumberCard(eCardNumber);

                    TransactionType type = entityManager.find(TransactionType.class, 1L);//TODO transaction type
                    saveCardFromBusinessTransaction(businessId, type);

                    return activateCardResponses;
                case "-024":
                    return new ActivateCardResponses(ResponseCode.NOT_ALLOWED_TO_CHANGE_STATE, "NOT ALLOWED TO CHANGE STATE");
                case "-011":
                    return new ActivateCardResponses(ResponseCode.AUTHENTICATE_IMPOSSIBLE, "Authenticate Impossible");
                case "-13":
                    return new ActivateCardResponses(ResponseCode.SERVICE_NOT_ALLOWED, "Service Not Allowed");
                case "-14":
                    return new ActivateCardResponses(ResponseCode.OPERATION_NOT_ALLOWED_FOR_THIS_SERVICE, "Operation Not Allowed For This Service");
                case "-060":
                    return new ActivateCardResponses(ResponseCode.UNABLE_TO_ACCESS_DATA, "Unable to Access Data");
                case "-120":
                    return new ActivateCardResponses(ResponseCode.THERE_ARE_NO_RECORDS_FOR_THE_REQUESTED_SEARCH, "There are no Records for the Requested Search");
                case "-140":
                    return new ActivateCardResponses(ResponseCode.THE_REQUESTED_PRODUCT_DOES_NOT_EXIST, "The Requested Product does not Exist");
                case "-160":
                    return new ActivateCardResponses(ResponseCode.THE_NUMBER_OF_ORDERS_ALLOWED_IS_EXCEEDED, "The Number of Orders Allowed is Exceeded");
                default:
                    return new ActivateCardResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new ActivateCardResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ActivateCardResponses(ResponseCode.INTERNAL_ERROR, "");
        }
    }

    public DesactivateCardResponses deactivateCardByBusiness(Long businessId, String eCardNumber, String timeZone) {
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        AccountCredentialServiceClient accountCredentialServiceClient = new AccountCredentialServiceClient();
        try {
            String Card = S3cur1ty3Cryt3r.aloEncrpter(eCardNumber, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            String encryptedString = Base64.encodeBase64String(encrypt(Card, Constants.PUBLIC_KEY));
            ChangeStatusCardResponse response = cardCredentialServiceClient.changeStatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, timeZone, encryptedString, CARD_DEACTIVE_STATUS);

            switch (response.getCodigoRespuesta()) {
                case "00":
                    ChangeStatusCredentialCard changeStatusCredentialcardResponse = new ChangeStatusCredentialCard(response.getInicio(), response.getFin(), response.getTiempo(), response.getCodigoRespuesta(), response.getDescripcion(), response.getTicketWS());
                    DesactivateCardResponses deactivateResponse = new DesactivateCardResponses(changeStatusCredentialcardResponse);

                    TransactionType type = entityManager.find(TransactionType.class, 1L);//TODO transaction type
                    saveCardFromBusinessTransaction(businessId, type);

                    return deactivateResponse;
                case "-024":
                    return new DesactivateCardResponses(ResponseCode.NOT_ALLOWED_TO_CHANGE_STATE, "NOT ALLOWED TO CHANGE STATE");
                case "-011":
                    return new DesactivateCardResponses(ResponseCode.AUTHENTICATE_IMPOSSIBLE, "Authenticate Impossible");
                case "-13":
                    return new DesactivateCardResponses(ResponseCode.SERVICE_NOT_ALLOWED, "Service Not Allowed");
                case "-14":
                    return new DesactivateCardResponses(ResponseCode.OPERATION_NOT_ALLOWED_FOR_THIS_SERVICE, "Operation Not Allowed For This Service");
                case "-060":
                    return new DesactivateCardResponses(ResponseCode.UNABLE_TO_ACCESS_DATA, "Unable to Access Data");
                case "-120":
                    return new DesactivateCardResponses(ResponseCode.THERE_ARE_NO_RECORDS_FOR_THE_REQUESTED_SEARCH, "There are no Records for the Requested Search");
                case "-140":
                    return new DesactivateCardResponses(ResponseCode.THE_REQUESTED_PRODUCT_DOES_NOT_EXIST, "The Requested Product does not Exist");
                case "-160":
                    return new DesactivateCardResponses(ResponseCode.THE_NUMBER_OF_ORDERS_ALLOWED_IS_EXCEEDED, "The Number of Orders Allowed is Exceeded");
                default:
                    return new DesactivateCardResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new DesactivateCardResponses(ResponseCode.INTERNAL_ERROR, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new DesactivateCardResponses(ResponseCode.INTERNAL_ERROR, "");
        }
    }

    public CheckStatusCardResponses checkStatusCard(String encriptedCard, String timeZone) {
        CardCredentialServiceClient cardCredentialServiceClient = new CardCredentialServiceClient();
        try {
            String card = S3cur1ty3Cryt3r.aloEncrpter(encriptedCard, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            String encryptedString = Base64.encodeBase64String(encrypt(card, Constants.PUBLIC_KEY));
            long currentTime = System.currentTimeMillis();
            StatusCardResponse statusCardResponse = cardCredentialServiceClient.StatusCard(Constants.CREDENTIAL_WEB_SERVICES_USER, timeZone, encryptedString);
            System.out.println("Resposne statusCardResponse : " + statusCardResponse.getCodigo() + " tiempo " + Long.toString(System.currentTimeMillis() - currentTime) + " retries " + credentialsRetries);
            if (!statusCardResponse.getCodigo().equals("-02")) {
                credentialsRetries = 0;
            }
            switch (statusCardResponse.getCodigo()) {
                case "00":
                    CheckStatusCredentialCard checkStatusCredentialCard = new CheckStatusCredentialCard(statusCardResponse.getCodigo(), statusCardResponse.getDescripcion(), statusCardResponse.getTicketWS(), statusCardResponse.getInicio(), statusCardResponse.getFin(), statusCardResponse.getTiempo(), statusCardResponse.getNumero(), statusCardResponse.getCuenta(), statusCardResponse.getCodigoEntidad(), statusCardResponse.getDescripcionEntidad(), statusCardResponse.getSucursal(), statusCardResponse.getCodigoProducto(), statusCardResponse.getDescripcionProducto(), statusCardResponse.getCodigoEstado(), statusCardResponse.getDescripcionEstado(), statusCardResponse.getActual(), statusCardResponse.getAnterior(), statusCardResponse.getDenominacion(), statusCardResponse.getTipo(), statusCardResponse.getIden(), statusCardResponse.getTelefono(), statusCardResponse.getDireccion(), statusCardResponse.getCodigoPostal(), statusCardResponse.getLocalidad(), statusCardResponse.getCodigoPais(), statusCardResponse.getDescripcionPais(), statusCardResponse.getMomentoUltimaActualizacion(), statusCardResponse.getMomentoUltimaOperacionAprobada(), statusCardResponse.getMomentoUltimaOperacionDenegada(), statusCardResponse.getMomentoUltimaBajaBoletin(), statusCardResponse.getContadorPinERR());
                    return new CheckStatusCardResponses(checkStatusCredentialCard, ResponseCode.SUCCESS, "");
                case "-024":
                    return new CheckStatusCardResponses(ResponseCode.NOT_ALLOWED_TO_CHANGE_STATE, "NOT ALLOWED TO CHANGE STATE");
                case "-011":
                    return new CheckStatusCardResponses(ResponseCode.AUTHENTICATE_IMPOSSIBLE, "Authenticate Impossible");
                case "-13":
                    return new CheckStatusCardResponses(ResponseCode.SERVICE_NOT_ALLOWED, "Service Not Allowed");
                case "-14":
                    return new CheckStatusCardResponses(ResponseCode.OPERATION_NOT_ALLOWED_FOR_THIS_SERVICE, "Operation Not Allowed For This Service");
                case "-060":
                    return new CheckStatusCardResponses(ResponseCode.UNABLE_TO_ACCESS_DATA, "Unable to Access Data");
                case "-120":
                    return new CheckStatusCardResponses(ResponseCode.THERE_ARE_NO_RECORDS_FOR_THE_REQUESTED_SEARCH, "There are no Records for the Requested Search");
                case "-140":
                    return new CheckStatusCardResponses(ResponseCode.THE_REQUESTED_PRODUCT_DOES_NOT_EXIST, "The Requested Product does not Exist");
                case "-160":
                    return new CheckStatusCardResponses(ResponseCode.THE_NUMBER_OF_ORDERS_ALLOWED_IS_EXCEEDED, "The Number of Orders Allowed is Exceeded");
                case "-030":
                    return new CheckStatusCardResponses(ResponseCode.NON_EXISTENT_ACCOUNT, "Non-existent account");
                case "-02":
                    if (credentialsRetries > 3) {
                        credentialsRetries = 0;
                        return new CheckStatusCardResponses(ResponseCode.INTERNAL_ERROR, "ERROR -02");
                    }
                    ++credentialsRetries;
                    Thread.sleep(2000);
                    return checkStatusCard(encriptedCard, timeZone);
                default:
                    return new CheckStatusCardResponses(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new CheckStatusCardResponses(ResponseCode.CREDENTIALS_WS_INAVAILABLE, "Credentials Web Service Inavailable");
        } catch (IOException ex) {
            ex.printStackTrace();
            return new CheckStatusCardResponses(ResponseCode.CREDENTIALS_WS_INAVAILABLE, "Credentials Web Service Inavailable");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CheckStatusCardResponses(ResponseCode.INTERNAL_ERROR, "");
        }

    }
}
