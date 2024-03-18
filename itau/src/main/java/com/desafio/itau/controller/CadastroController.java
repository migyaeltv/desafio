package com.desafio.itau.controller;

import com.desafio.itau.service.ContaCorrenteService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cadastro")
public class CadastroController {


    private static final Map<Long, String> dadosCadastroMock = new HashMap<>();

    static {
        dadosCadastroMock.put(1L, "Migyael");
        dadosCadastroMock.put(2L, "Guillermo");
    }

    @GetMapping("/buscarNomeCliente/{clienteId}")
    public ResponseEntity<String> buscarNomeCliente(@PathVariable Long clienteId) {
        String nomeCliente = dadosCadastroMock.get(clienteId);

        if (nomeCliente != null) {
            return ResponseEntity.ok(nomeCliente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
    }


}
