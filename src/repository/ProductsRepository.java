package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import model.Product;
import model.interfaces.ProductInterface;
import model.interfaces.UMInterface;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import repository.interfaces.ProductsRepositoryInterface;
import repository.interfaces.UMRepositoryInterface;
import view.alerts.Alerts;

import java.math.BigDecimal;
import java.util.List;

public class ProductsRepository implements ProductsRepositoryInterface{

    private SessionFactory factory;


    public ProductsRepository(SessionFactory factory) {
        this.factory = factory;
    }

    public boolean addNewProduct(ProductInterface product)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.persist(product);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la adaugarea in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean updateProduct(ProductInterface product)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.update(product);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la updatare in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean deleteProduct(ProductInterface product)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.delete(product);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la stergere in baza de date", e);
            return false;
        }
        return true;
    }

    public ObservableList<ProductInterface> getAllProducts()
    {
        List<ProductInterface> products = null;
        Session session = factory.openSession();
        products = session.createQuery("from model.interfaces.ProductInterface order by productName", ProductInterface.class).list();
        return FXCollections.observableArrayList(products);
    }

    public static void main(String[] args)
    {
        Configuration configuration = new Configuration();
        configuration.configure("hib_config/hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();

        ProductsRepository repository = new ProductsRepository(factory);
        UMRepositoryInterface umrep = new UMRepository(factory);

        Product product = new Product("Articol 1",new BigDecimal(45.13), "sac");

//        repository.deleteProduct(product);

        repository.addNewProduct(product);
        for(ProductInterface p : repository.getAllProducts())
        {
            System.out.println(p);
        }

        factory.close();
    }
}
