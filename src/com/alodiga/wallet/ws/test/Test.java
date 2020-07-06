/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.ws.test;

import com.alodiga.wallet.common.model.BalanceHistory;
import com.alodiga.wallet.common.model.BankOperation;
import com.alodiga.wallet.common.model.Transaction;
import com.alodiga.wallet.common.utils.AmazonSESSendMail;
import com.alodiga.wallet.common.utils.Mail;
import com.alodiga.wallet.common.utils.SendMailTherad;
import com.alodiga.wallet.common.utils.Utils;
import com.ericsson.alodiga.ws.Cuenta;
import com.ericsson.alodiga.ws.Usuario;



/**
 *
 * @author ltoro
 */
public class Test {
    
    
    public static void main(String[] args){      
        Usuario usuario = new Usuario();
        usuario.setNombre("Kerwin");
        usuario.setApellido("Gomez");
        usuario.setCredencial("DAnye");
        usuario.setEmail("moisegrat12@gmail.com");
        usuario.setMovil("584241934005");
        
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("01050614154515461528");
        usuario.setCuenta(cuenta);
        Transaction transaction = new Transaction();
        
        transaction.setId(1412L);
        transaction.getId();
        transaction.getTotalAmount();
        transaction.setTotalAmount(Float.valueOf("2"));
        BankOperation manualRecharge = new BankOperation();
        manualRecharge.setBankOperationNumber("12364");
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setCurrentAmount(20);
        balanceHistory.setOldAmount(25);
        
        transaction.setConcept("12356");

        Mail mail = Utils.sendMailUserTransferReciber("ES", Float.valueOf(25), "Pago a Comercio", "moises", "moisegrat@hotmail.com");
        System.out.println("body: " + mail.getBody());
        
        
        
//        SendMailTherad sendMailTherad = new SendMailTherad("ES", usuario, Integer.valueOf("1"));
//        sendMailTherad.run();
//        
//        try {
//                    //Envio de Correo Electronico
////                    EnvioCorreo.enviarCorreoHtml(new String[]{mail.getTo().get(0)},
////                mail.getSubject(),  mail.getBody(), Utils.obtienePropiedad("mail.user"), null);
//                    
//            AmazonSESSendMail.SendMail(mail.getSubject(), mail.getBody(), mail.getTo().get(0));
//            
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        
    }
    
    
}
