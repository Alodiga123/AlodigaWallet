/**
 *
 */
package com.alodiga.wallet.utils;

/**
 * @author jonathanxuya
 *
 */
public class Constants {

    // constante conexion
    public static final String sNombreSAAT = "jdbc/SAATDataSource";
    //constante 2
    public static final String sNombreSAATAdmin = "jdbc/SAATAdminDataSource";
    //cambio 3
    public static final String sNombreAlocash = "jdbc/AlocashDataSource";
    public static final String sNombreAload = "jdbc/AloadDataSource";
    public static final String sNombreAlorrp = "jdbc/AlorrpDataSource";
    public static final String sNombreAlobenefit = "jdbc/AlobenefitDataSource";
    public static final String sNombreAuthpos = "jdbc/AuthposDataSource";

    public static final String sNombreAuthposTransaccional = "jdbc/AuthposTransaccionalDataSource";

    // constante modulo
    public static final String sALOCASH = "ALOCASH";
    public static final String sAUTHPOS = "AUTHPOS";
    public static final String sALOBENEFIT = "ALOBENEFIT";

    // constante tipo producto
    public static final String sTPINLINE = "TP_PINLINE";
    public static final String sTTOPUP = "TP_TOPUP";
    public static final String sTBILLPAYMENT = "TP_BILLPAYMENT";
    public static final String sTTRANSFERENCIA = "TP_TRANSFER";
    public static final String sTTRANSFERENCIA_ALOCOIN = "TP_TRANSFER_ALOCOINS";
    public static final String sTRECARGAR_ALOCOIN = "TP_RECARGAR_ALOCOINS";
    public static final String sTCONVERTIR_ALOCOIN = "TP_CONVERTIR_ALOCOINS_DOLARES";
    public static final String sTPVERANUNCIO = "TP_VER_ANUNCIO";

    // constante tipo transaccion
    public static final String sTRANSFERCOINS_TRANSACTION = "TRANSACTION_TRANSFERENCIA_ALOCOINS";
    public static final String sCOMPRARPINLINE_TRANSACTION = "TRANSACTION_COMPRAR_PINLINE";
    public static final String sCONVERSIONALOCOINSDOLARES_TRANSACTION = "TRANSACTION_CONVERTIR_ALOCOIN_DOLARES";
    public static final String sRECARGARPINLINE_TRANSACTION = "TRANSACTION_RECARGAR_ALOCOINS";
    public static final String sRECARGARTOPUP_TRANSACTION = "TRANSACTION_RECARGAR_TELEFONO_TOPUP";
    public static final String sBILLPAYMENT_TRANSACTION = "TRANSACTION_BILLPAYMENT";
    public static final String sRECARGARALOCOINS_TRANSACTION = "TRANSACTION_RECARGAR_ALOCOINS";

    // constante config fee
    public static final String sTIPO_CLIENTE_ALOCASH = "TIPO_CLIENTE_ALOCASH";
    public static final String sTIPO_CLIENTE_ALOBENEFIT = "TIPO_CLIENTE_ALOBENEFIT";
    public static final String sTIPO_FEE_PORCENTAJE = "TIPO_FEE_PORCENTAJE";
    public static final String sTIPO_FEE_VALOR = "TIPO_FEE_VALOR";

    // constante tipos moneda
    public static final String sTIPO_MONEDA_DOLAR = "TIPO_DOLAR";
    public static final String sTIPO_MONEDA_ALOCOIN = "TIPO_ALOCOIN";
    public static final String sTIPO_MONEDA_HEALTHCOIN = "TIPO_HEALTH_CARE_COIN";
    public static final String sTIPO_MONEDA_ALODIGA = "TIPO_ALODIGA";
    // constante crypto
    public static final String sCONSTANT = "_ICON_##_ALODIGA_";
    // constante EstadoTarjeta
    public static final String sESTADOTARJETAACTIVA = "ACTIVA";
    public static final String sESTADOTARJETADESACTIVADA = "DESACTIVADA";

    // constante codigo error
    public static final String sERR_COD_00 = "00";
    public static final String sERR_COD_01 = "01";
    public static final String sERR_COD_02 = "02";
    public static final String sERR_COD_03 = "03";
    public static final String sERR_COD_04 = "04";
    public static final String sERR_COD_05 = "05";
    public static final String sERR_COD_06 = "06";
    public static final String sERR_COD_07 = "07";
    public static final String sERR_COD_08 = "08";
    public static final String sERR_COD_09 = "09";
    public static final String sERR_COD_10 = "10";
    public static final String sERR_COD_11 = "11";
    public static final String sERR_COD_12 = "12";
    public static final String sERR_COD_13 = "13";
    public static final String sERR_COD_14 = "14";
    public static final String sERR_COD_15 = "15";
    public static final String sERR_COD_16 = "16";
    public static final String sERR_COD_17 = "17";
    public static final String sERR_COD_18 = "18";
    public static final String sERR_COD_19 = "19";
    public static final String sERR_COD_20 = "20";
    public static final String sERR_COD_21 = "21";
    public static final String sERR_COD_96 = "96";
    public static final String sERR_COD_97 = "97";
    public static final String sERR_COD_98 = "98";
    public static final String sERR_COD_99 = "99";

