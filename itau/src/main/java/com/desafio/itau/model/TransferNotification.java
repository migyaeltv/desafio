package com.desafio.itau.model;

public class TransferNotification {
    private String transferId;
    private String message;

    public TransferNotification() {
    }

    public TransferNotification(String transferId, String message) {
        this.transferId = transferId;
        this.message = message;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
