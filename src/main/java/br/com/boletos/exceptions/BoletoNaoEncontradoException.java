package br.com.boletos.exceptions;

public class BoletoNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BoletoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}