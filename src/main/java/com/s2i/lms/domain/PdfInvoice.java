package com.s2i.lms.domain;

public class PdfInvoice {
    private String text;
    private String invoiceNumber;
    private String invoiceDate;

    public PdfInvoice(String text, String invoiceNumber, String invoiceDate) {
        this.text = text;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Override
    public String toString() {
        return "PdfInvoice{" +
            "text='" + text + '\'' +
            ", invoiceNumber='" + invoiceNumber + '\'' +
            ", invoiceDate='" + invoiceDate + '\'' +
            '}';
    }
}
