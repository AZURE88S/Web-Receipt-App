package com.example.webapptest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptResponse {
    private String merchant;
    private String date;
    private String total;
    private String currency;

    public ReceiptResponse() {
    }

    public ReceiptResponse(String merchant, String date, String total, String currency) {
        this.merchant = merchant;
        this.date = date;
        this.total = total;
        this.currency = currency;
    }

    public String getMerchant() {
        return merchant;
    }

    public String getDate() {
        return date;
    }

    public String getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}