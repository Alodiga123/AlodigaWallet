package com.alodiga.wallet.utils;

/*import com.alodiga.massiva.sms.SendSmsMassiva;
import static com.alodiga.massiva.sms.SendSmsMassiva.sendSmsMassiva;
import com.alodiga.twilio.sms.services.TwilioSmsSenderProxy;*/
import com.alodiga.massiva.sms.SendSmsMassiva;
import com.alodiga.twilio.sms.services.TwilioSmsSenderProxy;
import com.alodiga.wallet.model.Sms;
import java.math.BigInteger;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManager;

public class SendSmsThread extends Thread {

    private static final long serialVersionUID = 1L;
    private String movil;
    private String codigo;
    private Long languageId;
    private Integer sendSmsType;
    private String idioma;
    private Float amountPayment;
    private Float amountWithdrawal;
    private String userDestinationName;
    private String referenceNumberOperation;
    private Float amountRecharge;
    private String destinationNumber;
    private String productSource;
    private String productDestination;
    private Float amountExchange;
    private Long userId;
    EntityManager entityManager = null;

    public SendSmsThread(String movil, String codigo_) {
        this.codigo = codigo_;
        this.movil = movil;
    }

    public SendSmsThread(String movil, Integer sendSmsType) {
        this.movil = movil;

        this.sendSmsType = sendSmsType;
    }

    public SendSmsThread(String movil, String codigo, Integer sendSmsType) {
        this.movil = movil;
        this.codigo = codigo;
        this.sendSmsType = sendSmsType;
    }

    public SendSmsThread(String movil, String codigo_, Long languageId) {
        this.codigo = codigo_;
        this.movil = movil;
        this.languageId = languageId;
    }

    public SendSmsThread(String movil, Float amountPayment, Integer sendType, Long userId, EntityManager entityManager) {

        this.movil = movil;
        this.amountPayment = amountPayment;
        this.sendSmsType = sendType;
        this.userId = userId;
        this.entityManager = entityManager;
    }

    public SendSmsThread(String movil, Integer sendType, Float amountWithdrawal, Long userId, EntityManager entityManager) {

        this.movil = movil;
        this.amountWithdrawal = amountWithdrawal;
        this.sendSmsType = sendType;
        this.userId = userId;
        this.entityManager = entityManager;

    }

    public SendSmsThread(String movil, Float amountRecharge, String referenceNumberOperation, Integer sendType, Long userId, EntityManager entityManager) {

        this.movil = movil;
        this.amountRecharge = amountRecharge;
        this.referenceNumberOperation = referenceNumberOperation;
        this.sendSmsType = sendType;
        this.userId = userId;
        this.entityManager = entityManager;

    }

    public SendSmsThread(String movil, String destinationNumber, Float amountRecharge, Integer sendType, Long userId, EntityManager entityManager) {

        this.movil = movil;
        this.destinationNumber = destinationNumber;
        this.amountRecharge = amountRecharge;
        this.sendSmsType = sendType;
        this.userId = userId;
        this.entityManager = entityManager;

    }

    public SendSmsThread(String movil, String productSource, String productDestination, Float amountExchange, Integer sendType, Long userId, EntityManager entityManager) {

        this.movil = movil;
        this.productSource = productSource;
        this.productDestination = productDestination;
        this.amountExchange = amountExchange;
        this.sendSmsType = sendType;
        this.userId = userId;
        this.entityManager = entityManager;

    }

