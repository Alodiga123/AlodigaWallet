package com.alodiga.wallet.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.alodiga.wallet.exception.EmptyListException;
import com.alodiga.wallet.exception.GeneralException;
import com.alodiga.wallet.exception.NullParameterException;
import com.alodiga.wallet.exception.RegisterNotFoundException;
import com.alodiga.wallet.genericEJB.AbstractWalletEJB;
import com.alodiga.wallet.model.Enterprise;
import com.alodiga.wallet.model.Preference;
import com.alodiga.wallet.model.PreferenceField;
import com.alodiga.wallet.model.PreferenceType;
import com.alodiga.wallet.model.PreferenceValue;
import com.alodiga.wallet.respuestas.ResponseCode;
import com.alodiga.wallet.rest.response.EnterpriseResponse;
import com.alodiga.wallet.rest.response.PreferenceFieldListResponse;
import com.alodiga.wallet.rest.response.PreferenceFieldResponse;
import com.alodiga.wallet.rest.response.PreferenceResponse;
import com.alodiga.wallet.rest.response.PreferenceTypeListResponse;
import com.alodiga.wallet.rest.response.PreferenceTypeResponse;
import com.alodiga.wallet.rest.response.PreferenceValueListResponse;
import com.alodiga.wallet.rest.response.PreferenceValueResponse;
import com.alodiga.wallet.utils.Constants;

