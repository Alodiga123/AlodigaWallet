package com.alodiga.wallet.ejb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import org.apache.log4j.Logger;

import com.alodiga.wallet.common.ejb.PreferencesEJB;
import com.alodiga.wallet.common.ejb.PreferencesEJBLocal;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.genericEJB.AbstractWalletEJB;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.genericEJB.WalletContextInterceptor;
import com.alodiga.wallet.common.genericEJB.WalletLoggerInterceptor;
import com.alodiga.wallet.common.model.PreferenceField;
import com.alodiga.wallet.common.model.PreferenceType;
import com.alodiga.wallet.common.model.PreferenceValue;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.common.utils.QueryConstants;

import javax.persistence.Query;

@Interceptors({WalletLoggerInterceptor.class, WalletContextInterceptor.class})
@Stateless(name = EjbConstants.PREFERENCES_EJB, mappedName = EjbConstants.PREFERENCES_EJB)
@TransactionManagement(TransactionManagementType.BEAN)
public class PreferencesEJBImp extends AbstractWalletEJB implements PreferencesEJB, PreferencesEJBLocal {

    private static final Logger logger = Logger.getLogger(PreferencesEJBImp.class);

    
    public PreferenceField deletePreferenceField(EJBRequest request) throws GeneralException, NullParameterException {
        return null;
    }

    
    public PreferenceType deletePreferenceType(EJBRequest request) throws GeneralException, NullParameterException {
        return null;
    }

    public PreferenceValue deletePreferenceValue(EJBRequest request) throws GeneralException, NullParameterException {
        return null;
    }

