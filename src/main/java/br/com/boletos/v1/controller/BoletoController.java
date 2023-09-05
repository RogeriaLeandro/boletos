package br.com.boletos.v1.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import br.com.boletos.v1.dto.BoletoDTO;
import br.com.boletos.model.SituacaoBoleto;
import br.com.boletos.v1.service.BoletoService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;

@Slf4j
@Tag(name = "boleto-controller", description = "API de Operações básicas sobre os Boletos")
@Validated
@RestController
@RequestMapping(value = "/v1/boletos")
public class BoletoController {

    @Autowired
    private BoletoService boletoService;

    private static Logger logger = LoggerFactory.getLogger(BoletoController.class);

	@Operation(summary = "Consulta Boletos por UUID do Associado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Boletos por UUID de  Associado"),
			@ApiResponse(responseCode = "400", description = "Nenhum boleto encontrado para o uuid informado")})
	@GetMapping(value = "/{uuid}")
	public ResponseEntity<BoletoDTO> consultaBoletoPorUUIDAssociado(@RequestParam String uuid)  {
		return boletoService.consultarBoletoPorUuid(uuid)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.noContent().build());
	}

	@Operation(summary = "Consulta Boletos Pagos por UUID do Associado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Boletos Pagos por UUID de  Associado"),
			@ApiResponse(responseCode = "400", description = "Nenhum boleto pago encontrado para o uuid informado")})
	@GetMapping(value = "/pagos/{uuid}")
	public ResponseEntity<BoletoDTO> consultaBoletoPagoPorUUIDAssociado(@RequestParam String uuid)  {
		return boletoService.consultarBoletoPorUuidEPorSituacao(uuid, SituacaoBoleto.PAGO.getDescricaoSituacaoBoleto())
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.noContent().build());
	}

	@Operation(summary = "Consulta Boletos Em Aberto por UUID do Associado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Boletos Em Aberto por UUID de  Associado"),
			@ApiResponse(responseCode = "400", description = "Nenhum boleto em aberto encontrado para o uuid informado")})
	@GetMapping(value = "/em_aberto/{uuid}")
	public ResponseEntity<BoletoDTO> consultaBoletoEmAbertoPorUUIDAssociado(@RequestParam String uuid)  {
		return boletoService.consultarBoletoPorUuidEPorSituacao(uuid, SituacaoBoleto.EM_ABERTO.getDescricaoSituacaoBoleto())
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.noContent().build());
	}

	@Operation(summary = "Efetua Pagamento de Boleto - Busca por Documento, Identificador e Valor do Boleto")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Boleto Pago"),
			@ApiResponse(responseCode = "400", description = "Boleto já Pago Anteriormente / Valor Divergente ao Cadastrado / Data de Vencimento Expirada")})
	@PutMapping(value = "/{documentoAssociado}/{idBoleto}/{valor}")
	public ResponseEntity<Void> efetuaPagamentoBoleto(@PathVariable String documentoAssociado, @PathVariable String idBoleto, @PathVariable BigDecimal valor) {
		boletoService.efetuaPagamento(documentoAssociado, idBoleto, valor);
		return ResponseEntity.noContent().build();
	}

}
