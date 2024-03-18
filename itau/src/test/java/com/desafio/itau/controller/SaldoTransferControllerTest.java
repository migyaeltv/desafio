package com.desafio.itau.controller;

import com.desafio.itau.service.ContaCorrenteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SaldoTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContaCorrenteService contaCorrenteService;


    @Before
    public void setUp() {
        when(contaCorrenteService.verificarContaAtiva(anyLong())).thenReturn(true);
    }

    @Test
    public void testRealizarTransferenciaComSucesso() throws Exception {
        when(contaCorrenteService.verificarSaldoSuficiente(anyLong(), any())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\": 1, \"valor\": 500}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Transferência realizada com sucesso."));
    }

    @Test
    public void testRealizarTransferenciaComContaInativa() throws Exception {
        when(contaCorrenteService.verificarContaAtiva(anyLong())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\": 1, \"valor\": 500}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Conta corrente inativa."));
    }

    @Test
    public void testRealizarTransferenciaComSaldoInsuficiente() throws Exception {
        when(contaCorrenteService.verificarSaldoSuficiente(anyLong(), any())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\": 1, \"valor\": 20000}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Saldo insuficiente para realizar a transferência."));
    }
}
