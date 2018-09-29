package repository.interfaces;

import javafx.collections.ObservableList;
import model.InvoiceBlob;


public interface InvoiceRepositoryInterface {
    public boolean addNewInvoice(InvoiceBlob invoice);
    public boolean updateInvoice(InvoiceBlob invoice);
    public boolean deleteInvoice(InvoiceBlob invoice);
    public ObservableList<InvoiceBlob> getAllInvoices();
}
