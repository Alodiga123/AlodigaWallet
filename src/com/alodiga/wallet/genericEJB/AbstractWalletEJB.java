package com.alodiga.wallet.genericEJB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.alodiga.wallet.exception.EmptyListException;
import com.alodiga.wallet.exception.GeneralException;
import com.alodiga.wallet.exception.NullParameterException;
import com.alodiga.wallet.exception.RegisterNotFoundException;
import com.alodiga.wallet.utils.Constants;
import com.alodiga.wallet.utils.MessageFormatHelper;



public class AbstractWalletEJB {

    @Resource
    protected EJBContext context;
    protected EntityManager entityManager;
    protected EntityManagerWrapper entityManagerWrapper = null;
    protected MessageFormatHelper sysMessages, sysError;
    protected EntityManagerFactory emf = null;
    public AbstractWalletEJB() {
        emf = Persistence.createEntityManagerFactory("AlodigaWalletPU");
        entityManagerWrapper = new EntityManagerWrapper();
        entityManager = emf.createEntityManager();
        sysMessages = new MessageFormatHelper(Constants.MESSAGE_FILE_NAME);
        sysError = new MessageFormatHelper(Constants.ERROR_FILE_NAME);
    }

    //@Override
    @PostConstruct
    public void init() {
        entityManagerWrapper.setEntityManager(entityManager);
    }

    public boolean echo() throws Exception {
        return true;
    }

    protected String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public EntityManagerWrapper getEntityManagerWrapper() {
        return entityManagerWrapper;
    }

