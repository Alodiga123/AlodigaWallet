/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.utils;

/**
 *
 * @author ltoro
 */

    
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;

import org.apache.commons.codec.binary.Base64;


public class EncriptedRsa {

    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDcZKSFJW2gP/kskiaEuXz9nFDSHGKoDhLL+pIf0a1aGpfHeEhLf51cCyAGoX7rSDtlnmV1bwL5EUMsMj4KAmHhOSwygJAyQzthaYHTkEnOJ1NlDP7KyDdu9iyIEY87iWa4OlxJqElWYiHlUcrRZwWPWyTfk9v0lYyuu0lsF/n4tQIDAQAB";
    //private static String privateKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDcZKSFJW2gP/kskiaEuXz9nFDSHGKoDhLL+pIf0a1aGpfHeEhLf51cCyAGoX7rSDtlnmV1bwL5EUMsMj4KAmHhOSwygJAyQzthaYHTkEnOJ1NlDP7KyDdu9iyIEY87iWa4OlxJqElWYiHlUcrRZwWPWyTfk9v0lYyuu0lsF/n4tQIDAQAB";

    public static PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.decodeBase64(data.getBytes()), getPrivateKey(base64PrivateKey));
    }

    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
        try {
            String encryptedString = Base64.encodeBase64String(encrypt("5496720000744709", publicKey));
            System.out.println(encryptedString);
            //String decryptedString = RSAUtil.decrypt(encryptedString, privateKey);
            //System.out.println(decryptedString);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }

    }
    
}
