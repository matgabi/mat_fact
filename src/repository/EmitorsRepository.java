package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Emitor;
import model.interfaces.EmitorInterface;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import repository.interfaces.EmitorsRepositoryInterface;
import view.alerts.Alerts;

import java.util.List;

public class EmitorsRepository implements EmitorsRepositoryInterface{

    private SessionFactory factory;

    public EmitorsRepository(SessionFactory factory) {
        this.factory = factory;
    }

    public boolean addNewEmitor(EmitorInterface emitor) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(emitor);
            transaction.commit();
        } catch (Exception e) {
            Alerts.showExceptionAlert("Eroare la adaugarea in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean updateEmitor(EmitorInterface emitor) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(emitor);
            transaction.commit();
        } catch (Exception e) {
            Alerts.showExceptionAlert("Eroare la updatare in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean deleteEmitor(EmitorInterface emitor) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(emitor);
            transaction.commit();
        } catch (Exception e) {
            Alerts.showExceptionAlert("Eroare la stergere in baza de date", e);
            return false;
        }
        return true;
    }

    public ObservableList<EmitorInterface> getAllEmitors() {
        List<EmitorInterface> emitors = null;
        Session session = factory.openSession();
        emitors = session.createQuery("from model.interfaces.EmitorInterface", EmitorInterface.class).list();
        return FXCollections.observableArrayList(emitors);
    }

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure("hib_config/hibernate.cfg.xml");

        SessionFactory factory = configuration.buildSessionFactory();

        EmitorsRepository repository = new EmitorsRepository(factory);

        Emitor emitor = new Emitor();
        emitor.setFirstName("Gabriel");
        emitor.setLastName("Matasariu");
        emitor.setSeria("NT");
        emitor.setNr("763050");
        emitor.setCiEmitor("POLITIA NEAMT");

        repository.addNewEmitor(emitor);
        for(EmitorInterface e : repository.getAllEmitors())
        {
            System.out.println(e);
        }

        factory.close();
    }
}