    protected Boolean removeEntity(Object entity, Logger logger, String methodName) {
//    	FIXME utilizar el AUDIT DATA
        List<Object> entities = (List<Object>) entity;
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            for (Object o : entities) {
                entityManager.remove(entity);
            }
            transaction.commit();
        } finally {
            return false;
        }
    }

    protected Object saveEntity(Object entity, Logger logger, String methodName) throws GeneralException, NullParameterException {
        if (entity == null) {
            throw new NullParameterException( sysError.format(Constants.ERR_NULL_PARAMETER, this.getClass(), methodName, "Entity"), null);
        }
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (((WalletGenericEntity) entity).getPk() != null) {
                //processAuditData(EventTypeEnum.UPDATE, entity, auditData, entityManagerWrapper);
                entityManagerWrapper.update(entity);
            } else {
                entityManagerWrapper.save(entity);
                //processAuditData(EventTypeEnum.CREATE, entity, auditData, entityManagerWrapper);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (IllegalStateException e1) {
                throw new GeneralException(logger, sysError.format(Constants.ERR_GENERAL_EXCEPTION, this.getClass(), methodName, e.getMessage()), e);
            } catch (SecurityException e1) {
                 throw new GeneralException(logger, sysError.format(Constants.ERR_GENERAL_EXCEPTION, this.getClass(), methodName, e.getMessage()), e);
            }
            throw new GeneralException(logger, sysError.format(Constants.ERR_GENERAL_EXCEPTION, this.getClass(), methodName, e.getMessage()), e);
        }

        return entity;
    }

    protected Object listEntities(Class clazz, Integer first, Integer limit, Logger logger, String methodName) throws GeneralException, EmptyListException {
        Object oReturns = null;
        try {
            oReturns = entityManagerWrapper.get(clazz, first, limit);
            //persistAuditData(EventTypeEnum.READ, oReturns, auditData);

        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(Constants.ERR_GENERAL_EXCEPTION, clazz.getSimpleName(), e.getMessage()), e);
        }
        if (((List) oReturns).isEmpty()) {
            throw new EmptyListException(logger, sysError.format(Constants.ERR_EMPTY_LIST_EXCEPTION, clazz.getSimpleName(), methodName, clazz.getSimpleName() + "s"), null);
        }
        return oReturns;
    }

    protected Object loadEntity(Class clazz, Object pk, Logger logger, String methodName) throws NullParameterException, GeneralException, RegisterNotFoundException {
        Object oReturn = null;
        if (pk == null) {
            String error = sysError.format(Constants.ERR_NULL_PARAMETER, clazz.getSimpleName(), methodName, "id");
            throw new NullParameterException( error, null);
        }
        try {
            oReturn = entityManagerWrapper.load(clazz, pk);
            //persistAuditData(EventTypeEnum.READ, oReturn, auditData);
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(Constants.ERR_GENERAL_EXCEPTION, clazz.getSimpleName(), methodName, e.getMessage()), e);
        }
        if (oReturn == null) {
            throw new RegisterNotFoundException(logger, sysError.format(Constants.ERR_REGISTER_NOT_FOUND_EXCEPTION, clazz.getSimpleName(), methodName, clazz.getSimpleName(), pk), null);
        }
        return oReturn;
    }

    protected Object getNamedQueryResult(Class clazz, String namedQueryName, Map<? extends Object, ? extends Object> map, String method, Logger logger, String returnObjectName, Integer first, Integer limit) throws EmptyListException, GeneralException {
        Object object = null;

        if (map == null) {
            map = new HashMap<Object, Object>();
        }
        try {
            object = (Object) entityManagerWrapper.findByNamedQuery(namedQueryName, map, first, limit);
            //persistAuditData(EventTypeEnum.READ, object, auditData);

        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralException(logger, sysError.format(Constants.ERR_GENERAL_EXCEPTION, clazz.getClass(), e.getMessage()), e);
        }
        if (object == null || ((List<Object>) object).size() < 1) {
            throw new EmptyListException(logger, sysError.format(Constants.ERR_EMPTY_LIST_EXCEPTION, clazz.getClass(), method, returnObjectName), null);
        }

        return object;
    }



    public void executeNameQuery(Class clazz, String namedQueryName, Map<? extends Object, ? extends Object> map, String method, Logger logger, String returnObjectName, Integer first, Integer limit) throws EmptyListException, GeneralException {

        if (map == null) {
            map = new HashMap<Object, Object>();
        }
        try {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManagerWrapper.executeNamedQuery(namedQueryName, map);
            transaction.commit();
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(Constants.ERR_GENERAL_EXCEPTION, clazz.getClass(), e.getMessage()), e);
        }
    }

    public Query createQuery(String sql) throws NullParameterException, Exception {
        Query query = null;
        if (sql == null) {
            throw new NullParameterException("sql cannot be null");
        }
        try {
            query = entityManagerWrapper.createQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return query;
    }

//    protected Object createSearchQuery(Class clazz, Object request, Map orderMap, Logger logger, String methodName, String returnObjectName, boolean isFilter) throws GeneralException, EmptyListException, NullParameterException {
//        Object object = null;
//        Map map = request.getParams();
//        if (map == null) {
//            throw new NullParameterException( "Map data is empty", null);
//        }
//        try {
//            //System.out.println(".........A1.........."+(new Date()));
//            object = (Object) entityManagerWrapper.executeSearchQuery(clazz, map, orderMap, request.getFirst(), request.getLimit(), isFilter);
//            //System.out.println(".........A2.........."+(new Date()));
//            //persistAuditData(EventTypeEnum.READ, object, request.getAuditData());
//            //System.out.println(".........A3.........."+(new Date()));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new GeneralException(logger, sysError.format(Constants.ERR_GENERAL_EXCEPTION, clazz.getClass(), e.getMessage()), e);
//        }
//        if (object == null || ((List<Object>) object).size() < 1) {
//            throw new EmptyListException(logger, sysError.format(Constants.ERR_EMPTY_LIST_EXCEPTION, clazz.getClass(), methodName, returnObjectName), null);
//        }
//        return object;
//    }

    //Este metodo se utiliza para que el procesamiento sea mas rapido pero no guardara registro en la bitacora.
    protected Object saveEntity(Object entity) throws GeneralException, NullParameterException {
        if (entity == null) {
            System.out.println("EJB empty");
            throw new NullParameterException("NullParameterException ");
        }
        EntityTransaction transaction = entityManager.getTransaction();
        try {

            transaction.begin();
            if (((WalletGenericEntity) entity).getPk() != null) {
                //processAuditData(EventTypeEnum.UPDATE, entity, auditData, entityManagerWrapper);
                entityManagerWrapper.update(entity);
            } else {
                entityManagerWrapper.save(entity);
                //processAuditData(EventTypeEnum.CREATE, entity, auditData, entityManagerWrapper);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception e1) {
                throw new GeneralException("GeneralException saveEntity");
            }
            throw new GeneralException("GeneralException saveEntity");
        }

        return entity;
    }

}
