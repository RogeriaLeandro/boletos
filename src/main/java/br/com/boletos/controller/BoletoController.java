package br.com.boletos.controller;

import java.util.List;
import java.util.UUID;

import br.com.boletos.model.BoletoDTO;
import br.com.boletos.model.SituacaoBoleto;
import br.com.boletos.services.BoletoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.com.boletos.model.Boleto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;

@Slf4j
@Tag(name = "boleto-controller", description = "API de Operações básicas sobre os Boletos")
@Validated
@RestController
@RequestMapping(value = "/boleto")
public class BoletoController {


    @Autowired
    private BoletoService boletoService;

    private static Logger logger = LoggerFactory.getLogger(BoletoController.class);


	@Operation(summary = "Consulta Boletos por UUID do Associado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Boletos por UUID de  Associado"),
			@ApiResponse(responseCode = "204", description = "Nenhum boleto encontrado para o uuid informado")})
	@GetMapping(value = "/{uuid}")
	public ResponseEntity<List<BoletoDTO>> listaBoletosPorUUIDAssociado(@RequestParam String uuid)  {
		return ResponseEntity.ok().body(boletoService.consultarBoletosPorUuid(uuid));
	}

	@Operation(summary = "Consulta Boletos Pagos por UUID do Associado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Boletos Pagos por UUID de  Associado"),
			@ApiResponse(responseCode = "204", description = "Nenhum boleto pago encontrado para o uuid informado")})
	@GetMapping(value = "/pagos/{uuid}")
	public ResponseEntity<List<BoletoDTO>> listaBoletosPagosPorUUIDAssociado(@RequestParam String uuid)  {
		return ResponseEntity.ok().body(boletoService.consultarBoletosPorUuidEPorSituacao(uuid, SituacaoBoleto.PAGO.getDescricaoSituacaoBoleto()));
	}

	@Operation(summary = "Consulta Boletos Em Aberto por UUID do Associado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Boletos Em Aberto por UUID de  Associado"),
			@ApiResponse(responseCode = "204", description = "Nenhum boleto em aberto encontrado para o uuid informado")})
	@GetMapping(value = "/em_aberto/{uuid}")
	public ResponseEntity<List<BoletoDTO>> listaBoletosEmAbertoPorUUIDAssociado(@RequestParam String uuid)  {
		return ResponseEntity.ok().body(boletoService.consultarBoletosPorUuidEPorSituacao(uuid, SituacaoBoleto.EM_ABERTO.getDescricaoSituacaoBoleto()));
	}

//	@Operation(summary = "Efetua Pagamento de Boleto - Busca por Documento, Identificador e Valor do Boleto")
//	@ApiResponses(value = {  @ApiResponse(code = 200, message = "Success"),
//        @ApiResponse(code = 204, message = "No Content"),
//        @ApiResponse(code = 400, message = "Bad Request"),
//        @ApiResponse(code = 401, message = "Unauthorized"),
//        @ApiResponse(code = 500, message = "Internal Server Error")})
//	@PutMapping(value = "/{uuid}")
//	public ResponseEntity<String> alteraAssociado(@PathVariable UUID uuid, @RequestBody Associado associado) {
//
//		Associado associadoAtual = associadoService.findById(uuid);
//
//		if(associadoAtual != null) {
//			BeanUtils.copyProperties(associado, associadoAtual, "uuid");
//			associadoService.cadastrarAssociado(associadoAtual);
//			return ResponseEntity.status(HttpStatus.OK).body("Associado Alterado");
//		} else {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associado Não Encontrado");
//		}
//	}


    
}
