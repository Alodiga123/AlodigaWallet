package com.alodiga.wallet.bean;

import com.alodiga.autorization.credential.response.DispertionResponse;
import com.alodiga.wallet.common.enumeraciones.DocumentTypeE;
import com.alodiga.wallet.common.model.BalanceHistory;
import com.alodiga.wallet.common.model.Commission;
import com.alodiga.wallet.common.model.CommissionItem;
import com.alodiga.wallet.common.model.Product;
import com.alodiga.wallet.common.model.Sequences;
import com.alodiga.wallet.common.model.Transaction;
import com.alodiga.wallet.common.model.TransactionSource;
import com.alodiga.wallet.common.model.TransactionStatus;
import com.alodiga.wallet.common.model.TransactionType;
import com.alodiga.wallet.common.utils.Constante;
import com.alodiga.wallet.common.utils.SendMailTherad;
import com.alodiga.wallet.common.utils.SendSmsThread;
import com.alodiga.wallet.dao.TransactionDAO;
import com.alodiga.wallet.responses.CardResponse;
import com.alodiga.wallet.responses.RechargeValidationResponse;
import com.alodiga.wallet.responses.ResponseCode;
import com.alodiga.wallet.responses.TransactionResponse;
import com.alodiga.wallet.utils.TransactionHelper;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import credentialautorizationclient.CredentialAutorizationClient;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hvarona
 */