    public void run() {
        String message = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        switch (sendSmsType) {
            case Constants.SEND_TYPE_SMS_COMERCE_PEYMENT:
                // code block
                message = getLangujeByPhoneNumber((movil).toString()).equals(Constants.SPANISH_LANGUAGE) ? "Billetera Alodiga, Usted a efectuado un pago el dia: " + sdf.format(timestamp) + " por un monto de: " + amountPayment + " Bs." : "Alodiga Wallet, You have made a payment the day: " + sdf.format(timestamp) + " by a sum of: " + amountPayment + " $.";
                break;

            case Constants.SEND_TYPE_SMS_WITHDRAWALS:
                // code block
                message = getLangujeByPhoneNumber((movil).toString()).equals(Constants.SPANISH_LANGUAGE) ? "Billetera Alodiga, Usted a efectuado un retiro el dia: " + sdf.format(timestamp) + " por un monto de: " + amountWithdrawal + " Bs." : "Alodiga Wallet, You have made a withdrawal the day: " + sdf.format(timestamp) + " by a sum of: " + amountWithdrawal + " $.";
                break;

            case Constants.SEND_TYPE_SMS_RECHARGE:
                // code block
                message = getLangujeByPhoneNumber((movil).toString()).equals(Constants.SPANISH_LANGUAGE) ? "Billetera Alodiga, Usted a efectuado una recarga el dia: " + sdf.format(timestamp) + " por un monto de: " + amountRecharge + " Bs." + " numero de referencia: " + referenceNumberOperation : "Alodiga Wallet, You have made a withdrawal the day: " + sdf.format(timestamp) + " by a sum of: " + amountRecharge + " $." + " reference number: " + referenceNumberOperation;
                break;

            case Constants.SEND_TYPE_SMS_TOP_UP:
                // code block 
                message = getLangujeByPhoneNumber((movil).toString()).equals(Constants.SPANISH_LANGUAGE) ? "Billetera Alodiga, Usted a efectuado un Top Up el dia: " + sdf.format(timestamp) + " por un monto de: " + amountRecharge + " Bs." + " al numero de destino: " + destinationNumber : "Alodiga Wallet, You have made a withdrawal TopUP the day: " + sdf.format(timestamp) + " by a sum of: " + amountRecharge + " $." + " number destinaion: " + destinationNumber;
                break;
            case Constants.SEND_TYPE_SMS_COMERCE_PEYMENT_RECIBER:
                // code block 
                message = getLangujeByPhoneNumber((movil).toString()).equals(Constants.SPANISH_LANGUAGE) ? "Billetera Alodiga, Usted a recibido un pago el dia: " + sdf.format(timestamp) + " por un monto de: " + amountPayment + " Bs." : "Alodiga Wallet, You have received a payment the day: " + sdf.format(timestamp) + " by a sum of: " + amountPayment + " $.";
                break;
            case Constants.SEND_TYPE_SMS_TRANSFER_ACCOUNT:
                // code block 
                message = getLangujeByPhoneNumber((movil).toString()).equals(Constants.SPANISH_LANGUAGE) ? "Billetera Alodiga, Usted a realizado una transferencia el dia: " + sdf.format(timestamp) + " por un monto de: " + amountPayment + " Bs." : "Alodiga Wallet, You have made a transfer the day: " + sdf.format(timestamp) + " by a sum of: " + amountPayment + " $.";
                break;
            case Constants.SEND_TYPE_SMS_TRANSFER_ACCOUNT_RECIBER:
                // code block 
                message = getLangujeByPhoneNumber((movil).toString()).equals(Constants.SPANISH_LANGUAGE) ? "Billetera Alodiga, Usted a recibido una transferencia el dia: " + sdf.format(timestamp) + " por un monto de: " + amountPayment + " Bs." : "Alodiga Wallet, You have received a transfer the day: " + sdf.format(timestamp) + " by a sum of: " + amountPayment + " $.";
                break;
            case Constants.SEND_TYPE_SMS_EXCHANGE_PRODUCT:
                // code block 
                message = getLangujeByPhoneNumber((movil).toString()).equals(Constants.SPANISH_LANGUAGE) ? "Billetera Alodiga, Usted a realizado un intercambio de producto el dia: " + sdf.format(timestamp) + " producto origen: " + productSource + " " + " producto destino: " + productDestination + " por un monto de: " + amountExchange + " Bs." : "Alodiga Wallet, You have made a product exchange the day: " + sdf.format(timestamp) + " product source: " + productSource + " " + "product destination" + productDestination + " by a sum of: " + amountExchange + " $.";
                break;
            case Constants.SEND_TYPE_SMS_TRASNFER_CARD_TO_CARD:
                // code block 
                message = getLangujeByPhoneNumber((movil).toString()).equals(Constants.SPANISH_LANGUAGE) ? "Billetera Alodiga, Usted a realizado una transferencia  el dia: " + sdf.format(timestamp) + " por un monto de: " + amountPayment + " Bs." : "Alodiga Wallet, You have made a transfer the day: " + sdf.format(timestamp) + " by a sum of: " + amountPayment + " $.";
                break;
            case Constants.SEND_TYPE_SMS_TRASNFER_CARD_TO_CARD_RECIBER:
                // code block 
                message = getLangujeByPhoneNumber((movil).toString()).equals(Constants.SPANISH_LANGUAGE) ? "Billetera Alodiga, Usted a recibido una transferencia el dia: " + sdf.format(timestamp) + " por un monto de: " + amountPayment + " Bs." : "Alodiga Wallet, You have received a transfer the day: " + sdf.format(timestamp) + " by a sum of: " + amountPayment + " $.";
                break;

            case Constants.SEND_TYPE_SMS_TEST:
                // code block 
                message = getLangujeByPhoneNumber((movil).toString()).equals(Constants.SPANISH_LANGUAGE) ? "Billetera Alodiga, Su codigo de seguridad para el registro es: " + codigo : "Alodiga Wallet, Your security code is: " + codigo;
                break;
                
                
        }
        try {
            //String message = getLangujeByPhoneNumber(movil).equals(Constante.SPANISH_LANGUAGE) ? "Billetera Alodiga, Su codigo de seguridad para el registro es: " + codigo : "Alodiga Wallet, Your security code is: " + codigo ;
            //Solo aplica para dos o tres pasises si se desea hacer dinamicamente se debe agregar un plan de numeraciòn
            String countryCode = movil.substring(0, 2);
            if (movil.substring(0, 1).equals("1")) {
                //lo envia por USA
                TwilioSmsSenderProxy proxy = new TwilioSmsSenderProxy();
                proxy.sendTwilioSMS(movil, message);
            } else if (movil.substring(0, 2).equals("58")) {
                //Venezuela  integras con Massiva
//                APIOperations aPIOperations = new APIOperations();
                SendSmsMassiva sendSmsMassiva = new SendSmsMassiva();
                try {
                    //String response = aPIOperations.sendSmsSimbox(message, movil, userId);
                    String response = sendSmsMassiva.sendSmsMassiva(message, movil);
                    Sms sms = new Sms();
                    sms.setUserId(BigInteger.valueOf(userId));
                    //sms.setIntegratorName(Constants.INTEGRATOR_SIMBOX);
                    sms.setIntegratorName(Constants.INTEGRATOR_MASSIVA);
                    sms.setSender(movil);
                    sms.setDestination(movil);
                    sms.setContent(message);
                    sms.setCreationDate(new Timestamp(new Date().getTime()));
                    if (getelement(response, "status").equals("1")) {
                        sms.setStatus(Constants.SEND_SMS);
                        sms.setAdditional(getelementIntoLabel(response, "celular", "sid"));
                    } else {
                        sms.setStatus(Constants.SEND_SMS_FAILED);
                        sms.setAdditional(null);
                    }

                    //sms.setAdditional(response);
                    entityManager.flush();
                    entityManager.persist(sms);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (movil.substring(0, 2).equals("52")) {
                //lo envia por TWILIO A MEXICO
                TwilioSmsSenderProxy proxy = new TwilioSmsSenderProxy();
                proxy.sendTwilioSMS(movil, message);
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    private Long getLangujeByPhoneNumber(String phone) {
        if (phone.substring(0, 1).equals("1")) {
            return Constants.ENGLISH_LANGUAGE;
        } else {
            return Constants.SPANISH_LANGUAGE;
        }
    }

    private static String getelement(String document, String element) {
        return (document.split("<" + element + ">")[1].split("</" + element + ">")[0]).trim();
    }

    private static String getelementIntoLabel(String document, String element, String element2) {
        return (document.split("<" + element)[1].split(element2 + "=")[1].split(" ")[0].replace("\"", ""));
    }
}
