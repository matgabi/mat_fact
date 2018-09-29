package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;
import model.TVA;
import model.interfaces.ProductInterface;
import model.interfaces.TvaInterface;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import repository.interfaces.TVARepositoryInterface;
import sun.util.resources.cldr.vi.TimeZoneNames_vi;
import view.alerts.Alerts;

import java.math.BigDecimal;
import java.util.List;

public class TVARepository implements TVARepositoryInterface{

    private SessionFactory factory;


    public TVARepository(SessionFactory factory) {
        this.factory = factory;
    }

    public boolean addNewTva(TvaInterface tva)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.persist(tva);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la adaugarea in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean updateTva(TvaInterface tva)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.update(tva);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la updatare in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean deleteTva(TvaInterface tva)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.delete(tva);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la stergere in baza de date", e);
            return false;
        }
        return true;
    }

    public ObservableList<TvaInterface> getAllTvaValues()
    {
        List<TvaInterface> tvaValues = null;
        Session session = factory.openSession();
        tvaValues = session.createQuery("from model.interfaces.TvaInterface", TvaInterface.class).list();
        return FXCollections.observableArrayList(tvaValues);
    }

    public static void main(String[] args)
    {
        Configuration configuration = new Configuration();
        configuration.configure("hib_config/hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();

        TVARepository repository = new TVARepository(factory);

        TVA tva = new TVA("19", "TVA - 19%");
        repository.addNewTva(tva);

        for(TvaInterface p : repository.getAllTvaValues())
        {
            System.out.println(p);
        }

        factory.close();
    }

}
