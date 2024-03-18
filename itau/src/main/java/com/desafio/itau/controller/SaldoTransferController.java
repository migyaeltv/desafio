package com.desafio.itau.controller;

import com.desafio.itau.model.TransferNotification;
import com.desafio.itau.model.TransferRequest;
import com.desafio.itau.service.ContaCorrenteService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1")
public class SaldoTransferController {
    @Autowired
    private ContaCorrenteService contaCorrenteService;
    @Autowired
    private BacenMockController bacenMockController;



    @PostMapping("/transferencia")
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @CircuitBreaker(name = "transferenciaCircuitBreaker")
    @Retry(name = "transferenciaRetry")
    @TimeLimiter(name = "transferenciaTimeLimiter")

    public ResponseEntity<String> realizarTransferencia(@RequestBody TransferRequest transferRequest){

        Long clienteId = transferRequest.getClienteId();
        BigDecimal valorTransferencia = BigDecimal.valueOf(transferRequest.getValorTransferencia());

        if (!contaCorrenteService.verificarContaAtiva(transferRequest.getClienteId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conta corrente inativa.");
        }

        if (!contaCorrenteService.verificarSaldoSuficiente(transferRequest.getClienteId(), BigDecimal.valueOf(transferRequest.getValorTransferencia()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente para realizar a transferência.");
        }

        if (contaCorrenteService.excedeLimiteDiario(clienteId, valorTransferencia)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A transferência excede o limite diário de R$ 1.000,00.");
        }
        UUID uuid = UUID.randomUUID();
        String transferId= uuid.toString();
        System.out.printf("Realizando Transferencia e notificando ao Bacen");
        contaCorrenteService.registrarTransferencia(clienteId, valorTransferencia);

        TransferNotification notification = new TransferNotification(transferId, "Transferência realizada com sucesso.");
        ResponseEntity<String> response = bacenMockController.notificarTransferencia(notification);

        return ResponseEntity.ok("Transferência realizada com sucesso.");
    }


}

