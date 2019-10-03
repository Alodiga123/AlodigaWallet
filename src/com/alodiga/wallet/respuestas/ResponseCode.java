package com.alodiga.wallet.respuestas;

public enum ResponseCode {
	
	EXITO ("00"),
	DATOS_INVALIDOS("01"),
	CONTRASENIA_EXPIRADA("03"),
	IP_NO_CONFIANZA("04"),
	CREDENCIALES_INVALIDAS("05"),
	USUARIO_BLOQUEADO("06"),
	CODIGO_VALIDACION_INVALIDO("07"),
	NUMERO_TELEFONO_YA_EXISTE("08"),
	ENVIO_CORREO_FALLIDO("09"),
	CORREO_YA_EXISTE("10"),
	DATOS_NULOS("11"),
	PRIMER_INGRESO("12"),
	EIN_YA_EXISTE("14"),
	FEDERAL_TAX_YA_EXISTE("15"),
	CUENTA_BANCARIA_YA_EXISTE("17"),
	GENERAR_CODIGO("18"),
	TOKEN_EXPIRADO("19"),
	SOLICITUD_TARJETA_ACTIVA("20"),
	AFILIACIONES_MAXIMAS_ALCANZADAS("21"),
        
        
        USER_NOT_HAS_PHONE_NUMBER("22"),
        USER_NOT_HAS_PRODUCT("23"),
        TRANSACTION_LIST_NOT_FOUND_EXCEPTION("24"),
        EMPTY_LIST_HAS_BANK("25"),
        EMPTY_LIST_COUNTRY("26"),
        

        
	APLICACION_NO_EXISTE("94"),
	USUARIO_SOSPECHOSO("95"),
	USUARIO_PENDIENTE("96"),
        
	USUARIO_NO_EXISTE("97"),
	ERROR_CREDENCIALES("98"),
	ERROR_INTERNO("99"),
        //Mensajes Guardar Transaction
        TRANSACTION_AMOUNT_LIMIT("30"),
        TRANSACTION_MAX_NUMBER_BY_ACCOUNT("31"),
        TRANSACTION_MAX_NUMBER_BY_CUSTOMER("32"),
        USER_HAS_NOT_BALANCE("33");
	
	private String codigo; 
	
	private ResponseCode(String codigo){
		this.codigo = codigo;
	}
	
	public String getCodigo(){
		return codigo;
	}
}
