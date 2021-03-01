package com.alodiga.wallet.bean;

import com.alodiga.wallet.common.model.Commission;
import com.alodiga.wallet.common.model.Product;
import com.alodiga.wallet.common.model.TransactionType;
import com.alodiga.wallet.common.utils.Constante;
import com.alodiga.wallet.dao.TransactionDAO;
import com.alodiga.wallet.responses.ResponseCode;
import com.alodiga.wallet.responses.TransactionValidationResponse;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author henry
 */
@Stateless(name = "FsProcessorBusinessWallet",
        mappedName = "ejb/FsProcessorBusinessWallet")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class APIBusinessWalletOperations {

    @PersistenceContext(unitName = "AlodigaWalletPU")
    private EntityManager entityManager;

    public TransactionValidationResponse getTransactionValidation(Float amount,
            Long productId, Long transactionTypeId, boolean includeFee) {

        try {
            Product product = entityManager.find(Product.class, productId);
            TransactionType transactionType = entityManager.find(TransactionType.class, transactionTypeId);

            Commission commission = TransactionDAO.getCommision(product, transactionType, entityManager);
            if (commission == null) {
                return new TransactionValidationResponse(ResponseCode.INTERNAL_ERROR, "No comission");
            }
            float fee = commission.getValue();
            float amountBefore, amountAfter;
            if (fee < 0) {
                fee = 0;
            }
            if (includeFee) {
                amountAfter = amount;
                if (commission.getIsPercentCommision() == 1 && fee != 0) {
                    fee = (amountAfter * fee) / (100 + fee);
                    fee = Math.round(fee * 100) / 100;
                }
                amountBefore = amountAfter - fee;
            } else {
                amountBefore = amount;
                if (commission.getIsPercentCommision() == 1 && fee != 0) {
                    fee = (amountBefore * fee) / 100;
                    fee = Math.round(fee * 100) / 100;
                }
                amountAfter = amountBefore + fee;
            }

            return new TransactionValidationResponse(amountBefore, fee, amountAfter);
        } catch (Exception e) {
            e.printStackTrace();
            return new TransactionValidationResponse(ResponseCode.INTERNAL_ERROR, e.getMessage());
        }
    }

    public TransactionValidationResponse getWithdrawalValidation(Float amount,
            Long productId, boolean includeFee) {
        /*try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<TransactionType> from = cq.from(TransactionType.class);
            cq.select(from.get("id")).where(cb.equal(from.get("code"), WITHDRAWAL_TRANSACTION_CODE));
            Long transactionTypeId = entityManager.createQuery(cq).getSingleResult();*/

        return getTransactionValidation(amount, productId, Constante.sTransationTypeBusinessManualWithdrawal, includeFee);

        /*} catch (Exception e) {
            e.printStackTrace();
            return new TransactionValidationResponse(ResponseCode.INTERNAL_ERROR, e.getMessage());
        }*/
    }

    public TransactionValidationResponse getTransferUserValidation(Float amount,
            Long productId, boolean includeFee) {
        /*try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<TransactionType> from = cq.from(TransactionType.class);
            cq.select(from.get("id")).where(cb.equal(from.get("code"), WITHDRAWAL_TRANSACTION_CODE));
            Long transactionTypeId = entityManager.createQuery(cq).getSingleResult();*/

        return getTransactionValidation(amount, productId, Constante.sTransationTypeTA, includeFee);

        /*} catch (Exception e) {
            e.printStackTrace();
            return new TransactionValidationResponse(ResponseCode.INTERNAL_ERROR, e.getMessage());
        }*/
    }

}