@Stateless(name = "FsProcessorRechargeWallet",
        mappedName = "ejb/FsProcessorRechargeWallet")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class APIRechargeOperations {

    @PersistenceContext(unitName = "AlodigaWalletPU")
    private EntityManager entityManager;

    @EJB
    private APIOperations operations;

    private Double truncDouble(Double in) {
        return Math.floor(in * 100) / 100;
    }

    public RechargeValidationResponse getRechargeProductValidation(Long userId,
            Long productId, Double amountToRecharge, boolean includeFee) {
        try {
            Product product = entityManager.find(Product.class, productId);
            TransactionType transactionType = entityManager.find(TransactionType.class, 1L);

            Commission commission = TransactionDAO.getCommision(product, transactionType, entityManager);
            if (commission == null) {
                return new RechargeValidationResponse(ResponseCode.INTERNAL_ERROR, "No comission");
            }

            AmountFee amountFee = new AmountFee(commission, amountToRecharge, includeFee);

            return new RechargeValidationResponse(amountFee.amountAfter, amountFee.fee, amountFee.totalAmount);
        } catch (Exception e) {
            e.printStackTrace();
            return new RechargeValidationResponse(ResponseCode.INTERNAL_ERROR, e.getMessage());
        }
    }

    public TransactionResponse rechargeWallet(Long businessId, Long userId, Long productId,
            Double amountToRecharge, boolean includeFee) {
        try {
            Product product = entityManager.find(Product.class, productId);
            TransactionType transactionType = entityManager.find(TransactionType.class, 1L);

            Commission commission = TransactionDAO.getCommision(product, transactionType, entityManager);
            if (commission == null) {
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "No comission");
            }
            AmountFee amountFee = new AmountFee(commission, amountToRecharge, includeFee);

            //TODO validaciones de preferencias
            Transaction recharge = new Transaction();
            recharge.setUserSourceId(BigInteger.valueOf(businessId));
            recharge.setUserDestinationId(BigInteger.valueOf(userId));
            recharge.setProductId(product);
            recharge.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, 2L);
            recharge.setTransactionSourceId(transactionSource);
            recharge.setCreationDate(new Timestamp(System.currentTimeMillis()));
            recharge.setConcept("Recharge Wallet");
            recharge.setAmount((float) amountFee.amountAfter);
            recharge.setTransactionStatus(TransactionStatus.CREATED.name());
            recharge.setTotalAmount((float) amountFee.totalAmount);
            recharge.setTransactionNumber(TransactionHelper.generateNextRechargeSequence(TransactionHelper.OriginApplicationType.BUSINESS_PORTAL));

            recharge.setIndClosed(false);

            entityManager.persist(recharge);

            CommissionItem commissionItem = new CommissionItem((float) amountFee.fee,
                    new Timestamp(System.currentTimeMillis()), recharge, commission);
            entityManager.persist(commissionItem);

            recharge.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(recharge);

            BalanceHistory newBalance = TransactionDAO.addBalanceToProduct(userId,
                    product, (float) amountFee.amountAfter, recharge, entityManager);
            if (newBalance == null) {
                recharge.setTransactionStatus(TransactionStatus.FAILED.name());
                entityManager.merge(recharge);
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Balance Problem");
            }

            recharge.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(recharge);

            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario user = proxy.getUsuarioporId(
                    "usuarioWS", "passwordWS", userId.toString());

            new SendMailTherad("ES", (float) amountFee.amountAfter, "Recarga",
                    user.getDatosRespuesta().getNombre() + " " + user.getDatosRespuesta().getApellido(),
                    user.getDatosRespuesta().getEmail(), Integer.valueOf("9")).start();

            new SendSmsThread(user.getDatosRespuesta().getMovil(),
                    (float) amountFee.amountAfter, Integer.valueOf("28"),
                    Long.valueOf(user.getDatosRespuesta().getUsuarioID()),
                    entityManager).start();

            TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.SUCCESS, "EXITO", recharge);
            return transactionResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process recharge wallet");
        }
    }

    public RechargeValidationResponse getRechargeCardValidation(Double amountToRecharge, boolean includeFee) {
        try {
            Product product = entityManager.find(Product.class, 3L); //Producto de Tarjeta            
            TransactionType transactionType = entityManager.find(TransactionType.class, 1L);

            Commission commission = TransactionDAO.getCommision(product, transactionType, entityManager);
            if (commission == null) {
                return new RechargeValidationResponse(ResponseCode.INTERNAL_ERROR, "No comission");
            }

            AmountFee amountFee = new AmountFee(commission, amountToRecharge, includeFee);

            return new RechargeValidationResponse(amountFee.amountAfter, amountFee.fee, amountFee.totalAmount);
        } catch (Exception e) {
            e.printStackTrace();
            return new RechargeValidationResponse(ResponseCode.INTERNAL_ERROR, e.getMessage());
        }
    }

    public TransactionResponse rechargeCard(Long businessId, String email, Double amountToRecharge, boolean includeFee) {
        try {
            Date date = new Date();
            Product product = entityManager.find(Product.class, 3L); //Producto de Tarjeta
            TransactionType transactionType = entityManager.find(TransactionType.class, 1L);

            Commission commission = TransactionDAO.getCommision(product, transactionType, entityManager);
            if (commission == null) {
                return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "No comission");
            }
            AmountFee amountFee = new AmountFee(commission, amountToRecharge, includeFee);

            //Se busca por el email el alias que devuelve credencial
            CardResponse cardResponse = operations.getCardByEmail(email);
            String alias = cardResponse.getaliasCard();

            //Se genera la secuencia de la transacción
            Sequences sequences = operations.getSequencesByDocumentTypeByOriginApplication(new Long(DocumentTypeE.PROREC.getId()), 3L);
            String Numbersequence = operations.generateNumberSequence(sequences);
            String sequence = TransactionHelper.generateNextRechargeSequence(TransactionHelper.OriginApplicationType.BUSINESS_PORTAL);

            //Se efectúa la recarga de la tarjeta
            CredentialAutorizationClient credentialAutorizationClient = new CredentialAutorizationClient();
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            SimpleDateFormat sdg = new SimpleDateFormat("yyyyMMdd");
            String hourString = sdf.format(date);
            String dateString = sdg.format(date);
            DispertionResponse dispertionResponse = credentialAutorizationClient.dispertionTransfer(dateString, hourString, alias, String.valueOf(amountFee.amountAfter), sequence);

            switch (dispertionResponse.getCodigoError()) {
                case ("-1"): {
                    APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
                    RespuestaUsuario responseUser = proxy.getUsuarioporemail("usuarioWS", "passwordWS", email);
                    Long userId = Long.valueOf(responseUser.getDatosRespuesta().getUsuarioID());

                    //DispertionTransferCredential dispertionTransferCredential = new DispertionTransferCredential(dispertionResponse.getCodigoError(), dispertionResponse.getMensajeError(), dispertionResponse.getCodigoRespuesta(), dispertionResponse.getMensajeRespuesta(), dispertionResponse.getCodigoAutorizacion());
                    //Se guarda el objeto Transaction
                    Transaction transaction = new Transaction();
                    transaction.setId(null);
                    transaction.setTransactionNumber(Numbersequence);
                    transaction.setTransactionSequence(sequence);
                    transaction.setUserSourceId(BigInteger.valueOf(businessId));
                    transaction.setUserDestinationId(BigInteger.valueOf(userId));
                    transaction.setProductId(product);
                    transaction.setTransactionTypeId(transactionType);
                    TransactionSource transactionSource = entityManager.find(TransactionSource.class, transactionType.getId());
                    transaction.setTransactionSourceId(transactionSource);
                    Date date_ = new Date();
                    Timestamp creationDate = new Timestamp(date_.getTime());
                    transaction.setCreationDate(creationDate);
                    transaction.setAmount((float) amountFee.amountAfter);
                    transaction.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
                    transaction.setConcept("Recharge Card");
                    transaction.setTotalAmount((float) amountFee.amountAfter);
                    entityManager.persist(transaction);

                    //BalanceHistory del producto de destino
                    BalanceHistory balanceProductDestination = operations.loadLastBalanceHistoryByAccount(userId, product.getId());
                    BalanceHistory balanceHistory = new BalanceHistory();
                    balanceHistory.setId(null);
                    balanceHistory.setUserId(userId);
                    if (balanceProductDestination == null) {
                        balanceHistory.setOldAmount(Constante.sOldAmountUserDestination);
                        balanceHistory.setCurrentAmount((float) amountFee.amountAfter);
                    } else {
                        balanceHistory.setOldAmount(balanceProductDestination.getCurrentAmount());
                        Float currentAmountUserDestination = balanceProductDestination.getCurrentAmount() + (float) amountFee.amountAfter;
                        balanceHistory.setCurrentAmount(currentAmountUserDestination);
                        balanceHistory.setVersion(balanceProductDestination.getId());
                    }
                    balanceHistory.setProductId(product);
                    balanceHistory.setTransactionId(transaction);
                    Date balanceDate = new Date();
                    balanceHistory.setDate(balanceDate);
                    entityManager.persist(balanceHistory);

                    CommissionItem commissionItem = new CommissionItem((float) amountFee.fee,
                            new Timestamp(System.currentTimeMillis()), transaction, commission);
                    entityManager.persist(commissionItem);

                    //TODO cardCredentials
                    transaction.setTransactionStatus(TransactionStatus.COMPLETED.name());
                    entityManager.merge(transaction);

                    TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.SUCCESS, "EXITO", transaction);
                    return transactionResponse;

                }
                case "204": {
                    return new TransactionResponse(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
                }
                case "913": {
                    return new TransactionResponse(ResponseCode.INVALID_AMOUNT, "INVALID AMOUNT");
                }
                case "203": {
                    return new TransactionResponse(ResponseCode.EXPIRATION_DATE_DIFFERS, "EXPIRATION DATE DIFFERS");
                }
                case "205": {
                    return new TransactionResponse(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
                }
                case "202": {
                    return new TransactionResponse(ResponseCode.LOCKED_CARD, "LOCKED CARD");
                }
                case "201": {
                    return new TransactionResponse(ResponseCode.LOCKED_CARD, "LOCKED CARD");
                }
                case "03": {
                    return new TransactionResponse(ResponseCode.LOCKED_CARD, "LOCKED CARD");
                }
                case "28": {
                    return new TransactionResponse(ResponseCode.LOCKED_CARD, "LOCKED_CARD");
                }
                case "211": {
                    return new TransactionResponse(ResponseCode.BLOCKED_ACCOUNT, "BLOCKED ACCOUNT");
                }
                case "210": {
                    return new TransactionResponse(ResponseCode.INVALID_ACCOUNT, "INVALID ACCOUNT");
                }
                case "998": {
                    return new TransactionResponse(ResponseCode.INSUFFICIENT_BALANCE, "INSUFFICIENT BALANCE");
                }
                case "986": {
                    return new TransactionResponse(ResponseCode.INSUFFICIENT_LIMIT, "INSUFFICIENT LIMIT");
                }
                case "987": {
                    return new TransactionResponse(ResponseCode.CREDIT_LIMIT_0, "CREDIT LIMIT 0");
                }
                case "988": {
                    return new TransactionResponse(ResponseCode.CREDIT_LIMIT_0_OF_THE_DESTINATION_ACCOUNT, "CREDIT LIMIT 0 OF THE DESTINATION ACCOUNT");
                }
                case "999": {
                    return new TransactionResponse(ResponseCode.ERROR_PROCESSING_THE_TRANSACTION, "ERROR PROCESSING THE TRANSACTION");
                }
                case "101": {
                    return new TransactionResponse(ResponseCode.INVALID_TRANSACTION, "INVALID TRANSACTION");
                }
                case "105": {
                    return new TransactionResponse(ResponseCode.ERROR_VALIDATION_THE_TERMINAL, "ERROR VALIDATION THE TERMINAL");
                }
                case "241": {
                    return new TransactionResponse(ResponseCode.DESTINATION_ACCOUNT_LOCKED, "DESTINATION ACCOUNT LOCKED");
                }
                case "230": {
                    return new TransactionResponse(ResponseCode.INVALID_DESTINATION_CARD, "INVALID DESTINATION CARD");
                }
                case "240": {
                    return new TransactionResponse(ResponseCode.INVALID_DESTINATION_ACCOUNT, "INVALID DESTINATION ACCOUNT");
                }
                case "301": {
                    return new TransactionResponse(ResponseCode.THE_AMOUNT_MUST_BE_POSITIVE_AND_THE_AMOUNT_IS_REPORTED, "THE AMOUNT MUST BE POSITIVE AND THE AMOUNT IS REPORTED");
                }
                case "302": {
                    return new TransactionResponse(ResponseCode.INVALID_TRANSACTION_DATE, "INVALID TRANSACTION DATE");
                }
                case "303": {
                    return new TransactionResponse(ResponseCode.INVALID_TRANSACTION_TIME, "INVALID TRANSACTION TIME");
                }
                case "994": {
                    return new TransactionResponse(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NN");
                }
                case "991": {
                    return new TransactionResponse(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_SN, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION SN");
                }
                case "992": {
                    return new TransactionResponse(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
                }
                case "993": {
                    return new TransactionResponse(ResponseCode.SOURCE_OR_DESTINATION_ACCOUNT_IS_NOT_COMPATIBLE_WITH_THIS_OPERATION_NS, "SOURCE OR DESTINATION ACCOUNT IS NOT COMPATIBLE WITH THIS OPERATION NS");
                }
                case "990": {
                    return new TransactionResponse(ResponseCode.TRASACTION_BETWEEN_ACCOUNTS_NOT_ALLOWED, "TRASACTION BETWEEN ACCOUNTS NOT ALLOWED");
                }
                case "120": {
                    return new TransactionResponse(ResponseCode.TRADE_VALIDATON_ERROR, "TRADE VALIDATON ERROR");
                }
                case "110": {
                    return new TransactionResponse(ResponseCode.DESTINATION_CARD_DOES_NOT_SUPPORT_TRANSACTION, "DESTINATION CARD DOES NOT SUPPORT TRANSACTION");
                }
                case "111": {
                    return new TransactionResponse(ResponseCode.OPERATION_NOT_ENABLED_FOR_THE_DESTINATION_CARD, "OPERATION NOT ENABLED FOR THE DESTINATION CARD");
                }
                case "206": {
                    return new TransactionResponse(ResponseCode.BIN_NOT_ALLOWED, "BIN NOT ALLOWED");
                }
                case "207": {
                    return new TransactionResponse(ResponseCode.STOCK_CARD, "STOCK CARD");
                }
                /*case "205": {
                    return new TransactionResponse(ResponseCode.THE_ACCOUNT_EXCEEDS_THE_MONTHLY_LIMIT, "THE ACCOUNT EXCEEDS THE MONTHLY LIMIT");
                }
                case "101": {
                    return new TransactionResponse(ResponseCode.THE_PAN_FIELD_IS_MANDATORY, "THE PAN FIELD IS MANDATORY");
                }*/
                case "102": {
                    return new TransactionResponse(ResponseCode.THE_AMOUNT_TO_BE_RECHARGE_IS_INCORRECT, "THE AMOUNT TO BE RECHARGE IS INCORRECT");
                }
                case "3": {
                    return new TransactionResponse(ResponseCode.EXPIRED_CARD, "EXPIRED CARD");
                }
                case "8": {
                    return new TransactionResponse(ResponseCode.NON_EXISTENT_CARD, "NON EXISTENT CARD");
                }
                case "33": {
                    return new TransactionResponse(ResponseCode.THE_AMOUNT_MUST_BE_GREATER_THAN_0, "THE AMOUNT MUST BE GREATER THAN 0");
                }
                case "1": {
                    return new TransactionResponse(ResponseCode.SUCCESSFUL_RECHARGE, "SUCCESSFUL RECHARGE");
                }
                case "410": {
                    return new TransactionResponse(ResponseCode.ERROR_VALIDATING_PIN, "THE PAN FIELD IS MANDATORY");
                }
                case "430": {
                    return new TransactionResponse(ResponseCode.ERROR_VALIDATING_CVC1, "ERROR VALIDATING CVC1");
                }
                case "400": {
                    return new TransactionResponse(ResponseCode.ERROR_VALIDATING_CVC2, "ERROR VALIDATING CVC2");
                }
                case "420": {
                    return new TransactionResponse(ResponseCode.PIN_CHANGE_ERROR, "PIN CHANGE ERROR");
                }
                case "250": {
                    return new TransactionResponse(ResponseCode.ERROR_VALIDATING_THE_ITEM, " ERROR VALIDATING THE ITEM");
                }
                default:
                    return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "ERROR INTERNO");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.INTERNAL_ERROR, "Error in process recharge wallet");
        }
    }

    private class AmountFee {

        double totalAmount;
        double amountAfter;
        double fee;

        public AmountFee(Commission commission, double rechargeAmount, boolean includeFee) {
            fee = commission.getValue();
            if (fee < 0) {
                fee = 0;
            }
            if (includeFee) {
                totalAmount = truncDouble(rechargeAmount);
                if (commission.getIsPercentCommision() == 1 && fee != 0) {
                    fee = (totalAmount * fee) / (100 + fee);
                    fee = truncDouble(fee);
                }
                amountAfter = totalAmount - fee;

            } else {
                amountAfter = truncDouble(rechargeAmount);
                if (commission.getIsPercentCommision() == 1 && fee != 0) {
                    fee = (amountAfter * fee) / 100;
                    fee = truncDouble(fee);
                }
                totalAmount = amountAfter + fee;
            }
        }
    }

}
