package br.com.boletos.integracao.associado.service;

import br.com.boletos.integracao.associado.client.AssociadoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class AssociadoService {

    @Autowired(required = true)
    private AssociadoClient associadoClient;

    public boolean associadoECadastrado(String idAssociado) {
        var associado = associadoClient.consultarAssociado(idAssociado);

        if(associado != null){
            return true;
        }
        return false;
    }
}
