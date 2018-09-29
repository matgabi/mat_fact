package model;


import org.hibernate.annotations.GenericGenerator;
import view.alerts.Alerts;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialBlob;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

@Entity
@Table(name = "facturi")
public class InvoiceBlob implements Serializable{

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")

    private int id;

    @Column
    private boolean isDeleted;

    @Column
    @Lob
    private Blob invoiceBytes;

    public InvoiceBlob(){}
    public InvoiceBlob(byte[] invoiceBytes){
        try{
            this.invoiceBytes = new SerialBlob(invoiceBytes);
        }catch (SQLException e){
            Alerts.showExceptionAlert("eroare la serializare", e);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Blob getInvoiceBytes() {
        return invoiceBytes;
    }

    public void setInvoiceBytes(Blob invoiceBytes) {
        this.invoiceBytes = invoiceBytes;
    }
}
