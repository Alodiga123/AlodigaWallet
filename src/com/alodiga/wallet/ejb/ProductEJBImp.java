package com.alodiga.wallet.ejb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.alodiga.wallet.common.ejb.ProductEJB;
import com.alodiga.wallet.common.ejb.ProductEJBLocal;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.genericEJB.AbstractWalletEJB;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.genericEJB.WalletContextInterceptor;
import com.alodiga.wallet.common.genericEJB.WalletLoggerInterceptor;
import com.alodiga.wallet.common.model.Category;
import com.alodiga.wallet.common.model.Period;
import com.alodiga.wallet.common.model.Product;
import com.alodiga.wallet.common.model.ProductData;
import com.alodiga.wallet.common.model.Provider;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.common.utils.QueryConstants;

@Interceptors({WalletLoggerInterceptor.class, WalletContextInterceptor.class})
@Stateless(name = EjbConstants.PRODUCT_EJB, mappedName = EjbConstants.PRODUCT_EJB)
@TransactionManagement(TransactionManagementType.BEAN)
public class ProductEJBImp extends AbstractWalletEJB implements ProductEJB, ProductEJBLocal {

    private static final Logger logger = Logger.getLogger(ProductEJBImp.class);

    public Category deleteCategory(EJBRequest request) throws GeneralException, NullParameterException {
        return null;
    }

