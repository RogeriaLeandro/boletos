package br.com.boletos.exceptions;

public class AssociadoNaoExisteNaAPIException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AssociadoNaoExisteNaAPIException(String mensagem) {
        super(mensagem);
    }
}
