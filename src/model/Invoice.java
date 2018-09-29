package model;


import model.interfaces.*;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Invoice implements InvoiceInterface, Serializable {

    private int id; //identificator unic(va fi identic cu cel din baza de date(InvoiceBlob))

    private int batchNumber; //numarul facturi

    private String name;
    private String pdfPath = "C:\\temp\\facturier";
    private String format;
    private File pdf;

    private SellerInterface seller;
    private ClientInterface client;
    private EmitorInterface emitor;
    private ArrayList<TableItem> products;
    private TableItem discount;
    private CarInterface car;

    private BatchInterface batch;
    private LocalDate date;
    private TvaInterface tva;

    private InvoiceInfoInterface info;

    private BigDecimal totalValue;
    private BigDecimal totalTvaValue;
    private BigDecimal totalDiscountValue;
    private BigDecimal totalDiscountTvaValue;

    public Invoice(){
        totalValue = BigDecimal.ZERO;
        totalTvaValue = BigDecimal.ZERO;
        totalDiscountValue = BigDecimal.ZERO;
        totalDiscountTvaValue= BigDecimal.ZERO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public File getPdf() {
        return pdf;
    }

    public void setPdf(File pdf) {
        this.pdf = pdf;
    }

    public SellerInterface getSeller() {
        return seller;
    }

    public void setSeller(SellerInterface seller) {
        this.seller = seller;
    }

    public ClientInterface getClient() {
        return client;
    }

    public void setClient(ClientInterface client) {
        this.client = client;
    }

    public EmitorInterface getEmitor() {
        return emitor;
    }

    public void setEmitor(EmitorInterface emitor) {
        this.emitor = emitor;
    }

    public ArrayList<TableItem> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<TableItem> products) {
        this.products = products;
        autoCompute();
    }

    public TableItem getDiscount(){
        return discount;
    }

    public void setDiscount(TableItem discount) {
        this.discount = discount;
    }

    public CarInterface getCar() {
        return car;
    }

    public void setCar(CarInterface car) {
        this.car = car;
    }

    public BatchInterface getBatch() {
        return batch;
    }

    public void setBatch(BatchInterface batch){
        this.batch = batch;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TvaInterface getTva() {
        return tva;
    }

    public void setTva(TvaInterface tva) {
        this.tva = tva;
    }

    public InvoiceInfoInterface getInfo() {
        return info;
    }

    public void setInfo(InvoiceInfoInterface info) {
        this.info = info;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getTotalTvaValue() {
        return totalTvaValue;
    }

    public void setTotalTvaValue(BigDecimal totalTvaValue) {
        this.totalTvaValue = totalTvaValue;
    }

    public BigDecimal getTotalDiscountValue() {
        return totalDiscountValue;
    }

    public void setTotalDiscountValue(BigDecimal totalDiscountValue) {
        this.totalDiscountValue = totalDiscountValue;
    }

    public BigDecimal getTotalDiscountTvaValue() {
        return totalDiscountTvaValue;
    }

    public void setTotalDiscountTvaValue(BigDecimal totalDiscountTvaValue) {
        this.totalDiscountTvaValue = totalDiscountTvaValue;
    }

    private void autoCompute(){
        for(TableItem p : products){
            totalValue = totalValue.add(p.getProductValue());
            totalTvaValue = totalTvaValue.add(p.getProductTvaValue());
            totalDiscountValue = totalDiscountValue.subtract(p.getProductDiscountValue());
            totalDiscountTvaValue = totalDiscountTvaValue.subtract(p.getProductDiscountTvaValue());
        }
        totalValue = totalValue.add(totalDiscountValue);
        totalTvaValue = totalTvaValue.add(totalDiscountTvaValue);

    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", batchNumber=" + batchNumber +
                ", name='" + name + '\'' +
                ", pdfPath='" + pdfPath + '\'' +
                ", pdf=" + pdf +
                ", seller=" + seller +
                ", client=" + client +
                ", emitor=" + emitor +
                ", products=" + products +
                ", car=" + car +
                ", date=" + date +
                ", tva=" + tva +
                ", totalValue=" + totalValue +
                ", totalTvaValue=" + totalTvaValue +
                ", totalDiscountValue=" + totalDiscountValue +
                ", totalDiscountTvaValue=" + totalDiscountTvaValue +
                '}';
    }


}
