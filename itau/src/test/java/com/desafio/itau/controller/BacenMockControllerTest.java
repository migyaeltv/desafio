package com.desafio.itau.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.desafio.itau.model.TransferNotification;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BacenMockControllerTest {

    private BacenMockController bacenMockController;
    private static final int LIMITE_REQUISICOES = 3;

    @Before
    public void setUp() {
        bacenMockController = new BacenMockController();
    }

    @Test
    public void testNotificarTransferenciaSucesso() {
        // Configuração do cenáriov
        TransferNotification transferNotification = new TransferNotification();

        // Execução do método a ser testado
        ResponseEntity<String> responseEntity = bacenMockController.notificarTransferencia(transferNotification);

        // Verificação
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Notificação recebida com sucesso pelo BACEN.", responseEntity.getBody());
    }

    @Test
    public void testNotificarTransferenciaExcedeLimite() {
        // Configuração do cenário
        TransferNotification transferNotification = new TransferNotification();
        // Simula o limite de requisições excedido
        for (int i = 0; i < LIMITE_REQUISICOES; i++) {
            bacenMockController.notificarTransferencia(transferNotification);
        }

        // Execução do método a ser testado
        ResponseEntity<String> responseEntity = bacenMockController.notificarTransferencia(transferNotification);

        // Verificação
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, responseEntity.getStatusCode());
        assertEquals("Limite de requisições excedido no BACEN. Código de status: 429 (Too Many Requests)", responseEntity.getBody());
    }
}