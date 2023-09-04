package br.com.boletos.handler;

import br.com.boletos.exceptions.AssociadoNaoExisteNaAPIException;
import br.com.boletos.exceptions.BoletoPagoException;
import br.com.boletos.exceptions.DataVencimentoAposDataAtualException;
import br.com.boletos.exceptions.ValorDeBoletoDivergenteNoPagamentoException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class BoletosErrorHandler {


    @ExceptionHandler(value = {BoletoPagoException.class,
                                DataVencimentoAposDataAtualException.class,
                                ValorDeBoletoDivergenteNoPagamentoException.class,
                                AssociadoNaoExisteNaAPIException.class})
    public ResponseEntity<Object> catchBoleto(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<Object>(ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
