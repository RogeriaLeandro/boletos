package br.com.boletos.integracao.associado.service;

import br.com.boletos.integracao.associado.client.AssociadoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

public class AssociadoService {

    @Autowired
    private AssociadoClient associadoClient;

    public boolean associadoECadastrado(String idAssociado) {
        var associado = associadoClient.consultarAssociado(idAssociado);

        if(associado == null){
            return false;
        }
        return true;
    }

    public boolean documentoEValido(String documento) {
        var associado = associadoClient.validarDocumento(documento);

        if(associado == null){
            return false;
        }
        return true;
    }

}
