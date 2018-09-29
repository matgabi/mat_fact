package repository.interfaces;

import javafx.collections.ObservableList;
import model.Batch;
import model.InvoiceBlob;
import model.Seller;
import model.interfaces.*;

public interface RepositoryInterface {
    public boolean addNewCar(CarInterface car);
    public boolean updateCar(CarInterface car);
    public boolean deleteCar(CarInterface car);
    public ObservableList<CarInterface> getAllCars();

    public boolean addNewClient(ClientInterface client);
    public boolean updateClient(ClientInterface client);
    public boolean deleteClient(ClientInterface client);
    public ObservableList<ClientInterface> getAllClients();

    public boolean addNewEmitor(EmitorInterface emitor);
    public boolean updateEmitor(EmitorInterface emitor);
    public boolean deleteEmitor(EmitorInterface emitor);
    public ObservableList<EmitorInterface> getAllEmitors();

    public boolean addNewInvoice(InvoiceBlob invoice);
    public boolean updateInvoice(InvoiceBlob invoice);
    public boolean deleteInvoice(InvoiceBlob invoice);
    public ObservableList<InvoiceBlob> getAllInvoices();

    public boolean addNewProduct(ProductInterface product);
    public boolean updateProduct(ProductInterface product);
    public boolean deleteProduct(ProductInterface product);
    public ObservableList<ProductInterface> getAllProducts();

    public boolean addNewSeller(SellerInterface seller);
    public boolean updateSeller(SellerInterface seller);
    public boolean deleteSeller(SellerInterface seller);
    public ObservableList<SellerInterface> getAllSellers();

    public boolean addNewTva(TvaInterface tva);
    public boolean updateTva(TvaInterface tva);
    public boolean deleteTva(TvaInterface tva);
    public ObservableList<TvaInterface> getAllTvaValues();

    public boolean addNewUm(UMInterface um);
    public boolean updateUm(UMInterface um);
    public boolean deleteUm(UMInterface um);
    public ObservableList<UMInterface> getAllUmValues();

    public boolean addNewBatch(BatchInterface batch);
    public boolean updateBatch(BatchInterface batch);
    public boolean deleteBatch(BatchInterface batch);
    public ObservableList<BatchInterface> getAllBatches();

}
