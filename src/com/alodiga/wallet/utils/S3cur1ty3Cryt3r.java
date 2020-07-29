package com.alodiga.wallet.utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static com.alodiga.wallet.utils.ParseLong.desencryptMD5;
import static com.alodiga.wallet.utils.ParseLong.ncr1pt;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;



/**
 *
 * @author ltoro
 */
public class S3cur1ty3Cryt3r {
    
    public static String aloDesencript(String trame, String key, String reference, String k2,String vector) throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, KeyLongException, Exception {
        //TODO Incorporar Salting al key
        ParseInt encryptString = new ParseInt();
        if (!validLenght(key)) {
            throw new KeyLongException("Invalid Key long");
        }
        String criter = "";
        String keyEn = key;
        String v = trame;
        for (int i = 0; i < keyEn.length(); i++) {
            v = ncr1pt(v, keyEn, k2);
        }
        criter = encryptString.mkdir(v, key, k2);
        String keyy2 = key; //llave
        String iv = vector; // vector de inicialización
        criter = StringEncrypt.encrypt(keyy2, iv, criter);
        return criter;
    }

    /**
     * @author kerwin gomez
     * @param String trame: this trame is necesary for the desencryp.
     * @param key: key is necesary for dessencrypt
     * @version 1.0
     *
     */
    public static String aloEncrpter(String srgs, String key, String reference, String k2,String vector) throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, KeyLongException, Exception {
       //TODO Incorporar Salting
        String pass = "";
        String keyy2 = key; //llave
        String iv = vector; // vector de inicialización
        srgs = StringEncrypt.decrypt(keyy2, iv, srgs);
        try {
            ParseInt encryptString = new ParseInt();
            if (!validLenght(key)) {
                throw new KeyLongException("Invalid Key long");
            }
            String keyEn = key;
            pass = encryptString.DesEncrypter(srgs, key, k2);
            for (int i = 0; i < keyEn.length(); i++) {
                pass = desencryptMD5(pass, keyEn, k2);
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return pass;
    }

    public static boolean validLenght(String key) {
        boolean returnb = key.length() == 16L ? true : false;
        return returnb;
    }

 public static void main(String[] args) {
		
    	
    	
        String value = "maria,d,tercerparametro,p";
        String enc;
        try {
            enc = aloDesencript(value, "1nt3r4xt3l3ph0ny", value, "DESede","0123456789ABCDEF");
            System.out.println(enc);
            String desc = aloEncrpter("maria,d,tercerparametro,p", "1nt3r4xt3l3ph0ny", null, "DESede","0123456789ABCDEF");
            System.out.println(desc);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(S3cur1ty3Cryt3r.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(S3cur1ty3Cryt3r.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(S3cur1ty3Cryt3r.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(S3cur1ty3Cryt3r.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(S3cur1ty3Cryt3r.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyLongException ex) {
            Logger.getLogger(S3cur1ty3Cryt3r.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(S3cur1ty3Cryt3r.class.getName()).log(Level.SEVERE, null, ex);
        }
	}



}
