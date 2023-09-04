package br.com.boletos.exceptions;

public class DataVencimentoAposDataAtualException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public DataVencimentoAposDataAtualException(String mensagem) {
        super(mensagem);
    }
}
