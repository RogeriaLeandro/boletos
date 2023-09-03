package br.com.boletos.exceptions;

public class ValorDeBoletoDivergenteNoPagamentoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public ValorDeBoletoDivergenteNoPagamentoException(String mensagem) {
        super(mensagem);
    }
}
