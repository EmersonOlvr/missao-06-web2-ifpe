package br.ifpe.web2.missoes.exceptions;

public class FileValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public FileValidationException(String mensagem) {
		super(mensagem);
	}

}