@Stateless(name = "FsPreferences", mappedName = "ejb/FsPreferences")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class APIPreferences extends AbstractWalletEJB  {


    private static final Logger logger = Logger.getLogger(APIPreferences.class);


    public PreferenceValueResponse getLastPreferenceValueByPreferenceField(Long preferenceFieldId)  {
    	PreferenceValue preferenceValue  = null;
		try {
			logger.info( "getLastPreferenceValueByPreferenceField:"+ preferenceFieldId);
			preferenceValue = entityManager.createNamedQuery("PreferenceValue.findByPreferenceFieldId", PreferenceValue.class).setParameter("preferenceFieldId", preferenceFieldId).getSingleResult();
		} catch (NoResultException e) {
			logger.info( "PreferenceValue not found id:"+preferenceFieldId);
			return new PreferenceValueResponse(ResponseCode.PROFILE_NOT_FOUND, "PreferenceValue not found id:"+preferenceFieldId);
		}catch (Exception e) {
			logger.info( "Error getLastPreferenceValueByPreferenceField");
			return new PreferenceValueResponse(ResponseCode.ERROR_INTERNO, "Error getLastPreferenceValueByPreferenceField");
		}
		logger.info("PreferenceValue found id:"+preferenceFieldId);
		return new PreferenceValueResponse(ResponseCode.EXITO, "", preferenceValue);
		
    }
    
    private PreferenceValue getLastPreferenceValueByPreferenceFieldId(Long preferenceFieldId)  {
    	PreferenceValue preferenceValue  = null;
		try {
			logger.info( "getLastPreferenceValueByPreferenceFieldId:"+ preferenceFieldId);
			preferenceValue = entityManager.createNamedQuery("PreferenceValue.findByPreferenceFieldId", PreferenceValue.class).setParameter("preferenceFieldId", preferenceFieldId).getSingleResult();
		} catch (NoResultException e) {
			logger.info( "PreferenceValue not found id:"+preferenceFieldId);
			
		}catch (Exception e) {
			logger.info( "Error getLastPreferenceValueByPreferenceFieldId");
			
		}
		logger.info("PreferenceValue found id:"+preferenceFieldId);
		return preferenceValue;
		
    }
    

    public Map<Long, String> getLastPreferenceValues() throws GeneralException, RegisterNotFoundException, NullParameterException, EmptyListException {
        Map<Long, String> currentValues = new HashMap<Long, String>();
        List<PreferenceField> fields = this.getListPreferenceFields();
        for (PreferenceField field : fields) {
            PreferenceValue pv = getLastPreferenceValueByPreferenceFieldId(field.getId());
            if (pv != null) {
                currentValues.put(field.getId(), pv.getValue());
            }
        }
        return currentValues;
    }

    
    public PreferenceFieldListResponse getPreferenceFields() {
    	 List<PreferenceField> preferenceFields = new ArrayList<PreferenceField>();
         try {
         	logger.info( "getPreferenceFields");
         	preferenceFields = entityManager.createNamedQuery("PreferenceField.findAll", PreferenceField.class).setHint("toplink.refresh", "true").getResultList();

         } catch (Exception e) {
         	logger.error( "Error getPreferenceFields");
 			return new PreferenceFieldListResponse(ResponseCode.ERROR_INTERNO, "Error in getPreferenceFields");
         }
         if (preferenceFields.isEmpty()) {
         	logger.error( "getPreferenceFields empty");
 			return new PreferenceFieldListResponse(ResponseCode.PERMISSION_GROUP_NOT_FOUND, "PreferenceFields not founds");
         }
         logger.info( "Return getPreferenceFields founds");
 		return new PreferenceFieldListResponse(ResponseCode.EXITO, "Exito", preferenceFields);
    }
    
    public List<PreferenceField> getListPreferenceFields() throws GeneralException, RegisterNotFoundException, NullParameterException, EmptyListException {
    	return (List<PreferenceField>) listEntities(PreferenceField.class, null,null, logger, getMethodName());
   }

    
    public PreferenceTypeListResponse getPreferenceTypes()  {
   	 List<PreferenceType> preferenceTypes = new ArrayList<PreferenceType>();
     try {
     	logger.info( "getPreferenceTypes");
     	preferenceTypes = entityManager.createNamedQuery("PreferenceType.findAll", PreferenceType.class).setHint("toplink.refresh", "true").getResultList();

     } catch (Exception e) {
     	logger.error( "Error getPreferenceTypes");
			return new PreferenceTypeListResponse(ResponseCode.ERROR_INTERNO, "Error in getPreferenceTypes");
     }
     if (preferenceTypes.isEmpty()) {
     	logger.error( "getPreferenceTypes empty");
			return new PreferenceTypeListResponse(ResponseCode.PERMISSION_GROUP_NOT_FOUND, "PreferenceTypes not founds");
     }
     logger.info( "Return PreferenceTypes founds");
	 return new PreferenceTypeListResponse(ResponseCode.EXITO, "Exito", preferenceTypes);
    }

    
    public PreferenceValueListResponse getPreferenceValues() {
      	 List<PreferenceValue> preferenceValues = new ArrayList<PreferenceValue>();
         try {
         	logger.info( "getPreferenceValues");
         	preferenceValues = entityManager.createNamedQuery("PreferenceValue.findAll", PreferenceValue.class).setHint("toplink.refresh", "true").getResultList();

         } catch (Exception e) {
         	logger.error( "Error getPreferenceValues");
    			return new PreferenceValueListResponse(ResponseCode.ERROR_INTERNO, "Error in getPreferenceValues");
         }
         if (preferenceValues.isEmpty()) {
         	logger.error( "getPreferenceValues empty");
    			return new PreferenceValueListResponse(ResponseCode.PERMISSION_GROUP_NOT_FOUND, "PreferenceValues not founds");
         }
         logger.info( "Return PreferenceValues founds");
    	 return new PreferenceValueListResponse(ResponseCode.EXITO, "Exito", preferenceValues);
    }

    
    public List<PreferenceValue> getPreferenceValuesByEnterpriseIdAndFieldId(Long enterpriseId, Long fieldId) throws GeneralException, EmptyListException  {
        List<PreferenceValue> preferenceValues = new ArrayList<PreferenceValue>();

        Query query = null;
        try {
            query = createQuery("SELECT p FROM PreferenceValue p WHERE p.preferenceField.id=?1 AND p.enterprise.id= ?2");
            query.setParameter("1", fieldId);
            query.setParameter("2", enterpriseId);
            preferenceValues = query.setHint("toplink.refresh", "true").getResultList();

        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(Constants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (preferenceValues.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(Constants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
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
            throw new GeneralException(logger, sysError.format(Constants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (preferenceValue == null) {
            throw new RegisterNotFoundException(logger, sysError.format(Constants.ERR_REGISTER_NOT_FOUND_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return preferenceValue;
    }

    
    public PreferenceFieldResponse loadPreferenceField(Long preferenceFieldId)  {
    	PreferenceField preferenceField = null; 
    	try {
    		preferenceField = this.loadPreferenceFieldById(preferenceFieldId);
		} catch (NullParameterException e) {
			return new PreferenceFieldResponse(ResponseCode.PREFERENCE_FIELD_NULL_PARAMETER, "PreferenceFieldId null parameter");
		} catch (RegisterNotFoundException e) {
			return new PreferenceFieldResponse(ResponseCode.PREFERENCE_FIELD_NOD_FOUND, "PreferenceField not founds");
		} catch (GeneralException e) {
			return new PreferenceFieldResponse(ResponseCode.ERROR_INTERNO, "Error in loadPreferenceField");
		}
    	 return new PreferenceFieldResponse(ResponseCode.EXITO, "Exito", preferenceField);
    }
    
    public PreferenceField loadPreferenceFieldById(Long preferenceFieldId) throws NullParameterException, GeneralException, RegisterNotFoundException  {
    	return (PreferenceField) loadEntity(PreferenceField.class, preferenceFieldId, logger, getMethodName());
    }

    
    public PreferenceTypeResponse loadPreferenceType(Long preferenceTypeId)  {
    	PreferenceType preferenceType = null; 
    	try {
    		preferenceType = this.loadPreferenceTypeById(preferenceTypeId);
		} catch (NullParameterException e) {
			return new PreferenceTypeResponse(ResponseCode.PREFERENCE_TYPE_NULL_PARAMETER, "PreferenceTypeId null parameter");
		} catch (RegisterNotFoundException e) {
			return new PreferenceTypeResponse(ResponseCode.PREFERENCE_TYPE_NOD_FOUND, "PreferenceType not founds");
		} catch (GeneralException e) {
			return new PreferenceTypeResponse(ResponseCode.ERROR_INTERNO, "Error in loadPreferenceType");
		}
    	 return new PreferenceTypeResponse(ResponseCode.EXITO, "Exito", preferenceType);   
    }
    
    public PreferenceType loadPreferenceTypeById(Long preferenceTypeId) throws NullParameterException, GeneralException, RegisterNotFoundException  {
    	return (PreferenceType) loadEntity(PreferenceType.class, preferenceTypeId, logger, getMethodName());	   
    }

    
    public PreferenceValueResponse loadPreferenceValue(Long preferenceValueId) {
    	PreferenceValue preferenceValue = null; 
    	try {
    		preferenceValue = this.loadPreferenceValueById(preferenceValueId);
    	} catch (NullParameterException e) {
			return new PreferenceValueResponse(ResponseCode.PREFERENCE_TYPE_NULL_PARAMETER, "PreferenceValueId null parameter");
		} catch (RegisterNotFoundException e) {
			return new PreferenceValueResponse(ResponseCode.PREFERENCE_TYPE_NOD_FOUND, "PreferenceValue not founds");
		} catch (GeneralException e) {
			return new PreferenceValueResponse(ResponseCode.ERROR_INTERNO, "Error in loadPreferenceValue");
		}
    	 return new PreferenceValueResponse(ResponseCode.EXITO, "Exito", preferenceValue);
    }
    
    public PreferenceValue loadPreferenceValueById(Long preferenceValueId) throws NullParameterException, GeneralException, RegisterNotFoundException {
    	return	(PreferenceValue) loadEntity(PreferenceValue.class, preferenceValueId, logger, getMethodName());	
    }
    
    public PreferenceResponse loadPreference(Long preferenceId) {
    	Preference preference = null; 
    	try {
    		preference = this.loadPreferenceById(preferenceId);
    	} catch (NullParameterException e) {
			return new PreferenceResponse(ResponseCode.PREFERENCE_NULL_PARAMETER, "PreferenceId null parameter");
		} catch (RegisterNotFoundException e) {
			return new PreferenceResponse(ResponseCode.PREFERENCE_NOD_FOUND, "Preference not founds");
		} catch (GeneralException e) {
			return new PreferenceResponse(ResponseCode.ERROR_INTERNO, "Error in loadPreference");
		}
    	 return new PreferenceResponse(ResponseCode.EXITO, "Exito", preference);
    }
    
    public Preference loadPreferenceById(Long preferenceId) throws NullParameterException, GeneralException, RegisterNotFoundException {
    	return	(Preference) loadEntity(Preference.class, preferenceId, logger, getMethodName());	
    }

    public EnterpriseResponse loadEnterprise(Long enterpriseId)  {
    	Enterprise enterprise = null; 
    	try {
    		enterprise = this.loadEnterprisedById(enterpriseId);
		} catch (NullParameterException e) {
			return new EnterpriseResponse(ResponseCode.ENTERPRISE_NULL_PARAMETER, "enterpriseId null parameter");
		} catch (RegisterNotFoundException e) {
			return new EnterpriseResponse(ResponseCode.ENTERPRISE_NOT_FOUND, "enterprise not founds");
		} catch (GeneralException e) {
			return new EnterpriseResponse(ResponseCode.ERROR_INTERNO, "Error in loadEnterprisedById");
		}
    	 return new EnterpriseResponse(ResponseCode.EXITO, "Exito", enterprise);
    }
    
    public Enterprise loadEnterprisedById(Long enterpriseId) throws NullParameterException, GeneralException, RegisterNotFoundException  {
    	return (Enterprise) loadEntity(Enterprise.class, enterpriseId, logger, getMethodName());
    }
    
    public PreferenceFieldResponse savePreferenceField(PreferenceField preferenceField)  {
        try {
        	preferenceField = (PreferenceField) saveEntity(preferenceField);
        } catch (NullParameterException e) {
        	return new PreferenceFieldResponse(ResponseCode.PREFERENCE_FIELD_NULL_PARAMETER, "PreferenceField null parameter");
        } catch (GeneralException e) {
        	return new PreferenceFieldResponse(ResponseCode.ERROR_INTERNO, "Error in savePreferenceField");
		}
        return new PreferenceFieldResponse(ResponseCode.EXITO, "Exito", preferenceField);
    }

    
    public PreferenceTypeResponse savePreferenceType(PreferenceType preferenceType) {
        try {
        	preferenceType = (PreferenceType) saveEntity(preferenceType, logger, getMethodName());
        } catch (NullParameterException e) {
        	return new PreferenceTypeResponse(ResponseCode.PREFERENCE_TYPE_NULL_PARAMETER, "PreferenceType null parameter");
        } catch (GeneralException e) {
        	return new PreferenceTypeResponse(ResponseCode.ERROR_INTERNO, "Error in savePreferenceType");
		}
        return new PreferenceTypeResponse(ResponseCode.EXITO, "Exito", preferenceType); 
    }

    
    public PreferenceValueResponse savePreferenceValue(PreferenceValue preferenceValue) {
        try {
        	preferenceValue = (PreferenceValue) saveEntity(preferenceValue, logger, getMethodName());
		} catch (NullParameterException e) {
			return new PreferenceValueResponse(ResponseCode.PREFERENCE_TYPE_NULL_PARAMETER, "PreferenceValueId null parameter");
		} catch (GeneralException  e) {
			return new PreferenceValueResponse(ResponseCode.ERROR_INTERNO, "Error in savePreferenceValue");
		}
    	 return new PreferenceValueResponse(ResponseCode.EXITO, "Exito", preferenceValue);
    }

//    
//    public List<PreferenceValue> savePreferenceValues(List<PreferenceValue> preferenceValues) {
//        Timestamp time = new Timestamp(new Date().getTime());
//        List<PreferenceValue> returnValues = new ArrayList<PreferenceValue>();
//        for (PreferenceValue pv : preferenceValues) {
//            PreferenceValue oldPv = null;
//            pv.setBeginningDate(time);
//            try {
//                oldPv = getLastPreferenceValueByPreferenceFieldId(pv.getPreferenceFieldId().getId());
//                if (oldPv != null) {
//                    if (pv.getValue().equals(oldPv.getValue()) == false) //SETEO ENDING DAY  Y SALVO EL NUEVO VALOR
//                    {
//                        oldPv.setEndingDate(time);
//                        saveEntity(oldPv, logger, getMethodName());
//                        returnValues.add((PreferenceValue) saveEntity(pv, logger, getMethodName()));
//                    }
//                }
//            } catch (EmptyListException e) {
//                e.printStackTrace();
//            }
//            if (oldPv == null) {
//                returnValues.add((PreferenceValue) saveEntity(pv, logger, getMethodName())); //SALVO EL NUEVO VALOR
//            }        
//        }
//
//        return returnValues;
//    }

   }
