package br.com.boletos.exceptions;

public class BoletoPagoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BoletoPagoException(String mensagem) {
        super(mensagem);
    }
}
