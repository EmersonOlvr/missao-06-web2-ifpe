package br.ifpe.web2.missoes.exceptions;

public class FotoNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public FotoNotFoundException(String mensagem) {
		super(mensagem);
	}
	public FotoNotFoundException(String mensagem, Throwable cause) {
		super(mensagem, cause);
	}

}
