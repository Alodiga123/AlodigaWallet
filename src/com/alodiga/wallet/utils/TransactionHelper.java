package com.alodiga.wallet.utils;

import com.alodiga.wallet.common.ejb.UtilsEJB;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.model.Sequences;
import com.alodiga.wallet.common.utils.EjbConstants;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author henry
 */
public abstract class TransactionHelper {

    public static enum OriginApplicationType {
        ALODIGA_WALLET,
        ADMIN_WALLET,
        BUSINESS_PORTAL;
    }

    public static String generateNextRechargeSequence(OriginApplicationType originApplicationType) {
        try {
            //TODO change to query the database internally
            Properties props = new Properties();
            props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
            props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
            props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
            props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
            InitialContext intialContext = new InitialContext(props);
            
            UtilsEJB utilsEJB = (UtilsEJB) intialContext.lookup(EjbConstants.UTILS_EJB);
            
            StringBuilder sequence = new StringBuilder();
            int originAplicationId = -1;
            switch (originApplicationType) {
                case ALODIGA_WALLET:
                    sequence.append("APP-AWAAPP-");
                    originAplicationId = 1;
                    break;
                case ADMIN_WALLET:
                    sequence.append("ADM-AWAWEB-");
                    originAplicationId = 2;
                    break;
                case BUSINESS_PORTAL:
                    sequence.append("PBW-PORNEG-");
                    originAplicationId = 3;
                    break;
                default:
                    break;
            }
            Map<String, Object> params = new HashMap();
            params.put(EjbConstants.PARAM_DOCUMENT_TYPE_ID, 4);
            EJBRequest request = new EJBRequest();
            request.setParams(params);

            List<Sequences> sequences = utilsEJB.getSequencesByDocumentType(request);
            for (Sequences s : sequences) {
                if (s.getOriginApplicationId().getId() == originAplicationId) {
                    if (s.getCurrentValue() > 1) {
                        sequence.append(String.valueOf(s.getCurrentValue()));
                    } else {
                        sequence.append(String.valueOf(s.getInitialValue()));
                    }
                    s.setCurrentValue(s.getCurrentValue() + 1);
                    utilsEJB.saveSequences(s);
                    break;
                }
            }
            sequence.append("-");

            Calendar cal = Calendar.getInstance();
            sequence.append(cal.getDisplayName(Calendar.YEAR, Calendar.LONG, Locale.getDefault()));
            return sequence.toString();
        } catch (EmptyListException | GeneralException | NullParameterException | RegisterNotFoundException | NamingException ex) {
            Logger.getLogger(TransactionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "1";
    }

}
