package repository.interfaces;


import javafx.collections.ObservableList;
import model.Seller;
import model.interfaces.SellerInterface;


public interface SellersRepositoryInterface {

    public boolean addNewSeller(SellerInterface seller);
    public boolean updateSeller(SellerInterface seller);
    public boolean deleteSeller(SellerInterface seller);
    public ObservableList<SellerInterface> getAllSellers();

}
