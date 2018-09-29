package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.UM;
import model.interfaces.UMInterface;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import repository.interfaces.UMRepositoryInterface;
import view.alerts.Alerts;

import java.util.List;

public class UMRepository implements UMRepositoryInterface{

    private SessionFactory factory;


    public UMRepository(SessionFactory factory) {
        this.factory = factory;
    }

    public boolean addNewUm(UMInterface um)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.persist(um);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la adaugarea in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean updateUm(UMInterface um)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.update(um);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la updatare in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean deleteUm(UMInterface um)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.delete(um);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la stergere in baza de date", e);
            return false;
        }
        return true;
    }

    public ObservableList<UMInterface> getAllUmValues()
    {
        List<UMInterface> ums = null;
        Session session = factory.openSession();
        ums = session.createQuery("from model.interfaces.UMInterface", UMInterface.class).list();
        return FXCollections.observableArrayList(ums);
    }

    public static void main(String[] args)
    {
        Configuration configuration = new Configuration();
        configuration.configure("hib_config/hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();

        UMRepository repository = new UMRepository(factory);

        UM um = new UM("sac");
        repository.addNewUm(um);

        for(UMInterface p : repository.getAllUmValues())
        {
            System.out.println(p);
        }

        factory.close();
    }
}
