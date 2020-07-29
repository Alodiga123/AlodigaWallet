package com.alodiga.wallet.dao;

import com.alodiga.wallet.model.BalanceHistory;
import com.alodiga.wallet.model.BalanceHistory_;
import com.alodiga.wallet.model.Commission;
import com.alodiga.wallet.model.Commission_;
import com.alodiga.wallet.model.Product;
import com.alodiga.wallet.model.Transaction;
import com.alodiga.wallet.model.TransactionType;
import com.alodiga.wallet.utils.Constante;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author hvarona
 */
public abstract class TransactionDAO {

    public static Commission getCommision(Product product,
            TransactionType transactionType, EntityManager entityManager) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Commission> cq = cb.createQuery(Commission.class);
            Root<Commission> from = cq.from(Commission.class);
            cq.select(from);
            cq.where(cb.and(
                    cb.and(
                            cb.equal(from.get(Commission_.productId), product),
                            cb.equal(from.get(Commission_.transactionTypeId), transactionType))),
                    cb.isNull(from.get(Commission_.endingDate)));

            return entityManager.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BalanceHistory loadLastBalanceHistoryByAccount(Long userId,
            Product product, EntityManager entityManager) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<BalanceHistory> cq = cb.createQuery(BalanceHistory.class);
            Root<BalanceHistory> from = cq.from(BalanceHistory.class);
            cq.select(from);
            cq.where(cb.and(
                    cb.equal(from.get(BalanceHistory_.userId), userId),
                    cb.equal(from.get(BalanceHistory_.productId), product)));
            cq.orderBy(cb.desc(from.get(BalanceHistory_.id)));

            Query query = entityManager.createQuery(cq);
            query.setMaxResults(1).setHint("toplink.refresh", "true");
            BalanceHistory result = (BalanceHistory) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public static BalanceHistory addBalanceToProduct(long userId, Product product,
            float amounttoAdd, Transaction transaction, EntityManager entityManager) {
        try {
            BalanceHistory oldBalance = loadLastBalanceHistoryByAccount(userId, product, entityManager);
            BalanceHistory newBalance = new BalanceHistory();
            newBalance.setId(null);
            newBalance.setUserId(userId);
            if (oldBalance == null) {
                newBalance.setOldAmount(Constante.sOldAmountUserDestination);
                newBalance.setCurrentAmount(amounttoAdd);
            } else {
                newBalance.setOldAmount(oldBalance.getCurrentAmount());
                newBalance.setCurrentAmount(oldBalance.getCurrentAmount() + amounttoAdd);
                newBalance.setVersion(oldBalance.getId());
            }
            newBalance.setProductId(product);
            newBalance.setTransactionId(transaction);
            newBalance.setDate(new Timestamp(System.currentTimeMillis()));
            entityManager.persist(newBalance);
            return newBalance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
