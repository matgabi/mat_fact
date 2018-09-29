package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.interfaces.*;
import repository.interfaces.RepositoryInterface;
import sun.util.resources.cldr.vi.TimeZoneNames_vi;
import view.alerts.Alerts;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class NewInvoiceState {

    private int id; //necessary when editing an invoice

    private BatchInterface batch;
    private SellerInterface seller;
    private ClientInterface client;
    private EmitorInterface emitor;
    private ObservableList<TableItem> products;
    private TableItem discount;
    private CarInterface car;
    private TvaInterface tva;
    private String invoiceFormat;
    private LocalDate date;
    private InvoiceInfoInterface info;

    private BatchInterface _batch;
    private ClientInterface _client;
    private ObservableList<TableItem> _products;
    private TvaInterface _tva;



    private RepositoryInterface repository;
    private PdfGenerator pdfGenerator;

    private boolean editingState;

    public NewInvoiceState(RepositoryInterface repository, PdfGenerator pdfGenerator){
        this.repository = repository;
        this.pdfGenerator = pdfGenerator;

        batch = repository.getAllBatches().get(0);
        seller = null;
        client = null;
        emitor = null;
        discount = new DiscountItem(BigDecimal.ZERO, BigDecimal.ZERO);
        car = null;
        tva = null;
        info = null;

        _batch = null;
        _client = null;
        _tva = null;


        products = FXCollections.observableArrayList();
        products.add(discount);
        _products = null;
    }

    public void changeTva(TvaInterface tva){
        products.remove(products.size() - 1);
        for(TableItem t : products){
            ((ProductItem)t).setTva(tva);
        }
        recomputeDiscount();
    }

    public void insertNewProduct(TableItem product){

        if(products.size() == 0){
            products.add(product);
        }else{
            products.remove(products.size() - 1);
            products.add(product);
        }
        recomputeDiscount();
    }

    public void recomputeDiscountOnEdit(){
        products.remove(products.size() - 1);
        BigDecimal totalDiscountValue = BigDecimal.ZERO;
        BigDecimal totalDiscountTvaValue = BigDecimal.ZERO;
        for(TableItem p : products){
            totalDiscountValue = totalDiscountValue.add(p.getProductDiscountValue());
            totalDiscountTvaValue = totalDiscountTvaValue.add(p.getProductDiscountTvaValue());
        }

        ((DiscountItem)discount).setValue(totalDiscountValue.multiply(BigDecimal.valueOf(-1)));
        ((DiscountItem)discount).setTvaValue(totalDiscountTvaValue.multiply(BigDecimal.valueOf(-1)));
        products.add(discount);
    }

    private void recomputeDiscount(){
        BigDecimal totalDiscountValue = BigDecimal.ZERO;
        BigDecimal totalDiscountTvaValue = BigDecimal.ZERO;
        for(TableItem p : products){
            totalDiscountValue = totalDiscountValue.add(p.getProductDiscountValue());
            totalDiscountTvaValue = totalDiscountTvaValue.add(p.getProductDiscountTvaValue());
        }

        ((DiscountItem)discount).setValue(totalDiscountValue.multiply(BigDecimal.valueOf(-1)));
        ((DiscountItem)discount).setTvaValue(totalDiscountTvaValue.multiply(BigDecimal.valueOf(-1)));
        products.add(discount);
    }

    public void deleteProduct(TableItem product){
        if(products.size() > 1){
            products.remove(product);
            products.remove(products.size() - 1);
            recomputeDiscount();
        }

    }

    public BigDecimal getTotalValue(){
        BigDecimal totalValue = BigDecimal.valueOf(0);
        for(TableItem p : products)
        {
            totalValue = totalValue.add(p.getProductValue());
        }
        return totalValue;
    }

    public BigDecimal getTotalTvaValue()
    {
        BigDecimal totalTvaValue = BigDecimal.valueOf(0);
        for(TableItem p : products)
        {
            totalTvaValue = totalTvaValue.add(p.getProductTvaValue());
        }

        return totalTvaValue;
    }

    public BigDecimal getTotal()
    {
        BigDecimal totalValue = BigDecimal.valueOf(0);
        BigDecimal totalTvaValue = BigDecimal.valueOf(0);
        for(TableItem p : products)
        {
            totalValue = totalValue.add(p.getProductValue());
            totalTvaValue = totalTvaValue.add(p.getProductTvaValue());
        }
        return totalTvaValue.add(totalValue);
    }

    //--------------------getters and setters----------------------

    public BatchInterface getBatch() {
        return batch;
    }

    public void setBatch(BatchInterface batch) {
        this.batch = batch;
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

    public ObservableList<TableItem> getProducts() {
        return products;
    }

    public void setProducts(ObservableList<TableItem> products) {
        this.products.clear();
        this.products.setAll(products);

        recomputeDiscount();
    }

    public TableItem getDiscount() {
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

    public TvaInterface getTva() {
        return tva;
    }

    public void setTva(TvaInterface tva) {
        this.tva = tva;
    }

    public String getInvoiceFormat(){
        return invoiceFormat;
    }

    public void setInvoiceFormat(String invoiceFormat){
        this.invoiceFormat = invoiceFormat;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public InvoiceInfoInterface getInfo() {
        return info;
    }

    public void setInfo(InvoiceInfoInterface info) {
        this.info = info;
    }

    public boolean isEditingState() {
        return editingState;
    }

    public void setEditingState(boolean editingState) {
        this.editingState = editingState;
    }

    public void clearProductsTable(){
        products.clear();
        recomputeDiscount();
    }

    public void loadEditableInvoice(InvoiceInterface invoice){
        setEditingState(true);
        _batch = batch;
        _client = client;
        _products = FXCollections.observableArrayList(products);
        _tva = tva;

        id = invoice.getId();
        setBatch(invoice.getBatch());
        setSeller(invoice.getSeller());
        setClient(invoice.getClient());
        setEmitor(invoice.getEmitor());
        setProducts(FXCollections.observableArrayList(invoice.getProducts()));
        setCar(invoice.getCar());
        setTva(invoice.getTva());
        setInvoiceFormat(invoice.getFormat());
        setDate(invoice.getDate());
        setInfo(invoice.getInfo());
    }

    public void cancelEditInvoice(){
        setBatch(_batch);
        setClient(_client);
        _products.remove(_products.size() - 1);
        setProducts(_products);
        setTva(_tva);

        seller = null;
        emitor = null;
        car = null;
        info = null;
        invoiceFormat = "a4";
        setEditingState(false);

    }

    public boolean editSelectedInvoice(){
        ArrayList<TableItem> products_ = new ArrayList<>(products);

        products_.remove(products_.size() - 1); //delete discount row
        InvoiceInterface invoice = new Invoice();
        invoice.setId(id);
        invoice.setBatchNumber(batch.getCurrentBatchNumber());
        invoice.setName(client.getName().replace(" ", "_")+"_"+batch.getCurrentBatchNumber());
        invoice.setFormat(invoiceFormat);
        invoice.setSeller(seller);
        invoice.setClient(client);
        invoice.setEmitor(emitor);
        invoice.setProducts(new ArrayList<>(products_));
        invoice.setDiscount(discount);
        invoice.setCar(car);
        invoice.setBatch(batch);
        invoice.setDate(date);
        invoice.setTva(tva);
        invoice.setInfo(info);

        File pdfFile =  pdfGenerator.createPdf(invoiceFormat, invoice);
        if(pdfFile == null){
            Alerts.showWarningAlert("Nu s-a putut crea pdf-ul!");
            return false;
        }

        invoice.setPdf(pdfFile);
        ByteArrayOutputStream dbout = new ByteArrayOutputStream();
        ObjectOutputStream dboutstraeam;

        try {
            dboutstraeam = new ObjectOutputStream(dbout);
            dboutstraeam.writeObject(invoice);
            dboutstraeam.flush();
        }catch (Exception e){
            e.printStackTrace();
        }

        InvoiceBlob invoiceBlob = new InvoiceBlob(dbout.toByteArray());
        invoiceBlob.setDeleted(false);
        invoiceBlob.setId(id);
        repository.updateInvoice(invoiceBlob);

        setBatch(_batch);
        setClient(_client);
        _products.remove(_products.size() - 1);
        setProducts(_products);
        setTva(_tva);

        seller = null;
        emitor = null;
        car = null;
        info = null;
        invoiceFormat = "a4";
        setEditingState(false);

        System.out.println("Factura editata cu succes");
        return true;
    }

    public void generateInvoice(){
        ArrayList<TableItem> products_ = new ArrayList<>(products);

        products_.remove(products_.size() - 1); //delete discount row
        InvoiceInterface invoice = new Invoice();
        invoice.setBatchNumber(batch.getCurrentBatchNumber());
        invoice.setName(client.getName().replace(" ", "_")+"_"+batch.getCurrentBatchNumber());
        invoice.setFormat(invoiceFormat);
        invoice.setSeller(seller);
        invoice.setClient(client);
        invoice.setEmitor(emitor);
        invoice.setProducts(new ArrayList<>(products_));
        invoice.setDiscount(discount);
        invoice.setCar(car);
        invoice.setBatch(batch);
        invoice.setDate(date);
        invoice.setTva(tva);
        invoice.setInfo(info);

        File pdfFile =  pdfGenerator.createPdf(invoiceFormat, invoice);
        if(pdfFile == null){
            Alerts.showWarningAlert("Nu s-a putut crea pdf-ul!");
            return;
        }
        invoice.setPdf(pdfFile);
        ByteArrayOutputStream dbout = new ByteArrayOutputStream();
        ObjectOutputStream dboutstraeam;

        try {
            dboutstraeam = new ObjectOutputStream(dbout);
            dboutstraeam.writeObject(invoice);
            dboutstraeam.flush();
        }catch (Exception e){
            Alerts.showExceptionAlert("Eroare la serializarea facturii. Factura nu a fost adaugata in baza de date!!", e);
        }

        InvoiceBlob invoiceBlob = new InvoiceBlob(dbout.toByteArray());
        invoiceBlob.setDeleted(false);
        repository.addNewInvoice(invoiceBlob);
        repository.updateBatch(batch);
    }
}