    public Product deleteProduct(EJBRequest request) throws GeneralException, NullParameterException {
        return null;
    }

   
    public void deleteProductHasProvider(Long productId) throws NullParameterException, GeneralException {
        if (productId == null) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "productId or providerId"), null);
        }
        try {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            //String sql = "DELETE FROM ProductHasProvider php WHERE php.product.id=" + productId;
            StringBuilder sqlBuilder = new StringBuilder("DELETE FROM ProductHasProvider php WHERE php.product.id=?1");
            Query query = createQuery(sqlBuilder.toString());
            query.setParameter("1", productId);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
    }

    public void deletePromotionTypeHasPromotion(EJBRequest request) throws NullParameterException, GeneralException {
        Object param = request.getParam();
        if (param == null || !(param instanceof Long)) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "promotionId"), null);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("promotionId", (Long) param);
        try {
            //executeNameQuery(PromotionTypeHasPromotion.class, QueryConstants.DELETE_PROMOTION_TYPE_HAS_PROMOTION, map, getMethodName(), logger, "PromotionTypeHasPromotion", null, null);
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
    }

    public Provider deleteProvider(EJBRequest request) throws GeneralException, NullParameterException {
        return null;
    }

    public Boolean deletePinFree(EJBRequest request) throws GeneralException, NullParameterException {
        return (Boolean) removeEntity(request, logger, getMethodName());
    }

    public Product enableProduct(EJBRequest request) throws GeneralException, NullParameterException, RegisterNotFoundException {
        return (Product) saveEntity(request, logger, getMethodName());
    }

    public List<Product> filterProducts(EJBRequest request) throws GeneralException, EmptyListException, NullParameterException {
        if (request == null) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "request"), null);
        }

        Map<String, Object> params = request.getParams();
        if (params == null) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "params"), null);
        }
        Boolean isFilter = true;
        Map orderField = new HashMap();
        orderField.put(Product.NAME, QueryConstants.ORDER_DESC);
        return (List<Product>) createSearchQuery(Product.class, request, orderField, logger, getMethodName(), "customers", isFilter);
    }

 
    public List<Category> getCategories(EJBRequest request) throws GeneralException, EmptyListException, NullParameterException {

        return (List<Category>) listEntities(Category.class, request, logger, getMethodName());
    }


    public List<Product> getProductsByEnterprise(Long enterpriseId) throws GeneralException, EmptyListException, NullParameterException {
        List<Product> products = null;

        if (enterpriseId == null || enterpriseId.equals("")) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "enterpriseId"), null);
        }
        //
        Query query = null;
        try {
            query = createQuery("SELECT p FROM Product p WHERE p.enterprise.id = ?1");
            query.setParameter("1", enterpriseId);
            products = query.setHint("toplink.refresh", "true").getResultList();
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (products.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return products;
    }

    public List<Provider> getProviders(EJBRequest request) throws GeneralException, EmptyListException, NullParameterException {

        return (List<Provider>) listEntities(Provider.class, request, logger, getMethodName());
    }

    public List<Provider> getSMSProviders(EJBRequest request) throws GeneralException, EmptyListException, NullParameterException {

        List<Provider> providers = null;
        //
        Query query = null;
        try {
            query = createQuery("SELECT p FROM Provider p WHERE p.isSMSProvider=1 AND p.enabled=1");

            if (request.getLimit() != null && request.getLimit() > 0) {
                query.setMaxResults(request.getLimit());
            } else {
                query.setMaxResults(20);
            }

            providers = query.setHint("toplink.refresh", "true").getResultList();

        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (providers.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return providers;
    }

    public Float getPercentDiscount(Long levelId, Long productId) throws GeneralException, NullParameterException {
        if (levelId == null) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "levelId"), null);
        }
        if (productId == null) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "productId"), null);
        }
        Float discount = 0F;
        try {
            Query query = createQuery("SELECT lhp FROM LevelHasProduct lhp WHERE lhp.endingDate IS NULL AND lhp.level.id=?1 AND lhp.product.id=?2 ");
            query.setParameter("1", levelId);
            query.setParameter("2", productId);
//            discount = ((LevelHasProduct) query.getSingleResult()).getDiscountPercent();
        } catch (Exception ex) {
            ex.getMessage();
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), ex.getMessage()), null);
        }
        return discount;
    }

    public Category loadCategory(EJBRequest request) throws GeneralException, RegisterNotFoundException, NullParameterException {

        return (Category) loadEntity(Category.class, request, logger, getMethodName());
    }

   
    public Product loadProduct(EJBRequest request) throws GeneralException, RegisterNotFoundException, NullParameterException {
        return (Product) loadEntity(Product.class, request, logger, getMethodName());
    }

    public Product loadProductById(Long productId) throws GeneralException, RegisterNotFoundException, NullParameterException {
        if (productId == null) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "productId"), null);
        }
        Product product = new Product();
        try {
            Query query = createQuery("SELECT p FROM Product p WHERE p.id = ?1");
            query.setParameter("1", productId);
            product = (Product) query.getSingleResult();
        } catch (Exception ex) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), ex.getMessage()), null);
        }
        if (product == null) {
            throw new RegisterNotFoundException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return product;
    }

    
    public Provider loadProvider(EJBRequest request) throws GeneralException, RegisterNotFoundException, NullParameterException {

        return (Provider) loadEntity(Provider.class, request, logger, getMethodName());
    }

    public Category saveCategory(EJBRequest request) throws GeneralException, NullParameterException {
        return (Category) saveEntity(request, logger, getMethodName());
    }

    public Product saveProduct(EJBRequest request) throws GeneralException, NullParameterException {

        return (Product) saveEntity(request, logger, getMethodName());
    }

    public ProductData saveProductData(EJBRequest request) throws GeneralException, NullParameterException {
        return (ProductData) saveEntity(request, logger, getMethodName());
    }


    public Provider saveProvider(EJBRequest request) throws GeneralException, NullParameterException {

        return (Provider) saveEntity(request, logger, getMethodName());
    }

    public List<Period> getPeriods(EJBRequest request) throws GeneralException, EmptyListException, NullParameterException {

        return (List<Period>) listEntities(Period.class, request, logger, getMethodName());

    }

   
    public List<Product> getProductsByAccount(EJBRequest request) throws GeneralException, NullParameterException, EmptyListException {
        List<Product> products = null;

        Map<String, Object> params = request.getParams();
        StringBuilder sqlBuilder = new StringBuilder("SELECT DISTINCT ahp.product FROM AccountProduct ahp WHERE ahp.account.id =?1");
        //String sql = "SELECT DISTINCT ahp.product FROM AccountProduct ahp WHERE ahp.account.id =:accountId";
        if (!params.containsKey(QueryConstants.PARAM_ACCOUNT)) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), params.containsKey(QueryConstants.PARAM_ACCOUNT)), null);
        }
        Query query = null;
        try {
            if (params.containsKey(QueryConstants.PARAM_ENDED) && params.get(QueryConstants.PARAM_ENDED).equals(true)) {
                sqlBuilder.append(" AND ahp.endingDate IS NOT NULL");
                //sql += " AND ahp.endingDate IS NOT NULL";
            }
            if (params.containsKey(QueryConstants.PARAM_ENDED) && params.get(QueryConstants.PARAM_ENDED).equals(false)) {
                sqlBuilder.append(" AND ahp.endingDate IS NULL");
                //sql += " AND ahp.endingDate IS NULL";
            }
            query = createQuery(sqlBuilder.toString());
            query.setParameter("1", params.get(QueryConstants.PARAM_ACCOUNT));
            if (request.getLimit() != null && request.getLimit() > 0) {
                query.setMaxResults(request.getLimit());
            }

            products = query.getResultList();
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (products.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return products;
    }

    public List<Product> getProducts(EJBRequest request) throws GeneralException, EmptyListException, NullParameterException {

        return (List<Product>) listEntities(Product.class, request, logger, getMethodName());
    }


  }