    public static final String SQL_LISTA_EMPRESAS = "	SELECT  "
            + "    distinct e.empresaId, e.nombre "
            + " FROM "
            + "    authpos.pos p "
            + "        INNER JOIN "
            + "    alodigaCore.usuario u ON u.usuarioId = p.usuario_id "
            + "        INNER JOIN "
            + "    alodigaCore.perfilAloPos pa ON pa.perfilAloPosId = u.perfilAloPosId "
            + "        INNER JOIN "
            + "    alodigaCore.empresa e ON pa.empresaId = e.empresaId "
            + "        INNER JOIN "
            + "    authpos.tipo_pos tp ON tp.tipo_pos_id = p.tipo_pos_id ";

    public static final String sCLIENTE_RRP = "ProgramRRP";
    public static final String socialNetworkPassword = "Alo+123";

    //Pago a Comercios (Transacciones)
    public static final String sPreferenceTransaction = "transaction";
    public static final String sValidatePreferenceTransaction1 = "MAX_TRANSACTION_AMOUNT_LIMIT";
    public static final String sValidatePreferenceTransaction2 = "MAX_TRANSACTION_NUMBER_BY_ACCOUNT";
    public static final String sValidatePreferenceTransaction3 = "MAX_TRANSACTION_NUMBER_BY_CUSTOMER";
    public static final Long sTransationType = 2l;
    public static final Long sTransactionSource = 2l;
    public static final String sTransactionConcept = "Payment Shop";

    public static final String ALODIGA_WALLET_USUARIO_API = "usuarioWS";
    public static final String ALODIGA_WALLET_PASSWORD_API = "passwordWS";

    // constantes SendTypeEmail
    public static final int SEND_TYPE_EMAIL_REGISTER = 1;
    public static final int SEND_TYPE_EMAIL_RECHARGE = 2;
    public static final int SEND_TYPE_EMAIL_COMERCE_PEYMENT = 3;
    public static final int SEND_TYPE_EMAIL_WITHDRAWALS = 4;
    public static final int SEND_TYPE_EMAIL_CHANGE_PASSWORD = 5;
    public static final int SEND_TYPE_EMAIL_TOP_UP = 6;
    public static final int SEND_TYPE_EMAIL_COMERCE_PEYMENT_RECIBER = 7;
    public static final int SEND_TYPE_EMAIL_TRANSFER_ACCOUNT = 8;
    public static final int SEND_TYPE_EMAIL_TRANSFER_ACCOUNT_RECIBER = 9;
    public static final int SEND_TYPE_EMAIL_EXCHANGE_PRODUCT = 10;
    public static final int SEND_TYPE_EMAIL_TRASNFER_CARD_TO_CARD = 11;
    public static final int SEND_TYPE_EMAIL_TRASNFER_CARD_TO_CARD_RECIBER = 12;
    
    // constantes SendTypeSMS
    public static final int SEND_TYPE_SMS_REGISTER = 20;
    public static final int SEND_TYPE_SMS_RECHARGE = 21;
    public static final int SEND_TYPE_SMS_COMERCE_PEYMENT = 22;
    public static final int SEND_TYPE_SMS_WITHDRAWALS = 23;
    public static final int SEND_TYPE_SMS_CHANGE_PASSWORD = 24;
    public static final int SEND_TYPE_SMS_TOP_UP = 25;
    public static final int SEND_TYPE_SMS_COMERCE_PEYMENT_RECIBER = 26;
    public static final int SEND_TYPE_SMS_TRANSFER_ACCOUNT = 27;
    public static final int SEND_TYPE_SMS_TRANSFER_ACCOUNT_RECIBER = 28;
    public static final int SEND_TYPE_SMS_EXCHANGE_PRODUCT = 29;
    public static final int SEND_TYPE_SMS_TRASNFER_CARD_TO_CARD= 30;
    public static final int SEND_TYPE_SMS_TRASNFER_CARD_TO_CARD_RECIBER = 31;
    public static final int SEND_TYPE_SMS_TEST = 32;
    
    
    //internat Access Api Operation
    public static final String USER_API = "usuarioWS";
    public static final String PASSWORD_API = "passwordWS";

