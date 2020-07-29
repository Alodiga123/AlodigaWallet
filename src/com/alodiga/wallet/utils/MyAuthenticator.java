/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.utils;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;

/**
 *
 * @author ltoro
 */
public class MyAuthenticator extends Authenticator {

    private String user;
    private String password;

    public MyAuthenticator(String user_, String password_) {
        this.password = password_;
        this.user = user_;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        try {
            String promptString = getRequestingPrompt();
            System.out.println(promptString);
            String hostname = getRequestingHost();
            System.out.println(hostname);
            InetAddress ipaddr = getRequestingSite();
            System.out.println(ipaddr);
            int port = getRequestingPort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PasswordAuthentication(user, password.toCharArray());
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
