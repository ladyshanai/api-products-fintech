package products.api.fintech.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import products.api.fintech.dto.FixedTermDepositRequest;
import products.api.fintech.dto.FixedTermDepositResponse;
import products.api.fintech.service.FixedTermDepositService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fixed-term-deposits")
@Tag(name = "Fixed Term Deposits", description = "API para gestionar plazos fijos")
public class FixedTermDepositController {

    private final FixedTermDepositService fixedTermDepositService;

    public FixedTermDepositController(FixedTermDepositService fixedTermDepositService) {
        this.fixedTermDepositService = fixedTermDepositService;
    }

    @PostMapping
    @Operation(summary = "Crear plazo fijo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plazo fijo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Plazo fijo no encontrado")
    })
    public ResponseEntity<FixedTermDepositResponse> createFixedTermDeposit(
            @RequestBody FixedTermDepositRequest request
    ) {
        var response = fixedTermDepositService.createFixedTermDeposit(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancelar plazo fijo", description = "Cancela un plazo fijo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plazo fijo cancelado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Plazo fijo no encontrado")
    })
    public ResponseEntity<FixedTermDepositResponse> cancelFixedTermDeposit(
            @Parameter(description = "ID del plazo fijo") @PathVariable Long id
    ) {
        var response = fixedTermDepositService.cancelFixedTermDeposit(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los plazos fijos")
    @ApiResponse(responseCode = "200", description = "Lista de plazos fijos")
    public ResponseEntity<List<FixedTermDepositResponse>> getAllFixedTermDeposits() {
        var response = fixedTermDepositService.getAllFixedTermDeposits();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener plazo fijo por ID")
    @ApiResponse(responseCode = "200", description = "Plazo fijo encontrado")
    public ResponseEntity<FixedTermDepositResponse> getFixedTermDepositById(@Parameter(description = "ID de plazo fijo") @PathVariable Long id) {
        var response = fixedTermDepositService.getFixedTermDepositById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Obtener plazos fijos por cuenta")
    @ApiResponse(responseCode = "200", description = "Lista de plazos fijos")
    public ResponseEntity<List<FixedTermDepositResponse>> getFixedTermDepositsByAccount(
            @PathVariable Long accountId
    ) {
        var response = fixedTermDepositService.getFixedTermDepositsByAccount(accountId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar plazo fijo")
    @ApiResponse(responseCode = "200", description = "Plazo fijo eliminado")
    public ResponseEntity<Void> deleteFixedTermDeposit(@Parameter(description = "ID de plazo fijo a eliminar") @PathVariable Long id) {
        fixedTermDepositService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}