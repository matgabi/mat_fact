package repository.interfaces;


import javafx.collections.ObservableList;
import model.Product;
import model.interfaces.ProductInterface;


public interface ProductsRepositoryInterface {

    public boolean addNewProduct(ProductInterface product);
    public boolean updateProduct(ProductInterface product);
    public boolean deleteProduct(ProductInterface product);
    public ObservableList<ProductInterface> getAllProducts();

}
