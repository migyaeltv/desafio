package com.desafio.itau.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.desafio.itau.controller.CadastroController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CadastroControllerTest {

    private CadastroController cadastroController;

    @Before
    public void setUp() {
        cadastroController = new CadastroController();
    }

    @Test
    public void testBuscarNomeClienteExistente() {
        Long clienteId = 1L;

        ResponseEntity<String> responseEntity = cadastroController.buscarNomeCliente(clienteId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("João"));
    }

    @Test
    public void testBuscarNomeClienteNaoExistente() {
        Long clienteId = 3L;

        ResponseEntity<String> responseEntity = cadastroController.buscarNomeCliente(clienteId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Cliente não encontrado."));
    }
}
