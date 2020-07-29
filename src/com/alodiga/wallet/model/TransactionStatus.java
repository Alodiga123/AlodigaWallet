package com.alodiga.wallet.model;

public enum TransactionStatus {
    CREATED("CREATED"),
    IN_PROCESS("IN_PROCESS"),
    FAILED("FAILED"),
    COMPLETED("COMPLETED");
    
    private String codigo;
    
    TransactionStatus(String codigo) { 
       this.codigo = codigo;
    }
   
    public String getCodigo(){
        return codigo;
    }
    
}
