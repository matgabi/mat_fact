package repository;

import javafx.collections.ObservableList;
import model.InvoiceBlob;
import model.Seller;
import model.interfaces.*;
import repository.interfaces.*;

public class Repository implements RepositoryInterface {

    private CarsRepositoryInterface carsRepository;
    private ClientsRepositoryInterface clientsRepository;
    private EmitorsRepositoryInterface emitorsRepository;
    private InvoiceRepositoryInterface invoicesRepository;
    private ProductsRepositoryInterface productsRepository;
    private SellersRepositoryInterface sellersRepository;
    private TVARepositoryInterface tvaRepository;
    private UMRepositoryInterface umRepository;
    private BatchRepositoryInterface batchRepository;

    public Repository(CarsRepositoryInterface carsRepository,
                      ClientsRepositoryInterface clientsRepository,
                      EmitorsRepositoryInterface emitorsRepository,
                      InvoiceRepositoryInterface invoicesRepository,
                      ProductsRepositoryInterface productsRepository,
                      SellersRepositoryInterface sellersRepository,
                      TVARepositoryInterface tvaRepository,
                      UMRepositoryInterface umRepository,
                      BatchRepositoryInterface batchRepository
                      ) {
        this.carsRepository = carsRepository;
        this.clientsRepository = clientsRepository;
        this.emitorsRepository = emitorsRepository;
        this.invoicesRepository = invoicesRepository;
        this.productsRepository = productsRepository;
        this.sellersRepository = sellersRepository;
        this.tvaRepository = tvaRepository;
        this.umRepository = umRepository;
        this.batchRepository = batchRepository;
    }


    @Override
    public boolean addNewCar(CarInterface car) {
        return carsRepository.addNewCar(car);
    }

    @Override
    public boolean updateCar(CarInterface car) {
        return carsRepository.updateCar(car);
    }

    @Override
    public boolean deleteCar(CarInterface car) {
        return carsRepository.deleteCar(car);
    }

    @Override
    public ObservableList<CarInterface> getAllCars() {
        return carsRepository.getAllCars();
    }

    @Override
    public boolean addNewClient(ClientInterface client) {
        return clientsRepository.addNewClient(client);
    }

    @Override
    public boolean updateClient(ClientInterface client) {
        return clientsRepository.updateClient(client);
    }

    @Override
    public boolean deleteClient(ClientInterface client) {
        return clientsRepository.deleteClient(client);
    }

    @Override
    public ObservableList<ClientInterface> getAllClients() {
        return clientsRepository.getAllClients();
    }

    @Override
    public boolean addNewEmitor(EmitorInterface emitor) {
        return emitorsRepository.addNewEmitor(emitor);
    }

    @Override
    public boolean updateEmitor(EmitorInterface emitor) {
        return emitorsRepository.updateEmitor(emitor);
    }

    @Override
    public boolean deleteEmitor(EmitorInterface emitor) {
        return emitorsRepository.deleteEmitor(emitor);
    }

    @Override
    public ObservableList<EmitorInterface> getAllEmitors() {
        return emitorsRepository.getAllEmitors();
    }

    @Override
    public boolean addNewInvoice(InvoiceBlob invoice) {
        return invoicesRepository.addNewInvoice(invoice);
    }

    @Override
    public boolean updateInvoice(InvoiceBlob invoice) {
        return invoicesRepository.updateInvoice(invoice);
    }

    @Override
    public boolean deleteInvoice(InvoiceBlob invoice) {
        return invoicesRepository.deleteInvoice(invoice);
    }

    @Override
    public ObservableList<InvoiceBlob> getAllInvoices() {
        return invoicesRepository.getAllInvoices();
    }

    @Override
    public boolean addNewProduct(ProductInterface product) {
        return productsRepository.addNewProduct(product);
    }

    @Override
    public boolean updateProduct(ProductInterface product) {
        return productsRepository.updateProduct(product);
    }

    @Override
    public boolean deleteProduct(ProductInterface product) {
        return productsRepository.deleteProduct(product);
    }

    @Override
    public ObservableList<ProductInterface> getAllProducts() {
        return productsRepository.getAllProducts();
    }

    @Override
    public boolean addNewSeller(SellerInterface seller) {
        return sellersRepository.addNewSeller(seller);
    }

    @Override
    public boolean updateSeller(SellerInterface seller) {
        return sellersRepository.updateSeller(seller);
    }

    @Override
    public boolean deleteSeller(SellerInterface seller) {
        return sellersRepository.deleteSeller(seller);
    }

    @Override
    public ObservableList<SellerInterface> getAllSellers() {
        return sellersRepository.getAllSellers();
    }

    @Override
    public boolean addNewTva(TvaInterface tva) {
        return tvaRepository.addNewTva(tva);
    }

    @Override
    public boolean updateTva(TvaInterface tva) {
        return tvaRepository.updateTva(tva);
    }

    @Override
    public boolean deleteTva(TvaInterface tva) {
        return tvaRepository.deleteTva(tva);
    }

    @Override
    public ObservableList<TvaInterface> getAllTvaValues() {
        return tvaRepository.getAllTvaValues();
    }

    @Override
    public boolean addNewUm(UMInterface um) {
        return umRepository.addNewUm(um);
    }

    @Override
    public boolean updateUm(UMInterface um) {
        return umRepository.updateUm(um);
    }

    @Override
    public boolean deleteUm(UMInterface um) {
        return umRepository.deleteUm(um);
    }

    @Override
    public ObservableList<UMInterface> getAllUmValues() {
        return umRepository.getAllUmValues();
    }

    @Override
    public boolean addNewBatch(BatchInterface batch) {
        return batchRepository.addNewBatch(batch);
    }

    @Override
    public boolean updateBatch(BatchInterface batch) {
        return batchRepository.updateBatch(batch);
    }

    @Override
    public boolean deleteBatch(BatchInterface batch) {
        return batchRepository.deleteBatch(batch);
    }

    @Override
    public ObservableList<BatchInterface> getAllBatches() {
        return batchRepository.getAllBatches();
    }
}
