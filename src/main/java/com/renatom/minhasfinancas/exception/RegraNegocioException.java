package com.renatom.minhasfinancas.exception;

// Extends RuntimeException e nao Exception
public class RegraNegocioException extends RuntimeException {

//	Criar o construtor da classe passando a msg como parametro
	public RegraNegocioException(String msg) {
		super(msg);
	}
	
}
