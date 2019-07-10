package br.ifpe.web2.missoes.exceptions;

public class FuncionarioNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public FuncionarioNotFoundException(String mensagem) {
		super(mensagem);
	}

}
