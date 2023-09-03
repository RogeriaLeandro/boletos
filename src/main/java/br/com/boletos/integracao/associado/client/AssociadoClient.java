package br.com.boletos.integracao.associado.client;

import org.springframework.cloud.openfeign.FeignClient;
import br.com.boletos.integracao.associado.dto.AssociadoDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "Associado-Api", url = "${integracao.associado-api.url}")
public interface AssociadoClient {

    @RequestMapping(method = RequestMethod.GET, value = "/v1/associados/{idAssociado}")
    AssociadoDTO consultarAssociado(@PathVariable(value = "idAssociado") String idAssociado);
}
