package model.reports;

import java.time.LocalDate;

public class ClientReportContent {

    private String productName;
    private String clientName;

    private String invoiceName;
    private LocalDate date;

    private double quantity;
    private String um;

    public ClientReportContent(String clientName, String productName, String invoiceName, LocalDate date, double quantity, String um) {

        this.productName = productName;
        this.clientName = clientName;
        this.invoiceName = invoiceName;
        this.date = date;
        this.quantity = quantity;
        this.um = um;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    private String getFormattedDate(LocalDate date) {
        String _date = "";

        String day = Integer.toString(date.getDayOfMonth());
        String month = Integer.toString(date.getMonthValue());
        String year = Integer.toString(date.getYear());

        _date += day.length() == 1 ? "0" + day : day;
        _date += ".";
        _date += month.length() == 1 ? "0" + month : month;
        _date += ".";
        _date += year;

        return _date;
    }

    @Override
    public String toString(){
        return String.format("%s  %40.2f%4s  %20s  %15s\n", productName, quantity, um, invoiceName, getFormattedDate(date));
    }


}
