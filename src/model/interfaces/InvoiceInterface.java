package model.interfaces;

import model.Batch;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public interface InvoiceInterface {
    public int getId();
    public void setId(int id);
    public int getBatchNumber();
    public void setBatchNumber(int batchNumber);
    public String getName();
    public void setName(String name);
    public String getFormat();
    public void setFormat(String format);
    public String getPdfPath();
    public void setPdfPath(String pdfPath);
    public File getPdf();
    public void setPdf(File pdf);
    public SellerInterface getSeller();
    public void setSeller(SellerInterface seller);
    public ClientInterface getClient();
    public void setClient(ClientInterface client);
    public EmitorInterface getEmitor();
    public void setEmitor(EmitorInterface emitor);
    public ArrayList<TableItem> getProducts();
    public void setProducts(ArrayList<TableItem> products);
    public TableItem getDiscount();
    public void setDiscount(TableItem discount);
    public CarInterface getCar();
    public void setCar(CarInterface car);
    public BatchInterface getBatch();
    public void setBatch(BatchInterface batch);
    public LocalDate getDate();
    public void setDate(LocalDate date);
    public TvaInterface getTva();
    public void setTva(TvaInterface tva);
    public InvoiceInfoInterface getInfo();
    public void setInfo(InvoiceInfoInterface info);
    public BigDecimal getTotalValue();
    public void setTotalValue(BigDecimal totalValue);
    public BigDecimal getTotalTvaValue();
    public void setTotalTvaValue(BigDecimal totalTvaValue);
    public BigDecimal getTotalDiscountValue();
    public void setTotalDiscountValue(BigDecimal totalDiscountValue);
    public BigDecimal getTotalDiscountTvaValue();
    public void setTotalDiscountTvaValue(BigDecimal totalDiscountTvaValue);

}
