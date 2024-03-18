package com.desafio.itau.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ContaCorrenteService {

    private Map<Long, BigDecimal> saldosIniciais = new HashMap<>();
    private Map<Long, Map<LocalDate, BigDecimal>> totalTransferenciasPorDia = new HashMap<>();
    private BigDecimal limiteDiario = BigDecimal.valueOf(1000); // Limite diário de transferência
    public ContaCorrenteService() {
        saldosIniciais.put(1L, BigDecimal.valueOf(5000)); // Saldo inicial para o cliente 1
        saldosIniciais.put(2L, BigDecimal.valueOf(7000)); // Saldo inicial para o cliente 2
    }
    public boolean verificarContaAtiva(Long clienteId) {
        return true;
    }
    public boolean verificarSaldoSuficiente(Long clienteId, BigDecimal valorTransferencia) {
        BigDecimal saldoCliente = saldosIniciais.getOrDefault(clienteId, BigDecimal.ZERO);
        return saldoCliente.compareTo(valorTransferencia) >= 0;
    }

    public boolean excedeLimiteDiario(Long clienteId, BigDecimal valorTransferencia) {
        Map<LocalDate, BigDecimal> transferenciasPorDia = totalTransferenciasPorDia.getOrDefault(clienteId, new HashMap<>());
        BigDecimal totalTransferidoHoje = transferenciasPorDia.getOrDefault(LocalDate.now(), BigDecimal.ZERO);
        BigDecimal novoTotal = totalTransferidoHoje.add(valorTransferencia);
        return novoTotal.compareTo(limiteDiario) > 0;
    }

    public void registrarTransferencia(Long clienteId, BigDecimal valorTransferencia) {
        BigDecimal saldoCliente = saldosIniciais.getOrDefault(clienteId, BigDecimal.ZERO);
        Map<LocalDate, BigDecimal> transferenciasPorDia = totalTransferenciasPorDia.getOrDefault(clienteId, new HashMap<>());
        BigDecimal totalTransferidoHoje = transferenciasPorDia.getOrDefault(LocalDate.now(), BigDecimal.ZERO);

        if (!excedeLimiteDiario(clienteId,valorTransferencia) && verificarSaldoSuficiente(clienteId, valorTransferencia)) {
            saldosIniciais.put(clienteId, saldoCliente.subtract(valorTransferencia));
            transferenciasPorDia.put(LocalDate.now(), totalTransferidoHoje.add(valorTransferencia));
            totalTransferenciasPorDia.put(clienteId, transferenciasPorDia);
        } else {
            throw new RuntimeException("A transferência excede o limite diário ou o saldo é insuficiente.");
        }
    }

    public void resetarLimiteDiario() {
        totalTransferenciasPorDia.clear();
    }
}
