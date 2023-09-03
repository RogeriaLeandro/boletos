package br.com.boletos.resources;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.boletos.model.Boleto;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "boleto-resource")
@SwaggerDefinition(tags = {
        			@Tag(name = "boleto-resource", 
		     		description = "API de Boletos")
					})
@Validated
@RestController
@RequestMapping(value = "/boleto")
public class BoletoResource {

    private static Logger logger = LoggerFactory.getLogger(BoletoResource.class);


	@Operation(summary = "Consulta Boleto por UUID do Associado")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
							@ApiResponse(code = 204, message = "No Content"),
							@ApiResponse(code = 400, message = "Bad Request"),
							@ApiResponse(code = 401, message = "Unauthorized"),
							@ApiResponse(code = 500, message = "Internal Server Error")})
	@GetMapping(value = "/{uuid}")
	public ResponseEntity<Boleto> boletoPorDocumentoUUIDAssociado(@PathVariable UUID uuid) {
		return null;
		// return associadoService.findById(uuid)
		// 	.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent.build());
	}

	@Operation(summary = "Efetua Pagamento de Boleto - Busca por Documento, Identificador e Valor do Boleto")
	@ApiResponses(value = {  @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 204, message = "No Content"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
	@PutMapping(value = "/{uuid}")
	public ResponseEntity<String> alteraAssociado(@PathVariable UUID uuid, @RequestBody Associado associado) {
		
		Associado associadoAtual = associadoService.findById(uuid);
		
		if(associadoAtual != null) {
			BeanUtils.copyProperties(associado, associadoAtual, "uuid");
			associadoService.cadastrarAssociado(associadoAtual);
			return ResponseEntity.status(HttpStatus.OK).body("Associado Alterado");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associado Não Encontrado");
		}
	}


    
}
