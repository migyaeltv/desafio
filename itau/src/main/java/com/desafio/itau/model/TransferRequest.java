package com.desafio.itau.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferRequest {
    @JsonProperty("clienteId")
    private Long clienteId;
    @JsonProperty("valor")

    private Double valorTransferencia;


    public TransferRequest() {
    }

    public TransferRequest(Long clienteId, Double valorTransferencia) {
        this.clienteId = clienteId;
        this.valorTransferencia = valorTransferencia;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public double getValorTransferencia() {
        return valorTransferencia;
    }

    public void setValorTransferencia(Double valorTransferencia) {
        this.valorTransferencia = valorTransferencia;
    }

}