    //URL SERVCES SIMBOX
    public static final String URL_SIMBOX = "https://200.73.192.251/api/send_sms";
    public static final String TYPE_REQUEST_SIMBOX = "POST";
    public static final String REQUEST_PROPERTIE_CONTENT_TYPE = "Content-Type";
    public static final String REQUEST_PROPERTIE_CONTENT_TYPE_VALUE = "application/json";

    //CREDENCIAL 
    public static final String USER_SIMBOX_1 = "admin";
    public static final String PASSWORD_SIMBOX_1 = "Opextel.2019";

    //Language
    public static final Long SPANISH_LANGUAGE = 1L;
    public static final Long ENGLISH_LANGUAGE = 2L;

    //Integrator Name
    public static final String INTEGRATOR_SIMBOX = "Simbox";
    public static final String INTEGRATOR_TWILIO = "Twilio";
    public static final String INTEGRATOR_MASSIVA = "Massiva";

    //SMS
    public static final String SEND_SMS = "Enviado";
    public static final String SEND_SMS_FAILED = "Failed";

    //CUMPLIMIENT
    public static final Long VALIDATE = 2L;
    public static final Long EXPIRATE = 3L;
    public static final Long NOT_VALIDATE = 4L;

    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDcZKSFJW2gP/kskiaEuXz9nFDSHGKoDhLL+pIf0a1aGpfHeEhLf51cCyAGoX7rSDtlnmV1bwL5EUMsMj4KAmHhOSwygJAyQzthaYHTkEnOJ1NlDP7KyDdu9iyIEY87iWa4OlxJqElWYiHlUcrRZwWPWyTfk9v0lYyuu0lsF/n4tQIDAQAB";
    public static final String CREDENTIAL_TIME_ZONE = "America/Caracas";
    public static final String CREDENTIAL_WEB_SERVICES_USER_TEST = "usuarioWStest";
    public static final String CREDENTIAL_WEB_SERVICES_USER = "PilotoWS";
    public static final String CREDENTIAL_WEB_SERVICES_URL_TEST = "https://10.70.10.85:8000/CASA_SRTMX_TarjetaService?wsdl";
    public static final String CREDENTIAL_WEB_SERVICES_URL_PROD = "https://10.70.10.71:8000/CASA_SRTMX_TarjetaService?wsdl";
    public static final Long PREPAY_CARD_CREDENTIAL = 3L;

    //Transferencias CARD TO CARD
    public static final Long TRANSFER_CARD_TO_CARD = 8L;
    public static final String TRANSACTION_CONCEPT_TRANSFER_CARD_TO_CARD = "Transfer card to card";

    //Transferencias CARD TO CARD SOURCE
    public static final Long TRANSFER_CARD_TO_CARD_SOURCE = 5L;
    
    //Remettence
    public static final String COMMENTARY_REMETTENCE = "REMESA";
    public static final Boolean SENDING_OPTION_SMS_REMETTENCE = true;
    public static final String BANK_REMETTENCE = "1";
    public static final String PAYMENT_SERVICE_REMETTENCE = "1";
    public static final String SALES_TYPE_REMETTENCE = "1";
    public static final String LANGUAGE_REMETTENCE = "1";
    public static final String STORE_REMETTENCE = "1";
    public static final String PAYMENT_METHOD_REMITTANCE = "3";
    public static final String SERVICE_TYPE_REMITTANCE = "1";
    public static final String USER_REMITTANCE = "1";
    public static final String CASH_BOX_REMITTANCE = "CAJERO";
    public static final Float ADDITIONAL_CHANGES_REMITTANCE = 1F;
    public static final String SALES_PRICE_REMITTANCE = "1";
    public static final String POINT_REMITTANCE = "1";
    public static final Long PRODUCT_REMITTANCE = 2L;
    public static final String CORRESPONDENT_REMITTANCE = "1";
    
    
    //AFINITAS
    public static final Long PRODUCT_AFINITAS = 2L;
    
    //PAYMENT INFO
    public static final Long PAYMENT_PATNER = 2L;
    public static final Long PAYMENT_TYPE = 2L;
    
    public static final int USA_CODE = 1;
    
    public static final String PROPERTIES_PACKAGE = "com.alodiga.wallet.utils.";
    public static final String ERROR_FILE_NAME = PROPERTIES_PACKAGE + "system_error";
    public static final String MESSAGE_FILE_NAME = PROPERTIES_PACKAGE + "system_message";
    public static final String ERR_GENERAL_EXCEPTION = "E000";
    public static final String ERR_NULL_PARAMETER = "E001";
    public static final String ERR_INCORRECT_PARAMETER = "E002";
    public static final String ERR_EMPTY_LIST_EXCEPTION = "E003";
    public static final String ERR_REGISTER_NOT_FOUND_EXCEPTION = "E004";
    
}
