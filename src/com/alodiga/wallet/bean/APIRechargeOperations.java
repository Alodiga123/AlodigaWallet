package com.alodiga.wallet.bean;

import com.alodiga.wallet.common.model.BalanceHistory;
import com.alodiga.wallet.common.model.Commission;
import com.alodiga.wallet.common.model.CommissionItem;
import com.alodiga.wallet.common.model.Product;
import com.alodiga.wallet.common.model.Transaction;
import com.alodiga.wallet.common.model.TransactionSource;
import com.alodiga.wallet.common.model.TransactionStatus;
import com.alodiga.wallet.common.model.TransactionType;
import com.alodiga.wallet.common.utils.SendMailTherad;
import com.alodiga.wallet.common.utils.SendSmsThread;
import com.alodiga.wallet.dao.TransactionDAO;
import com.alodiga.wallet.responses.RechargeValidationResponse;
import com.alodiga.wallet.responses.ResponseCode;
import com.alodiga.wallet.responses.TransactionResponse;
import com.alodiga.wallet.utils.TransactionHelper;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;

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

    
    private EntityManager entityManager;

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
                return new RechargeValidationResponse(ResponseCode.ERROR_INTERNO, "No comission");
            }

            AmountFee amountFee = new AmountFee(commission, amountToRecharge, includeFee);

            return new RechargeValidationResponse(amountFee.amountBefore, amountFee.fee, amountFee.totalAmount);
        } catch (Exception e) {
            e.printStackTrace();
            return new RechargeValidationResponse(ResponseCode.ERROR_INTERNO, e.getMessage());
        }
    }

    public TransactionResponse rechargeWallet(Long businessId, Long userId, Long productId,
            Double amountToRecharge, boolean includeFee) {
        try {
            Product product = entityManager.find(Product.class, productId);
            TransactionType transactionType = entityManager.find(TransactionType.class, 1L);

            Commission commission = TransactionDAO.getCommision(product, transactionType, entityManager);
            if (commission == null) {
                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "No comission");
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
            recharge.setAmount((float) amountFee.amountBefore);
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
                    product, (float) amountFee.amountBefore, recharge, entityManager);
            if (newBalance == null) {
                recharge.setTransactionStatus(TransactionStatus.FAILED.name());
                entityManager.merge(recharge);
                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Balance Problem");
            }

            recharge.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(recharge);

            APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
            RespuestaUsuario user = proxy.getUsuarioporId(
                    "usuarioWS", "passwordWS", userId.toString());

            new SendMailTherad("ES", (float) amountFee.amountBefore, "Recarga",
                    user.getDatosRespuesta().getNombre() + " " + user.getDatosRespuesta().getApellido(),
                    user.getDatosRespuesta().getEmail(), Integer.valueOf("9")).start();

            new SendSmsThread(user.getDatosRespuesta().getMovil(),
                    (float) amountFee.amountBefore, Integer.valueOf("28"),
                    Long.valueOf(user.getDatosRespuesta().getUsuarioID()),
                    entityManager).start();

            TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.EXITO, "EXITO", recharge);
            return transactionResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process recharge wallet");
        }
    }

    public RechargeValidationResponse getRechargeCardValidation(Double amountToRecharge, boolean includeFee) {
        try {
            Product product = entityManager.find(Product.class, 3L); //Producto de Tarjeta            
            TransactionType transactionType = entityManager.find(TransactionType.class, 1L);

            Commission commission = TransactionDAO.getCommision(product, transactionType, entityManager);
            if (commission == null) {
                return new RechargeValidationResponse(ResponseCode.ERROR_INTERNO, "No comission");
            }

            AmountFee amountFee = new AmountFee(commission, amountToRecharge, includeFee);

            return new RechargeValidationResponse(amountFee.amountBefore, amountFee.fee, amountFee.totalAmount);
        } catch (Exception e) {
            e.printStackTrace();
            return new RechargeValidationResponse(ResponseCode.ERROR_INTERNO, e.getMessage());
        }
    }

    public TransactionResponse rechargeCard(Long businessId, String eCardNumber, Double amountToRecharge, boolean includeFee) {
        try {
            Product product = entityManager.find(Product.class, 3L); //Producto de Tarjeta
            TransactionType transactionType = entityManager.find(TransactionType.class, 1L);

            Commission commission = TransactionDAO.getCommision(product, transactionType, entityManager);
            if (commission == null) {
                return new TransactionResponse(ResponseCode.ERROR_INTERNO, "No comission");
            }
            AmountFee amountFee = new AmountFee(commission, amountToRecharge, includeFee);

            //TODO validaciones de preferencias
            Transaction recharge = new Transaction();
            recharge.setUserSourceId(BigInteger.valueOf(businessId));
            recharge.setUserDestinationId(null);
            recharge.setProductId(product);
            recharge.setTransactionTypeId(transactionType);
            TransactionSource transactionSource = entityManager.find(TransactionSource.class, 2L);
            recharge.setTransactionSourceId(transactionSource);
            recharge.setCreationDate(new Timestamp(System.currentTimeMillis()));
            recharge.setConcept("Recharge Card");
            recharge.setAmount((float) amountFee.amountBefore);
            recharge.setTransactionStatus(TransactionStatus.CREATED.name());
            recharge.setTotalAmount((float) amountFee.totalAmount);
            Calendar cal = Calendar.getInstance();
            recharge.setTransactionNumber(TransactionHelper.generateNextRechargeSequence(TransactionHelper.OriginApplicationType.BUSINESS_PORTAL));
            entityManager.persist(recharge);

            CommissionItem commissionItem = new CommissionItem((float) amountFee.fee,
                    new Timestamp(System.currentTimeMillis()), recharge, commission);
            entityManager.persist(commissionItem);

            recharge.setTransactionStatus(TransactionStatus.IN_PROCESS.name());
            entityManager.merge(recharge);

            //TODO cardCredentials
            recharge.setTransactionStatus(TransactionStatus.COMPLETED.name());
            entityManager.merge(recharge);

            TransactionResponse transactionResponse = new TransactionResponse(ResponseCode.EXITO, "EXITO", recharge);
            return transactionResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionResponse(ResponseCode.ERROR_INTERNO, "Error in process recharge wallet");
        }
    }

    private class AmountFee {

        double totalAmount;
        double amountBefore;
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
                amountBefore = totalAmount - fee;

            } else {
                amountBefore = truncDouble(rechargeAmount);
                if (commission.getIsPercentCommision() == 1 && fee != 0) {
                    fee = (amountBefore * fee) / 100;
                    fee = truncDouble(fee);
                }
                totalAmount = amountBefore + fee;
            }
        }
    }

}
