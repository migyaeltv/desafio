package com.desafio.itau.controller;

import com.desafio.itau.model.TransferNotification;
import com.desafio.itau.model.TransferRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bacen")
public class BacenMockController {
    private static final int LIMITE_REQUISICOES = 3;
    private int contadorRequisicoes = 0;

    @PostMapping("/notificarTransferencia")
    public ResponseEntity<String> notificarTransferencia(@RequestBody TransferNotification transferNotification) {
        contadorRequisicoes++;
        if (contadorRequisicoes > LIMITE_REQUISICOES) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Limite de requisições excedido no BACEN. Código de status: 429 (Too Many Requests)");
        }
        return ResponseEntity.ok("Notificação recebida com sucesso pelo BACEN.");
    }
}
