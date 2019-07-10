package br.ifpe.web2.missoes.exceptions;

public class FileStorageException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public FileStorageException(String mensagen) {
		super(mensagen);
	}
	public FileStorageException(String mensagem, Throwable cause) {
		super(mensagem, cause);
	}

}
