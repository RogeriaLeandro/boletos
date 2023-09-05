package br.com.boletos.exceptions;

public class DataVencimentoAntesDataAtualException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public DataVencimentoAntesDataAtualException(String mensagem) {
        super(mensagem);
    }
}