    private PreferenceValue getLastPreferenceValueByPreferenceField(Long preferenceFieldId, Long enterpriseId) throws GeneralException, NullParameterException, EmptyListException {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("preferenceFieldId", preferenceFieldId);
        params.put("enterpriseId", enterpriseId);
        EJBRequest request = new EJBRequest();
        request.setParams(params);
        request.setLimit(1);
        List<PreferenceValue> preferences = new ArrayList<PreferenceValue>();
        try {
            preferences = (List<PreferenceValue>) getNamedQueryResult(PreferencesEJBImp.class, QueryConstants.PREFERENCE_VALUE_BY_PREFERENCE_FIELD, request, getMethodName(), logger, "preferenceValue");
            return preferences.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Map<Long, String> getLastPreferenceValues(EJBRequest request) throws GeneralException, RegisterNotFoundException, NullParameterException, EmptyListException {
        Map<Long, String> currentValues = new HashMap<Long, String>();
        Map<String, Object> params = request.getParams();
        List<PreferenceField> fields = this.getPreferenceFields(request);
        for (PreferenceField field : fields) {
            PreferenceValue pv = getLastPreferenceValueByPreferenceField(field.getId(), Long.valueOf("" + params.get("enterpriseId")));
            if (pv != null) {
                currentValues.put(field.getId(), pv.getValue());
            }
        }
        return currentValues;
    }

    
    public List<PreferenceField> getPreferenceFields(EJBRequest request) throws GeneralException, RegisterNotFoundException, NullParameterException, EmptyListException {
        return (List<PreferenceField>) listEntities(PreferenceField.class, request, logger, getMethodName());
    }

    
    public List<PreferenceType> getPreferenceTypes(EJBRequest request) throws GeneralException, RegisterNotFoundException, NullParameterException, EmptyListException {
        return (List<PreferenceType>) listEntities(PreferenceType.class, request, logger, getMethodName());
    }

    
    public List<PreferenceValue> getPreferenceValues(EJBRequest request) throws GeneralException, RegisterNotFoundException, NullParameterException, EmptyListException {
        return (List<PreferenceValue>) listEntities(PreferenceValue.class, request, logger, getMethodName());
    }

    
    public List<PreferenceValue> getPreferenceValuesByEnterpriseIdAndFieldId(Long enterpriseId, Long fieldId) throws GeneralException, RegisterNotFoundException, NullParameterException, EmptyListException {
        List<PreferenceValue> preferenceValues = new ArrayList<PreferenceValue>();

        Query query = null;
        try {
            query = createQuery("SELECT p FROM PreferenceValue p WHERE p.preferenceField.id=?1 AND p.enterprise.id= ?2");
            query.setParameter("1", fieldId);
            query.setParameter("2", enterpriseId);
            preferenceValues = query.setHint("toplink.refresh", "true").getResultList();

        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (preferenceValues.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return preferenceValues;
    }

    public PreferenceValue loadActivePreferenceValuesByEnterpriseIdAndFieldId(Long enterpriseId, Long fieldId) throws GeneralException, RegisterNotFoundException, NullParameterException {

        PreferenceValue preferenceValue = null;
        try {
            Query query = null;

            query = createQuery("SELECT p FROM PreferenceValue p WHERE p.preferenceField.id=?1 AND p.enterprise.id= ?2 AND p.endingDate IS NULL");
            query.setParameter("1", fieldId);
            query.setParameter("2", enterpriseId);
            preferenceValue = (PreferenceValue) query.setHint("toplink.refresh", "true").getSingleResult();

        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (preferenceValue == null) {
            throw new RegisterNotFoundException(logger, sysError.format(EjbConstants.ERR_REGISTER_NOT_FOUND_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return preferenceValue;
    }

    
    public PreferenceField loadPreferenceField(EJBRequest request) throws GeneralException, RegisterNotFoundException, NullParameterException {
        return (PreferenceField) loadEntity(PreferenceField.class, request, logger, getMethodName());
    }

    
    public PreferenceType loadPreferenceType(EJBRequest request) throws GeneralException, RegisterNotFoundException, NullParameterException {
        return (PreferenceType) loadEntity(PreferenceType.class, request, logger, getMethodName());
    }

    
    public PreferenceValue loadPreferenceValue(EJBRequest request) throws GeneralException, RegisterNotFoundException, NullParameterException {
        return (PreferenceValue) loadEntity(PreferenceValue.class, request, logger, getMethodName());
    }

    
    public PreferenceField savePreferenceField(EJBRequest request) throws GeneralException, NullParameterException {
        return (PreferenceField) saveEntity(request, logger, getMethodName());
    }

    
    public PreferenceType savePreferenceType(EJBRequest request) throws GeneralException, NullParameterException {
        return (PreferenceType) saveEntity(request, logger, getMethodName());
    }

    
    public PreferenceValue savePreferenceValue(EJBRequest request) throws GeneralException, NullParameterException {
        return (PreferenceValue) saveEntity(request, logger, getMethodName());
    }

    
    public List<PreferenceValue> savePreferenceValues(EJBRequest request) throws GeneralException, NullParameterException {
        Timestamp time = new Timestamp(new Date().getTime());
        List<PreferenceValue> returnValues = new ArrayList<PreferenceValue>();
        List<PreferenceValue> preferenceValues = (List<PreferenceValue>) request.getParam();
        for (PreferenceValue pv : preferenceValues) {
            PreferenceValue oldPv = null;
            pv.setBeginningDate(time);
            try {
                oldPv = getLastPreferenceValueByPreferenceField(pv.getPreferenceFieldId().getId(), pv.getEnterpriseId().getId());
                if (oldPv != null) {
                    if (pv.getValue().equals(oldPv.getValue()) == false) //SETEO ENDING DAY  Y SALVO EL NUEVO VALOR
                    {
                        oldPv.setEndingDate(time);
                        saveEntity(oldPv, logger, getMethodName());
                        returnValues.add((PreferenceValue) saveEntity(pv, logger, getMethodName()));
                    }
                }
            } catch (EmptyListException e) {
                e.printStackTrace();
            }
            if (oldPv == null) {
                returnValues.add((PreferenceValue) saveEntity(pv, logger, getMethodName())); //SALVO EL NUEVO VALOR
            }        }

        return returnValues;
    }
}
